package com.souche.bmgateway.core.dubbo.api.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.ma.service.base.model.BankAcctDetailInfo;
import com.netfinworks.ma.service.response.BankAccountInfoResponse;
import com.netfinworks.payment.common.v2.enums.PartyRole;
import com.netfinworks.pbs.service.context.vo.PartyInfo;
import com.netfinworks.tradeservice.facade.enums.TradeType;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import com.netfinworks.tradeservice.facade.model.paymethod.BalancePayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.CashPayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.OnlineBankPayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PosPayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.QuickPayMethod;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ParamValidateException;
import com.souche.bmgateway.core.service.dto.BalanceTransferDTO;
import com.souche.bmgateway.core.service.dto.EntryDTO;
import com.souche.bmgateway.core.service.dto.InstantPayDTO;
import com.souche.bmgateway.core.service.dto.TradeFeeDTO;
import com.souche.bmgateway.core.service.dto.TradeInfoDTO;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;
import com.souche.bmgateway.core.service.dto.WalletFrozenDTO;
import com.souche.bmgateway.core.service.dto.WalletFundoutDTO;
import com.souche.bmgateway.core.service.dto.WalletRefundDTO;
import com.souche.bmgateway.core.service.dto.WalletUnFrozenDTO;
import com.souche.bmgateway.enums.PayMethodEnums;
import com.souche.bmgateway.model.request.trade.CreateInstantTradeRequest;
import com.souche.bmgateway.model.request.trade.CreatePayOrderRequest;
import com.souche.bmgateway.model.request.trade.EntryRequest;
import com.souche.bmgateway.model.request.trade.FundoutTradeRequest;
import com.souche.bmgateway.model.request.trade.InstantPayRequest;
import com.souche.bmgateway.model.request.trade.PayMethodInfo;
import com.souche.bmgateway.model.request.trade.TradeFeeQueryRequest;
import com.souche.bmgateway.model.request.trade.TransferAccountRequest;
import com.souche.bmgateway.model.request.trade.WalletDepositRequest;
import com.souche.bmgateway.model.request.trade.WalletFrozenRequest;
import com.souche.bmgateway.model.request.trade.WalletRefundRequest;
import com.souche.bmgateway.model.request.trade.WalletUnFrozenRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 将外部请求参数构造service层DTO
 *
 * @author zs. Created on 18/11/20.
 */
public class TradeDtoBuilder {

	/**
	 * 创建交易单
	 *
	 * @param createPayOrderRequest
	 * @param splitParameterList
	 * @param buyerInfo
	 * @param sellerInfo
	 * @return
	 */
	public static TradeInfoDTO buildTradeInfoDTO(CreatePayOrderRequest createPayOrderRequest,
			List<SplitParameter> splitParameterList, Pair<String, String> buyerInfo, Pair<String, String> sellerInfo) {
		TradeInfoDTO tradeInfoDTO = new TradeInfoDTO();
		tradeInfoDTO.setTradeSourceVoucherNo(createPayOrderRequest.getOuterTradeNo());
		tradeInfoDTO.setTradeVoucherNo(null);
		tradeInfoDTO.setBizNo(createPayOrderRequest.getBizOrderNo());
		tradeInfoDTO.setSplitParameterList(splitParameterList);
		tradeInfoDTO.setTradeMemo(createPayOrderRequest.getSubject());
		tradeInfoDTO.setBizProductCode(createPayOrderRequest.getBizProductCode());
		tradeInfoDTO.setProductDesc(createPayOrderRequest.getProductDesc());
		tradeInfoDTO.setTradeAmount(new Money(createPayOrderRequest.getTradeAmount()));
		if (StringUtils.isNotBlank(createPayOrderRequest.getCoinAmount())) {
			tradeInfoDTO.setCoinAmount(new Money(createPayOrderRequest.getCoinAmount()));
		}
		tradeInfoDTO.setSellerId(sellerInfo.getLeft());
		tradeInfoDTO.setSellerAccountNo(sellerInfo.getRight());
		tradeInfoDTO.setTradeMemo(createPayOrderRequest.getSubject());
		tradeInfoDTO.setExtension(createPayOrderRequest.getExtension());
		tradeInfoDTO.setTradeType(TradeType.INSTANT_ACQUIRING);
		tradeInfoDTO.setShowUrl(createPayOrderRequest.getShowUrl());
		tradeInfoDTO.setPartnerId(createPayOrderRequest.getPartnerId());
		tradeInfoDTO.setGmtInvalid(createPayOrderRequest.getExpirationTime());
		tradeInfoDTO.setBuyerId(buyerInfo.getLeft());
		tradeInfoDTO.setBuyerIp(createPayOrderRequest.getBuyerIp());
		tradeInfoDTO.setBuyerAccountNo(buyerInfo.getRight());
		tradeInfoDTO.setNotifyUrl(createPayOrderRequest.getNotifyUrl());
		tradeInfoDTO.setPaymentVoucherNo(null);
		tradeInfoDTO.setPaymentSourceVoucherNo(null);
		tradeInfoDTO.setPayMethodList(null);
		tradeInfoDTO.setPayExtension(null);
		return tradeInfoDTO;
	}

	/**
	 * 基于支付单支付
	 *
	 * @param instantPayRequest
	 * @param buyerInfo
	 * @return
	 */
	public static InstantPayDTO buildPaymentInfo(InstantPayRequest instantPayRequest, Pair<String, String> buyerInfo)
			throws ParamValidateException {
		InstantPayDTO instantPayDTO = new InstantPayDTO();
		instantPayDTO.setPaymentSourceVoucherNo(instantPayRequest.getRequestNo());
		instantPayDTO.setTradeVoucherNo(instantPayRequest.getTradeVoucherNo());
		instantPayDTO.setBuyerId(buyerInfo.getLeft());
		instantPayDTO.setBuyerAccountNo(buyerInfo.getRight());
		instantPayDTO.setPaymentVoucherNo(null);
		instantPayDTO.setPayMethodList(buildPaymentMethod(instantPayRequest.getPayMethod()));
		instantPayDTO.setExtension(instantPayRequest.getExtension());
		instantPayDTO.setAccessChannel("WEB");
		instantPayDTO.setGmtSubmit(new Date());
		return instantPayDTO;
	}

	/**
	 * 创建支付单并支付，设置支付相关信息
	 *
	 * @param tradeInfoDTO
	 * @param createInstantTradeRequest
	 * @return
	 */
	public static void buildPayInfoDTO(TradeInfoDTO tradeInfoDTO, CreateInstantTradeRequest createInstantTradeRequest)
			throws ParamValidateException {
		tradeInfoDTO.setPaymentSourceVoucherNo(createInstantTradeRequest.getRequestNo());
		tradeInfoDTO.setTradeVoucherNo(null);
		tradeInfoDTO.setPaymentVoucherNo(null);
		tradeInfoDTO.setPayMethodList(buildPaymentMethod(createInstantTradeRequest.getPayMethod()));
		tradeInfoDTO.setPayExtension(createInstantTradeRequest.getExtension());
	}

	/**
	 * 构建支付方式
	 *
	 * @param payMethodInfo
	 * @return
	 * @throws ParamValidateException
	 */
	private static List<PayMethod> buildPaymentMethod(PayMethodInfo payMethodInfo) throws ParamValidateException {
		PayMethodEnums payMethodEnums = PayMethodEnums.getPayMethodEnums(payMethodInfo.getPayMethod());
		if (payMethodEnums == null) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "支付方式不存在");
		}
		List<String> payChannelInfo = Splitter.on(",").trimResults().splitToList(payMethodInfo.getMemo());
		// 机构 例如ALIPAY，WEIXIN，ICBC等
		String bankCode = payChannelInfo.get(0);
		// 对公/对私：B/C
		String companyOrPersonal = payChannelInfo.get(1);
		// 借记/贷记/综合
		String dbcr = payChannelInfo.get(2);
		// 卡号或卡id
		String bankId = parseCard(payChannelInfo.get(3), "N");
		String bankCardNo = parseCard(payChannelInfo.get(3), "B");

		List<PayMethod> payMethodList = Lists.newArrayList();
		switch (payMethodEnums) {
		case BALANCE:
			BalancePayMethod balancePayMethod = new BalancePayMethod();
			balancePayMethod.setPayerId(null);
			balancePayMethod.setPayerAccountNo(null);
			balancePayMethod.setAmount(new Money(payMethodInfo.getAmount()));
			balancePayMethod.setPayChannel(PayMethodEnums.BALANCE.getCode());
			balancePayMethod.setPayMode(PayMethodEnums.BALANCE.name());
			balancePayMethod.setExtension(payMethodInfo.getExtension());
			payMethodList.add(balancePayMethod);
			break;
		case ONLINE_BANK:
			OnlineBankPayMethod onlineBankPayMethod = new OnlineBankPayMethod();
			onlineBankPayMethod.setDbcr(onlineBankPayMethod.getDbcr());
			onlineBankPayMethod.setCompanyOrPersonal(onlineBankPayMethod.getCompanyOrPersonal());
			onlineBankPayMethod.setBankCode(bankCode);
			onlineBankPayMethod.setAmount(new Money(payMethodInfo.getAmount()));
			// payChannel取bankCode
			onlineBankPayMethod.setPayChannel(bankCode);
			onlineBankPayMethod.setPayMode(PayMethodEnums.ONLINE_BANK.name());
			onlineBankPayMethod.setExtension(payMethodInfo.getExtension());
			payMethodList.add(onlineBankPayMethod);
			break;
		case POS:
			PosPayMethod posPayMethod = new PosPayMethod();
			posPayMethod.setDbcr(dbcr);
			posPayMethod.setCompanyOrPersonal(companyOrPersonal);
			posPayMethod.setBankCode(bankCode);
			posPayMethod.setAmount(new Money(payMethodInfo.getAmount()));
			// payChannel取bankCode
			posPayMethod.setPayChannel(bankCode);
			posPayMethod.setPayMode(PayMethodEnums.POS.name());
			posPayMethod.setExtension(payMethodInfo.getExtension());
			payMethodList.add(posPayMethod);
			break;
		case QPAY:
			QuickPayMethod quickPayMethod = new QuickPayMethod();
			quickPayMethod.setPayerId("");
			quickPayMethod.setDbcr(dbcr);
			quickPayMethod.setCompanyOrPersonal(companyOrPersonal);
			quickPayMethod.setBankCode(bankCode);
			quickPayMethod.setBankCardId(bankId);
			quickPayMethod.setName("");
			quickPayMethod.setCertNo("");
			quickPayMethod.setBankCardNo(bankCardNo);
			quickPayMethod.setValidDa("");
			quickPayMethod.setVcc("");
			quickPayMethod.setPhone("");
			quickPayMethod.setSignNo("");
			quickPayMethod.setAmount(new Money(payMethodInfo.getAmount()));
			// payChannel取bankCode
			quickPayMethod.setPayChannel(bankCode);
			quickPayMethod.setPayMode(PayMethodEnums.QPAY.name());
			quickPayMethod.setExtension(payMethodInfo.getExtension());
			payMethodList.add(quickPayMethod);
			break;
		case CASH:
			CashPayMethod cashPayMethod = new CashPayMethod();
			cashPayMethod.setOperatorId("");
			cashPayMethod.setDbcr(dbcr);
			cashPayMethod.setCompanyOrPersonal(companyOrPersonal);
			cashPayMethod.setBankCode(bankCode);
			cashPayMethod.setCashVoucherNo("");
			cashPayMethod.setTradeSourceVoucherNo("");
			cashPayMethod.setAmount(new Money(payMethodInfo.getAmount()));
			cashPayMethod.setPayChannel(PayMethodEnums.CASH.getCode());
			cashPayMethod.setPayMode(PayMethodEnums.CASH.name());
			cashPayMethod.setExtension(payMethodInfo.getExtension());
			payMethodList.add(cashPayMethod);
			break;
		default:
		}
		return payMethodList;
	}

	/**
	 * 从支付方式备注中获取卡id或卡号
	 *
	 * @param memoInfo
	 * @param flag
	 *            B+bankid或N+bank_account_no
	 * @return
	 */
	private static String parseCard(String memoInfo, String flag) throws ParamValidateException {
		if (StringUtils.isNotBlank(memoInfo)) {
			if (memoInfo.startsWith(flag)) {
				return memoInfo.substring(1);
			} else {
				throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "支付方式-备注格式错误");
			}
		}
		return null;
	}

	/**
	 * 转账
	 *
	 * @param transferAccountRequest
	 * @param buyerMemberInfo
	 * @param sellerMemberInfo
	 * @return
	 */
	public static BalanceTransferDTO buildTransferDTO(TransferAccountRequest transferAccountRequest,
			Pair<String, String> buyerMemberInfo, Pair<String, String> sellerMemberInfo) {
		BalanceTransferDTO balanceTransferDTO = new BalanceTransferDTO();
		balanceTransferDTO.setOuterTradeNo(transferAccountRequest.getOuterTradeNo());
		balanceTransferDTO.setBizOrderNo(transferAccountRequest.getBizOrderNo());
		balanceTransferDTO.setPayVoucherNo(null);
		balanceTransferDTO.setTradeVoucherNo(null);
		balanceTransferDTO.setProductCode(transferAccountRequest.getBizProductCode());
		balanceTransferDTO.setTransferAmount(transferAccountRequest.getTransferAmount());
		balanceTransferDTO.setFundoutMemberId(buyerMemberInfo.getLeft());
		balanceTransferDTO.setFundOutAccountId(buyerMemberInfo.getRight());
		balanceTransferDTO.setFundinMemberId(sellerMemberInfo.getLeft());
		balanceTransferDTO.setFundInAccountId(sellerMemberInfo.getRight());
		balanceTransferDTO.setNotifyUrl(transferAccountRequest.getNotifyUrl());
		balanceTransferDTO.setTradeMemo(transferAccountRequest.getTradeMemo());
		balanceTransferDTO.setExtension(transferAccountRequest.getExtension());
		balanceTransferDTO.setPartnerId(transferAccountRequest.getPartnerId());
		return balanceTransferDTO;
	}

	/**
	 * 退款
	 *
	 * @param walletRefundRequest
	 * @param splitParameterList
	 * @return
	 */
	public static WalletRefundDTO buildRefundDTO(WalletRefundRequest walletRefundRequest,
			List<SplitParameter> splitParameterList) {
		WalletRefundDTO walletRefundDTO = new WalletRefundDTO();
		walletRefundDTO.setOuterTradeNo(walletRefundRequest.getOuterTradeNo());
		walletRefundDTO.setOrigOuterTradeNo(walletRefundRequest.getOrigOuterTradeNo());
		walletRefundDTO.setTradeSourceVoucherNo(null);
		walletRefundDTO.setRefundVoucherNo(null);
		walletRefundDTO.setProductCode(walletRefundRequest.getBizProductCode());
		walletRefundDTO.setRefundAmount(new Money(walletRefundRequest.getRefundAmount()));
		walletRefundDTO.setGoldCoinAmount(new Money(walletRefundRequest.getGoldCoinAmount()));
		walletRefundDTO.setSplitParameterList(splitParameterList);
		walletRefundDTO.setNotifyUrl(walletRefundRequest.getNotifyUrl());
		walletRefundDTO.setMemo(walletRefundRequest.getMemo());
		walletRefundDTO.setExtension(walletRefundRequest.getExtension());
		walletRefundDTO.setPartnerId(walletRefundRequest.getPartnerId());
		return walletRefundDTO;
	}

	/**
	 * 登帐
	 *
	 * @param entryRequest
	 * @return
	 */
	public static EntryDTO buildEntryDTO(EntryRequest entryRequest) {
		EntryDTO entryDTO = new EntryDTO();
		entryDTO.setPaymentVoucherNo(entryRequest.getRequestNo());
		entryDTO.setBizProductCode(entryRequest.getBizProductCode());
		entryDTO.setGmtPaymentInitiate(new Date());
		entryDTO.setAmount(new Money(entryRequest.getAmount()));
		entryDTO.setMemo(entryRequest.getMemo());
		entryDTO.setExtension(entryRequest.getExtension());
		return entryDTO;
	}

	/**
	 * 查询手续费
	 * 
	 * @param tradeFeeQueryRequest
	 * @return
	 */
	public static TradeFeeDTO buildTradeFeeDTO(TradeFeeQueryRequest tradeFeeQueryRequest) {
		TradeFeeDTO tradeFeeDTO = new TradeFeeDTO();
		tradeFeeDTO.setRequestNo(tradeFeeQueryRequest.getRequestNo());
		tradeFeeDTO.setProductCode(tradeFeeQueryRequest.getBizProductCode());
		tradeFeeDTO.setPayChannel(tradeFeeQueryRequest.getPayChannel());
		tradeFeeDTO.setPaymentInitiate(new Date());
		tradeFeeDTO.setPayableAmount(new Money(tradeFeeQueryRequest.getPayableAmount()));
		List<PartyInfo> partyInfoList = Lists.newArrayListWithCapacity(2);
		if (StringUtils.isNotBlank(tradeFeeQueryRequest.getBuyerId())) {
			PartyInfo buyerPartyInfo = new PartyInfo();
			buyerPartyInfo.setMemberId(tradeFeeQueryRequest.getBuyerId());
			buyerPartyInfo.setPartyRole(PartyRole.PAYER);
			partyInfoList.add(buyerPartyInfo);
		}
		if (StringUtils.isNotBlank(tradeFeeQueryRequest.getSellerId())) {
			PartyInfo sellerPartyInfo = new PartyInfo();
			sellerPartyInfo.setMemberId(tradeFeeQueryRequest.getSellerId());
			sellerPartyInfo.setPartyRole(PartyRole.PAYEE);
			partyInfoList.add(sellerPartyInfo);
		}
		tradeFeeDTO.setPartyInfoList(partyInfoList);
		tradeFeeDTO.setExtension(tradeFeeQueryRequest.getExtension());
		return tradeFeeDTO;
	}

	/**
	 * 构建冻结参数
	 *
	 * @param walletFrozenRequest
	 * @param memberInfo
	 * @return
	 */
	public static WalletFrozenDTO buildWalletFrozenDTO(WalletFrozenRequest walletFrozenRequest,
			Pair<String, String> memberInfo) {
		WalletFrozenDTO walletFrozenDTO = new WalletFrozenDTO();
		walletFrozenDTO.setOuterTradeNo(walletFrozenRequest.getOuterTradeNo());
		walletFrozenDTO.setBizProductCode(walletFrozenRequest.getBizProductCode());
		walletFrozenDTO.setMemberId(walletFrozenRequest.getMemberId());
		walletFrozenDTO.setAccountNo(memberInfo.getRight());
		walletFrozenDTO.setFreezeAmount(new Money(walletFrozenRequest.getFreezeAmount()));
		walletFrozenDTO.setExtension(walletFrozenRequest.getExtension());
		walletFrozenDTO.setMemo(walletFrozenRequest.getMemo());
		walletFrozenDTO.setPartnerId(walletFrozenRequest.getPartnerId());
		return walletFrozenDTO;

	}

	/**
	 * 构建解冻参数
	 *
	 * @param walletUnFrozenRequest
	 * @return
	 */
	public static WalletUnFrozenDTO buildWalletUnFrozenDTO(WalletUnFrozenRequest walletUnFrozenRequest) {
		WalletUnFrozenDTO walletUnFrozenDTO = new WalletUnFrozenDTO();
		walletUnFrozenDTO.setOuterTradeNo(walletUnFrozenRequest.getOuterTradeNo());
		walletUnFrozenDTO.setPartnerId(walletUnFrozenRequest.getPartnerId());
		walletUnFrozenDTO.setBizProductCode(walletUnFrozenRequest.getBizProductCode());
		walletUnFrozenDTO.setMemo(walletUnFrozenRequest.getMemo());
		walletUnFrozenDTO.setUnFreezeAmount(new Money(walletUnFrozenRequest.getUnFrozenAmount()));
		walletUnFrozenDTO.setExtension(walletUnFrozenRequest.getExtension());
		return walletUnFrozenDTO;
	}

	/**
	 * 构建充值参数
	 * 
	 * @param depositRequest
	 * @param memberInfo
	 * @return
	 */
	public static WalletDepositDTO buildWalletDepositDTO(WalletDepositRequest depositRequest,
			Pair<String, String> memberInfo) throws ParamValidateException {
		WalletDepositDTO walletDepositDTO = new WalletDepositDTO();
		walletDepositDTO.setAccessChannel(Constants.ACCESS_CHANNEL);
		walletDepositDTO.setOutTradeNo(depositRequest.getOutTradeNo());
		walletDepositDTO.setTradeVoucherNo(null);
		walletDepositDTO.setPaymentVoucherNo(null);
		walletDepositDTO.setBizProductCode(depositRequest.getBizProductCode());
		walletDepositDTO.setAmount(depositRequest.getAmount());
		walletDepositDTO.setMemberId(memberInfo.getLeft());
		walletDepositDTO.setAccountType(depositRequest.getAccountType());
		walletDepositDTO.setAccountIdentity(depositRequest.getAccountIdentity());
		walletDepositDTO.setAccountNo(memberInfo.getRight());
		walletDepositDTO.setGmtSubmit(new Date());
		walletDepositDTO.setExtension(depositRequest.getExtension());

		PayMethodEnums payMethodEnum = PayMethodEnums.getPayMethodEnums(depositRequest.getPayMethod().getPayMethod());
		if (isNotValidDepositPayMethod(depositRequest, payMethodEnum)) {
			throw new ParamValidateException(ErrorCodeEnums.PAY_METHOD_ERROR, "支付方式信息错误");
		}
		Map<String, String> payExtension = StringUtils.isNotBlank(depositRequest.getPayMethod().getExtension())
				? (Map<String, String>) JSONObject.parse(depositRequest.getPayMethod().getExtension())
				: Maps.newHashMapWithExpectedSize(10);
		List<String> payMethodInfo = Splitter.on(",").trimResults()
				.splitToList(depositRequest.getPayMethod().getMemo());
		payExtension.put(Constants.KEY_BANK_CODE, payMethodInfo.get(0));
		payExtension.put(Constants.KEY_COMPANY_OR_PERSONAL, payMethodInfo.get(1));
		payExtension.put(Constants.KEY_DBCR, payMethodInfo.get(2));
		walletDepositDTO.setPayMethod(payMethodEnum);
		walletDepositDTO.setPaymentChannel(payMethodInfo.get(0));
		walletDepositDTO.setPayExtension(JSON.toJSONString(payExtension));
		walletDepositDTO.setRemark(depositRequest.getRemark());
		walletDepositDTO.setNotifyUrl(depositRequest.getNotifyUrl());
		walletDepositDTO.setPartnerId(depositRequest.getPartnerId());

		return walletDepositDTO;
	}

	private static boolean isNotValidDepositPayMethod(WalletDepositRequest depositRequest,
			PayMethodEnums payMethodEnum) {
		return (!PayMethodEnums.ONLINE_BANK.equals(payMethodEnum) && !PayMethodEnums.POS.equals(payMethodEnum))
				|| StringUtils.isBlank(depositRequest.getPayMethod().getMemo());
	}

	/**
	 * 构建提现参数
	 * 
	 * @param fundoutRequestDO
	 * @return
	 */
	public static WalletFundoutDTO buildFundoutDTO(FundoutTradeRequest fundoutRequestDO,
			Pair<String, String> memberInfo, BankAccountInfoResponse bankAccountInfoResponse) {
		WalletFundoutDTO walletFundoutDTO = new WalletFundoutDTO();
		walletFundoutDTO.setAccountNo(memberInfo.getRight());
		walletFundoutDTO.setAccountType(fundoutRequestDO.getAccountType());
		walletFundoutDTO.setAmount(fundoutRequestDO.getAmount());
		// walletFundoutDTO.setBankAccountName(fundoutRequestDO.getBankAccountName());
		walletFundoutDTO.setBankCardId(fundoutRequestDO.getBankCardId());
		walletFundoutDTO.setBizProductCode(fundoutRequestDO.getBizProductCode());
		walletFundoutDTO.setCardNo(fundoutRequestDO.getCardNo());
		walletFundoutDTO.setExtension(fundoutRequestDO.getExtension());
		walletFundoutDTO.setTradeVoucherNo(null);
		walletFundoutDTO.setMemberId(fundoutRequestDO.getMemberId());
		walletFundoutDTO.setNotifyUrl(fundoutRequestDO.getNotifyUrl());
		walletFundoutDTO.setOutTradeNo(fundoutRequestDO.getOutTradeNo());
		walletFundoutDTO.setPartnerId(fundoutRequestDO.getPartnerId());
		walletFundoutDTO.setPaymentVoucherNo(null);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String reqTime = sf.format(new Date());
		walletFundoutDTO.setRequestTime(reqTime);
		// walletFundoutDTO.setReturnUrl(fundoutRequestDO.getReturnUrl());
		if (null != bankAccountInfoResponse && null != bankAccountInfoResponse.getBankAcctInfo()) {
			BankAcctDetailInfo bankAcctInfo = bankAccountInfoResponse.getBankAcctInfo();
			walletFundoutDTO.setBankName(bankAcctInfo.getBankName());
			walletFundoutDTO.setCardTye(bankAcctInfo.getCardType());
			walletFundoutDTO.setCity(bankAcctInfo.getCity());
			walletFundoutDTO.setProv(bankAcctInfo.getProvince());
			walletFundoutDTO.setBankBranch(bankAcctInfo.getBankBranch());
			walletFundoutDTO.setBankCode(bankAcctInfo.getBankCode());
			//walletFundoutDTO.setBankLineNo(bankAcctInfo.getBankBranch());
			walletFundoutDTO.setName(bankAcctInfo.getRealName());
			Integer cardAttribute = bankAcctInfo.getCardAttribute();
			walletFundoutDTO.setCompanyOrPersonal(cardAttribute);
		}

		return walletFundoutDTO;
	}
}
