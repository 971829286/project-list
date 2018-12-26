package com.souche.bmgateway.core.service.voucher;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.voucher.service.facade.domain.access.GatewayAccessInfo;
import com.netfinworks.voucher.service.facade.domain.enums.ControlType;
import com.netfinworks.voucher.service.facade.domain.voucher.*;
import com.souche.bmgateway.core.constant.AccessInfo;
import com.souche.bmgateway.core.enums.ServiceKind;
import com.souche.bmgateway.core.service.dto.*;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * @author XuJinNiu
 * @since 2018-12-18
 */
public class VoucherConvert {

	/**
	 * 基本参数转换
	 * 
	 * @param partnerId
	 * @param service
	 * @return
	 */
	private static GatewayAccessInfo baseFrom(String partnerId, String service, String notifyUrl) {
		GatewayAccessInfo accessInfo = new GatewayAccessInfo();
		accessInfo.setInputCharset(AccessInfo.INPUT_CHARSET);
		accessInfo.setPartnerId(partnerId);
		accessInfo.setSign(AccessInfo.SING);
		accessInfo.setSignType(AccessInfo.SIGN_TYPE);
		accessInfo.setService(service);
		accessInfo.setNotifyUrl(notifyUrl);
		return accessInfo;
	}

	/**
	 * 构建支付凭证
	 *
	 * @param walletFundOutDTO
	 * @return
	 */
	public static PaymentVoucherInfo convertPaymentVoucher(WalletFundoutDTO walletFundOutDTO) {
		GatewayAccessInfo accessInfo = baseFrom(walletFundOutDTO.getPartnerId(), ServiceKind.fundout.getCode(),
				walletFundOutDTO.getNotifyUrl());
		PaymentVoucherInfo paymentVoucherInfo = new PaymentVoucherInfo();
		paymentVoucherInfo.setAccess(JSON.toJSONString(accessInfo));
		paymentVoucherInfo.setAccessType(GatewayAccessInfo.accessType());
		paymentVoucherInfo.setVoucherNo(null);
		paymentVoucherInfo.setSource(walletFundOutDTO.getPartnerId());
		paymentVoucherInfo.setSourceVoucherNo(walletFundOutDTO.getOutTradeNo());
		paymentVoucherInfo.setProductCode(walletFundOutDTO.getBizProductCode());
		paymentVoucherInfo.setPaymentDetail(JSON.toJSONString(walletFundOutDTO));
		paymentVoucherInfo.setRelatedSourceVoucherNo(walletFundOutDTO.getTradeVoucherNo());
		paymentVoucherInfo.setExtension(walletFundOutDTO.getExtension());
		paymentVoucherInfo.setGmtCreate(new Date());
		paymentVoucherInfo.setGmtModified(new Date());
		return paymentVoucherInfo;
	}

	/**
	 * 构建统一交易凭证
	 *
	 * @param walletFundOutDTO
	 * @return
	 */
	public static SimpleOrderVoucherInfo convert(WalletFundoutDTO walletFundOutDTO) {
		SimpleOrderVoucherInfo simpleInfo = new SimpleOrderVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(walletFundOutDTO.getPartnerId(), ServiceKind.fundout.getCode(),
				walletFundOutDTO.getNotifyUrl());
		simpleInfo.setAccess(JSON.toJSONString(accessInfo));
		simpleInfo.setAccessType(GatewayAccessInfo.accessType());
		simpleInfo.setVoucherNo(null);
		simpleInfo.setSource(walletFundOutDTO.getPartnerId());
		simpleInfo.setSourceVoucherNo(walletFundOutDTO.getOutTradeNo());
		simpleInfo.setProductCode(walletFundOutDTO.getBizProductCode());
		simpleInfo.setAmount(new Money(walletFundOutDTO.getAmount()));
		simpleInfo.setPayerId(walletFundOutDTO.getMemberId());
		simpleInfo.setRequestTime(new Date());
		simpleInfo.setExtension(walletFundOutDTO.getExtension());
		simpleInfo.setGmtCreate(new Date());
		simpleInfo.setGmtModified(new Date());
		return simpleInfo;
	}

	/**
	 * 构建统一交易凭证
	 *
	 * @param walletDepositDTO
	 * @return
	 */
	public static SimpleOrderVoucherInfo convertSimpleOrderVoucher(WalletDepositDTO walletDepositDTO) {
		SimpleOrderVoucherInfo simpleInfo = new SimpleOrderVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(walletDepositDTO.getPartnerId(), ServiceKind.create_deposit.getCode(),
				walletDepositDTO.getNotifyUrl());
		simpleInfo.setAccess(JSON.toJSONString(accessInfo));
		simpleInfo.setAccessType(GatewayAccessInfo.accessType());

		simpleInfo.setVoucherNo(null);
		simpleInfo.setSource(walletDepositDTO.getPartnerId());
		simpleInfo.setSourceVoucherNo(walletDepositDTO.getOutTradeNo());
		simpleInfo.setProductCode(walletDepositDTO.getBizProductCode());
		simpleInfo.setAmount(new Money(walletDepositDTO.getAmount()));
		simpleInfo.setPayeeId(walletDepositDTO.getMemberId());
		simpleInfo.setPayerId(walletDepositDTO.getMemberId());
		simpleInfo.setRequestTime(new Date());
		simpleInfo.setGmtCreate(new Date());
		simpleInfo.setGmtModified(new Date());
		simpleInfo.setDescription(walletDepositDTO.getRemark());
		simpleInfo.setExtension(walletDepositDTO.getExtension());
		return simpleInfo;
	}

	/**
	 * 构建支付凭证
	 *
	 * @param walletDepositDTO
	 * @return
	 */
	public static PaymentVoucherInfo convertPaymentVoucher(WalletDepositDTO walletDepositDTO) {
		PaymentVoucherInfo paymentVoucherInfo = new PaymentVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(walletDepositDTO.getPartnerId(), ServiceKind.create_deposit.getCode(),
				walletDepositDTO.getNotifyUrl());
		paymentVoucherInfo.setAccess(JSON.toJSONString(accessInfo));
		paymentVoucherInfo.setAccessType(GatewayAccessInfo.accessType());

		paymentVoucherInfo.setVoucherNo(null);
		paymentVoucherInfo.setSource(walletDepositDTO.getPartnerId());
		paymentVoucherInfo.setSourceVoucherNo(walletDepositDTO.getTradeVoucherNo());
		paymentVoucherInfo.setRelatedSourceVoucherNo(walletDepositDTO.getOutTradeNo());
		paymentVoucherInfo.setProductCode(walletDepositDTO.getBizProductCode());
		paymentVoucherInfo.setPaymentDetail(JSON.toJSONString(walletDepositDTO));
		paymentVoucherInfo.setGmtCreate(new Date());
		paymentVoucherInfo.setGmtModified(new Date());
		paymentVoucherInfo.setMemo(null);
		paymentVoucherInfo.setExtension(walletDepositDTO.getPayExtension());
		return paymentVoucherInfo;
	}

	/**
	 * 创建交易单并支付-构建交易凭证信息
	 *
	 * @param tradeInfoDTO
	 * @return
	 */
	public static List<TradeVoucherInfo> convertTradeVoucher(TradeInfoDTO tradeInfoDTO) {
		List<TradeVoucherInfo> tradeVoucherInfoList = Lists.newArrayList();
		TradeVoucherInfo tradeVoucherInfo = new TradeVoucherInfo();
		tradeVoucherInfo.setSubject(tradeInfoDTO.getTradeMemo());
		tradeVoucherInfo.setOtherParties(null);
		tradeVoucherInfo.setValidDate(tradeInfoDTO.getGmtInvalid());
		tradeVoucherInfo.setSellerMemberId(tradeInfoDTO.getSellerId());
		tradeVoucherInfo.setBuyerMemberId(tradeInfoDTO.getBuyerId());
		tradeVoucherInfo.setRoyaltyParameters(JSON.toJSONString(tradeInfoDTO.getSplitParameterList()));
		tradeVoucherInfo.setBizNo(tradeInfoDTO.getBizNo());
		tradeVoucherInfo.setBody(tradeInfoDTO.getProductDesc());
		tradeVoucherInfo.setShowUrl(tradeInfoDTO.getShowUrl());
		tradeVoucherInfo.setQuantity(1L);
		tradeVoucherInfo.setPrice(new Money(tradeInfoDTO.getTradeAmount().getAmount()));
		tradeVoucherInfo.setTotalAmount(new Money(tradeInfoDTO.getTradeAmount().getAmount()));
		tradeVoucherInfo.setEnsureAmount(null);
		if (tradeInfoDTO.getCoinAmount() != null) {
			tradeVoucherInfo.setGoldCoin(new Money(tradeInfoDTO.getCoinAmount().getAmount()));
		}
		tradeVoucherInfo.setDepositAmount(null);
		tradeVoucherInfo.setDepositNo(null);
		tradeVoucherInfo.setSubmitTime(new DateTime(new Date()).toString("yyyy-MM-dd"));
		tradeVoucherInfo.setRelatedSourceVoucherNo(null);
		tradeVoucherInfo.setGmtCreate(new Date());
		tradeVoucherInfo.setGmtModified(new Date());
		tradeVoucherInfo.setMemo(tradeInfoDTO.getTradeMemo());
		tradeVoucherInfo.setVoucherNo(null);
		tradeVoucherInfo.setSourceVoucherNo(tradeInfoDTO.getTradeSourceVoucherNo());
		tradeVoucherInfo.setSource(tradeInfoDTO.getPartnerId());
		GatewayAccessInfo accessInfo = baseFrom(tradeInfoDTO.getPartnerId(), ServiceKind.create_instant_trade.getCode(),
				tradeInfoDTO.getNotifyUrl());
		tradeVoucherInfo.setAccess(JSON.toJSONString(accessInfo));
		tradeVoucherInfo.setAccessType(GatewayAccessInfo.accessType());
		tradeVoucherInfo.setProductCode(tradeInfoDTO.getBizProductCode());
		tradeVoucherInfo.setExtension(tradeInfoDTO.getExtension());
		tradeVoucherInfoList.add(tradeVoucherInfo);
		return tradeVoucherInfoList;
	}

	/**
	 * 创建交易单并支付-构建支付凭证信息
	 *
	 * @param tradeInfoDTO
	 * @return
	 */
	public static PaymentVoucherInfo convertPaymentVoucher(TradeInfoDTO tradeInfoDTO) {
		PaymentVoucherInfo paymentVoucherInfo = new PaymentVoucherInfo();
		paymentVoucherInfo.setPaymentDetail(JSON.toJSONString(tradeInfoDTO.getPayMethodList()));
		paymentVoucherInfo.setRelatedSourceVoucherNo(null);
		paymentVoucherInfo.setGmtCreate(new Date());
		paymentVoucherInfo.setGmtModified(new Date());
		paymentVoucherInfo.setMemo(null);
		paymentVoucherInfo.setVoucherNo(null);
		paymentVoucherInfo.setSourceVoucherNo(tradeInfoDTO.getTradeVoucherNo());
		paymentVoucherInfo.setSource(tradeInfoDTO.getPartnerId());
		GatewayAccessInfo accessInfo = baseFrom(tradeInfoDTO.getPartnerId(), ServiceKind.create_instant_trade.getCode(),
				tradeInfoDTO.getNotifyUrl());
		paymentVoucherInfo.setAccess(JSON.toJSONString(accessInfo));
		paymentVoucherInfo.setAccessType(GatewayAccessInfo.accessType());
		paymentVoucherInfo.setProductCode(tradeInfoDTO.getBizProductCode());
		paymentVoucherInfo.setExtension(tradeInfoDTO.getPayExtension());
		return paymentVoucherInfo;
	}

	/**
	 * 基于交易单支付-构建支付凭证信息
	 *
	 * @param instantPayDTO
	 * @return
	 */
	public static PaymentVoucherInfo convertPaymentVoucher(InstantPayDTO instantPayDTO) {
		PaymentVoucherInfo paymentVoucherInfo = new PaymentVoucherInfo();
		paymentVoucherInfo.setPaymentDetail(JSON.toJSONString(instantPayDTO.getPayMethodList()));
		paymentVoucherInfo.setRelatedSourceVoucherNo(instantPayDTO.getPaymentSourceVoucherNo());
		paymentVoucherInfo.setGmtCreate(new Date());
		paymentVoucherInfo.setGmtModified(new Date());
		paymentVoucherInfo.setMemo(null);
		paymentVoucherInfo.setVoucherNo(null);
		paymentVoucherInfo.setSourceVoucherNo(instantPayDTO.getTradeVoucherNo());
		paymentVoucherInfo.setSource(instantPayDTO.getPartnerId());
		GatewayAccessInfo accessInfo = baseFrom(instantPayDTO.getPartnerId(), ServiceKind.create_pay.getCode(), null);
		paymentVoucherInfo.setAccess(JSON.toJSONString(accessInfo));
		paymentVoucherInfo.setAccessType(GatewayAccessInfo.accessType());
		paymentVoucherInfo.setProductCode(instantPayDTO.getBizProductCode());
		paymentVoucherInfo.setExtension(instantPayDTO.getExtension());
		return paymentVoucherInfo;
	}

	/**
	 * 构建转账交易凭证
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	public static SimpleOrderVoucherInfo convertTransferTradeVoucher(BalanceTransferDTO balanceTransferDTO) {
		SimpleOrderVoucherInfo simpleInfo = new SimpleOrderVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(balanceTransferDTO.getPartnerId(),
				ServiceKind.balance_transfer.getCode(), balanceTransferDTO.getNotifyUrl());
		simpleInfo.setAccess(JSON.toJSONString(accessInfo));
		simpleInfo.setAccessType(GatewayAccessInfo.accessType());
		simpleInfo.setProductCode(balanceTransferDTO.getProductCode());
		simpleInfo.setSource(balanceTransferDTO.getPartnerId());
		simpleInfo.setSourceVoucherNo(balanceTransferDTO.getOuterTradeNo());
		simpleInfo.setAmount(new Money(balanceTransferDTO.getTransferAmount()));
		simpleInfo.setPayerId(balanceTransferDTO.getFundoutMemberId());
		simpleInfo.setPayeeId(balanceTransferDTO.getFundinMemberId());
		simpleInfo.setRequestTime(new Date());
		simpleInfo.setMemo(balanceTransferDTO.getTradeMemo());
		simpleInfo.setExtension(balanceTransferDTO.getExtension());
		return simpleInfo;
	}

	/**
	 * 构建转账支付凭证
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	public static PaymentVoucherInfo convertTransferPaymentVoucher(BalanceTransferDTO balanceTransferDTO) {
		PaymentVoucherInfo payInfo = new PaymentVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(balanceTransferDTO.getPartnerId(),
				ServiceKind.balance_transfer.getCode(), balanceTransferDTO.getNotifyUrl());
		payInfo.setAccess(JSON.toJSONString(accessInfo));
		payInfo.setAccessType(GatewayAccessInfo.accessType());
		payInfo.setPaymentDetail("BALANCE"); // 固定值
		payInfo.setProductCode(balanceTransferDTO.getProductCode());
		payInfo.setSource(balanceTransferDTO.getPartnerId());
		payInfo.setSourceVoucherNo(balanceTransferDTO.getTradeVoucherNo());
		payInfo.setRelatedSourceVoucherNo(balanceTransferDTO.getOuterTradeNo());
		payInfo.setMemo(balanceTransferDTO.getTradeMemo());
		return payInfo;
	}

	/**
	 * 构建退款凭证信息
	 *
	 * @param walletRefundDTO
	 * @return
	 */
	public static RefundVoucherInfo convertRefundVoucher(WalletRefundDTO walletRefundDTO) {
		RefundVoucherInfo refundVoucher = new RefundVoucherInfo();
		refundVoucher.setProductCode(walletRefundDTO.getProductCode());
		refundVoucher.setRefundAmount(walletRefundDTO.getRefundAmount());
		// 订金
		refundVoucher.setDepositAmount(null);
		refundVoucher.setRefundEnsureAmount(null);
		refundVoucher.setGoldCoin(walletRefundDTO.getGoldCoinAmount());
		refundVoucher.setMemo(walletRefundDTO.getMemo());
		refundVoucher.setExtension(walletRefundDTO.getExtension());
		refundVoucher.setRoyaltyParameters(JSON.toJSONString(walletRefundDTO.getSplitParameterList()));
		GatewayAccessInfo accessInfo = baseFrom(walletRefundDTO.getPartnerId(), ServiceKind.create_refund.getCode(),
				walletRefundDTO.getNotifyUrl());
		refundVoucher.setAccess(JSON.toJSONString(accessInfo));
		refundVoucher.setAccessType(GatewayAccessInfo.accessType());
		refundVoucher.setSource(walletRefundDTO.getPartnerId());
		refundVoucher.setSourceVoucherNo(walletRefundDTO.getTradeSourceVoucherNo());
		refundVoucher.setRelatedSourceVoucherNo(walletRefundDTO.getOrigOuterTradeNo());
		return refundVoucher;
	}

	/**
	 * 构建确认凭证信息
	 *
	 * @param walletFrozenDTO
	 * @return
	 */
	public static ControlVoucherInfo convertControlVoucherInfo(WalletFrozenDTO walletFrozenDTO) {
		GatewayAccessInfo accessInfo = baseFrom(walletFrozenDTO.getPartnerId(), ServiceKind.frozen_funds.getCode(),
				null);
		ControlVoucherInfo ctrlInfo = new ControlVoucherInfo();
		ctrlInfo.setAccess(JSON.toJSONString(accessInfo));
		ctrlInfo.setAccessType(GatewayAccessInfo.accessType());
		ctrlInfo.setProductCode(walletFrozenDTO.getBizProductCode());
		ctrlInfo.setSource(walletFrozenDTO.getPartnerId());
		ctrlInfo.setSourceVoucherNo(walletFrozenDTO.getOuterTradeNo());
		ctrlInfo.setMemo(walletFrozenDTO.getMemo());
		ctrlInfo.setRequestTime(new Date());
		ctrlInfo.setControlType(ControlType.FROZEN.getCode());
		return ctrlInfo;
	}

	/**
	 * 构建确认凭证信息
	 *
	 * @param walletUnFrozenDTO
	 * @return
	 */
	public static ControlVoucherInfo convertControlVoucherInfo(WalletUnFrozenDTO walletUnFrozenDTO) {
		ControlVoucherInfo ctrlInfo = new ControlVoucherInfo();
		GatewayAccessInfo accessInfo = baseFrom(walletUnFrozenDTO.getPartnerId(), ServiceKind.unfreeze_funds.getCode(),
				null);
		ctrlInfo.setAccess(JSON.toJSONString(accessInfo));
		ctrlInfo.setAccessType(GatewayAccessInfo.accessType());
		ctrlInfo.setProductCode(walletUnFrozenDTO.getBizProductCode());
		ctrlInfo.setSource(walletUnFrozenDTO.getPartnerId());
		ctrlInfo.setSourceVoucherNo(walletUnFrozenDTO.getOuterTradeNo());
		ctrlInfo.setMemo(walletUnFrozenDTO.getMemo());
		ctrlInfo.setRequestTime(new Date());
		ctrlInfo.setRelatedSourceVoucherNo(walletUnFrozenDTO.getOrigOuterTradeNo());
		ctrlInfo.setControlType(ControlType.FROZEN.getCode()); //
		return ctrlInfo;
	}

}
