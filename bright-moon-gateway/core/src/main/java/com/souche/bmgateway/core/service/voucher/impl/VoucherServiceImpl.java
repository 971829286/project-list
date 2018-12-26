package com.souche.bmgateway.core.service.voucher.impl;

import com.netfinworks.voucher.service.facade.domain.voucher.ControlVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.PaymentVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.TradeVoucherInfo;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.exception.VoucherException;
import com.souche.bmgateway.core.manager.weijin.VoucherManager;
import com.souche.bmgateway.core.service.dto.*;
import com.souche.bmgateway.core.service.voucher.VoucherConvert;
import com.souche.bmgateway.core.service.voucher.VoucherService;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;
import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zs.
 * Created on 18/11/23.
 */
@Service("voucherService")
@Slf4j(topic = "service")
public class VoucherServiceImpl implements VoucherService {

	@Resource
	private VoucherManager voucherManager;


	@Override
	public TradeVoucherInfo queryTradeVoucherInfo(String tradeVoucherNo) {
		TradeVoucherInfo tradeVoucherInfo = null;
		try {
			tradeVoucherInfo = voucherManager.queryTradeByVoucherNo(tradeVoucherNo);
		} catch (ManagerException e) {
			log.error("查询交易凭证异常，请求参数：{}, 异常：{}", tradeVoucherNo, e);
		}
		return tradeVoucherInfo;
	}

	/**
	 * 记录交易支付凭证
	 *
	 * @param tradeInfoDTO
	 */
	@Override
	public void setTradeInfoVoucher(TradeInfoDTO tradeInfoDTO) throws VoucherException {
		try {
			Map<String, String> tradeVoucher = voucherManager.batchRecordTradeVoucher(VoucherConvert.convertTradeVoucher(tradeInfoDTO));
			tradeInfoDTO.setTradeVoucherNo(tradeVoucher.get(tradeInfoDTO.getTradeSourceVoucherNo()));
			if (tradeInfoDTO.getPayMethodList() != null && !tradeInfoDTO.getPayMethodList().isEmpty()) {
				String paymentVoucherNo = voucherManager.recordPaymentVoucher(VoucherConvert.convertPaymentVoucher(tradeInfoDTO));
				tradeInfoDTO.setPaymentVoucherNo(paymentVoucherNo);
			}
		} catch (ManagerException e) {
			log.error("记录支付凭证异常，请求参数：{}, 异常：", tradeInfoDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}

	}

	/**
	 * 记录支付凭证号
	 *
	 * @param instantPayDTO
	 * @throws VoucherException
	 */
	@Override
	public void setInstantPayVoucher(InstantPayDTO instantPayDTO) throws VoucherException {
		try {
			//记录支付凭证号
			String paymentVoucherNo = voucherManager.recordPaymentVoucher(VoucherConvert.convertPaymentVoucher(instantPayDTO));
			instantPayDTO.setPaymentVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("记录支付凭证异常，请求参数：{}, 异常：", instantPayDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}

	/**
	 * 记录转账凭证号
	 *
	 * @param balanceTransferDTO
	 * @throws VoucherException
	 */
	@Override
	public void setBalanceTransferVoucher(BalanceTransferDTO balanceTransferDTO) throws VoucherException {
		try {
			String tradeVoucherNo = voucherManager.recordUnifiedVoucher(VoucherConvert.convertTransferTradeVoucher(balanceTransferDTO));
			balanceTransferDTO.setTradeVoucherNo(tradeVoucherNo);
			String paymentVoucherNo = voucherManager.recordPaymentVoucher(VoucherConvert.convertTransferPaymentVoucher(balanceTransferDTO));
			balanceTransferDTO.setPayVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("记录转账凭证异常，请求参数：{}, 异常：", balanceTransferDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}


	/**
	 * 记录退款凭证
	 *
	 * @param walletRefundDTO
	 * @throws VoucherException
	 */
	@Override
	public void setWalletRefundVoucher(WalletRefundDTO walletRefundDTO) throws VoucherException {
		try {
			String refundVoucher = voucherManager.recordRefundVoucher(VoucherConvert.convertRefundVoucher(walletRefundDTO));
			walletRefundDTO.setRefundVoucherNo(refundVoucher);
		} catch (ManagerException e) {
			log.error("记录退款凭证异常，请求参数：{}, 异常：", walletRefundDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}

	/**
	 * 记录冻结凭证
	 *
	 * @param walletFrozenDTO
	 * @throws VoucherException
	 */
	@Override
	public void setWalletFrozenVoucher(WalletFrozenDTO walletFrozenDTO) throws VoucherException {
		try {
			String paymentVoucherNo = voucherManager.saveControlRecord(VoucherConvert.convertControlVoucherInfo(walletFrozenDTO));
			walletFrozenDTO.setPaymentVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("保存统一凭证异常,请求参数：{}, 异常:", walletFrozenDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}

	/**
	 * 解冻时,获取冻结凭证.
	 *
	 * @param walletUnFrozenDTO
	 * @throws VoucherException
	 */
	@Override
	public void setOrigOuterTradeVoucher(WalletUnFrozenDTO walletUnFrozenDTO) throws VoucherException {
		try {
			String origOuterTradeNo = voucherManager.queryTradeBy(walletUnFrozenDTO.getOuterTradeNo(), walletUnFrozenDTO.getPartnerId(), ControlVoucherInfo.voucherType());
			walletUnFrozenDTO.setOrigOuterTradeNo(origOuterTradeNo);
		} catch (ManagerException e) {
			log.error("解冻账户时:查询交易凭证信息发生异常,请求参数:{},异常:{}", walletUnFrozenDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}

	/**
	 * 记录解冻凭证
	 *
	 * @param walletUnFrozenDTO
	 * @throws VoucherException
	 */
	@Override
	public void setWalletUnFrozenVoucher(WalletUnFrozenDTO walletUnFrozenDTO) throws VoucherException {
		try {
			String paymentVoucherNo = voucherManager.saveControlRecord(VoucherConvert.convertControlVoucherInfo(walletUnFrozenDTO));
			walletUnFrozenDTO.setPaymentVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("解冻账户时:保存统一凭证异常,请求参数:{},异常:{}", walletUnFrozenDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}

	}

	/**
	 * 记录交易凭证号
	 *
	 * @param walletFundOutDTO
	 * @throws VoucherException
	 */
	@Override
	public void setWalletFundOutVoucher(WalletFundoutDTO walletFundOutDTO) throws VoucherException {
		try {
			// 交易凭证号
			SimpleOrderVoucherInfo simpleOrderVoucherInfo = VoucherConvert.convert(walletFundOutDTO);
			String voucherNo = voucherManager.recordUnifiedVoucher(simpleOrderVoucherInfo);
			walletFundOutDTO.setTradeVoucherNo(voucherNo);
		} catch (ManagerException e) {
			log.error("记录凭证异常，请求参数：{}, 异常：", walletFundOutDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}


	/**
	 * 记录支付凭证号
	 *
	 * @param walletFundOutDTO
	 * @throws VoucherException
	 */
	@Override
	public void setPaymentVoucher(WalletFundoutDTO walletFundOutDTO) throws VoucherException {
		try {
			// 支付凭证号
			PaymentVoucherInfo paymentVoucherInfo = VoucherConvert.convertPaymentVoucher(walletFundOutDTO);
			String paymentVoucherNo = voucherManager.recordPaymentVoucher(paymentVoucherInfo);
			walletFundOutDTO.setPaymentVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("记录凭证异常，请求参数：{}, 异常：", walletFundOutDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}

	/**
	 * 交易凭证落地
	 *
	 * @param walletDepositDTO
	 * @throws VoucherException
	 */
	@Override
	public void setTradeVoucher(WalletDepositDTO walletDepositDTO) throws VoucherException {
		// 交易凭证落地
		try {
			SimpleOrderVoucherInfo simpleOrderVoucherInfo = VoucherConvert.convertSimpleOrderVoucher(walletDepositDTO);
			String VoucherNo = voucherManager.recordUnifiedVoucher(simpleOrderVoucherInfo);
			walletDepositDTO.setTradeVoucherNo(VoucherNo);
		} catch (ManagerException e) {
			log.error("记录凭证异常，请求参数：{}, 异常：", walletDepositDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}


	/**
	 * 支付凭证落地
	 *
	 * @param walletDepositDTO
	 * @throws VoucherException
	 */
	@Override
	public void setPaymentVoucher(WalletDepositDTO walletDepositDTO) throws VoucherException {
		try {
			// 支付凭证落地
			PaymentVoucherInfo paymentVoucherInfo = VoucherConvert.convertPaymentVoucher(walletDepositDTO);
			String paymentVoucherNo = voucherManager.recordPaymentVoucher(paymentVoucherInfo);
			walletDepositDTO.setPaymentVoucherNo(paymentVoucherNo);
		} catch (ManagerException e) {
			log.error("记录凭证异常，请求参数：{}, 异常：", walletDepositDTO, e);
			throw new VoucherException(ErrorCodeEnums.VOUCHER_SERVICE_ERROR);
		}
	}


}
