package com.souche.bmgateway.core.manager;

import com.netfinworks.common.domain.OperationEnvironment;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.deposit.api.DepositService;
import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.netfinworks.deposit.api.domain.PaymentInfo;
import com.netfinworks.payment.common.v2.enums.PayMode;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.manager.weijin.DepositManager;
import com.souche.optimus.common.util.UUIDUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 
 * @author luobing 18/11/21
 *
 */

public class DepositManagerTest extends BaseTest {

	@Resource
	private DepositManager depositManager;
	
	@Resource
	private DepositService depositService;

	@Test
	public void deposit() {
		OperationEnvironment operationEnvironment = new OperationEnvironment();
		operationEnvironment.setClientId("ffgateway");
		DepositRequest depositRequest = new DepositRequest();
		depositRequest.setAccessChannel(Constants.ACCESS_CHANNEL);
		depositRequest.setAccountNo("200100100120000000005600001");
		depositRequest.setExtension(null);
		depositRequest.setMemberId("200000000056");

		PaymentInfo paymentInfo = new PaymentInfo();
		Money money = new Money();
		money.setAmount("0.01");
		paymentInfo.setAmount(money);

		paymentInfo.setExtension("{\"BANK_CODE\":\"TMALLPOS\",\"COMPANY_OR_PERSONAL\":\"C\",\"DBCR\":\"GC\"}");
		paymentInfo.setGmtSubmit(new Date());
		

		paymentInfo.setPaymentChannel("TMALLPOS");
		paymentInfo.setPayMode(PayMode.POS);
		paymentInfo.setPaymentVoucherNo(UUIDUtil.getID());


		depositRequest.setPaymentInfo(paymentInfo);
		depositRequest.setRemark("");
		depositRequest.setTradeVoucherNo(UUIDUtil.getID());
		depositRequest.setdepositProduct("10101");
		DepositResponse deposit;
		try {
			//deposit = depositService.deposit(depositRequest, operationEnvironment);
			deposit = depositManager.deposit(depositRequest);
			System.out.println(deposit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void moneyTest() {
		Money money = new Money("12.22");
		System.out.println(money.toString());
		
		String format = String.format("%s,C,DC", "alpp");
		System.out.println(format);
	}

}
