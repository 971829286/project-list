package com.souche.bmgateway.core.witness.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.netfinworks.cmf.fss.service.facade.counter.InstOrderProcessFacade;
import com.netfinworks.cmf.fss.service.facade.domain.InstOrderInfo;
import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.netfinworks.dts.service.facade.enums.BusinessType;
import com.netfinworks.dts.service.facade.request.BaseRequset;
import com.netfinworks.dts.service.facade.request.trade.CreateDepositRequest;
import com.netfinworks.dts.service.facade.request.trade.CreateInstantTradeRequest;
import com.netfinworks.dts.service.facade.request.trade.CreateRefundRequest;
import com.netfinworks.dts.service.facade.request.trade.PayToCardRequest;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.payment.common.v2.enums.PartyRole;
import com.netfinworks.pbs.service.context.vo.PartyInfo;
import com.netfinworks.tradeservice.facade.model.AcquiringTradeItemDetail;
import com.netfinworks.tradeservice.facade.model.PaymentInfo;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import com.netfinworks.tradeservice.facade.model.TradeItemDetail;
import com.netfinworks.tradeservice.facade.model.paymethod.OnlineBankPayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.PosPayMethod;
import com.netfinworks.tradeservice.facade.model.paymethod.QuickPayMethod;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.PayAttrEnums;
import com.souche.bmgateway.core.service.dto.AccountSimpleDTO;
import com.souche.bmgateway.core.service.dto.TradeFeeDTO;
import com.souche.bmgateway.core.service.dto.TradeQueryDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.trade.WalletTradeService;
import com.souche.bmgateway.core.witness.WitnessTradeDataDealService;
import com.souche.bmgateway.enums.PayMethodEnums;
import com.souche.bmgateway.model.request.trade.SplitInfo;
import com.souche.bmgateway.model.response.trade.TradeFeeQueryResponse;
import com.souche.bmgateway.model.response.trade.TradeQueryResponse;
import com.souche.witness.api.domain.MqTransfer;
import com.souche.witness.api.enums.TransferStatus;
import com.souche.witness.api.repository.TransferDbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易信息同步交易见证
 *
 * @author zs. Created on 17/12/4.
 */
@Service("witnessTradeDataDealService")
@Slf4j(topic = "witness")
public class WitnessTradeDataDealServiceImpl implements WitnessTradeDataDealService {

	@Resource
	private TransferDbService transferDbService;
	@Resource
	private WalletTradeService walletTradeService;
	@Resource
	private InstOrderProcessFacade instOrderProcessFacade;
	@Resource
	private WalletMemberService walletMemberService;

	@Override
	public void saveDepositDataToWitness(DepositRequest depositRequest, DepositResponse depositResponse) {
		log.info("充值交易见证-充值请求参数:depositRequest=>{}, depositResponse=>{}", depositRequest, depositResponse);
		CreateDepositRequest createDepositRequest = new CreateDepositRequest();
		createDepositRequest.setBusiness_type(BusinessType.DEPOSIT.getCode());
		createDepositRequest.setOuter_trade_no(depositRequest.getTradeVoucherNo());
		createDepositRequest.setOuter_inst_order_no(depositResponse.getInstOrderNo());
		if (null != depositResponse && depositResponse.getRespResult().isSuccess()) {
			createDepositRequest.setWhite_channel_code(getPayWhiteChannelCode(depositResponse.getInstOrderNo(),
					depositRequest.getPaymentInfo().getPayMode().getCode()));
		}
		createDepositRequest.setProduct_code(depositRequest.getdepositProduct());
		createDepositRequest.setAmount(depositRequest.getPaymentInfo().getAmount());

		Triple<String, String, String> memberInfo = getMemberInfo(depositRequest.getAccountNo());
		createDepositRequest.setUid(memberInfo.getLeft());
		createDepositRequest.setAccount_type(memberInfo.getMiddle());
		createDepositRequest.setAccount_identity(memberInfo.getRight());
		createDepositRequest.setPay_method(buildDepositPayMethod(depositRequest));
		createDepositRequest.setFee_info(buildDepositFeeInfo(depositRequest));
		insertMqTransfer(createDepositRequest, createDepositRequest.getOuter_trade_no());
	}

	/**
	 * 构建充值的手续费信息
	 *
	 * @param depositRequest
	 * @return
	 */
	private Map<String, String> buildDepositFeeInfo(DepositRequest depositRequest) {
		TradeFeeDTO tradeFeeDTO = new TradeFeeDTO();
		tradeFeeDTO.setRequestNo(depositRequest.getTradeVoucherNo());
		tradeFeeDTO.setProductCode(depositRequest.getdepositProduct());
		tradeFeeDTO.setPayChannel(depositRequest.getPaymentInfo().getPaymentChannel());
		tradeFeeDTO.setPaymentInitiate(new Date());
		tradeFeeDTO.setPayableAmount(depositRequest.getPaymentInfo().getAmount());
		List<PartyInfo> partyInfoList = Lists.newArrayListWithCapacity(1);
		PartyInfo partyInfo = new PartyInfo();
		partyInfo.setMemberId(depositRequest.getMemberId());
		partyInfo.setPartyRole(PartyRole.PAYEE);
		partyInfoList.add(partyInfo);
		tradeFeeDTO.setPartyInfoList(partyInfoList);
		tradeFeeDTO.setExtension(depositRequest.getExtension());
		return getTradeFeeMap(tradeFeeDTO);
	}

	/**
	 * 构建充值payMethod
	 *
	 * @param depositRequest
	 * @return
	 */
	private Map<String, String> buildDepositPayMethod(DepositRequest depositRequest) {
		Map<String, String> payMethod = new HashMap<>(8);
		payMethod.put("pay_method", depositRequest.getPaymentInfo().getPayMode().getCode());
		payMethod.put("amount", depositRequest.getPaymentInfo().getAmount().toString());
		String payExtension = depositRequest.getPaymentInfo().getExtension();
		if (StringUtils.isNotBlank(payExtension)) {
			JSONObject extension = (JSONObject) JSONObject.parse(payExtension);
			payMethod.put("memo", String.format("%s,%s,%s", extension.get(Constants.KEY_BANK_CODE),
					extension.get(Constants.KEY_COMPANY_OR_PERSONAL), extension.get(Constants.KEY_DBCR)));
		}
		payMethod.put("extension", "");
		return payMethod;
	}

	@Override
	public void saveInstantTradeDataToWitness(TradeRequest tradeRequest, PaymentResponse paymentResponse) {
		log.info("同步交易见证-即时交易入账OR转账请求参数:tradeRequest=>{}, paymentResponse=>{}", tradeRequest, paymentResponse);
		CreateInstantTradeRequest createInstantTradeRequest = new CreateInstantTradeRequest();
		TradeItemDetail tradeItemDetail = tradeRequest.getTradeItemDetailList().get(0);
		PaymentInfo paymentInfo = tradeRequest.getPaymentInfo();
		createInstantTradeRequest.setBusiness_type(BusinessType.INSTANTTRADE.getCode());
		createInstantTradeRequest.setOuter_trade_no(tradeItemDetail.getTradeSourceVoucherNo());
		createInstantTradeRequest.setBuyer_id(tradeRequest.getBuyerId());
		PayMethod payMethod = paymentInfo.getPayMethodList().get(0);
		createInstantTradeRequest.setPay_method(buildInstantTradePayMethod(payMethod));
		if (paymentResponse.isSuccess()) {
			String instOrderNo = paymentResponse.getPayMethodResultList().get(0).getInstPayNo();
			createInstantTradeRequest.setOuter_inst_order_no(instOrderNo);
			createInstantTradeRequest
					.setWhite_channel_code(getPayWhiteChannelCode(instOrderNo, payMethod.getPayMode()));
		}
		createInstantTradeRequest.setFee_info(buildInstantTradeFeeInfo(tradeRequest));
		createInstantTradeRequest.setSubject(tradeItemDetail.getTradeMemo());
		createInstantTradeRequest.setPrice(tradeItemDetail.getTradeAmount().getAmount().toString());
		createInstantTradeRequest.setQuantity("1");
		createInstantTradeRequest.setTotal_amount(tradeItemDetail.getTradeAmount().getAmount().toString());
		createInstantTradeRequest.setRoyalty_parameters(
				buildSplitInfo(((AcquiringTradeItemDetail) tradeItemDetail).getSplitParameterList()));
		Triple<String, String, String> sellerInfo = getMemberInfo(tradeItemDetail.getSellerAccountNo());
		createInstantTradeRequest.setSeller_id(sellerInfo.getLeft());
		createInstantTradeRequest.setAccount_type(sellerInfo.getMiddle());
		createInstantTradeRequest.setAccount_identity(sellerInfo.getRight());
		insertMqTransfer(createInstantTradeRequest, createInstantTradeRequest.getOuter_trade_no());
	}

	/**
	 * 构建分润信息
	 *
	 * @param splitParameterList
	 * @return
	 */
	private String buildSplitInfo(List<SplitParameter> splitParameterList) {
		List<SplitInfo> splitInfoList = Lists.newArrayList();
		if (splitParameterList == null || splitParameterList.isEmpty()) {
			return null;
		}
		for (SplitParameter splitParameter : splitParameterList) {
			Triple<String, String, String> sellerInfo = getMemberInfo(splitParameter.getPayeeAccountNo());
			SplitInfo splitInfo = new SplitInfo();
			splitInfo.setMemberId(sellerInfo.getLeft());
			splitInfo.setAccountType(sellerInfo.getMiddle());
			splitInfo.setAccountIdentity(sellerInfo.getRight());
			splitInfo.setAmount(splitParameter.getAmount().toString());
			splitInfoList.add(splitInfo);
		}
		return JSON.toJSONString(splitInfoList);
	}

	/**
	 * 构建支付方式
	 *
	 * @param payMethod
	 * @return
	 */
	private Map<String, String> buildInstantTradePayMethod(PayMethod payMethod) {
		Map<String, String> payMethodMap = new HashMap<String, String>();
		payMethodMap.put("pay_method", payMethod.getPayMode());
		payMethodMap.put("amount", payMethod.getAmount().toString());
		payMethodMap.put("extension", payMethod.getExtension());
		if (payMethod instanceof PosPayMethod) {
			PosPayMethod posPayMethod = (PosPayMethod) payMethod;
			payMethodMap.put("memo", String.format("%s,%s,%s", posPayMethod.getBankCode(),
					posPayMethod.getCompanyOrPersonal(), posPayMethod.getDbcr()));
		} else if (payMethod instanceof OnlineBankPayMethod) {
			OnlineBankPayMethod onlineBankPayMethod = (OnlineBankPayMethod) payMethod;
			payMethodMap.put("memo", String.format("%s,%s,%s", onlineBankPayMethod.getBankCode(),
					onlineBankPayMethod.getCompanyOrPersonal(), onlineBankPayMethod.getDbcr()));
		} else if (payMethod instanceof QuickPayMethod) {
			QuickPayMethod quickPayMethod = (QuickPayMethod) payMethod;
			String bankId = StringUtils.isNotBlank(quickPayMethod.getBankCardId())
					? "B" + quickPayMethod.getBankCardId()
					: "N" + quickPayMethod.getBankCardNo();
			payMethodMap.put("memo", String.format("%s,%s,%s,%s", quickPayMethod.getBankCode(),
					quickPayMethod.getCompanyOrPersonal(), quickPayMethod.getDbcr(), bankId));
		}
		return payMethodMap;
	}

	/**
	 * 获取支付渠道编码
	 *
	 * @param instOrderNo
	 * @param originPayMethod
	 * @return 渠道编码
	 */
	private String getPayWhiteChannelCode(String instOrderNo, String originPayMethod) {
		if (PayMethodEnums.BALANCE.equals(StringUtils.upperCase(originPayMethod))) {
			return "";
		}
		List<InstOrderInfo> instOrderInfos = instOrderProcessFacade.qurey(Lists.newArrayList(instOrderNo));
		log.info("sideswipe->cmf 查询渠道支付方式返回：" + instOrderInfos);
		return instOrderInfos==null?null:instOrderInfos.get(0).getFundChannelCode();
	}

	/**
	 * 获取支付手续费信息
	 *
	 * @param tradeRequest
	 * @return
	 */
	private Map<String, String> buildInstantTradeFeeInfo(TradeRequest tradeRequest) {
		PayMethod payMethod = tradeRequest.getPaymentInfo().getPayMethodList().get(0);
		TradeItemDetail tradeItemDetail = tradeRequest.getTradeItemDetailList().get(0);
		TradeFeeDTO tradeFeeDTO = new TradeFeeDTO();
		tradeFeeDTO.setRequestNo(tradeItemDetail.getTradeSourceVoucherNo());
		tradeFeeDTO.setProductCode(tradeItemDetail.getBizProductCode());
		tradeFeeDTO.setPayChannel(payMethod.getPayChannel());
		tradeFeeDTO.setPaymentInitiate(new Date());
		tradeFeeDTO.setPayableAmount(payMethod.getAmount());
		List<PartyInfo> partyInfoList = Lists.newArrayListWithCapacity(2);
		PartyInfo buyerInfo = new PartyInfo();
		buyerInfo.setMemberId(tradeRequest.getBuyerId());
		buyerInfo.setPartyRole(PartyRole.PAYER);
		partyInfoList.add(buyerInfo);
		PartyInfo sellerInfo = new PartyInfo();
		sellerInfo.setMemberId(tradeItemDetail.getSellerId());
		sellerInfo.setPartyRole(PartyRole.PAYEE);
		partyInfoList.add(sellerInfo);
		tradeFeeDTO.setPartyInfoList(partyInfoList);
		return getTradeFeeMap(tradeFeeDTO);
	}

	/**
	 * 查询手续费
	 *
	 * @param tradeFeeDTO
	 * @return
	 */
	private Map<String, String> getTradeFeeMap(TradeFeeDTO tradeFeeDTO) {
		TradeFeeQueryResponse feeQueryResponse = walletTradeService.queryFee(tradeFeeDTO);
		if (!feeQueryResponse.isSuccess()) {
			log.error("查询手续费失败！！！" + feeQueryResponse.getMsg());
			// TODO 针对参数拼接所造成的异常应该记录数据库，但是需要报警，并解决后重试
		}
		Map<String, String> feeInfo = new HashMap<>(2);
		feeInfo.put("buyerFee", feeQueryResponse.getBuyerFee());
		feeInfo.put("sellerFee", feeQueryResponse.getSellerFee());
		return feeInfo;
	}

	/**
	 * 查询会员信息<uid, accountType, accountIdentity>
	 *
	 * @param accountNo
	 * @return
	 */
	private Triple<String, String, String> getMemberInfo(String accountNo) {
		String accountIdentity = "";
		Account account = walletMemberService.queryAccountInfo(new AccountSimpleDTO(accountNo));
		if (StringUtils.isNotBlank(account.getExtention())) {
			accountIdentity = JSONObject.parseObject(account.getExtention()).getString(Constants.ACCT_IDENTITY);
		}
		return Triple.of(account.getMemberId(), String.valueOf(account.getAccountType()), accountIdentity);
	}

	@Override
	public void saveInstantPayDataToWitness(PaymentRequest paymentRequest, PaymentResponse paymentResponse) {
		log.info("同步交易见证-基于交易单支付请求参数:paymentRequest=>{}, paymentResponse=>{}", paymentRequest, paymentResponse);
		//1、交易信息
		TradeQueryResponse tradeQueryResponse = walletTradeService.queryTrade(new TradeQueryDTO(paymentRequest
				.getTradeVoucherNoList().get(0)));
		//2、支付信息（支付方式，机构号，渠道）
		PaymentInfo paymentInfo = paymentRequest.getPaymentInfo();
		CreateInstantTradeRequest createInstantTradeRequest = new CreateInstantTradeRequest();
		createInstantTradeRequest.setOuter_trade_no(tradeQueryResponse.getOuterTradeNo());
		createInstantTradeRequest.setOuter_inst_order_no(tradeQueryResponse.getPayMethodResult().getInstPayNo());
		createInstantTradeRequest.setWhite_channel_code(getPayWhiteChannelCode(tradeQueryResponse.getPayMethodResult()
				.getInstPayNo(), paymentInfo.getPayMethodList().get(0).getPayMode()));
		createInstantTradeRequest.setBuyer_id(tradeQueryResponse.getBuyerId());
		createInstantTradeRequest.setPay_method(buildInstantTradePayMethod(paymentInfo.getPayMethodList().get(0)));
		Map<String, String> feeInfo = new HashMap<>(2);
		feeInfo.put("buyerFee", tradeQueryResponse.getPayerFee());
		feeInfo.put("sellerFee", tradeQueryResponse.getPayeeFee());
		createInstantTradeRequest.setFee_info(feeInfo);
		createInstantTradeRequest.setSubject(tradeQueryResponse.getSubject());
		createInstantTradeRequest.setPrice(tradeQueryResponse.getTradeAmount());
		createInstantTradeRequest.setQuantity("1");
		createInstantTradeRequest.setTotal_amount(tradeQueryResponse.getTradeAmount());
		createInstantTradeRequest.setRoyalty_parameters(JSON.toJSONString(tradeQueryResponse.getSplitInfoList()));
		Triple<String, String, String> sellerMemberInfo = getMemberInfo(tradeQueryResponse.getSellerAccountNo());
		createInstantTradeRequest.setSeller_id(sellerMemberInfo.getLeft());
		createInstantTradeRequest.setAccount_type(sellerMemberInfo.getMiddle());
		createInstantTradeRequest.setAccount_identity(sellerMemberInfo.getRight());
		createInstantTradeRequest.setProduct_code(tradeQueryResponse.getBizProductCode());
		insertMqTransfer(createInstantTradeRequest, createInstantTradeRequest.getOuter_trade_no());
	}

	@Override
	public void saveRefundToWitness(RefundRequest refundRequest, RefundResponse refundResponse) {
		log.info("同步交易见证-退款请求参数:refundRequest=>{}, refundResponse=>{}", refundRequest, refundResponse);
		CreateRefundRequest createRefundRequest = new CreateRefundRequest();
		createRefundRequest.setBusiness_type(BusinessType.REFUND.getCode());
		createRefundRequest.setOuter_trade_no(refundRequest.getTradeVoucherNo());
		createRefundRequest.setOrig_outer_trade_no(refundRequest.getOrigTradeVoucherNo());
		if (refundResponse.isSuccess()) {
			// TODO 查询机构单号
			createRefundRequest.setOuter_inst_order_no("");
		}
		createRefundRequest.setRefund_amount(refundRequest.getRefundAmount());
		createRefundRequest.setRoyalty_parameters(buildSplitInfo(refundRequest.getSplitParameterList()));
		insertMqTransfer(createRefundRequest, createRefundRequest.getOuter_trade_no());
	}

	@Override
	public void saveFundoutToWitness(FundoutRequest fundoutRequest, FundoutResponse fundoutResponse) {
		log.info("同步交易见证-提现请求参数:fundoutRequest=>{}, fundoutResponse=>{}", fundoutRequest, fundoutResponse);
		PayToCardRequest payToCardRequest = new PayToCardRequest();
		payToCardRequest.setBusiness_type(BusinessType.PAYTOCARD.getCode());
		payToCardRequest.setOuter_trade_no(fundoutRequest.getFundoutOrderNo());
		// TODO 设置机构单号
		// TODO 设置渠道编码
		Triple<String, String, String> fundoutMember = getMemberInfo(fundoutRequest.getAccountNo());
		payToCardRequest.setUid(fundoutMember.getLeft());
		payToCardRequest.setAccount_type(fundoutMember.getMiddle());
		payToCardRequest.setAccount_identity(fundoutMember.getRight());
		payToCardRequest.setAmount(fundoutRequest.getAmount());
		payToCardRequest.setBank_account_no(fundoutRequest.getAccountNo());
		payToCardRequest.setBank_id(fundoutRequest.getCardId());
		payToCardRequest.setPay_attribute(PayAttrEnums.NORMAL.getCode());
		payToCardRequest.setProduct_code(fundoutRequest.getProductCode());
		insertMqTransfer(payToCardRequest, payToCardRequest.getOuter_trade_no());
	}

	/**
	 * 构造mqTransfer
	 *
	 * @param baseRequset
	 * @param request_no
	 */
	private void insertMqTransfer(BaseRequset baseRequset, String request_no) {
		log.info("插入MQ记录表，BaseRequset{}", ReflectionToStringBuilder.toString(baseRequset));
		MqTransfer mqTransfer = new MqTransfer();
		mqTransfer.setBusinessType(baseRequset.getBusiness_type());
		mqTransfer.setMessageData(JSON.toJSONString(baseRequset));
		mqTransfer.setRetryCount(0);
		mqTransfer.setVoucherNo(request_no);
		mqTransfer.setStatus(TransferStatus.PROCESS.getCode());
		transferDbService.insertTransfer(mqTransfer);
	}

}
