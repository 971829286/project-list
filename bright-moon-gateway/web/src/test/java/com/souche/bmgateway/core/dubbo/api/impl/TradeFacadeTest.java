package com.souche.bmgateway.core.dubbo.api.impl;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.util.GatewaySignUtil;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.dubbo.api.TradeFacade;
import com.souche.bmgateway.model.request.*;
import com.souche.bmgateway.model.response.EntryResponse;
import com.souche.bmgateway.model.response.FundoutTradeResponse;
import com.souche.bmgateway.model.response.TradeResponse;
import com.souche.optimus.common.util.UUIDUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class TradeFacadeTest extends BaseTest {

    // 调用接口，固定参数
    private static String PRODUCT_CODE = "10213";
    private static String VERSION = "1.0";
    private static String INPUT_CHARSET = "UTF-8";
    private static String FEE = "0.0";// 提现所需费用

    private static String CARD_TYPE = "DC";// 卡类型"DC"
    private static String SIGN_TYPE = "RSA";//签名类型
    private static String FUNDOUT_GRADE = "0";
    private static String SERVICE = "fundout";
    @Resource
    TradeFacade tradeFacade;

    /**
     * 即时到账交易
     * 200000000056
     200000041002
     200000021004
     200000041003
     200000041004
     200000021005
     201000020000
     */
    @Test
    public void createInstantTradeTest() {
        CreateInstantTradeRequest createInstantTradeRequest = new CreateInstantTradeRequest();
        createInstantTradeRequest.setRequestNo(System.currentTimeMillis() + "");
        String instId = UUID.randomUUID().toString().substring(0, 18);
        String len = instId.length() + "";
        createInstantTradeRequest.setService("create_instant_trade");
        createInstantTradeRequest.setRequestNo(System.currentTimeMillis() + "");
        createInstantTradeRequest.setTradeList(len + ":" + instId + "~1:0~4:0.01~1:1~4:0.01~0:~12:200000000012~9:MEMBER_ID~" +
                len + ":" + instId + "~0:~0:~0:~0:~0:~0:~64:http://spay-inner.stable.proxy.dasouche.com/notify/receive_trade~0:");
//        createInstantTradeRequest.setBuyerId("anonymous");
        createInstantTradeRequest.setBuyerIdType("MEMBER_ID");
        createInstantTradeRequest.setBuyerIp("127.0.0.1");
        createInstantTradeRequest.setPayMethod("3:pos^4:0.01^13:TMALLPOS,C,GC");
        createInstantTradeRequest.setGoCashier("N");
        createInstantTradeRequest.setIsWebAccess("N");
        createInstantTradeRequest.setExtension(
                "{\"biz\":\"Pay\",\"biz_code\":\"ZHDD1428199090\",\"productCode\":\"20201\",\"trade_type\":\"TRADE\"}");

        TradeResponse resp = tradeFacade.createInstantTrade(createInstantTradeRequest);
        System.out.println(resp);
        Assert.assertEquals(true, resp.isSuccess());

    }

    /**
     * 渠道不存在
     */
    @Test
    public void noChannelTest() {
        CreateInstantTradeRequest tradeRequest2 = new CreateInstantTradeRequest();
        tradeRequest2.setRequestNo(System.currentTimeMillis() + "");
        String instId3 = UUID.randomUUID().toString().substring(0, 18);
        String len3 = instId3.length() + "";
        tradeRequest2.setService("create_instant_trade");
        tradeRequest2.setRequestNo(System.currentTimeMillis() + "");
        tradeRequest2.setTradeList(len3 + ":" + instId3 + "~1:0~4:0.01~1:1~4:0.01~0:~12:200000000012~9:MEMBER_ID~" +
                len3 + ":" + instId3 + "~0:~0:~0:~0:~0:~0:~64:http://spay-inner.stable.proxy.dasouche.com/notify/receive_trade~0:");
        tradeRequest2.setBuyerId("200000000038");
        tradeRequest2.setBuyerIdType("MEMBER_ID");
        tradeRequest2.setBuyerIp("127.0.0.1");
        tradeRequest2.setPayMethod("3:pos^4:0.01^13:TMALLPOc,C,GC");
        tradeRequest2.setGoCashier("N");
        tradeRequest2.setIsWebAccess("N");
        tradeRequest2.setExtension(
                "{\"biz\":\"Pay\",\"biz_code\":\"ZHDD1428199090\",\"productCode\":\"20201\",\"trade_type\":\"TRADE\"}");

        TradeResponse resp3 = tradeFacade.createInstantTrade(tradeRequest2);
        System.out.println(resp3);
        Assert.assertEquals(false, resp3.isSuccess());
    }



    @Resource
    UesService uesService;
    @Test
    public void fundoutTradeTest() {
        FundoutTradeRequest fundoutRequestDO = new FundoutTradeRequest();
        String cardNo =  "P193-1755";
        fundoutRequestDO.setOutTradeNo(UUID.randomUUID().toString().replaceAll("", ""));
        fundoutRequestDO.setCardNo(cardNo);
        fundoutRequestDO.setProductCode(PRODUCT_CODE);
        fundoutRequestDO.setMemberId("200000000056");
        fundoutRequestDO.setAccountNo("200100100120000000005600001");
        fundoutRequestDO.setPartnerId("188888888888");
        fundoutRequestDO.setService(SERVICE);
        fundoutRequestDO.setVersion(VERSION);
        fundoutRequestDO.setSignType(SIGN_TYPE);
        fundoutRequestDO.setInputCharset(INPUT_CHARSET);
        fundoutRequestDO.setBankAccountName("浙江大搜车融资租赁有限公司");
        fundoutRequestDO.setCompanyOrPersonal("B");
        fundoutRequestDO.setBankCardId("22");
        fundoutRequestDO.setNotifyUrl("http://m1.docker.souche.com:20002/auction/callback");
        fundoutRequestDO.setAmount("0.01");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String reqTime = sf.format(new Date());

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("service", SERVICE);
        params.put("version", VERSION);
        params.put("inputCharset", INPUT_CHARSET);
        params.put("outTradeNo", fundoutRequestDO.getOutTradeNo());
        params.put("amount", fundoutRequestDO.getAmount());
        params.put("requestTime", reqTime);
        params.put("productCode", PRODUCT_CODE);
        params.put("memberId", fundoutRequestDO.getMemberId());
        params.put("accountId", fundoutRequestDO.getAccountNo());
        String sign = GatewaySignUtil.signRSA(params, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO/6rPCvyCC+IMalLzTy3cVBz/+wamCFNiq9qKEilEBDTttP7Rd/GAS51lsfCrsISbg5td/w25+wulDfuMbjjlW9Afh0p7Jscmbo1skqIOIUPYfVQEL687B0EmJufMlljfu52b2efVAyWZF9QBG1vx/AJz1EVyfskMaYVqPiTesZAgMBAAECgYEAtVnkk0bjoArOTg/KquLWQRlJDFrPKP3CP25wHsU4749t6kJuU5FSH1Ao81d0Dn9m5neGQCOOdRFi23cV9gdFKYMhwPE6+nTAloxI3vb8K9NNMe0zcFksva9c9bUaMGH2p40szMoOpO6TrSHO9Hx4GJ6UfsUUqkFFlN76XprwE+ECQQD9rXwfbr9GKh9QMNvnwo9xxyVl4kI88iq0X6G4qVXo1Tv6/DBDJNkX1mbXKFYL5NOW1waZzR+Z/XcKWAmUT8J9AkEA8i0WT/ieNsF3IuFvrIYG4WUadbUqObcYP4Y7Vt836zggRbu0qvYiqAv92Leruaq3ZN1khxp6gZKl/OJHXc5xzQJACqr1AU1i9cxnrLOhS8m+xoYdaH9vUajNavBqmJ1mY3g0IYXhcbFm/72gbYPgundQ/pLkUCt0HMGv89tn67i+8QJBALV6UgkVnsIbkkKCOyRGv2syT3S7kOv1J+eamGcOGSJcSdrXwZiHoArcCZrYcIhOxOWB/m47ymfE1Dw/+QjzxlUCQCmnGFUO9zN862mKYjEkjDN65n1IUB9Fmc1msHkIZAQaQknmxmCIOHC75u4W0PGRyVzq8KkxpNBq62ICl7xmsPM=");
        fundoutRequestDO.setSign(sign);
        FundoutTradeResponse resp = tradeFacade.fundoutTrade(fundoutRequestDO);
        System.out.println(resp);
        Assert.assertEquals(true, resp.isSuccess());
    }

    @Test
    public void testTransfer() {
        TransferAccountRequest transferAccountRequest = new TransferAccountRequest();
        transferAccountRequest.setOuterTradeNo(System.currentTimeMillis() + "");
        transferAccountRequest.setFundinIdentityNo("200000000012");
        transferAccountRequest.setFundinIdentityType("MEMBER_ID");
        transferAccountRequest.setFundinAccountType("201");
        transferAccountRequest.setFundoutIdentityNo("200000000038");
        transferAccountRequest.setFundoutIdentityType("MEMBER_ID");
        transferAccountRequest.setFundoutAccountType("201");
        transferAccountRequest.setTransferAmount("0.01");
        transferAccountRequest.setTransferType("normal");
        transferAccountRequest.setExtension("{\"biz_code\":\"123456\"}");
        System.out.println("转账返回参数：");

        TradeResponse resp = tradeFacade.transferAccount(transferAccountRequest);
        System.out.println(resp);
        Assert.assertEquals(true, resp.isSuccess());
    }


    /**
     * 余额不足
     */
    @Test
    public void testTransferBanlace() {
        TransferAccountRequest transferAccountRequest = new TransferAccountRequest();
        transferAccountRequest.setOuterTradeNo(System.currentTimeMillis() + "");
        transferAccountRequest.setFundinIdentityNo("200000000012");
        transferAccountRequest.setFundinIdentityType("MEMBER_ID");
        transferAccountRequest.setFundinAccountType("201");
        transferAccountRequest.setFundoutIdentityNo("200000000038");
        transferAccountRequest.setFundoutIdentityType("MEMBER_ID");
        transferAccountRequest.setFundoutAccountType("201");
        transferAccountRequest.setTransferAmount("180");
        transferAccountRequest.setTransferType("normal");
        transferAccountRequest.setExtension("{\"biz_code\":\"123456\"}");
        System.out.println("转账返回参数：");

        TradeResponse resp = tradeFacade.transferAccount(transferAccountRequest);
        System.out.println(resp);
        Assert.assertEquals(false, resp.isSuccess());
    }


    @Test
    public void testEntry() {
        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setDrMemberId("200000041002");
        entryRequest.setDrAccountNo("200100100420000004100200001");
        entryRequest.setDrFundType("DR");
        entryRequest.setCrMemberId("200000021004");
        entryRequest.setCrAccountNo("200100100120000002100400001");
        entryRequest.setCrFundType("DR");
        entryRequest.setAmount("0.01");
        entryRequest.setBizProductCode("test");
        entryRequest.setPaymentVoucherNo(UUIDUtil.getID());
        EntryResponse resp = tradeFacade.entry(entryRequest);
        System.out.println(resp);
        Assert.assertEquals(true, resp.isSuccess());
    }


    /**
     * 余额不足
     */
    @Test
    public void testEntryBalance() {
        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setDrMemberId("200000000012");
        entryRequest.setDrAccountNo("200100100120000000001200001");
        entryRequest.setDrFundType("DR");
        entryRequest.setCrMemberId("200000000038");
        entryRequest.setCrAccountNo("200100100220000000003800001");
        entryRequest.setCrFundType("DR");
        entryRequest.setAmount("200");
        entryRequest.setBizProductCode("test");
        entryRequest.setPaymentVoucherNo(UUIDUtil.getID());
        EntryResponse resp = tradeFacade.entry(entryRequest);
        System.out.println(resp);
        Assert.assertEquals(false, resp.isSuccess());
    }


    



    @Test
    public void testQuery() {
        QueryPayRequest queryPayRequest = new QueryPayRequest();
        queryPayRequest.setRequestNo("1533375979047");
        TradeResponse resp = tradeFacade.queryPay(queryPayRequest);
        System.out.println(resp);
        Assert.assertEquals(true, resp.isSuccess());
    }

    /**
     * 查询账号不存在
     */
    @Test
    public void testQueryNotExit() {
        QueryPayRequest queryPayRequest = new QueryPayRequest();
        queryPayRequest.setRequestNo("153337597904711");
        TradeResponse resp = tradeFacade.queryPay(queryPayRequest);
        System.out.println(resp);
        Assert.assertEquals(false, resp.isSuccess());
    }







//    public static void main(String[] args) {
        /* 读取数据 */
//        try {
//            List<String> sss = FileUtils.readLines(new File("/Users/lannuobing/Downloads/sss"));
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/lannuobing/Downloads/origin.csv")),
//                    "UTF-8"));
//
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/lannuobing/Downloads/aaa"),true),
//                    "UTF-8"));
//
//            String lineTxt =null;
//            int count=0;
//            while ((lineTxt = br.readLine()) != null) {
//                if(lineTxt.contains(sss.get(count))){
//                    count++;
//                    continue;
//                }
//                bw.write(lineTxt+"\n");
//
//
//            }
//            br.close();
//            bw.close();
//        } catch (Exception e) {
//            System.err.println("read errors :" + e);
//        }
//update  tr_bank_account t set t.SIGN_NO= 'P8113052' where right(t.BANK_ACCOUNT_NO,4) = '7816' and t.MEMBER_ID= '100001155770' and BANK_NAME= '工商银行';

//    }




}
