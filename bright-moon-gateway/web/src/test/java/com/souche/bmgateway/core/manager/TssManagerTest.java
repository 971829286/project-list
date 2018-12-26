package com.souche.bmgateway.core.manager;

import com.google.common.collect.Lists;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.tradeservice.facade.enums.TradeType;
import com.netfinworks.tradeservice.facade.model.AcquiringTradeItemDetail;
import com.netfinworks.tradeservice.facade.model.PaymentInfo;
import com.netfinworks.tradeservice.facade.model.TradeItemDetail;
import com.netfinworks.tradeservice.facade.model.paymethod.BalancePayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.weijin.TssManager;
import com.souche.optimus.common.util.UUIDUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zs.
 *         Created on 18/11/15.
 */
public class TssManagerTest extends BaseTest {

    @Resource
    private TssManager tssManager;

    @Test
    public void testcreatePayOrder() throws ManagerException {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setBuyerId("200000000056");
        tradeRequest.setAccessChannel("WEB");
        tradeRequest.setExtension("{\"key\":\"value\"}");
        tradeRequest.setGmtSubmit(new Date());
        tradeRequest.setTradeItemDetailList(getTradeItemDetails());
        tradeRequest.setPaymentInfo(null);
        PaymentResponse andPay = tssManager.createAndPay(tradeRequest);
        Assert.assertTrue(andPay.isSuccess());
        Assert.assertEquals("交易下单成功", andPay.getResultMessage());
    }


    @Test
    public void testPay() throws ManagerException {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBuyerId("200000000056");
        paymentRequest.setGmtSubmit(new Date());
        paymentRequest.setAccessChannel("WEB");
        paymentRequest.setTradeVoucherNoList(Lists.newArrayList("Yp8UYb16bx")); //支付时用该凭证号做关联
        paymentRequest.setPaymentInfo(getPaymentInfo());
        PaymentResponse paymentResponse = tssManager.pay(paymentRequest);
        Assert.assertFalse(paymentResponse.isSuccess());
        Assert.assertEquals("F0003", paymentResponse.getErrorCode());
        Assert.assertEquals("交易已过期不能支付", paymentResponse.getResultMessage());
    }


    @Test
    public void testCreateAndPay() throws ManagerException {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setBuyerId("200000000056");
        tradeRequest.setAccessChannel("WEB");
        tradeRequest.setExtension("{\"key\":\"value\"}");
        tradeRequest.setGmtSubmit(new Date());
        tradeRequest.setTradeItemDetailList(getTradeItemDetails());
        tradeRequest.setPaymentInfo(getPaymentInfo());
        PaymentResponse andPay = tssManager.createAndPay(tradeRequest);
        Assert.assertTrue(andPay.isSuccess());
        Assert.assertEquals("交易下单成功", andPay.getResultMessage());
    }


    @Test
    public void testRefund() throws ManagerException {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAccessChannel("WEB");
        refundRequest.setTradeVoucherNo(UUIDUtil.getID());
        refundRequest.setTradeSourceVoucherNo(UUIDUtil.getID());
//        refundRequest.setBizOrderNo("43254325432");    //取原订单业务单号
        refundRequest.setOrigTradeVoucherNo("5x48uxtff9");
        refundRequest.setRefundAmount(new Money("10"));
        refundRequest.setRefundEnsureAmount(new Money());
        refundRequest.setSplitParameterList(Lists.newArrayList());      //只有入款的时候有分账，退款的时候才需传分账参数
        refundRequest.setGmtSubmit(new Date());
        refundRequest.setMemo("");
        refundRequest.setExtension("");
//        refundRequest.setRefundPrepayAmount(new Money());
//        refundRequest.setRefundCoinAmount(new Money());
        RefundResponse refund = tssManager.refund(refundRequest);
    }


    private PaymentInfo getPaymentInfo() {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setBuyerAccountNo("200100100420000000005600001");
        paymentInfo.setPaymentVoucherNo(UUIDUtil.getID());
        paymentInfo.setPaymentSourceVoucherNo(UUIDUtil.getID());
        paymentInfo.setPayMethodList(getPayMethods());
        paymentInfo.setExtension("{\"789\":\"123\"}");
        return paymentInfo;
    }

    private List<TradeItemDetail> getTradeItemDetails() {
        List<TradeItemDetail> tradeItemDetailList = Lists.newArrayList();
        AcquiringTradeItemDetail tradeItemDetail = new AcquiringTradeItemDetail();
        tradeItemDetail.setTradeVoucherNo(UUIDUtil.getID());
        tradeItemDetail.setTradeSourceVoucherNo(UUIDUtil.getID());
        tradeItemDetail.setBizProductCode("43254543");
        tradeItemDetail.setProductDesc("车易拍收车款");
        tradeItemDetail.setTradeAmount(new Money("100"));
        tradeItemDetail.setSellerId("200000021114");
        tradeItemDetail.setSellerAccountNo("200100100120000002111400001");
        tradeItemDetail.setTradeMemo("车易拍");
        tradeItemDetail.setExtension("{\"hello\":\"world\"}");
        tradeItemDetail.setTradeType(TradeType.INSTANT_ACQUIRING);
        tradeItemDetail.setShowUrl("");
        tradeItemDetail.setOrigTradeVoucherNo("");
        tradeItemDetail.setBizNo("12345678");
        tradeItemDetail.setPartnerId("188888888888");
        tradeItemDetail.setGmtInvalid(null);    //失效时间
        tradeItemDetailList.add(tradeItemDetail);
        return tradeItemDetailList;
    }

    private List<PayMethod> getPayMethods() {
        List<PayMethod> payMethodList = Lists.newArrayList();
        BalancePayMethod balancePayMethod = new BalancePayMethod();
        balancePayMethod.setAmount(new Money("100"));
        balancePayMethod.setPayMode("BALANCE");
        balancePayMethod.setPayChannel("01");
        balancePayMethod.setPayerAccountNo("200100100420000000005600001"); // 余额方式要再填一次账号和ID
        balancePayMethod.setPayerId("200000000056");
        balancePayMethod.setExtension("{\"123\":\"456\"}");
        payMethodList.add(balancePayMethod);
        return payMethodList;
    }

}
