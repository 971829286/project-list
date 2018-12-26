package com.souche.bmgateway.core.job;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.enums.MerchantEnums;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.merchant.MerchantService;
import com.souche.bmgateway.core.util.HttpClientUtil;
import com.souche.bmgateway.model.request.member.OpenAccountRequest;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.optimus.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 商户入驻结果查询定时任务
 *
 * @author chenwj
 * @since 2018/8/6
 */
@Slf4j(topic = "job")
public class MerchantResultQueryJobTest extends BaseTest {

    @Resource
    private MerchantSettleFlowRepository repository;

    @Resource
    private MerchantService merchantService;

    @Resource
    private WalletMemberService memberService;

    @Test
    public void callBackTest() throws Exception {
        String url = "http://seapig.gh.dasouche.net/rest/callback/confirmSettleMerchant.json";
        String param = "isSuccess=false&shopCode=GH00000036&extraInfo=" + URLEncoder.encode("商户在支付渠道入驻失败", "UTF-8");
        String result = HttpClientUtil.sendGet(url, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info("响应->{}", jsonObject.toJSONString());
        Assert.assertTrue(jsonObject.getBoolean("success"));
    }

    @Test
    public void resultQueryJobTest() throws Exception {
        log.info("<商户入驻结果查询>定时任务，开始...");
        List<MerchantSettleFlow> list = repository.selectByDay();
        log.info("<商户入驻结果查询>符合条件的记录数：{}", list.size());
        if (list.size() > 0) {
            for (MerchantSettleFlow flow : list) {
                // status=P时，去网商查询入驻结果
                if (StringUtils.equals(flow.getStatus(), "P")) {
                    MerchantResultQueryRequest record = new MerchantResultQueryRequest();
                    record.setOrderNo(flow.getMybankOrderNo());
                    Result<String> result = merchantService.queryMerchantResult(record);
                    callBackAndUpdateFlow(flow, result);
                }
                // status!=P且回调店铺状态不为成功时，重试回调
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
            openAccountRequest.setAccountType("204");
            CreateAccountResponse response = memberService.openAccount(openAccountRequest);
            log.info("开通204账户，结果->{}", response.toString());
        }
    }

    private void callBackFail(JSONObject json, MerchantSettleFlow flow, Result<String> result) throws UnsupportedEncodingException {
        String failReason;
        if (json != null) {
            failReason = URLEncoder.encode(json.getString("FailReason"), "UTF-8");
            flow.setReturnMsg(json.getString("FailReason"));
            flow.setRespInfo(result.getData());
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
        flow.setOutMerchantId(merchantId);
        flow.setReturnMsg("入驻成功");
        if (result != null) {
            flow.setRespInfo(result.getData());
        }
        flow.setStatus("S");
        flow.setGtmModified(new Date());
        repository.update(flow);
    }

}
