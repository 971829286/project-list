package com.souche.bmgateway.core.job;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.MerchantEnums;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.merchant.MerchantService;
import com.souche.bmgateway.core.util.HttpClientUtil;
import com.souche.bmgateway.model.request.member.OpenAccountRequest;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 商户入驻结果
 *
 * @author chenwj
 * @since 2018/7/23
 */
@Slf4j(topic = "job")
@Component
public class MerchantResultQueryJob implements SimpleJob {

    @Autowired
    private MerchantSettleFlowRepository repository;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private WalletMemberService memberService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("<商户入驻结果查询>定时任务，开始...");
        try {
            List<MerchantSettleFlow> list = repository.selectByDay();
            log.info("<商户入驻结果查询>符合条件的记录数：{}", list.size());
            if (list.size() > 0) {
                for (MerchantSettleFlow flow : list) {
                    // status=P时，去网商查询入驻结果
                    if (StringUtils.equals(flow.getStatus(), "P")) {
                        // 参数拼装
                        MerchantResultQueryRequest record = new MerchantResultQueryRequest();
                        record.setOrderNo(flow.getMybankOrderNo());
                        // 调用
                        Result<String> result = merchantService.queryMerchantResult(record);
                        // 接口调用失败
                        if (StringUtils.equals(result.getCode(), ErrorCodeEnums.QUERY_MER_RESULT_ERROR.getCode())) {
                            log.info("<商户入驻结果查询>查询失败，原因->", result.getMsg());
                        }
                        // 回调并更新
                        callBackAndUpdateFlow(flow, result);
                    }
                    // call_back_status!=S即回调失败或未回调的数据库
                    if (!StringUtils.equals(flow.getCallBackStatus(), "S")) {
                        if (StringUtils.equals(flow.getStatus(), "F")) {
                            callBackFail(null, flow, null);
                        }
                        if (StringUtils.equals(flow.getStatus(), "S")) {
                            JSONObject json = new JSONObject();
                            json.put("MerchantId", flow.getOutMerchantId());
                            Result<String> result = new Result<>();
                            result.setData(flow.getRespInfo());
                            callBackSuccess(json, flow, result);
                        }
                    }
                }
                log.info("<商户入驻结果查询>定时任务，结束...");
                log.info("-------------------------------");
            } else {
                log.info("<商户入驻结果查询>暂无数据，结束...");
                log.info("-------------------------------");
            }

        } catch (UnsupportedEncodingException | OptimusExceptionBase e) {
            log.info("<商户入驻结果查询>查询失败，原因->{}", e.getMessage());
        }
    }

    private void callBackAndUpdateFlow(MerchantSettleFlow flow, Result<String> result) throws UnsupportedEncodingException {
        JSONObject json = JSONObject.parseObject(result.getData());
        // 入驻失败，更新流水
        if (StringUtils.equals(json.getString(MerchantEnums.REGISTER_STATUS.getMCode()), MerchantEnums.REGISTER_STATUS_FAIL.getMCode())) {
            // 回调
            callBackFail(json, flow, result);
            log.info("<商户入驻结果查询>入驻失败，原因：{}...", json.getString("FailReason"));
        }
        // 入驻成功，更新流水
        if (StringUtils.equals(json.getString(MerchantEnums.REGISTER_STATUS.getMCode()), MerchantEnums.REGISTER_STATUS_SUCCESS.getMCode())) {
            callBackSuccess(json, flow, result);
            log.info("<商户入驻结果查询>入驻成功，网商商户号：{}，数据更新成功...", json.getString("MerchantId"));
            OpenAccountRequest openAccountRequest = new OpenAccountRequest();
            openAccountRequest.setMemberId(flow.getMemberId());
            openAccountRequest.setMemberId("204");
            CreateAccountResponse response = memberService.openAccount(openAccountRequest);
            log.info("开通204账户，结果->{}", response.toString());
        }
    }

    private void callBackFail(JSONObject json, MerchantSettleFlow flow, Result<String> result) throws UnsupportedEncodingException {
        String failReason;
        if (json != null) {
            failReason = URLEncoder.encode(json.getString("FailReason"), "UTF-8");
            flow.setRespInfo(result.getData());
            flow.setReturnMsg(json.getString("FailReason"));
        } else {
            failReason = URLEncoder.encode(flow.getReturnMsg(), "UTF-8");
        }
        String param = "isSuccess=false&shopCode=" + flow.getShopCode() + "&extraInfo=" + failReason;
        String httpResult = HttpClientUtil.sendGet(flow.getCallBackUrl(), param);
        JSONObject jsonHttp = JSONObject.parseObject(httpResult);
        log.info("回调，响应->{}", jsonHttp.toJSONString());
        if (jsonHttp.getBoolean("success")) {
            if (jsonHttp.getBoolean("data")) {
                flow.setCallBackStatus("S");
            }
        } else {
            flow.setCallBackStatus("F");
        }
        log.info("<商户入驻结果查询>入驻失败，数据更新，原因：{}", flow.getReturnMsg());
        flow.setStatus("F");
        flow.setGtmModified(new Date());
        repository.update(flow);
    }

    private void callBackSuccess(JSONObject json, MerchantSettleFlow flow, Result<String> result) {
        String merchantId = json.getString("MerchantId");
        // 回调
        String param = "isSuccess=true&shopCode=" + flow.getShopCode() + "&merchantAccount=" + merchantId;
        String httpResult = HttpClientUtil.sendGet(flow.getCallBackUrl(), param);
        JSONObject jsonHttp = JSONObject.parseObject(httpResult);
        log.info("回调，响应->{}", jsonHttp.toJSONString());
        if (jsonHttp.getBoolean("success")) {
            if (jsonHttp.getBoolean("data")) {
                flow.setCallBackStatus("S");
            }
        } else {
            flow.setCallBackStatus("F");
        }
        log.info("<商户入驻结果查询>入驻成功，数据更新，请求单号：{}", flow.getMybankOrderNo());
        flow.setReturnMsg("入驻成功");
        flow.setOutMerchantId(merchantId);
        if (result != null) {
            flow.setRespInfo(result.getData());
        }
        flow.setStatus("S");
        flow.setGtmModified(new Date());
        repository.update(flow);
    }

}
