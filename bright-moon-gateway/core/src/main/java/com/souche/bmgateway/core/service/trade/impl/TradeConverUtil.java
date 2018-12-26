package com.souche.bmgateway.core.service.trade.impl;

import com.alibaba.fastjson.JSONObject;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.souche.bmgateway.core.dto.request.InstantTradeRequest;
import com.souche.bmgateway.core.dto.request.QueryTradeRequest;
import com.souche.bmgateway.core.dto.request.TradeCommonRequest;
import com.souche.bmgateway.core.dto.request.TransferBalanceRequest;
import com.souche.bmgateway.core.manager.model.trade.PaymentToCardReq;
import com.souche.bmgateway.model.request.*;
import com.souche.bmgateway.model.response.TradeResponse;

import java.util.Date;

/**
 * @Author: yeyinxian
 * @Date: 2018/7/31 下午6:16
 */
@Deprecated
public class TradeConverUtil {

    public static void common(TradeCommonRequest request) {
        request.setVersion("1.0");
        request.set_input_charset("UTF-8");
        request.setSign_type("RSA");
        request.setPartner_id("188888888888");
    }

    public static TransferBalanceRequest convert(TransferAccountRequest transferAccountRequest) {
        TransferBalanceRequest transferBalanceRequest = new TransferBalanceRequest();
        common(transferBalanceRequest);
        transferBalanceRequest.setService("balance_transfer");
        transferBalanceRequest.setExtension(transferAccountRequest.getExtension());
        transferBalanceRequest.setOuter_trade_no(transferAccountRequest.getOuterTradeNo());
        transferBalanceRequest.setFundin_identity_no(transferAccountRequest.getFundinIdentityNo());
        transferBalanceRequest.setFundin_identity_type(transferAccountRequest.getFundinIdentityType());
        transferBalanceRequest.setFundin_account_type(transferAccountRequest.getFundinAccountType());
        transferBalanceRequest.setFundout_identity_no(transferAccountRequest.getFundoutIdentityNo());
        transferBalanceRequest.setFundout_identity_type(transferAccountRequest.getFundoutIdentityType());
        transferBalanceRequest.setFundout_account_type(transferAccountRequest.getFundoutAccountType());
        transferBalanceRequest.setTransfer_amount(transferAccountRequest.getTransferAmount());
        transferBalanceRequest.setTransfer_type(transferAccountRequest.getTransferType());
        transferBalanceRequest.setNotify_url(transferAccountRequest.getNotifyUrl());

        return transferBalanceRequest;
    }

    public static InstantTradeRequest convert(CreateInstantTradeRequest createInstantTradeRequest) {
        InstantTradeRequest instantTradeRequest = new InstantTradeRequest();
        common(instantTradeRequest);
        instantTradeRequest.setService("create_instant_trade");
        instantTradeRequest.setMemo(createInstantTradeRequest.getMemo());
        instantTradeRequest.setRequest_no(createInstantTradeRequest.getRequestNo());
        instantTradeRequest.setTrade_list(createInstantTradeRequest.getTradeList());
        instantTradeRequest.setOperator_id(createInstantTradeRequest.getOperatorId());
        instantTradeRequest.setBuyer_id(createInstantTradeRequest.getBuyerId());
        instantTradeRequest.setBuyer_id_type(createInstantTradeRequest.getBuyerIdType());
        instantTradeRequest.setBuyer_ip(createInstantTradeRequest.getBuyerIp());
        instantTradeRequest.setPay_method(createInstantTradeRequest.getPayMethod());
        instantTradeRequest.setGo_cashier(createInstantTradeRequest.getGoCashier());
        instantTradeRequest.setIs_web_access(createInstantTradeRequest.getIsWebAccess());
        instantTradeRequest.setSeller_acc_type(createInstantTradeRequest.getSellerAccType());
        instantTradeRequest.setExtension(createInstantTradeRequest.getExtension());
        return instantTradeRequest;
    }


    public static QueryTradeRequest convert(QueryPayRequest queryTrade) {
        QueryTradeRequest queryTradeRequest = new QueryTradeRequest();
        common(queryTradeRequest);
        queryTradeRequest.setRequest_no(queryTrade.getRequestNo());
        queryTradeRequest.setOperator_id(queryTrade.getOperatorId());
        queryTradeRequest.setService("query_pay");
        queryTradeRequest.setMemo(queryTrade.getMemo());
        return queryTradeRequest;
    }

    public static PaymentToCardReq convert(FundoutTradeRequest fundoutRequestDO) {
        PaymentToCardReq wReq = new PaymentToCardReq();
        wReq.setNotifyUrl(fundoutRequestDO.getNotifyUrl());
        wReq.setReturnUrl(fundoutRequestDO.getReturnUrl());
        wReq.setOuterTradeNo(fundoutRequestDO.getOutTradeNo());
        wReq.setPayableAmount(fundoutRequestDO.getAmount());
        wReq.setProductCode(fundoutRequestDO.getProductCode());
        wReq.setMemberId(fundoutRequestDO.getMemberId());
        wReq.setAccountId(fundoutRequestDO.getAccountNo());
        wReq.setPartnerId(fundoutRequestDO.getPartnerId());
        wReq.setService(fundoutRequestDO.getService());
        wReq.setVersion(fundoutRequestDO.getVersion());
        wReq.setSign(fundoutRequestDO.getSign());
        wReq.setSignType(fundoutRequestDO.getSignType());
        wReq.setInputCharset(fundoutRequestDO.getInputCharset());
        return wReq;
    }

    public static EntryAcountRequest convert(EntryRequest entryRequest) {
        EntryAcountRequest entryAcountRequest = new EntryAcountRequest();
        entryAcountRequest.setDrMemberId(entryRequest.getDrMemberId());
        entryAcountRequest.setDrAccountNo(entryRequest.getDrAccountNo());
        entryAcountRequest.setCrMemberId(entryRequest.getCrMemberId());
        entryAcountRequest.setCrAccountNo(entryRequest.getCrAccountNo());
        entryAcountRequest.setDrFundType(entryRequest.getDrFundType());
        entryAcountRequest.setCrFundType(entryRequest.getCrFundType());
        entryAcountRequest.setAmount(new Money(entryRequest.getAmount()));
        entryAcountRequest.setBizProductCode(entryRequest.getBizProductCode());
        entryAcountRequest.setPaymentVoucherNo(entryRequest.getPaymentVoucherNo());
        entryAcountRequest.setGmtPaymentInitiate(new Date());
        return entryAcountRequest;


    }


    /**
     * @param fundoutRequestDO
     * @return
     */
    public static FundoutRequest convertFundoutRequest(FundoutTradeRequest fundoutRequestDO) {
        FundoutRequest req = new FundoutRequest();
        req.setFundoutOrderNo(fundoutRequestDO.getFundoutOrderNo());
        req.setPaymentOrderNo(fundoutRequestDO.getPaymentOrderNo());
        req.setMemberId(fundoutRequestDO.getMemberId());
        req.setAccountNo(fundoutRequestDO.getAccountNo());
        req.setProductCode(fundoutRequestDO.getProductCode());
        req.setAmount(new Money(fundoutRequestDO.getAmount()));
        req.setCardNo(fundoutRequestDO.getCardNo());
        req.setName(fundoutRequestDO.getBankAccountName());
        req.setBankLineNo(fundoutRequestDO.getBankLineNo());
        req.setCompanyOrPersonal(fundoutRequestDO.getCompanyOrPersonal());
        req.setPurpose("");
        req.setFee(new Money("0"));
        req.setFundoutGrade("0");
        req.setExtension(fundoutRequestDO.getExtension());
        return req;
    }


    public static TradeResponse converResult(String resp) {
        TradeResponse tradeResponse = new TradeResponse();
        JSONObject jsonObject = JSONObject.parseObject(resp);
        tradeResponse.setSuccess("T".equals(jsonObject.getString("is_success")) ? true : false);
        tradeResponse.setCode(jsonObject.getString("error_code"));
        tradeResponse.setMsg(jsonObject.getString("error_message"));
        tradeResponse.setMemo(jsonObject.getString("memo"));
        tradeResponse.setExtension(jsonObject.getString("extension"));
        return tradeResponse;

    }
}
