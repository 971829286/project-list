package com.souche.bmgateway.core.service.trade.impl;

import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.netfinworks.ma.service.base.model.BankAcctDetailInfo;
import com.netfinworks.ma.service.response.BankAccountInfoResponse;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.pfs.service.payment.response.PaymentResponse;
import com.souche.bmgateway.core.dto.request.InstantTradeRequest;
import com.souche.bmgateway.core.dto.request.QueryTradeRequest;
import com.souche.bmgateway.core.dto.request.TransferBalanceRequest;
import com.souche.bmgateway.core.enums.CardTypeEnums;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ErrorCodeException;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.trade.PaymentToCardReq;
import com.souche.bmgateway.core.manager.weijin.MemberManager;
import com.souche.bmgateway.core.manager.weijin.TradeManager;
import com.souche.bmgateway.core.service.trade.TradeService;
import com.souche.bmgateway.core.util.BuilderUtil;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.enums.ResponseCode;
import com.souche.bmgateway.model.request.CreateInstantTradeRequest;
import com.souche.bmgateway.model.request.EntryRequest;
import com.souche.bmgateway.model.request.FundoutTradeRequest;
import com.souche.bmgateway.model.request.QueryPayRequest;
import com.souche.bmgateway.model.request.TransferAccountRequest;
import com.souche.bmgateway.model.response.EntryResponse;
import com.souche.bmgateway.model.response.FundoutTradeResponse;
import com.souche.bmgateway.model.response.TradeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author yyx
 */
@Service("tradeService")
@Slf4j(topic = "service")
@Deprecated
public class TradeServiceImpl implements TradeService {

    @Resource
    private TradeManager tradeManager;

    @Resource
    private MemberManager memberManager;

    @Resource
    private UesService uesService;

    @Override
    public TradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest) {
        log.info("即时到账，请求参数:{}" + createInstantTradeRequest);
        InstantTradeRequest instantTradeRequest = TradeConverUtil.convert(createInstantTradeRequest);
        Map<String, String> reqParams = BuilderUtil.build(instantTradeRequest);
        String resp = tradeManager.sendTradeRequest(reqParams);
        log.info(createInstantTradeRequest.getRequestNo()+"即时到账，返回参数:{}" + resp);

        TradeResponse tradeResponse = TradeConverUtil.converResult(resp);
        return tradeResponse;
    }

    @Override
    public FundoutTradeResponse submit(FundoutTradeRequest fundoutRequestDO) {
        try {
            log.info("提现，请求参数:{}" + fundoutRequestDO);
            PaymentToCardReq wReq = TradeConverUtil.convert(fundoutRequestDO);
            tradeManager.savePaymentToCardTradeRecord(wReq);
            tradeManager.savePayRecordByPaymentToCardReq(wReq);
            log.info("生成交易凭证号:{}" + wReq.getInnerTradeNo() + "支付凭证号:" + wReq.getPayVoucherNo());
            // 交易凭证号
            fundoutRequestDO.setFundoutOrderNo(wReq.getInnerTradeNo());
            // 支付凭证号
            fundoutRequestDO.setPaymentOrderNo(wReq.getPayVoucherNo());
            String extension = "{\"whiteChannelCode\":\"WSBANK20601\"}";
            fundoutRequestDO.setExtension(extension);
            // 传的卡号都为明文
            String encryptCardNo = uesService.encryptData(fundoutRequestDO.getCardNo());
            fundoutRequestDO.setCardNo(encryptCardNo);

            FundoutRequest request = TradeConverUtil.convertFundoutRequest(fundoutRequestDO);
            // 补齐请求参数
            buildFundoutReqeust(request, fundoutRequestDO);

            FundoutResponse response = tradeManager.submit(request);
            FundoutTradeResponse fundoutTradeResponse = new FundoutTradeResponse();
            fundoutTradeResponse.setPaymentOrderNo(response.getFundoutInfo().getPaymentOrderNo());
            fundoutTradeResponse.setFundoutOrderNo(response.getFundoutInfo().getFundoutOrderNo());
            fundoutTradeResponse.setMsg(response.getReturnMessage());
            fundoutTradeResponse.setCode(response.getReturnCode());
            if (ResponseCode.APPLY_SUCCESS.getCode().equalsIgnoreCase(fundoutTradeResponse.getCode())) {
                fundoutTradeResponse.setSuccess(true);
            } else {
                fundoutTradeResponse.setSuccess(false);
            }

            log.info(fundoutRequestDO.getPaymentOrderNo()+"提现，返回参数:{}" + fundoutTradeResponse);
            return fundoutTradeResponse;

        } catch (ErrorCodeException.CommonException e) {

            log.error("提现失败：" + e.getErrorCode() + ":" + e.getErrorMsg() + ":" + e.getMemo());
            return FundoutTradeResponse.createFailResp(new FundoutTradeResponse(), e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("提现异常" + e.getMessage());
            return FundoutTradeResponse.createFailResp(new FundoutTradeResponse(), ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode
                    (), "提现异常");
        }

    }

    private void buildFundoutReqeust(FundoutRequest request, FundoutTradeRequest fundoutRequestDO) {
        BankAccountInfoResponse bankAccountInfoResponse = null;
        try {
            bankAccountInfoResponse = memberManager.queryBankAccountDetail(fundoutRequestDO.getBankCardId());
        } catch (ManagerException e) {
            log.error("查询绑卡详细信息异常", e);
        }
        BankAcctDetailInfo bankAcctDetailInfo = bankAccountInfoResponse.getBankAcctInfo();
        request.setBankCode(bankAcctDetailInfo.getBankCode());
        request.setBankName(bankAcctDetailInfo.getBankName());
        request.setBranchName(bankAcctDetailInfo.getBankBranch());
        Integer cardType = bankAcctDetailInfo.getCardType();
        request.setCardType(CardTypeEnums.getByInsCode(cardType).getShortCode());
    }


    @Override
    public EntryResponse entry(EntryRequest entryRequest) {
        log.info("登账，请求参数:{}" + entryRequest);
        try {
            EntryAcountRequest entryAcountRequest = TradeConverUtil.convert(entryRequest);
            PaymentResponse paymentResponse = tradeManager.entryRequest(entryAcountRequest);
            log.info(entryRequest.getPaymentVoucherNo()+"登账，返回参数:{}" + paymentResponse);
            EntryResponse entryResponse = new EntryResponse();
            entryResponse.setBizPaymentSeqNo(paymentResponse.getBizPaymentSeqNo());
            entryResponse.setBizPaymentState(paymentResponse.getBizPaymentState());
            entryResponse.setBizSubState(paymentResponse.getBizSubState());
            entryResponse.setCode(paymentResponse.getReturnCode());
            entryResponse.setMsg(paymentResponse.getReturnMessage());
            if (ResponseCode.NEW_SUCCESS.getCode().equalsIgnoreCase(paymentResponse.getReturnCode())) {
                entryResponse.setSuccess(true);
            } else {
                entryResponse.setSuccess(false);
            }
            return entryResponse;
        } catch (ManagerException e) {
            return EntryResponse.createFailResp(new EntryResponse(), e.getCode(), e.getMessage());
        }

    }

    @Override
    public TradeResponse transferAccount(TransferAccountRequest transferAccountRequest) {
        log.info("转账，请求参数:{}" + transferAccountRequest);
        TransferBalanceRequest transferBalanceRequest = TradeConverUtil.convert(transferAccountRequest);
        Map<String, String> reqParams = BuilderUtil.build(transferBalanceRequest);
        String resp = tradeManager.sendTradeRequest(reqParams);
        log.info(transferAccountRequest.getOuterTradeNo()+"转账，返回参数:{}" + resp);
        TradeResponse tradeResponse = TradeConverUtil.converResult(resp);
        return tradeResponse;
    }

    @Override
    public TradeResponse queryTrade(QueryPayRequest queryTrade) {
        log.info("查询，请求参数:{}" + queryTrade);
        QueryTradeRequest queryTradeRequest = TradeConverUtil.convert(queryTrade);
        Map<String, String> reqParams = BuilderUtil.build(queryTradeRequest);
        String resp = tradeManager.sendTradeRequest(reqParams);
        log.info(queryTrade.getRequestNo()+"查询，返回参数:{}" + resp);

        TradeResponse tradeResponse = TradeConverUtil.converResult(resp);

        return tradeResponse;
    }

}
