package com.souche.bmgateway.core.service.deposit.impl;

import com.netfinworks.common.util.money.Money;
import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.PaymentInfo;
import com.netfinworks.payment.common.v2.enums.PayMode;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;
import com.souche.bmgateway.enums.PayMethodEnums;


/**
 * 充值参数转换
 *
 * @author luobing 2018/11/20
 */
public class WalletDepositConvert {

	/**
	 * 构建充值请求参数
	 *
	 * @param walletDepositDTO
	 * @return
	 */
	public static DepositRequest convert(WalletDepositDTO walletDepositDTO) {
		DepositRequest depositRequest = new DepositRequest();
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setAmount(new Money(walletDepositDTO.getAmount()));
		paymentInfo.setExtension(walletDepositDTO.getPayExtension());
		paymentInfo.setGmtSubmit(walletDepositDTO.getGmtSubmit());
		paymentInfo.setPaymentChannel(walletDepositDTO.getPaymentChannel());
		paymentInfo.setPaymentVoucherNo(walletDepositDTO.getPaymentVoucherNo());
		PayMethodEnums payMethod = walletDepositDTO.getPayMethod();
		paymentInfo.setPayMode(PayMode.getByCode(payMethod.name()));
		depositRequest.setPaymentInfo(paymentInfo);

		depositRequest.setAccessChannel(walletDepositDTO.getAccessChannel());
		depositRequest.setAccountNo(walletDepositDTO.getAccountNo());
		depositRequest.setdepositProduct(walletDepositDTO.getBizProductCode());
		depositRequest.setExtension(walletDepositDTO.getExtension());
		depositRequest.setMemberId(walletDepositDTO.getMemberId());
		depositRequest.setRemark(walletDepositDTO.getRemark());
		depositRequest.setTradeVoucherNo(walletDepositDTO.getOutTradeNo());
		return depositRequest;
	}
}
