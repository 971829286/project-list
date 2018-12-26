package com.souche.bmgateway.core.dubbo.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.domain.OrderShareApply;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.exception.CommonDefinedException;
import com.souche.bmgateway.core.dto.request.OrderShareDetail;
import com.souche.bmgateway.core.open.plat.OpenPlatformHttpClient;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import com.souche.bmgateway.core.util.ParamsValidate;
import com.souche.bmgateway.dubbo.api.OrderShareApplyFacade;
import com.souche.bmgateway.model.FundDetail;
import com.souche.bmgateway.model.request.OrderShareApplyRequest;
import com.souche.bmgateway.model.response.OrderShareApplyResponse;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.Base64Util;
import com.souche.optimus.common.util.Exceptions;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 分帐请求服务实现类
 * @Date: Created in 2018/07/10
 * @Modified By:
 */
@Service("orderShareApplyFacade")
@Slf4j(topic = "dubbo.impl")
public class OrderShareApplyFacadeImpl implements OrderShareApplyFacade {

    private static final String METHOD = OptimusConfig.getValue("share.apply.method");
    private static final String POST_URL = OptimusConfig.getValue("open.platform.url");

    @Resource
    OpenPlatformHttpClient openPlatformHttpClient;

    @Resource
    OrderShareApplyRepository orderShareApplyRepository;

    /**
     * 分帐请求申请接口
     *
     * @param orderShareApplyRequest
     * @return
     */
    @Override
    public OrderShareApplyResponse doApply(OrderShareApplyRequest orderShareApplyRequest) {

        OrderShareApplyResponse orderShareApplyResponse = new OrderShareApplyResponse();
        // 基础参数校验
        ParamsValidate.ParamsValidateResult<OrderShareApplyRequest> validateResult = ParamsValidate.validate(orderShareApplyRequest);
        if (validateResult.hasError()) {
            log.error("分帐请求申请失败=>{}", validateResult.getMsgError());
            orderShareApplyResponse.setOutTradeNo(orderShareApplyRequest.getOutTradeNo());
            orderShareApplyResponse.setSuccess(false);
            orderShareApplyResponse.setCode(CommonDefinedException.ILLEGAL_ARGUMENT.getErrorCode());
            orderShareApplyResponse.setMsg(CommonDefinedException.ILLEGAL_ARGUMENT.getErrorMsg() + validateResult.getMsgError());
            return orderShareApplyResponse;
        }

        //重复申请校验
        OrderShareApply orderShareApply = orderShareApplyRepository.selectByOutTradeNo(orderShareApplyRequest.getOutTradeNo());
        if (orderShareApply != null) {
            log.error("重复分帐请求orderShareApplyRequest=>{}", orderShareApplyRequest);
            orderShareApplyResponse.setOutTradeNo(orderShareApplyRequest.getOutTradeNo());
            orderShareApplyResponse.setSuccess(false);
            orderShareApplyResponse.setCode(CommonDefinedException.DUPLICATE_REQUEST_NO.getErrorCode());
            orderShareApplyResponse.setMsg(CommonDefinedException.DUPLICATE_REQUEST_NO.getErrorMsg() + validateResult.getMsgError());
            return orderShareApplyResponse;
        }

        //构建分账业务对象
        OrderShareDetail orderShareDetail = buildOrderShareDetail(orderShareApplyRequest);

        //保存分账申请信息
        boolean save = saveOrderShareApply(orderShareDetail);
        if (!save) {
            log.error("orderShareDetail=>{}保存分账申请信息失败", orderShareDetail);
            orderShareApplyResponse.setSuccess(false);
            orderShareApplyResponse.setMsg(CommonDefinedException.SAVE_SHARE_APPLY_ERROR.getErrorMsg());
            orderShareApplyResponse.setCode(CommonDefinedException.SAVE_SHARE_APPLY_ERROR.getErrorCode());
            return orderShareApplyResponse;
        }

        //发送分账请求申请信息给开发平台
        HttpBaseResponse httpBaseResponse = sendShareApply(orderShareDetail);

        //更新网商返回分账申请结果
        if (httpBaseResponse.isSuccess()) {
            updateShareApplyResult(httpBaseResponse, orderShareDetail);
        } else {
            log.error("httpBaseResponse=>{}调用开放平台分账返回请求失败", httpBaseResponse);
            //更新分账请求为失败
            orderShareApplyRepository.updateApplyStatus(orderShareDetail.getOutTradeNo(), "F");
        }

        return buildShareApplyResponse(httpBaseResponse);
    }

    /**
     * 更新网商返回分账申请结果
     *
     * @param httpBaseResponse
     */
    private void updateShareApplyResult(HttpBaseResponse httpBaseResponse, OrderShareDetail orderShareDetail) {
        try {
            Map<String, Object> data = httpBaseResponse.getData();
            log.info("更新网商返回分账申请结果,data=>{}", data);
            JSONObject jsonObject = JSONObject.parseObject(data.get("RespInfo").toString());
            orderShareApplyRepository.updateShareApplyResult(orderShareDetail.getOutTradeNo(), data.get("ShareOrderNo").toString(), jsonObject.getString("ResultStatus"), jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            orderShareApplyRepository.updateApplyStatus(orderShareDetail.getOutTradeNo(), "F");
            log.error("httpBaseResponse=>{}更新网商返回分账申请结果失败，异常=>{}", httpBaseResponse, e);
        }

    }

    /**
     * 发送分账请求申请信息给开放平台
     *
     * @param orderShareDetail
     * @return
     */
    private HttpBaseResponse sendShareApply(OrderShareDetail orderShareDetail) {
        HttpBaseResponse httpBaseResponse = new HttpBaseResponse();
        try {
            String json = buildJson(orderShareDetail);
            httpBaseResponse = openPlatformHttpClient.execute(POST_URL, json);
            //更新分账请求为处理中
            orderShareApplyRepository.updateApplyStatus(orderShareDetail.getOutTradeNo(), "P");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("orderShareDetail=>{}发送分账请求申请信息给开放平台失败，异常=>{}", orderShareDetail, e);
            httpBaseResponse.setSuccess(false);
            httpBaseResponse.setMsg("发送分账请求申请信息给开放平台失败:" + e);
            //更新分账请求为失败
            orderShareApplyRepository.updateApplyStatus(orderShareDetail.getOutTradeNo(), "F");
        }
        return httpBaseResponse;
    }

    /**
     * 构建分账业务对象
     *
     * @param orderShareApplyRequest
     * @return
     */
    private OrderShareDetail buildOrderShareDetail(OrderShareApplyRequest orderShareApplyRequest) {
        OrderShareDetail orderShareDetail = new OrderShareDetail();
        BeanUtils.copyProperties(orderShareApplyRequest, orderShareDetail);
        //payerFundDetails与payeeFundDetails为空时初始化
        if (orderShareApplyRequest.getPayeeFundDetails() == null) {
            orderShareDetail.setPayeeFundDetails(new ArrayList<>());
        }
        if (orderShareApplyRequest.getPayerFundDetails() == null) {
            orderShareDetail.setPayerFundDetails(new ArrayList<>());
        }
        orderShareDetail.setReqMsgId(UUIDUtil.getID());
        return orderShareDetail;
    }

    /**
     * 保存分账申请信息
     *
     * @param orderShareDetail
     * @return
     */
    private boolean saveOrderShareApply(OrderShareDetail orderShareDetail) {

        boolean save;
        try {
            OrderShareApply orderShareApply = new OrderShareApply();
            BeanUtils.copyProperties(orderShareDetail, orderShareApply);
            orderShareApply.setPayeeFundDetails(JSON.toJSONString(orderShareDetail.getPayeeFundDetails()));
            orderShareApply.setPayerFundDetails(JSON.toJSONString(orderShareDetail.getPayerFundDetails()));
            orderShareApply.setApplyStatus("I");
            orderShareApply.setNotifyStatus("P");
            orderShareApply.setQueryTimes(0);
            save = orderShareApplyRepository.saveOne(orderShareApply);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("orderShareDetail=>{},保存分账申请信息失败，异常=>{}", orderShareDetail, e);
            throw Exceptions.fail(CommonDefinedException.SAVE_SHARE_APPLY_ERROR.getErrorCode(), CommonDefinedException.SAVE_SHARE_APPLY_ERROR.getErrorMsg());
        }
        return save;
    }

    /**
     * 构建网商分帐请求参数
     *
     * @param orderShareDetail
     * @return
     */
    private String buildJson(OrderShareDetail orderShareDetail) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> bodyMap = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ReqMsgId", orderShareDetail.getReqMsgId());
        jsonObject.put("MerchantId", orderShareDetail.getMerchantId());
        jsonObject.put("RelateOrderNo", orderShareDetail.getRelateOrderNo());
        jsonObject.put("OutTradeNo", orderShareDetail.getOutTradeNo());
        jsonObject.put("TotalAmount", orderShareDetail.getTotalAmount());
        jsonObject.put("Currency", orderShareDetail.getCurrency());
        JSONArray jsonArrayPayer = new JSONArray();
        for (int i = 0; i < orderShareDetail.getPayerFundDetails().size(); i++) {
            FundDetail fundDetail = orderShareDetail.getPayerFundDetails().get(i);
            JSONObject jsonObjectPayer = new JSONObject();
            jsonObjectPayer.put("Amount", fundDetail.getAmount());
            jsonObjectPayer.put("Currency", fundDetail.getCurrency());
            jsonObjectPayer.put("ParticipantId", fundDetail.getParticipantId());
            jsonObjectPayer.put("ParticipantType", fundDetail.getParticipantType());
            jsonObjectPayer.put("Purpose", fundDetail.getPurpose());
            jsonArrayPayer.add(jsonObjectPayer);
        }
        jsonObject.put("PayerFundDetails", Base64Util.encrypt(jsonArrayPayer.toJSONString()));

        JSONArray jsonArrayPayee = new JSONArray();
        for (int i = 0; i < orderShareDetail.getPayeeFundDetails().size(); i++) {
            FundDetail fundDetail = orderShareDetail.getPayeeFundDetails().get(i);
            JSONObject jsonObjectPayee = new JSONObject();
            jsonObjectPayee.put("Amount", fundDetail.getAmount());
            jsonObjectPayee.put("Currency", fundDetail.getCurrency());
            jsonObjectPayee.put("ParticipantId", fundDetail.getParticipantId());
            jsonObjectPayee.put("ParticipantType", fundDetail.getParticipantType());
            jsonObjectPayee.put("Purpose", fundDetail.getPurpose());
            jsonArrayPayee.add(jsonObjectPayee);
        }
        jsonObject.put("PayeeFundDetails", Base64Util.encrypt(jsonArrayPayee.toJSONString()));

        jsonObject.put("ExtInfo", orderShareDetail.getExtInfo());
        jsonObject.put("Memo", orderShareDetail.getMemo());

        bodyMap.put("body", jsonObject.toJSONString());
        map.put("api", METHOD);
        map.put("data", JsonUtils.toJson(bodyMap));
        String data = JsonUtils.toJson(map);
        data = data.replaceAll("\\\\", "");
        data = data.replaceAll("\"\"", "\"");
        data = data.replaceAll("\"\\{", "{");
        data = data.replaceAll("\\}\"", "}");
        return data;
    }

    /**
     * 构建分账申请请求返回结果
     *
     * @param httpBaseResponse
     * @return
     */
    private OrderShareApplyResponse buildShareApplyResponse(HttpBaseResponse httpBaseResponse) {

        OrderShareApplyResponse orderShareApplyResponse = new OrderShareApplyResponse();
        if (httpBaseResponse.isSuccess()) {
            Map<String, Object> data = httpBaseResponse.getData();
            orderShareApplyResponse.setOutTradeNo(data.containsKey("OutTradeNo") ? data.get("OutTradeNo").toString() : "");
            orderShareApplyResponse.setShareOrderNo(data.containsKey("ShareOrderNo") ? data.get("ShareOrderNo").toString() : "");
            JSONObject jsonObject = JSONObject.parseObject(data.get("RespInfo").toString());
            if ("S".equals(jsonObject.getString("ResultStatus"))) {
                orderShareApplyResponse.setSuccess(true);
                orderShareApplyResponse.setCode(jsonObject.getString("ResultCode"));
                orderShareApplyResponse.setMsg(jsonObject.getString("ResultMsg"));
            } else {
                log.error("注意！！！网商分账申请返回失败=>{}", httpBaseResponse);
                orderShareApplyResponse.setSuccess(false);
                orderShareApplyResponse.setCode(jsonObject.getString("ResultCode"));
                orderShareApplyResponse.setMsg(jsonObject.getString("ResultMsg"));
            }

        } else {
            orderShareApplyResponse.setSuccess(httpBaseResponse.isSuccess());
            orderShareApplyResponse.setCode(httpBaseResponse.getCode());
            orderShareApplyResponse.setMsg(httpBaseResponse.getMsg());
        }

        return orderShareApplyResponse;
    }
}
