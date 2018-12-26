package com.souche.bmgateway.core.service.trade.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.payment.common.v2.enums.PartyRole;
import com.netfinworks.pbs.service.context.vo.PartyFeeInfo;
import com.netfinworks.pbs.service.context.vo.PaymentPricingResponse;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethodResult;
import com.netfinworks.tradeservice.facade.model.query.TradeBasicInfo;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.TradeDetailQueryResponse;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.weijin.PbsManager;
import com.souche.bmgateway.core.manager.weijin.PfsManager;
import com.souche.bmgateway.core.manager.weijin.TssManager;
import com.souche.bmgateway.core.service.dto.AccountSimpleDTO;
import com.souche.bmgateway.core.service.dto.BalanceTransferDTO;
import com.souche.bmgateway.core.service.dto.EntryDTO;
import com.souche.bmgateway.core.service.dto.InstantPayDTO;
import com.souche.bmgateway.core.service.dto.TradeFeeDTO;
import com.souche.bmgateway.core.service.dto.TradeInfoDTO;
import com.souche.bmgateway.core.service.dto.TradeQueryDTO;
import com.souche.bmgateway.core.service.dto.WalletFrozenDTO;
import com.souche.bmgateway.core.service.dto.WalletRefundDTO;
import com.souche.bmgateway.core.service.dto.WalletUnFrozenDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.trade.TradeConvert;
import com.souche.bmgateway.core.service.trade.WalletTradeService;
import com.souche.bmgateway.core.service.voucher.VoucherService;
import com.souche.bmgateway.model.request.trade.SplitInfo;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.trade.InstantTradeResponse;
import com.souche.bmgateway.model.response.trade.PayChannelResult;
import com.souche.bmgateway.model.response.trade.TradeFeeQueryResponse;
import com.souche.bmgateway.model.response.trade.TradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zs.
 * Created on 18/11/19.
 */
@Service("walletTradeService")
@Slf4j(topic = "service")
public class WalletTradeServiceImpl implements WalletTradeService {

	@Resource
	private TssManager tssManager;
	@Resource
	private PfsManager pfsManager;
	@Resource
	private PbsManager pbsManager;
	@Resource
	private VoucherService voucherService;
	@Resource
	private WalletMemberService walletMemberService;

	@Override
	public InstantTradeResponse createInstantAndPay(TradeInfoDTO tradeInfoDTO) {
		log.info("创建支付单并支付，请求参数：{}", tradeInfoDTO);
		PaymentResponse paymentResponse;
		try {
			voucherService.setTradeInfoVoucher(tradeInfoDTO);
			paymentResponse = tssManager.createAndPay(TradeConvert.convertTradeReq(tradeInfoDTO));
		} catch (Exception e) {
			log.error("下单并支付异常，请求参数：{}, 异常：", tradeInfoDTO, e);
			return InstantTradeResponse.createFailResp(new InstantTradeResponse(),
					ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
		return buildPaymentResult(paymentResponse, tradeInfoDTO.getTradeVoucherNo());
	}

	/**
	 * 构建即时交易返回结果
	 *
	 * @param paymentResponse
	 * @return
	 */
	private InstantTradeResponse buildPaymentResult(PaymentResponse paymentResponse, String tradeVoucherNo) {
		InstantTradeResponse instantTradeResponse = new InstantTradeResponse();
		instantTradeResponse.setSuccess(paymentResponse.isSuccess());
		instantTradeResponse.setCode(paymentResponse.getErrorCode());
		instantTradeResponse.setMsg(paymentResponse.getResultMessage());
		instantTradeResponse.setTradeVoucherNo(tradeVoucherNo);
		instantTradeResponse.setPaymentStatus(paymentResponse.getPaymentStatus());
		if (paymentResponse.getPayMethodResultList() != null) {
			for (PayMethodResult payMethodResult : paymentResponse.getPayMethodResultList()) {
				PayChannelResult payChannelResult = new PayChannelResult();
				BeanUtils.copyProperties(payMethodResult, payChannelResult);
				instantTradeResponse.setPayMethodResult(payChannelResult);
			}
		}
		instantTradeResponse.setExtension("");
		return instantTradeResponse;
	}

	@Override
	public InstantTradeResponse doPayWithPayOrder(InstantPayDTO instantPayDTO) {
		log.info("基于支付单支付，请求参数：{}", instantPayDTO);
		PaymentResponse paymentResponse;
		try {
			voucherService.setInstantPayVoucher(instantPayDTO);
			paymentResponse = tssManager.pay(TradeConvert.convertPaymentRequest(instantPayDTO));
		} catch (Exception e) {
			log.error("基于交易单支付异常，请求参数：{}, 异常：", instantPayDTO, e);
			return InstantTradeResponse.createFailResp(new InstantTradeResponse(),
					ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
		return buildPaymentResult(paymentResponse, instantPayDTO.getTradeVoucherNo());
	}

	@Override
	public CommonResponse transferAccount(BalanceTransferDTO balanceTransferDTO) {
		log.info("转账，请求参数：{}", balanceTransferDTO);
		PaymentResponse paymentResponse;
		try {
			voucherService.setBalanceTransferVoucher(balanceTransferDTO);
			paymentResponse = tssManager.createAndPay(TradeConvert.convertTransferRequest(balanceTransferDTO));
		} catch (Exception e) {
			log.error("转账异常，请求参数：{}, 异常：", balanceTransferDTO, e);
			return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
		return CommonResponse.createResp(paymentResponse.isSuccess(), paymentResponse.getErrorCode(), paymentResponse.getResultMessage());
	}

	@Override
	public CommonResponse refund(WalletRefundDTO walletRefundDTO) {
		log.info("退款，请求参数：{}", walletRefundDTO);
		try {
			voucherService.setWalletRefundVoucher(walletRefundDTO);
			tssManager.refund(TradeConvert.convertRefundRequest(walletRefundDTO));
			return CommonResponse.createSuccessResp();
		} catch (Exception e) {
			log.error("退款异常，请求参数：{}, 异常：", walletRefundDTO, e);
			return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
	}

	@Override
	public CommonResponse entry(EntryDTO entryDTO) {
		log.info("登帐，请求参数：{}", entryDTO);
		EntryAcountRequest entryAcountRequest = new EntryAcountRequest();
		try {
			BeanUtils.copyProperties(entryDTO, entryAcountRequest);
			pfsManager.enter(entryAcountRequest);
			return CommonResponse.createSuccessResp();
		} catch (ManagerException e) {
			log.error("登帐异常，请求参数：{}, 异常：", entryAcountRequest, e);
			return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
	}

	/**
	 * 钱包资金冻结
	 *
	 * @param walletFrozenDTO
	 * @return
	 */
	@Override
	public CommonResponse walletFrozen(WalletFrozenDTO walletFrozenDTO) {
		log.info("资金冻结服务, 请求参数: {}", walletFrozenDTO);
		try {
			voucherService.setWalletFrozenVoucher(walletFrozenDTO);
			pfsManager.freeze(TradeConvert.convertWalletFrozenRequest(walletFrozenDTO));
			return CommonResponse.createSuccessResp();
		} catch (Exception e) {
			log.error("冻结账户发生异常, 请求参数:{}, 异常:", walletFrozenDTO, e);
			return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
	}

	@Override
	public CommonResponse walletUnFrozen(WalletUnFrozenDTO walletUnFrozenDTO) {
		log.info("资金解冻服务, 请求参数 => {}", walletUnFrozenDTO);
		try {
			voucherService.setOrigOuterTradeVoucher(walletUnFrozenDTO);
			voucherService.setWalletUnFrozenVoucher(walletUnFrozenDTO);
			pfsManager.unfreeze(TradeConvert.convertWalletUnFrozenRequest(walletUnFrozenDTO));
		} catch (Exception e) {
			log.error("解冻账户时发生异常,请求参数:{},异常:{}", walletUnFrozenDTO, e);
		}
		return CommonResponse.createSuccessResp();
	}

	@Override
	public TradeQueryResponse queryTrade(TradeQueryDTO tradeQueryDTO) {
		log.info("查询交易信息, 请求参数 => {}", tradeQueryDTO);
		TradeDetailQueryResponse tradeDetailQueryResponse;
		try {
			tradeDetailQueryResponse = tssManager.queryTradeDetail(tradeQueryDTO.getTradeVoucherNo());
		} catch (ManagerException e) {
			log.error("查询交易信息 请求参数:{}, 异常:", tradeQueryDTO, e);
			return TradeQueryResponse.createFailResp(new TradeQueryResponse(), ErrorCodeEnums
					.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}
		return buildTradeQueryResponse(tradeDetailQueryResponse);
	}

	/**
	 * 构建查询请求结果
	 *
	 * @param tradeDetailQueryResponse
	 * @return
	 */
	private TradeQueryResponse buildTradeQueryResponse(TradeDetailQueryResponse tradeDetailQueryResponse) {
		TradeQueryResponse tradeQueryResponse = TradeQueryResponse.createSuccessResp(new TradeQueryResponse());
		TradeBasicInfo tradeBasic = tradeDetailQueryResponse.getTradeBasic();
		if (tradeDetailQueryResponse.getPayMethodResultList() != null) {
			for (PayMethodResult payMethodResult : tradeDetailQueryResponse.getPayMethodResultList()) {
				PayChannelResult payChannelResult = new PayChannelResult();
				BeanUtils.copyProperties(payMethodResult, payChannelResult);
				tradeQueryResponse.setPayMethodResult(payChannelResult);
			}
		}
		tradeQueryResponse.setOuterTradeNo(tradeBasic.getTradeSourceVoucherNo());
		tradeQueryResponse.setTradeVoucherNo(tradeBasic.getTradeVoucherNo());
		tradeQueryResponse.setBizProductCode(tradeBasic.getBizProductCode());
		tradeQueryResponse.setSubject(tradeBasic.getTradeMemo());
		tradeQueryResponse.setBuyerId(tradeBasic.getBuyerId());
		tradeQueryResponse.setSellerId(tradeBasic.getSellerId());
//		tradeQueryResponse.setSellerAccountNo(); TODO 交易服务查询接口需要返回卖家账号、买家手续费、卖家手续费
//		tradeQueryResponse.setPayerFee();
//		tradeQueryResponse.setPayeeFee();
		tradeQueryResponse.setSplitInfoList(buildSplitInfo(tradeBasic.getSplitParameter()));
		tradeQueryResponse.setTradeAmount(tradeBasic.getTradeAmount().toString());
		tradeQueryResponse.setTradeStatus(tradeBasic.getStatus());
		return tradeQueryResponse;
	}

	private List<SplitInfo> buildSplitInfo(List<SplitParameter> splitParameterList) {
		List<SplitInfo> splitInfoList = Lists.newArrayList();
		if (splitParameterList == null || splitParameterList.isEmpty()) {
			return null;
		}
		for (SplitParameter splitParameter : splitParameterList) {
			String accountIdentity = "";
			Account account = walletMemberService.queryAccountInfo(new AccountSimpleDTO(splitParameter.getPayeeAccountNo()));
			if (StringUtils.isNotBlank(account.getExtention())) {
				accountIdentity = JSONObject.parseObject(account.getExtention()).getString(Constants.ACCT_IDENTITY);
			}
			SplitInfo splitInfo = new SplitInfo();
			splitInfo.setMemberId(account.getMemberId());
			splitInfo.setAccountType(account.getAccountType().toString());
			splitInfo.setAccountIdentity(accountIdentity);
			splitInfo.setAmount(splitParameter.getAmount().toString());
			splitInfoList.add(splitInfo);
		}
		return splitInfoList;
	}

	@Override
	public TradeFeeQueryResponse queryFee(TradeFeeDTO tradeFeeDTO) {
		log.info("查询手续费信息，请求参数：{}", tradeFeeDTO);
		try {
			PaymentPricingResponse pricingResponse = pbsManager.getFeeInfo(TradeConvert.convertQueryTradeFee(tradeFeeDTO));
			TradeFeeQueryResponse feeQueryResponse = TradeFeeQueryResponse.createSuccessResp(new TradeFeeQueryResponse());
			List<PartyFeeInfo> partyFeeList = pricingResponse.getPartyFeeList();
			for (PartyFeeInfo partyFeeInfo : partyFeeList) {
				if (PartyRole.PAYER.equals(partyFeeInfo.getPartyRole()) && partyFeeInfo.getFee() != null) {
					feeQueryResponse.setBuyerId(partyFeeInfo.getMemberId());
					feeQueryResponse.setBuyerFee(partyFeeInfo.getFee().getAmount().toString());
				}
				if (PartyRole.PAYEE.equals(partyFeeInfo.getPartyRole()) && partyFeeInfo.getFee() != null) {
					feeQueryResponse.setSellerId(partyFeeInfo.getMemberId());
					feeQueryResponse.setSellerFee(partyFeeInfo.getFee().getAmount().toString());
				}
			}
			return feeQueryResponse;
		} catch (ManagerException e) {
			log.error("查询交易手续费信息异常，请求参数：{}, 异常：", tradeFeeDTO, e);
			return TradeFeeQueryResponse.createFailResp(new TradeFeeQueryResponse(),
					ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
		}

	}

}
