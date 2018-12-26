package com.souche.bmgateway.core.manager.weijin;

import com.alibaba.fastjson.JSON;
import com.netfinworks.fos.service.facade.FundoutFacade;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.netfinworks.pfs.service.payment.EntryAccountFacade;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.pfs.service.payment.response.PaymentResponse;
import com.netfinworks.voucher.service.facade.VoucherFacade;
import com.netfinworks.voucher.service.facade.domain.voucher.PaymentVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.netfinworks.voucher.service.facade.request.RecordingRequest;
import com.netfinworks.voucher.service.facade.response.RecordingResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ErrorCodeException;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.trade.PaymentToCardReq;
import com.souche.bmgateway.core.util.HttpClientUtil;
import com.souche.bmgateway.core.util.VoucherHelper;
import com.souche.bmgateway.enums.ResponseCode;
import com.souche.optimus.common.config.OptimusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zs.
 * Created on 18/7/20.
 */
@Service("tradeManager")
@Slf4j(topic = "manager")
@Deprecated
public class TradeManagerImpl extends CurrentOperationEnvironment implements TradeManager {


    @Resource
    private FundoutFacade fundoutFacade;

    @Resource
    private EntryAccountFacade entryAccountFacade;

    @Resource
    private VoucherFacade voucherFacade;

    /**
     * 支付网关地址
     **/
    public final static String GATEWAY_ACQUIRING = OptimusConfig.getValue("gateway.acquiring");

    @Override
    public String sendTradeRequest(Map<String, String> reqParams) {
        log.info("支付网关请求参数:" + reqParams);
        long beginTime = System.currentTimeMillis();
        String resp = HttpClientUtil.postUrlParams(GATEWAY_ACQUIRING, reqParams);
        long consumeTime = System.currentTimeMillis() - beginTime;
        log.info("支付网关请求返回:耗时:{} (ms); 响应结果:{} ", consumeTime, resp);
        return resp;
    }

    @Override
    public PaymentResponse entryRequest(EntryAcountRequest entryAcountRequest) throws ManagerException {

        long beginTime = System.currentTimeMillis();
        try {
            log.info("登账请求:" + entryAcountRequest);
            PaymentResponse resp = entryAccountFacade.enter(entryAcountRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info(entryAcountRequest.getPaymentVoucherNo() + "登账返回,耗时:{} (ms);响应结果:{} ", consumeTime, entryAcountRequest);
            if (null == resp) {
                throw new ManagerException(ErrorCodeEnums.RET_OBJECT_IS_NULL, "登账失败");
            }
            return resp;
        } catch (Exception e) {
            log.error("登账异常：" + e.getMessage());
            throw new ManagerException(ErrorCodeEnums.MANAGER_SERVICE_ERROR);
        }

    }


    @Override
    public FundoutResponse submit(FundoutRequest fundoutRequest) throws ManagerException {
        try {
            log.info("提现，请求参数:{}" + fundoutRequest);
            long beginTime = System.currentTimeMillis();

            FundoutResponse fundoutResponse = fundoutFacade.submit(fundoutRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;

            log.info(fundoutRequest.getPaymentOrderNo() + "提现，耗时:{} (ms); 结果:{} ", consumeTime, fundoutResponse);

            if (null == fundoutResponse) {
                throw new ManagerException(ErrorCodeEnums.RET_OBJECT_IS_NULL, "出款失败");
            }
            return fundoutResponse;
        } catch (Exception e) {
            log.error("提现异常：" + e.getMessage());
            throw new ManagerException(ErrorCodeEnums.MANAGER_SERVICE_ERROR);
        }
    }


    @Override
    public void savePaymentToCardTradeRecord(PaymentToCardReq req) throws ManagerException {
        log.info("保存单笔提现记录到统一凭证" + req);
        // 单笔用simple类型,批量用control类型
        SimpleOrderVoucherInfo winthdrawalInfo = VoucherHelper.paymentToCardReqTradeFrom(req);
        String infoJson = JSON.toJSONString(winthdrawalInfo);
        String voucherNo = saveRecord(infoJson, SimpleOrderVoucherInfo.voucherType());
        req.setInnerTradeNo(voucherNo);
    }

    @Override
    public String savePayRecordByPaymentToCardReq(PaymentToCardReq req) throws ManagerException {
        log.info("保存提现原始记录到支付类统一凭证:" + req);
        PaymentVoucherInfo payVoucherInfo = VoucherHelper.payFromByPaymentToCardReq(req);
        String infoJson = JSON.toJSONString(payVoucherInfo);
        String voucherNo = saveRecord(infoJson, PaymentVoucherInfo.voucherType());
        req.setPayVoucherNo(voucherNo);
        return voucherNo;
    }


    /**
     * 保存到统一凭证
     *
     * @param infoJson
     * @return
     * @throws ErrorCodeException.CommonException
     */
    private String saveRecord(String infoJson, String voucherType) throws ManagerException {

        RecordingRequest recordingRequest = new RecordingRequest();

        recordingRequest.setVoucher(infoJson);
        recordingRequest.setVoucherType(voucherType);
        log.info("保存交易凭证号：{}" + voucherType + infoJson);

        long start = System.currentTimeMillis();
        RecordingResponse resp = voucherFacade.record(recordingRequest);
        long end = System.currentTimeMillis();

        log.info("[Vocher->Mag](耗时：" + (end - start) + ")" + resp.toString());
        String voucherNo = null;
        String memo = resp.getReturnCode() + resp.getReturnMessage();

        if (ResponseCode.NEW_SUCCESS.getCode().equalsIgnoreCase(resp.getReturnCode())) {
            voucherNo = resp.getVoucherNo();
            log.info("返回的统一凭证号：" + voucherNo);
        } else if ("F0011".equalsIgnoreCase(resp.getReturnCode())) {
            log.error("商户请求/凭证号重复。(Data:" + infoJson + ")" + memo);
            throw new ManagerException(ErrorCodeEnums.REPEAT_REQUEST_ERROR);

        } else {
            log.error("保存到统一凭证失败。" + memo);
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
        return voucherNo;
    }

}
