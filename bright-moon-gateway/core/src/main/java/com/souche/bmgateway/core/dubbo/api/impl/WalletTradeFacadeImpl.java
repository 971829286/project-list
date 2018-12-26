package com.souche.bmgateway.core.dubbo.api.impl;

import com.google.common.collect.Lists;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.ma.service.response.BankAccountInfoResponse;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import com.netfinworks.voucher.service.facade.domain.voucher.TradeVoucherInfo;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.domain.TradeSceneConfDO;
import com.souche.bmgateway.core.dubbo.api.aspect.DubboService;
import com.souche.bmgateway.core.dubbo.api.builder.TradeDtoBuilder;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ParamValidateException;
import com.souche.bmgateway.core.manager.enums.AccountTypeEnums;
import com.souche.bmgateway.core.manager.weijin.MemberManager;
import com.souche.bmgateway.core.service.config.TradeSceneConfService;
import com.souche.bmgateway.core.service.deposit.WalletDepositService;
import com.souche.bmgateway.core.service.dto.AccountSimpleDTO;
import com.souche.bmgateway.core.service.dto.BalanceTransferDTO;
import com.souche.bmgateway.core.service.dto.EntryDTO;
import com.souche.bmgateway.core.service.dto.InstantPayDTO;
import com.souche.bmgateway.core.service.dto.TradeInfoDTO;
import com.souche.bmgateway.core.service.dto.TradeQueryDTO;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;
import com.souche.bmgateway.core.service.fos.WalletFundoutService;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.trade.WalletTradeService;
import com.souche.bmgateway.core.service.voucher.VoucherService;
import com.souche.bmgateway.dubbo.api.WalletTradeFacade;
import com.souche.bmgateway.model.request.QueryBalanceRequest;
import com.souche.bmgateway.model.request.member.QueryAccountsRequest;
import com.souche.bmgateway.model.request.trade.CreateInstantTradeRequest;
import com.souche.bmgateway.model.request.trade.CreatePayOrderRequest;
import com.souche.bmgateway.model.request.trade.EntryRequest;
import com.souche.bmgateway.model.request.trade.FundoutTradeRequest;
import com.souche.bmgateway.model.request.trade.InstantPayRequest;
import com.souche.bmgateway.model.request.trade.SplitInfo;
import com.souche.bmgateway.model.request.trade.TradeFeeQueryRequest;
import com.souche.bmgateway.model.request.trade.TradeQueryRequest;
import com.souche.bmgateway.model.request.trade.TransferAccountRequest;
import com.souche.bmgateway.model.request.trade.WalletDeductRequest;
import com.souche.bmgateway.model.request.trade.WalletDepositRequest;
import com.souche.bmgateway.model.request.trade.WalletFrozenRequest;
import com.souche.bmgateway.model.request.trade.WalletRefundRequest;
import com.souche.bmgateway.model.request.trade.WalletUnFrozenRequest;
import com.souche.bmgateway.model.response.AccountBalanceInfo;
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.member.QueryAccountsResponse;
import com.souche.bmgateway.model.response.member.WalletAccountInfo;
import com.souche.bmgateway.model.response.trade.CreatePayOrderResponse;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;
import com.souche.bmgateway.model.response.trade.InstantTradeResponse;
import com.souche.bmgateway.model.response.trade.TradeFeeQueryResponse;
import com.souche.bmgateway.model.response.trade.TradeQueryResponse;
import com.souche.bmgateway.model.response.trade.WalletDeductResponse;
import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 主要用于简单的参数校验及参数封装，具体的实现逻辑在Service层
 *
 * @author zs. Created on 18/7/17.
 */
@Service("walletTradeFacade")
@Slf4j(topic = "dubbo.impl")
public class WalletTradeFacadeImpl implements WalletTradeFacade {

	@Resource
	private WalletMemberService walletMemberService;

	@Resource
	private WalletTradeService walletTradeService;

	@Resource
	private WalletDepositService walletDepositService;

	@Resource
	private WalletFundoutService walletFundoutService;

	@Resource
	private VoucherService voucherService;

	@Resource
	private TradeSceneConfService tradeSceneConfService;

	@Resource
	private MemberManager memberManager;

	@Override
	@DubboService(desc = "充值")
	public WalletDepositResponse walletDeposit(WalletDepositRequest walletDepositRequest) {
		log.info("充值，请求参数：{}", walletDepositRequest);
		WalletDepositDTO walletDepositDTO;
		try {
			Pair<String, String> memberInfo = complementMemberInfo(walletDepositRequest.getMemberId(),
					walletDepositRequest.getAccountType(), walletDepositRequest.getAccountIdentity());
			walletDepositDTO = TradeDtoBuilder.buildWalletDepositDTO(walletDepositRequest, memberInfo);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return WalletDepositResponse.createFailResp(new WalletDepositResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletDepositService.walletDeposit(walletDepositDTO);
	}

	@Override
	@DubboService(desc = "创建交易单", checkProductCode = true)
	public CreatePayOrderResponse createPayOrder(CreatePayOrderRequest createPayOrderRequest) {
		// 构建请求参数
		TradeInfoDTO tradeInfoDTO;
		try {
			tradeInfoDTO = buildTradeInfoDTO(createPayOrderRequest);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return CreatePayOrderResponse.createFailResp(new CreatePayOrderResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		// 买卖方校验&分润集校验
		InstantTradeResponse instantTradeResponse = walletTradeService.createInstantAndPay(tradeInfoDTO);
		CreatePayOrderResponse createPayOrderResponse = new CreatePayOrderResponse();
		BeanUtils.copyProperties(instantTradeResponse, createPayOrderResponse);
		return createPayOrderResponse;
	}

	/**
	 * 构建 TradeInfoDTO
	 *
	 * @param createPayOrderRequest
	 * @return
	 */
	private TradeInfoDTO buildTradeInfoDTO(CreatePayOrderRequest createPayOrderRequest) throws ParamValidateException {
		Pair<String, String> buyerInfo;
		if (StringUtils.isBlank(createPayOrderRequest.getBuyerId())) {
			buyerInfo = Pair.of(Constants.ANYMOUS, null);
		} else {
			buyerInfo = complementMemberInfo(createPayOrderRequest.getBuyerId(), null, null);
		}

		Pair<String, String> sellerInfo;
		if (StringUtils.isNotBlank(createPayOrderRequest.getSellerId())) {
			sellerInfo = complementMemberInfo(createPayOrderRequest.getSellerId(),
					createPayOrderRequest.getSellerAccountType(), null);
		} else {
			// 如果没有有效的卖家信息则根据业务码查询
			TradeSceneConfDO tradeSceneConfDO = tradeSceneConfService
					.queryTradeConf(createPayOrderRequest.getBizProductCode());
			sellerInfo = complementMemberInfo(tradeSceneConfDO.getSellerId(), tradeSceneConfDO.getSellerAccountType(),
					tradeSceneConfDO.getSellerAccountIdentity());
		}
		List<SplitParameter> splitParameterList = complementSplitInfo(createPayOrderRequest.getSplitInfoList());
		return TradeDtoBuilder.buildTradeInfoDTO(createPayOrderRequest, splitParameterList, buyerInfo, sellerInfo);
	}

	/**
	 * 构建用户信息<memberId, accountNo> memberId 不能为空，accountType为空的时候返回基本户
	 * 
	 * @param memberId
	 * @param accountType
	 * @return
	 */
	private Pair<String, String> complementMemberInfo(String memberId, String accountType, String memberIdentity)
			throws ParamValidateException {

		// 会员id不能为空
		if (StringUtils.isBlank(memberId)) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "会员id为空" + memberId);
		}

		// 查询会员信息
		QueryAccountsRequest queryAccountsRequest = new QueryAccountsRequest();
		queryAccountsRequest.setMemberId(memberId);
		queryAccountsRequest.setAccountType(accountType);
		queryAccountsRequest.setMemberIdentity(memberIdentity);
		QueryAccountsResponse queryAccountsResponse = walletMemberService.queryWalletAccountsInfo(queryAccountsRequest);
		if (!queryAccountsResponse.isSuccess()) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "会员账户信息查询失败" + memberId);
		}

		// 账户列表为空
		if (queryAccountsResponse.getWalletAccountInfoList() == null
				|| queryAccountsResponse.getWalletAccountInfoList().isEmpty()) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "会员没有激活任何账号" + memberId);
		}

		String accountNo = null;
		if (StringUtils.isBlank(accountType)) {
			// 账户类型为空时取基本账号类型
			for (WalletAccountInfo walletAccountInfo : queryAccountsResponse.getWalletAccountInfoList()) {
				if (AccountTypeEnums.ENTERPRISE_BASE_ACCOUNT.getCode() == walletAccountInfo.getAccountType().longValue()
						|| AccountTypeEnums.PERSON_BASE_ACCOUNT.getCode() == walletAccountInfo.getAccountType()
								.longValue()) {
					accountNo = walletAccountInfo.getAccountId();
				}
			}
		} else {
			// 查询的账户不止一个，没有传入明确的账户标识
			if (queryAccountsResponse.getWalletAccountInfoList().size() > 1) {
				throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "账户标识不能为空" + memberId);
			}
			// 账户类型不为空时根据账户类型和标识只能查到一个账号
			accountNo = queryAccountsResponse.getWalletAccountInfoList().get(0).getAccountId();
		}
		return Pair.of(queryAccountsResponse.getMemberId(), accountNo);
	}

	/**
	 * 构建分润信息 TODO 后续分账信息也可以根据产品码配置获取，支持按比例分账
	 *
	 * @param splitInfoList
	 * @return
	 */
	private List<SplitParameter> complementSplitInfo(List<SplitInfo> splitInfoList) throws ParamValidateException {
		if (splitInfoList == null || splitInfoList.isEmpty()) {
			return null;
		}
		List<SplitParameter> splitParameterList = Lists.newArrayList();
		for (SplitInfo splitInfo : splitInfoList) {
			Pair<String, String> memberInfo = complementMemberInfo(splitInfo.getMemberId(), splitInfo.getAccountType(),
					splitInfo.getAccountIdentity());
			SplitParameter splitParameter = new SplitParameter();
			splitParameter.setPayeeId(memberInfo.getLeft());
			splitParameter.setPayeeAccountNo(memberInfo.getRight());
			splitParameter.setAmount(new Money(splitInfo.getAmount()));
			splitParameterList.add(splitParameter);
		}
		return splitParameterList;
	}

	@Override
	@DubboService(desc = "基于支付单支付")
	public InstantTradeResponse doPayWithPayOrder(InstantPayRequest instantPayRequest) {
		// 校验交易凭证号
		TradeVoucherInfo tradeVoucherInfo = voucherService.queryTradeVoucherInfo(instantPayRequest.getTradeVoucherNo());
		if (tradeVoucherInfo == null) {
			return InstantTradeResponse.createFailResp(new InstantTradeResponse(),
					ErrorCodeEnums.VOUCHER_INFO_NOT_EXIST.getCode(),
					"基于交易单支付失败" + ErrorCodeEnums.VOUCHER_INFO_NOT_EXIST.getMessage());
		}

		// 支付
		InstantPayDTO instantPayDTO;
		try {
			Pair<String, String> buyerInfo = complementMemberInfo(tradeVoucherInfo.getBuyerMemberId(), null, null);
			instantPayDTO = TradeDtoBuilder.buildPaymentInfo(instantPayRequest, buyerInfo);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return InstantTradeResponse.createFailResp(new InstantTradeResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletTradeService.doPayWithPayOrder(instantPayDTO);
	}

	@Override
	@DubboService(desc = "创建交易单并支付", checkProductCode = true)
	public InstantTradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest) {
		TradeInfoDTO tradeInfoDTO;
		try {
			tradeInfoDTO = buildTradeInfoDTO(createInstantTradeRequest.getCreatePayOrderRequest());
			TradeDtoBuilder.buildPayInfoDTO(tradeInfoDTO, createInstantTradeRequest);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return InstantTradeResponse.createFailResp(new InstantTradeResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletTradeService.createInstantAndPay(tradeInfoDTO);
	}

	@Override
	@DubboService(desc = "转账", checkProductCode = true)
	public CommonResponse transferAccount(TransferAccountRequest transferAccountRequest) {
		BalanceTransferDTO balanceTransferDTO;
		try {
			balanceTransferDTO = getBalanceTransferDTO(transferAccountRequest);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return CommonResponse.createFailResp(ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletTradeService.transferAccount(balanceTransferDTO);
	}

	/**
	 * 构建转账请求参数
	 *
	 * @param transferAccountRequest
	 * @return
	 * @throws ParamValidateException
	 */
	private BalanceTransferDTO getBalanceTransferDTO(TransferAccountRequest transferAccountRequest)
			throws ParamValidateException {
		Pair<String, String> buyerMemberInfo = null;
		Pair<String, String> sellerMemberInfo = null;

		if (StringUtils.isNotBlank(transferAccountRequest.getFundoutMemberId())
				&& StringUtils.isNotBlank(transferAccountRequest.getFundinMemberId())) {
			// 转账双方的信息都不为空
			buyerMemberInfo = complementMemberInfo(transferAccountRequest.getFundoutMemberId(),
					transferAccountRequest.getFundoutAccoutType(), transferAccountRequest.getFundoutIdentityNo());
			sellerMemberInfo = complementMemberInfo(transferAccountRequest.getFundinMemberId(),
					transferAccountRequest.getFundinAccoutType(), transferAccountRequest.getFundinIdentityNo());
		} else {
			// 买家或卖家信息不存在，根据产品码查询配置表，然后再设置买卖双方
			TradeSceneConfDO tradeSceneConfDO = tradeSceneConfService
					.queryTradeConf(transferAccountRequest.getBizProductCode());
			if (StringUtils.isBlank(transferAccountRequest.getFundoutMemberId())) {
				buyerMemberInfo = complementMemberInfo(tradeSceneConfDO.getBuyerId(),
						tradeSceneConfDO.getBuyerAccountType(), tradeSceneConfDO.getBuyerAccountIdentity());
			}
			if (StringUtils.isBlank(transferAccountRequest.getFundinMemberId())) {
				sellerMemberInfo = complementMemberInfo(tradeSceneConfDO.getSellerId(),
						tradeSceneConfDO.getSellerAccountType(), tradeSceneConfDO.getSellerAccountIdentity());
			}
		}
		return TradeDtoBuilder.buildTransferDTO(transferAccountRequest, buyerMemberInfo, sellerMemberInfo);
	}

	@Override
	@DubboService(desc = "扣款", checkProductCode = true)
	public WalletDeductResponse walletDeduct(WalletDeductRequest walletDeductRequest) {
		// 参数校验
		BalanceTransferDTO balanceTransferDTO;
		try {
			balanceTransferDTO = getBalanceTransferDTO(walletDeductRequest.getTransferAccountRequest());
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return WalletDeductResponse.createFailResp(new WalletDeductResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		// 查询转出方余额
		return queryBalanceAndTransfer(balanceTransferDTO);
	}

	/**
	 * 查询余额并转账 循环 循环结束条件：1、账户余额为0; 2、转账调用成功;
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	private WalletDeductResponse queryBalanceAndTransfer(BalanceTransferDTO balanceTransferDTO) {
		BigDecimal accountBalance;
		// 是否结束循环
		boolean isEnd = false;

		while (true) {
			accountBalance = getAccountBalance(balanceTransferDTO);

			// 用户余额为0则跳出循环
			if (accountBalance.equals(new BigDecimal(0))) {
				isEnd = true;
			}

			// 转账调用成功则跳出循环
			balanceTransferDTO.setTransferAmount(accountBalance.toString());
			CommonResponse commonResponse = walletTradeService.transferAccount(balanceTransferDTO);
			if (commonResponse.isSuccess()) {
				isEnd = true;
			}

			// 结束循环
			if (isEnd) {
				WalletDeductResponse walletDeductResponse = WalletDeductResponse
						.createSuccessResp(new WalletDeductResponse());
				walletDeductResponse.setDeductAmount(accountBalance.toString());
				return walletDeductResponse;
			}
		}
	}

	/**
	 * 查询账户余额
	 *
	 * @param balanceTransferDTO
	 * @return
	 */
	private BigDecimal getAccountBalance(BalanceTransferDTO balanceTransferDTO) {
		QueryBalanceRequest queryBalanceRequest = new QueryBalanceRequest();
		queryBalanceRequest.setMemberId(balanceTransferDTO.getFundoutMemberId());
		BalanceResponse balanceResponse = walletMemberService.queryAccountBalance(queryBalanceRequest);
		for (AccountBalanceInfo accountBalanceInfo : balanceResponse.getAccountList()) {
			if (balanceTransferDTO.getFundOutAccountId().equals(accountBalanceInfo.getAccountNo())) {
				return new BigDecimal(accountBalanceInfo.getBalance());
			}
		}
		return new BigDecimal(0);
	}

	@Override
	@DubboService(desc = "退款", checkProductCode = true)
	public CommonResponse refund(WalletRefundRequest walletRefundRequest) {
		// 校验原交易凭证号是否存在
		TradeVoucherInfo tradeVoucherInfo = voucherService
				.queryTradeVoucherInfo(walletRefundRequest.getOrigOuterTradeNo());
		if (tradeVoucherInfo == null) {
			return CommonResponse.createFailResp(ErrorCodeEnums.VOUCHER_INFO_NOT_EXIST.getCode(),
					"退款失败" + ErrorCodeEnums.VOUCHER_INFO_NOT_EXIST.getMessage());
		}

		List<SplitParameter> splitParameterList;
		try {
			splitParameterList = complementSplitInfo(walletRefundRequest.getSplitInfoList());
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return CommonResponse.createFailResp(ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}

		return walletTradeService.refund(TradeDtoBuilder.buildRefundDTO(walletRefundRequest, splitParameterList));
	}

	@Override
	@DubboService(desc = "提现")
	public FundoutTradeResponse walletFundout(FundoutTradeRequest fundoutRequestDO) {
		log.info("提现，请求参数 fundoutRequest:{}", fundoutRequestDO);
		Pair<String, String> memberInfo;
		BankAccountInfoResponse bankAccountInfoResponse = null;
		try {
			memberInfo = complementMemberInfo(fundoutRequestDO.getMemberId(), fundoutRequestDO.getAccountType(),
					fundoutRequestDO.getAccountIdentity());
			// 验证卡信息
			bankAccountInfoResponse = memberManager.queryBankAccountDetail(fundoutRequestDO.getBankCardId());
		} catch (Exception e) {
			log.error("参数校验异常", e);
			return FundoutTradeResponse.createFailResp(new FundoutTradeResponse(),
					ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		
		return walletFundoutService.walletFundout(TradeDtoBuilder.buildFundoutDTO(fundoutRequestDO, memberInfo,bankAccountInfoResponse));
	}

	@Override
	@DubboService(desc = "登帐", checkProductCode = true)
	public CommonResponse entry(EntryRequest entryRequest) {
		EntryDTO entryDTO = TradeDtoBuilder.buildEntryDTO(entryRequest);
		try {
			buildPartyAccountInfo(entryRequest, entryDTO);
		} catch (ParamValidateException e) {
			log.error("参数校验异常", e);
			return CommonResponse.createFailResp(ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletTradeService.entry(TradeDtoBuilder.buildEntryDTO(entryRequest));
	}

	/**
	 * 构建登帐参与方信息
	 *
	 * @param entryRequest
	 * @param entryDTO
	 * @throws ParamValidateException
	 */
	private void buildPartyAccountInfo(EntryRequest entryRequest, EntryDTO entryDTO) throws ParamValidateException {
		switch (entryRequest.getEntryType()) {
		case IN2IN:
			entryDTO.setDrAccountNo(validateInnerAccount(entryRequest.getDrInnerAccount()));
			entryDTO.setCrAccountNo(validateInnerAccount(entryRequest.getCrInnerAccount()));
			break;
		case OUT2OUT:
			validateDrOuterAccount(entryRequest, entryDTO);
			validateCrOuterAccount(entryRequest, entryDTO);
			break;
		case IN2OUT:
			entryDTO.setDrAccountNo(validateInnerAccount(entryRequest.getDrInnerAccount()));
			validateCrOuterAccount(entryRequest, entryDTO);
			break;
		case OUT2IN:
			validateDrOuterAccount(entryRequest, entryDTO);
			entryDTO.setCrAccountNo(validateInnerAccount(entryRequest.getCrInnerAccount()));
			break;
		default:
		}
	}

	/**
	 * 检验借方外部账户
	 *
	 * @param entryRequest
	 * @param entryDTO
	 * @return
	 * @throws ParamValidateException
	 */
	private void validateDrOuterAccount(EntryRequest entryRequest, EntryDTO entryDTO) throws ParamValidateException {
		if (StringUtils.isBlank(entryRequest.getDrMemberId()) || StringUtils.isBlank(entryRequest.getDrAccountType())
				|| StringUtils.isBlank(entryRequest.getDrFundType())) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "借方账户信息不完整");
		}
		Pair<String, String> drMemberInfo = complementMemberInfo(entryRequest.getDrMemberId(),
				entryRequest.getDrAccountType(), null);
		if (StringUtils.isBlank(drMemberInfo.getRight())) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "借方账户不存在");
		}
		entryDTO.setDrMemberId(drMemberInfo.getLeft());
		entryDTO.setDrAccountNo(drMemberInfo.getRight());
		entryDTO.setDrFundType(entryRequest.getDrFundType());
	}

	/**
	 * 检验贷方外部账户
	 *
	 * @param entryRequest
	 * @param entryDTO
	 * @return
	 * @throws ParamValidateException
	 */
	private void validateCrOuterAccount(EntryRequest entryRequest, EntryDTO entryDTO) throws ParamValidateException {
		if (StringUtils.isBlank(entryRequest.getCrMemberId()) || StringUtils.isBlank(entryRequest.getCrAccountType())
				|| StringUtils.isBlank(entryRequest.getCrFundType())) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "贷方账户信息不完整");
		}
		Pair<String, String> crMemberInfo = complementMemberInfo(entryRequest.getCrMemberId(),
				entryRequest.getCrAccountType(), null);
		if (StringUtils.isBlank(crMemberInfo.getRight())) {
			throw new ParamValidateException(ErrorCodeEnums.ILLEGAL_ARGUMENT, "贷方账户不存在");
		}
		entryDTO.setCrMemberId(crMemberInfo.getLeft());
		entryDTO.setCrAccountNo(crMemberInfo.getRight());
		entryDTO.setCrFundType(entryRequest.getCrFundType());
	}

	/**
	 * 校验内部账号
	 *
	 * @param accountNo
	 * @throws ParamValidateException
	 */
	private String validateInnerAccount(String accountNo) throws ParamValidateException {
		Account account = walletMemberService.queryAccountInfo(new AccountSimpleDTO(accountNo));
		if (account == null) {
			throw new ParamValidateException(ErrorCodeEnums.SYSTEM_ERROR, "内部账号不存在");
		}
		return account.getAccountId();
	}

	/**
	 * 冻结账户金额
	 * 
	 * @param walletFrozenRequest
	 * @return
	 */
	@Override
	@DubboService(desc = "资金冻结", checkProductCode = true)
	public CommonResponse walletFrozen(WalletFrozenRequest walletFrozenRequest) {
		Pair<String, String> memberInfo = null;
		try {
			memberInfo = complementMemberInfo(walletFrozenRequest.getMemberId(), walletFrozenRequest.getAccountType(),
					walletFrozenRequest.getAccountIdentity());
		} catch (ParamValidateException e) {
			log.error("资金冻结参数校验失败", e);
			CommonResponse.createFailResp(ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		}
		return walletTradeService.walletFrozen(TradeDtoBuilder.buildWalletFrozenDTO(walletFrozenRequest, memberInfo));
	}

	/**
	 * 解冻账户金额
	 * 
	 * @param walletUnFrozenRequest
	 * @return
	 */
	@Override
	@DubboService(desc = "资金解冻", checkProductCode = true)
	public CommonResponse walletUnFrozen(WalletUnFrozenRequest walletUnFrozenRequest) {
		Money freezeAmount = new Money(walletUnFrozenRequest.getUnFrozenAmount());
		if (!freezeAmount.greaterThan(new Money())) {
			return CommonResponse.createFailResp(ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(), "解冻金额需要大于0");
		}
		return walletTradeService.walletUnFrozen(TradeDtoBuilder.buildWalletUnFrozenDTO(walletUnFrozenRequest));
	}

	@Override
    @DubboService(desc = "交易查询")
	public TradeQueryResponse queryTrade(TradeQueryRequest tradeQueryRequest) {
        return walletTradeService.queryTrade(new TradeQueryDTO(tradeQueryRequest.getOuterOrderNo()));
	}

	@Override
	@DubboService(desc = "查询手续费", checkProductCode = true)
	public TradeFeeQueryResponse queryTradeFee(TradeFeeQueryRequest tradeFeeQueryRequest) {
		return walletTradeService.queryFee(TradeDtoBuilder.buildTradeFeeDTO(tradeFeeQueryRequest));
	}

}
