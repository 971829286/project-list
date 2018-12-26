package com.souche.bmgateway.core.service.trade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.voucher.service.facade.domain.access.GatewayAccessInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.weijin.VoucherManager;
import com.souche.bmgateway.core.service.dto.WalletFundoutDTO;
import com.souche.bmgateway.core.service.fos.WalletFundoutService;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;
import com.souche.optimus.common.util.UUIDUtil;


public class WalletFundoutServiceTest extends BaseTest {
	
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
	private WalletFundoutService fundoutService;
	
	@Resource
	private VoucherManager voucherManager;


	//@Resource
	//UesService uesService;

	@Test
	public void fundoutTradeTest() {
		
		WalletFundoutDTO fundoutRequestDO = new WalletFundoutDTO();
		String cardNo = "P193-1755";
		fundoutRequestDO.setOutTradeNo(UUID.randomUUID().toString().replaceAll("", ""));
		fundoutRequestDO.setCardNo(cardNo);
		fundoutRequestDO.setBizProductCode(PRODUCT_CODE);
		fundoutRequestDO.setMemberId("200000000056");
		fundoutRequestDO.setAccountNo("200100100120000000005600001");
		fundoutRequestDO.setPartnerId("188888888888");
		//fundoutRequestDO.set(SERVICE);
//		fundoutRequestDO.setVersion(VERSION);
//		fundoutRequestDO.setSignType(SIGN_TYPE);
//		fundoutRequestDO.setInputCharset(INPUT_CHARSET);
		fundoutRequestDO.setBankAccountName("浙江大搜车融资租赁有限公司");
		fundoutRequestDO.setCompanyOrPersonal(1);
		fundoutRequestDO.setBankCardId("22");
		fundoutRequestDO.setNotifyUrl("http://m1.docker.souche.com:20002/auction/callback");
		fundoutRequestDO.setAmount("0.01");
		fundoutRequestDO.setAccountType("201");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String reqTime = sf.format(new Date());

		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("service", SERVICE);
		params.put("version", VERSION);
		params.put("inputCharset", INPUT_CHARSET);
		params.put("outTradeNo", fundoutRequestDO.getOutTradeNo());
		params.put("amount", fundoutRequestDO.getAmount());
		params.put("requestTime", reqTime);
		params.put("bizProductCode", PRODUCT_CODE);
		params.put("memberId", fundoutRequestDO.getMemberId());
		params.put("accountId", fundoutRequestDO.getAccountNo());
//		String sign = GatewaySignUtil.signRSA(params,
//				"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO/6rPCvyCC+IMalLzTy3cVBz/+wamCFNiq9qKEilEBDTttP7Rd/GAS51lsfCrsISbg5td/w25+wulDfuMbjjlW9Afh0p7Jscmbo1skqIOIUPYfVQEL687B0EmJufMlljfu52b2efVAyWZF9QBG1vx/AJz1EVyfskMaYVqPiTesZAgMBAAECgYEAtVnkk0bjoArOTg/KquLWQRlJDFrPKP3CP25wHsU4749t6kJuU5FSH1Ao81d0Dn9m5neGQCOOdRFi23cV9gdFKYMhwPE6+nTAloxI3vb8K9NNMe0zcFksva9c9bUaMGH2p40szMoOpO6TrSHO9Hx4GJ6UfsUUqkFFlN76XprwE+ECQQD9rXwfbr9GKh9QMNvnwo9xxyVl4kI88iq0X6G4qVXo1Tv6/DBDJNkX1mbXKFYL5NOW1waZzR+Z/XcKWAmUT8J9AkEA8i0WT/ieNsF3IuFvrIYG4WUadbUqObcYP4Y7Vt836zggRbu0qvYiqAv92Leruaq3ZN1khxp6gZKl/OJHXc5xzQJACqr1AU1i9cxnrLOhS8m+xoYdaH9vUajNavBqmJ1mY3g0IYXhcbFm/72gbYPgundQ/pLkUCt0HMGv89tn67i+8QJBALV6UgkVnsIbkkKCOyRGv2syT3S7kOv1J+eamGcOGSJcSdrXwZiHoArcCZrYcIhOxOWB/m47ymfE1Dw/+QjzxlUCQCmnGFUO9zN862mKYjEkjDN65n1IUB9Fmc1msHkIZAQaQknmxmCIOHC75u4W0PGRyVzq8KkxpNBq62ICl7xmsPM=");
//		fundoutRequestDO.setSign(sign);
		 FundoutTradeResponse walletFundout = fundoutService.walletFundout(fundoutRequestDO);
		System.out.println(walletFundout);
		Assert.assertEquals(true, walletFundout.isSuccess());
	}
	@Test
	public void voucherManagerTest() throws ManagerException {
		SimpleOrderVoucherInfo simpleInfo = new SimpleOrderVoucherInfo();
        GatewayAccessInfo accessInfo = new GatewayAccessInfo();
        accessInfo.setService("fundout");
        accessInfo.setInputCharset(INPUT_CHARSET);
        accessInfo.setPartnerId("188888888888");
       // accessInfo.setNotifyUrl("http://m1.docker.souche.com:20002/auction/callback");
        accessInfo.setSign("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO/6rPCvyCC+IMalLzTy3cVBz/+wamCFNiq9qKEilEBDTttP7Rd/GAS51lsfCrsISbg5td/w25+wulDfuMbjjlW9Afh0p7Jscmbo1skqIOIUPYfVQEL687B0EmJufMlljfu52b2efVAyWZF9QBG1vx/AJz1EVyfskMaYVqPiTesZAgMBAAECgYEAtVnkk0bjoArOTg/KquLWQRlJDFrPKP3CP25wHsU4749t6kJuU5FSH1Ao81d0Dn9m5neGQCOOdRFi23cV9gdFKYMhwPE6+nTAloxI3vb8K9NNMe0zcFksva9c9bUaMGH2p40szMoOpO6TrSHO9Hx4GJ6UfsUUqkFFlN76XprwE+ECQQD9rXwfbr9GKh9QMNvnwo9xxyVl4kI88iq0X6G4qVXo1Tv6/DBDJNkX1mbXKFYL5NOW1waZzR+Z/XcKWAmUT8J9AkEA8i0WT/ieNsF3IuFvrIYG4WUadbUqObcYP4Y7Vt836zggRbu0qvYiqAv92Leruaq3ZN1khxp6gZKl/OJHXc5xzQJACqr1AU1i9cxnrLOhS8m+xoYdaH9vUajNavBqmJ1mY3g0IYXhcbFm/72gbYPgundQ/pLkUCt0HMGv89tn67i+8QJBALV6UgkVnsIbkkKCOyRGv2syT3S7kOv1J+eamGcOGSJcSdrXwZiHoArcCZrYcIhOxOWB/m47ymfE1Dw/+QjzxlUCQCmnGFUO9zN862mKYjEkjDN65n1IUB9Fmc1msHkIZAQaQknmxmCIOHC75u4W0PGRyVzq8KkxpNBq62ICl7xmsPM=");
        accessInfo.setSignType(SIGN_TYPE);
        simpleInfo.setAccess(JSON.toJSONString(accessInfo));
        //simpleInfo.setAccess(null);
        simpleInfo.setAccessType(GatewayAccessInfo.accessType());
        simpleInfo.setVoucherNo(null);
        simpleInfo.setSource("188888888888");
        simpleInfo.setSourceVoucherNo(UUIDUtil.getID());
        simpleInfo.setProductCode(PRODUCT_CODE);
        simpleInfo.setAmount(new Money("0.01"));
        simpleInfo.setPayerId("200000000056");
        simpleInfo.setRequestTime(new Date());
        simpleInfo.setExtension("");
        simpleInfo.setGmtCreate(new Date());
        simpleInfo.setGmtModified(new Date());
        
        voucherManager.recordUnifiedVoucher(simpleInfo);
	}
}
