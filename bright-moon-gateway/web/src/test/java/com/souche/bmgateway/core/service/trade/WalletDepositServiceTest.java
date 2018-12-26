package com.souche.bmgateway.core.service.trade;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.service.deposit.WalletDepositService;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;
import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import com.souche.optimus.common.util.UUIDUtil;

import org.junit.Test;

import java.util.Date;

import javax.annotation.Resource;

/**
 * @author luobing 18/11/22
 *
 */
public class WalletDepositServiceTest extends BaseTest {

	@Resource
	private WalletDepositService walletDepositService;

	@Test
	public void deposit() {
		WalletDepositDTO walletDepositDTO = new WalletDepositDTO();
		walletDepositDTO.setAccessChannel("WEB");
		// walletDepositDTO.setAccountIdentity(accountIdentity);
		walletDepositDTO.setAccountNo("200100100120000000005600001");
		// walletDepositDTO.setAccountType(accountType);//accountNo =
		// 200100100120000000005600001
		walletDepositDTO.setAmount("0.01");
		walletDepositDTO.setBizProductCode("10101");
		walletDepositDTO.setExtension("{\"BANK_CODE\":\"TMALLPOS\",\"COMPANY_OR_PERSONAL\":\"C\",\"DBCR\":\"GC\"}");
		walletDepositDTO.setGmtSubmit(new Date());
		walletDepositDTO.setMemberId("200000000056");
		//walletDepositDTO.setMemo("{\"BANK_CODE\":\"TMALLPOS\",\"COMPANY_OR_PERSONAL\":\"C\",\"DBCR\":\"GC\"}");
		walletDepositDTO.setNotifyUrl("www.souche.com/notify");
		walletDepositDTO.setOutTradeNo(UUIDUtil.getID());
		walletDepositDTO.setPartnerId("188888888888");
		walletDepositDTO.setPayExtension("{\"BANK_CODE\":\"TMALLPOS\",\"COMPANY_OR_PERSONAL\":\"C\",\"DBCR\":\"GC\"}");
		walletDepositDTO.setPaymentChannel("WEB");
		walletDepositDTO.setPaymentVoucherNo(UUIDUtil.getID());
		//walletDepositDTO.setPayMethod(PayMethodEnums.POS);
		//walletDepositDTO.setReturnUrl("www.souche.com");
		walletDepositDTO.setTradeVoucherNo(UUIDUtil.getID());

		WalletDepositResponse walletDeposit = walletDepositService.walletDeposit(walletDepositDTO);
		System.out.println(walletDeposit);
	}
}
