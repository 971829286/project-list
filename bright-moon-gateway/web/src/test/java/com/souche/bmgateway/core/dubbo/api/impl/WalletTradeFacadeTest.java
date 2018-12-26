package com.souche.bmgateway.core.dubbo.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.dubbo.api.WalletTradeFacade;
import com.souche.bmgateway.enums.PayMethodEnums;
import com.souche.bmgateway.model.request.trade.FundoutTradeRequest;
import com.souche.bmgateway.model.request.trade.PayMethodInfo;
import com.souche.bmgateway.model.request.trade.WalletDepositRequest;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;
import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import com.souche.optimus.common.util.UUIDUtil;
/**
 * 
 * @author luobing 2018/12/18
 *
 */
public class WalletTradeFacadeTest extends BaseTest {
	    // 调用接口，固定参数
		private static String PRODUCT_CODE = "10213";
		private static String VERSION = "1.0";
		private static String INPUT_CHARSET = "UTF-8";
		private static String FEE = "0.0";// 提现所需费用

		private static String CARD_TYPE = "DC";// 卡类型"DC"
		private static String SIGN_TYPE = "RSA";// 签名类型
		private static String FUNDOUT_GRADE = "0";
		private static String SERVICE = "fundout";

		@Resource
		private WalletTradeFacade walletTradeFacade;
		
	
	// 提现
		@Test
		public void walletFundoutTest() {
			FundoutTradeRequest fundoutTradeRequest = new FundoutTradeRequest();
			String cardNo = "P193-1755";
			fundoutTradeRequest.setOutTradeNo(UUIDUtil.getID());
			fundoutTradeRequest.setCardNo(cardNo);
			fundoutTradeRequest.setBizProductCode(PRODUCT_CODE);
			fundoutTradeRequest.setMemberId("200000000056");
			// fundoutRequestDO.setAccountNo("200100100120000000005600001");
			fundoutTradeRequest.setPartnerId("188888888888");
			// fundoutRequestDO.sets(SERVICE);
			fundoutTradeRequest.setVersion(VERSION);
			fundoutTradeRequest.setSignType(SIGN_TYPE);
			fundoutTradeRequest.setInputCharset(INPUT_CHARSET);
			//fundoutTradeRequest.setBankAccountName("浙江大搜车融资租赁有限公司");
			fundoutTradeRequest.setBankCardId("22");
			fundoutTradeRequest.setNotifyUrl("http://m1.docker.souche.com:20002/auction/callback");
			fundoutTradeRequest.setAmount("0.01");

			 FundoutTradeResponse walletFundout = walletTradeFacade.walletFundout(fundoutTradeRequest);
			System.out.println(walletFundout);
			Assert.assertEquals(true, walletFundout.isSuccess());
		}
		
		// 充值
		@Test
		public void depositTest() {
			WalletDepositRequest walletDepositRequest = new WalletDepositRequest();
			walletDepositRequest.setAccountIdentity("");
			walletDepositRequest.setAccountType("201");
			walletDepositRequest.setAmount("0.01");
			walletDepositRequest.setBizProductCode("10101");
			// walletDepositRequest.setExtension("{\"cardNo\":\"6226095711939460\"}");
			walletDepositRequest.setExtension("{\"BANK_CODE\":\"TMALLPOS\",\"COMPANY_OR_PERSONAL\":\"C\",\"DBCR\":\"GC\"}");
			walletDepositRequest.setMemberId("200000000056");
			walletDepositRequest.setMemo("");
			walletDepositRequest.setNotifyUrl("www.souche.com/notify");
			walletDepositRequest.setOutTradeNo(UUIDUtil.getID());
			walletDepositRequest.setPartnerId("188888888888");
			PayMethodInfo payMethodInfo = new PayMethodInfo();
			payMethodInfo.setPayMethod(PayMethodEnums.POS.name());
			payMethodInfo.setAmount("0.01");
			payMethodInfo.setMemo("TMALLPOS,C,GC");
			walletDepositRequest.setPayMethod(payMethodInfo);
			walletDepositRequest.setRemark("");
			//walletDepositRequest.setReturnUrl("www.souche.com");
			WalletDepositResponse walletDeposit = walletTradeFacade.walletDeposit(walletDepositRequest);
			System.out.println(walletDeposit);
		}

}
