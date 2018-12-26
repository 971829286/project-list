package com.souche.bmgateway.core.service.voucher;

import com.netfinworks.voucher.service.facade.domain.voucher.TradeVoucherInfo;
import com.souche.bmgateway.core.exception.VoucherException;
import com.souche.bmgateway.core.service.dto.*;

/**
 * @author zs.
 * Created on 18/11/23.
 */
public interface VoucherService {

	/**
	 * 查询交易凭证信息
	 *
	 * @param tradeVoucherNo
	 * @return
	 */
	TradeVoucherInfo queryTradeVoucherInfo(String tradeVoucherNo);

	/**
	 * 记录交易支付凭证
	 *
	 * @param tradeInfoDTO
	 */
	void setTradeInfoVoucher(TradeInfoDTO tradeInfoDTO) throws VoucherException;

	/**
	 * 记录支付凭证号
	 *
	 * @param instantPayDTO
	 * @throws VoucherException
	 */
	void setInstantPayVoucher(InstantPayDTO instantPayDTO) throws VoucherException;


	/**
	 * 记录转账凭证号
	 *
	 * @param balanceTransferDTO
	 * @throws VoucherException
	 */
	void setBalanceTransferVoucher(BalanceTransferDTO balanceTransferDTO) throws VoucherException;


	/**
	 * 记录退款凭证
	 *
	 * @param walletRefundDTO
	 * @throws VoucherException
	 */
	void setWalletRefundVoucher(WalletRefundDTO walletRefundDTO) throws VoucherException;


	/**
	 * 记录冻结凭证
	 *
	 * @param walletFrozenDTO
	 * @throws VoucherException
	 */
	void setWalletFrozenVoucher(WalletFrozenDTO walletFrozenDTO) throws VoucherException;


	/**
	 * 解冻时,获取冻结凭证.
	 *
	 * @param walletUnFrozenDTO
	 * @throws VoucherException
	 */
	void setOrigOuterTradeVoucher(WalletUnFrozenDTO walletUnFrozenDTO) throws VoucherException;


	/**
	 * 记录解冻凭证
	 *
	 * @param walletUnFrozenDTO
	 * @throws VoucherException
	 */
	void setWalletUnFrozenVoucher(WalletUnFrozenDTO walletUnFrozenDTO) throws VoucherException;


	/**
	 * 记录交易凭证号
	 *
	 * @param walletFundOutDTO
	 * @throws VoucherException
	 */
	void setWalletFundOutVoucher(WalletFundoutDTO walletFundOutDTO) throws VoucherException;


	/**
	 * 记录支付凭证号
	 *
	 * @param walletFundOutDTO
	 * @throws VoucherException
	 */
	void setPaymentVoucher(WalletFundoutDTO walletFundOutDTO) throws VoucherException;


	/**
	 * 交易凭证落地
	 *
	 * @param walletDepositDTO
	 * @throws VoucherException
	 */
	void setTradeVoucher(WalletDepositDTO walletDepositDTO) throws VoucherException;


	/**
	 * 支付凭证落地
	 *
	 * @param walletDepositDTO
	 * @throws VoucherException
	 */
	void setPaymentVoucher(WalletDepositDTO walletDepositDTO) throws VoucherException;


}
