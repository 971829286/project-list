package com.souche.bmgateway.core.manager.share.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.dto.request.OrderShareQueryRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.dto.response.OrderShareQueryResponse;
import com.souche.bmgateway.core.dto.response.ResponseInfo;
import com.souche.bmgateway.core.manager.share.OrderShareQueryService;
import com.souche.bmgateway.core.open.plat.OpenPlatformHttpClient;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 单笔分帐结果查询服务
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Service("orderShareQueryService")
@Slf4j(topic = "dubbo.impl")
public class OrderShareQueryServiceImpl implements OrderShareQueryService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderShareQueryServiceImpl.class);

    private static final String METHOD = OptimusConfig.getValue("share.query.method");
    private static final String POST_URL = OptimusConfig.getValue("open.platform.url");

    @Autowired
    OpenPlatformHttpClient openPlatformHttpClient;

    @Autowired
    private ONSProducer orderShareNotifyProducer;

    @Override
    public OrderShareQueryResponse doQuery(OrderShareQueryRequest orderShareQueryRequest) {

        String json = buildJson(orderShareQueryRequest);

        HttpBaseResponse httpBaseResponseDTO = openPlatformHttpClient.execute(POST_URL, json);

        return buildShareApplyResponse(httpBaseResponseDTO);
    }

    /**
     * 构建网商分帐查询参数
     *
     * @param request
     * @return
     */
    private String buildJson(OrderShareQueryRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("api", METHOD);
        Map<String, String> mapInner = new HashMap<String, String>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ReqMsgId", request.getReqMsgId());
        jsonObject.put("MerchantId", request.getMerchantId());
        jsonObject.put("RelateOrderNo", request.getRelateOrderNo());
        jsonObject.put("OutTradeNo", request.getOutTradeNo());
        jsonObject.put("ShareOrderNo", request.getShareOrderNo());

        mapInner.put("body", jsonObject.toJSONString());
        map.put("data", JsonUtils.toJson(mapInner));
        String data = JsonUtils.toJson(map);
        data = data.replaceAll("\\\\", "");
        data = data.replaceAll("\"\"", "\"");
        data = data.replaceAll("\"\\{", "{");
        data = data.replaceAll("\\}\"", "}");
        return data;
    }

    /**
     * 构建分账查询请求返回结果
     *
     * @param httpBaseResponseDTO
     * @return
     */
    private OrderShareQueryResponse buildShareApplyResponse(HttpBaseResponse httpBaseResponseDTO) {
        OrderShareQueryResponse orderShareQueryResponse = new OrderShareQueryResponse();
        try {
            Map<String, Object> data = httpBaseResponseDTO.getData();

            ResponseInfo responseInfo = new ResponseInfo();
            JSONObject object = JSON.parseObject(data.get("RespInfo").toString());
            responseInfo.setResultStatus(object.getString("ResultStatus"));
            responseInfo.setResultCode(object.getString("ResultCode"));
            responseInfo.setResultMsg(object.getString("ResultMsg"));

            orderShareQueryResponse.setResponseInfo(responseInfo);
            orderShareQueryResponse.setRelateOrderNo(data.get("RelateOrderNo").toString());
            orderShareQueryResponse.setOutTradeNo(data.get("OutTradeNo").toString());
            orderShareQueryResponse.setShareOrderNo(data.get("ShareOrderNo").toString());
            orderShareQueryResponse.setTotalAmount(data.get("TotalAmount").toString());
            orderShareQueryResponse.setCurrency(data.get("Currency").toString());
            orderShareQueryResponse.setShareDate(data.get("ShareDate").toString());
            orderShareQueryResponse.setStatus(data.get("Status").toString());
            orderShareQueryResponse.setErrorCode(data.get("ErrorCode").toString());
            orderShareQueryResponse.setErrorDesc(data.get("ErrorDesc").toString());
            orderShareQueryResponse.setExtInfo(data.get("ExtInfo").toString());
            orderShareQueryResponse.setMemo(data.get("Memo").toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("httpBaseResponseDTO=>{}构建分账查询请求返回结果失败,异常=>{}", httpBaseResponseDTO, e);
        }

        return orderShareQueryResponse;
    }
}
