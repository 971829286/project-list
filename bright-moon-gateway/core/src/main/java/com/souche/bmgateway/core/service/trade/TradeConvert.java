package com.souche.bmgateway.core.service.trade;

import com.google.common.collect.Lists;
import com.netfinworks.common.lang.StringUtil;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.pbs.service.context.vo.PayPricingReq;
import com.netfinworks.pfs.service.payment.request.FundsFreezeRequest;
import com.netfinworks.pfs.service.payment.request.FundsUnFreezeRequest;
import com.netfinworks.tradeservice.facade.enums.TradeType;
import com.netfinworks.tradeservice.facade.model.AcquiringTradeItemDetail;
import com.netfinworks.tradeservice.facade.model.PaymentInfo;
import com.netfinworks.tradeservice.facade.model.TradeItemDetail;
import com.netfinworks.tradeservice.facade.model.paymethod.BalancePayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.souche.bmgateway.enums.PayMethodEnums;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.FundPropTypeEnums;
import com.souche.bmgateway.core.service.dto.*;
import com.souche.bmgateway.core.service.dto.TradeFeeDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zs.
 * Created on 18/11/19.
 */
public class TradeConvert {


	/**
	 * 创建交易单并支付-构建交易请求参数
	 *
	 * @param tradeInfoDTO
	 * @return
	 */
	public static TradeRequest convertTradeReq(TradeInfoDTO tradeInfoDTO) {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setBuyerId(tradeInfoDTO.getBuyerId());
		tradeRequest.setAccessChannel("WEB");
		tradeRequest.setGmtSubmit(new Date());
		tradeRequest.setTradeItemDetailList(getTradeItemDetails(tradeInfoDTO));
		if (tradeInfoDTO.getPayMethodList() != null && !tradeInfoDTO.getPayMethodList().isEmpty()) {
			tradeRequest.setPaymentInfo(getPaymentInfo(tradeInfoDTO));
		}
		tradeRequest.setExtension(tradeInfoDTO.getExtension());
		return tradeRequest;
	}


	/**
	 * 获取支付参数
	 *
	 * @param tradeInfoDTO
	 * @return
	 */
	private static PaymentInfo getPaymentInfo(TradeInfoDTO tradeInfoDTO) {
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBuyerAccountNo(tradeInfoDTO.getBuyerAccountNo());
		paymentInfo.setPaymentVoucherNo(tradeInfoDTO.getPaymentVoucherNo());
		paymentInfo.setPaymentSourceVoucherNo(tradeInfoDTO.getPaymentSourceVoucherNo());
		paymentInfo.setPayMethodList(tradeInfoDTO.getPayMethodList());
		paymentInfo.setExtension(tradeInfoDTO.getPayExtension());
		return paymentInfo;
	}


	/**
	 * 获取交易信息
	 *
	 * @param tradeInfoDTO
	 * @return
	 */
	private static List<TradeItemDetail> getTradeItemDetails(TradeInfoDTO tradeInfoDTO) {
		List<TradeItemDetail> tradeItemDetailList = Lists.newArrayList();
		AcquiringTradeItemDetail acquiringTradeItemDetail = new AcquiringTradeItemDetail();
		BeanUtils.copyProperties(tradeInfoDTO, acquiringTradeItemDetail);
		tradeItemDetailList.add(acquiringTradeItemDetail);
		return tradeItemDetailList;
	}


	/**
	 * 基于交易单支付-构建支付请求参数
	 *
	 * @param instantPayDTO
	 * @return
	 */
	public static PaymentRequest convertPaymentRequest(InstantPayDTO instantPayDTO) {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTradeVoucherNoList(Lists.newArrayList(instantPayDTO.getTradeVoucherNo()));
		paymentRequest.setBuyerId(instantPayDTO.getBuyerId());
		paymentRequest.setAccessChannel("WEB");
		paymentRequest.setGmtSubmit(new Date());
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBuyerAccountNo(instantPayDTO.getBuyerAccountNo());
		paymentInfo.setExtension(instantPayDTO.getExtension());
		paymentInfo.setPaymentSourceVoucherNo(instantPayDTO.getPaymentSourceVoucherNo());
		paymentInfo.setPaymentVoucherNo(instantPayDTO.getPaymentVoucherNo());
		paymentInfo.setPayMethodList(instantPayDTO.getPayMethodList());
		paymentRequest.setPaymentInfo(paymentInfo);
		return paymentRequest;
	}


	/**
	 * 构建转账请求参数
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	public static TradeRequest convertTransferRequest(BalanceTransferDTO balanceTransferDTO) {
		TradeRequest request = new TradeRequest();
		request.setAccessChannel("WEB");
		request.setBuyerId(balanceTransferDTO.getFundoutMemberId());
		request.setGmtSubmit(new Date());

		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBuyerAccountNo(balanceTransferDTO.getFundOutAccountId());
		paymentInfo.setPaymentSourceVoucherNo(balanceTransferDTO.getOuterTradeNo());
		paymentInfo.setPaymentVoucherNo(balanceTransferDTO.getPayVoucherNo());

		// 支付方式只有余额（balance）
		List<PayMethod> payMethodList = setBalancePayMethod(balanceTransferDTO.getFundoutMemberId(),
				balanceTransferDTO.getFundOutAccountId(), balanceTransferDTO.getTransferAmount());

		paymentInfo.setPayMethodList(payMethodList);
		request.setPaymentInfo(paymentInfo);

		request.setTradeItemDetailList(setTransferTradeItemDetail(balanceTransferDTO));
		if (StringUtil.isNotEmpty(balanceTransferDTO.getExtension())) {
			request.setExtension(balanceTransferDTO.getExtension().replaceAll("&quot;", "\""));
		}
		return request;
	}


	/**
	 * 构建转账请求参数-设置交易信息
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	private static List<TradeItemDetail> setTransferTradeItemDetail(BalanceTransferDTO balanceTransferDTO) {
		List<TradeItemDetail> tradeItemDetailList = new ArrayList<TradeItemDetail>();

		AcquiringTradeItemDetail detail = new AcquiringTradeItemDetail();
		detail.setBizProductCode(balanceTransferDTO.getProductCode());
		detail.setTradeType(TradeType.INSTANT_TRASFER);

		detail.setBizNo(balanceTransferDTO.getBizOrderNo());
		detail.setPartnerId(balanceTransferDTO.getPartnerId());
		detail.setSellerAccountNo(balanceTransferDTO.getFundInAccountId());
		detail.setSellerId(balanceTransferDTO.getFundinMemberId());
		detail.setTradeAmount(new Money(balanceTransferDTO.getTransferAmount()));

		detail.setTradeSourceVoucherNo(balanceTransferDTO.getOuterTradeNo());
		detail.setTradeVoucherNo(balanceTransferDTO.getTradeVoucherNo());
		detail.setExtension(balanceTransferDTO.getExtension());
		detail.setTradeMemo(balanceTransferDTO.getTradeMemo());
		tradeItemDetailList.add(detail);
		return tradeItemDetailList;
	}


	/**
	 * 构建转账请求参数-设置支付方式
	 *
	 * @param buyerMemberId
	 * @param buyerAccount
	 * @param amount
	 * @return
	 */
	private static List<PayMethod> setBalancePayMethod(String buyerMemberId, String buyerAccount, String amount) {
		List<PayMethod> payMethodList = new ArrayList<PayMethod>();
		BalancePayMethod balancePayMethod = new BalancePayMethod();
		balancePayMethod.setAmount(new Money(amount));
		balancePayMethod.setPayMode(PayMethodEnums.BALANCE.name());
		balancePayMethod.setPayChannel(PayMethodEnums.BALANCE.getCode());
		balancePayMethod.setPayerAccountNo(buyerAccount);
		balancePayMethod.setPayerId(buyerMemberId);
		balancePayMethod.setExtension(null);
		payMethodList.add(balancePayMethod);
		return payMethodList;
	}


	/**
	 * 构建退款请求参数
	 *
	 * @param walletRefundDTO
	 * @return
	 */
	public static RefundRequest convertRefundRequest(WalletRefundDTO walletRefundDTO) {
		RefundRequest refundRequest = new RefundRequest();
		refundRequest.setAccessChannel("WEB");
		refundRequest.setGmtSubmit(new Date());
		refundRequest.setOrigTradeVoucherNo(walletRefundDTO.getOrigOuterTradeNo());
		refundRequest.setRefundAmount(walletRefundDTO.getRefundAmount());
		refundRequest.setRefundCoinAmount(walletRefundDTO.getGoldCoinAmount());
		refundRequest.setRefundEnsureAmount(null);
		refundRequest.setRefundPrepayAmount(null);
		refundRequest.setTradeSourceVoucherNo(walletRefundDTO.getTradeSourceVoucherNo());
		refundRequest.setTradeVoucherNo(walletRefundDTO.getRefundVoucherNo());
		refundRequest.setSplitParameterList(walletRefundDTO.getSplitParameterList());
		refundRequest.setMemo(walletRefundDTO.getMemo());
		refundRequest.setExtension(walletRefundDTO.getExtension());
		return refundRequest;
	}

	/**
	 * 构造算费请求参数
	 *
	 * @param tradeFeeDTO
	 * @return
	 */
	public static PayPricingReq convertQueryTradeFee(TradeFeeDTO tradeFeeDTO) {
		PayPricingReq payPricingReq = new PayPricingReq();
		payPricingReq.setRequestNo(tradeFeeDTO.getRequestNo());
		payPricingReq.setProductCode(tradeFeeDTO.getProductCode());
		payPricingReq.setPaymentCode(Constants.PAYMENT_CODE);
		payPricingReq.setFundsChannel(null);
		payPricingReq.setPayChannel(tradeFeeDTO.getPayChannel());
		payPricingReq.setPaymentInitiate(tradeFeeDTO.getPaymentInitiate());
		payPricingReq.setPartyInfoList(tradeFeeDTO.getPartyInfoList());
		payPricingReq.setPayableAmount(new Money(tradeFeeDTO.getPayableAmount().getAmount()));
		payPricingReq.setOrderAmount(new Money(tradeFeeDTO.getPayableAmount().getAmount()));
		payPricingReq.setExtension(tradeFeeDTO.getExtension());
		payPricingReq.setIgnoreError(false);
		return payPricingReq;
	}


	/**
	 * 构造钱包冻结请求参数
	 *
	 * @param walletFrozenDTO
	 * @return
	 */
	public static FundsFreezeRequest convertWalletFrozenRequest(WalletFrozenDTO walletFrozenDTO) {
		FundsFreezeRequest freezeRequest = new FundsFreezeRequest();
		freezeRequest.setMemberId(walletFrozenDTO.getMemberId());
		freezeRequest.setAccountNo(walletFrozenDTO.getAccountNo());
		freezeRequest.setFreezeAmount(walletFrozenDTO.getFreezeAmount());
		freezeRequest.setFundPropType(FundPropTypeEnums.DR.getCode());
		freezeRequest.setMemo(walletFrozenDTO.getMemo());
		freezeRequest.setBizProductCode(walletFrozenDTO.getBizProductCode());
		freezeRequest.setPaymentVoucherNo(walletFrozenDTO.getPaymentVoucherNo());
		freezeRequest.setGmtPaymentInitiate(new Date());
		return freezeRequest;
	}

	/**
	 * 构造钱包解冻请求参数
	 *
	 * @param walletUnFrozenDTO
	 * @return
	 */
	public static FundsUnFreezeRequest convertWalletUnFrozenRequest(WalletUnFrozenDTO walletUnFrozenDTO) {
		FundsUnFreezeRequest unFreeze = new FundsUnFreezeRequest();
		unFreeze.setBizProductCode(walletUnFrozenDTO.getBizProductCode());
		unFreeze.setOrigPaymentVoucherNo(walletUnFrozenDTO.getPaymentVoucherNo());
		unFreeze.setMemo(walletUnFrozenDTO.getMemo());
		unFreeze.setPaymentVoucherNo(walletUnFrozenDTO.getPaymentVoucherNo());
		unFreeze.setGmtPaymentInitiate(new Date());
		unFreeze.setUnfreezeAmount(walletUnFrozenDTO.getUnFreezeAmount());
		unFreeze.setExtension(walletUnFrozenDTO.getExtension());
		return unFreeze;
	}


}
