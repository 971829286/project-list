package com.souche.bmgateway.core.service.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netfinworks.common.lang.StringUtil;
import com.netfinworks.dts.api.models.WitnessMemberInfoResponse;
import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.ma.service.base.model.BankAccountInfo;
import com.netfinworks.ma.service.base.model.BankAcctInfo;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.base.model.DecipherItem;
import com.netfinworks.ma.service.base.model.DecipherResult;
import com.netfinworks.ma.service.base.model.VerifyInfo;
import com.netfinworks.ma.service.request.AccountQueryRequest;
import com.netfinworks.ma.service.request.AccountRequest;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
import com.netfinworks.ma.service.request.BankAccRemoveRequest;
import com.netfinworks.ma.service.request.BankAccountRequest;
import com.netfinworks.ma.service.request.DecipherInfoRequest;
import com.netfinworks.ma.service.request.MemberIntegratedIdRequest;
import com.netfinworks.ma.service.request.MemberIntegratedRequest;
import com.netfinworks.ma.service.request.PersonalMemberInfoRequest;
import com.netfinworks.ma.service.request.VerifyInputRequest;
import com.netfinworks.ma.service.request.VerifyQueryRequest;
import com.netfinworks.ma.service.response.AccountInfo;
import com.netfinworks.ma.service.response.AccountResponse;
import com.netfinworks.ma.service.response.BankAccInfoResponse;
import com.netfinworks.ma.service.response.BankAccountResponse;
import com.netfinworks.ma.service.response.CreateMemberInfoResponse;
import com.netfinworks.ma.service.response.DecipherInfoResponse;
import com.netfinworks.ma.service.response.IntegratedPersonalResponse;
import com.netfinworks.ma.service.response.MemberIntegratedResponse;
import com.netfinworks.ma.service.response.OpenAccountResponse;
import com.netfinworks.ma.service.response.VerifyInfoResponse;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.CardAttrEnums;
import com.souche.bmgateway.core.enums.CardTypeEnums;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.PayAttrEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.enums.MemberTypeEnums;
import com.souche.bmgateway.core.manager.enums.VerifyStatusEnums;
import com.souche.bmgateway.core.manager.enums.VerifyTypeEnums;
import com.souche.bmgateway.core.manager.model.AuthRealNameResponse;
import com.souche.bmgateway.core.manager.umpay.AuthRealNameManager;
import com.souche.bmgateway.core.manager.weijin.MemberManager;
import com.souche.bmgateway.core.manager.witness.WitnessManager;
import com.souche.bmgateway.core.service.dto.AccountSimpleDTO;
import com.souche.bmgateway.core.service.dto.MemberSimpleDTO;
import com.souche.bmgateway.core.service.dto.SyncIdCardDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.member.builder.BankCardInfoBO;
import com.souche.bmgateway.core.service.member.builder.WalletMemberBuilder;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.core.witness.aspect.TradeWitness;
import com.souche.bmgateway.model.request.AuthRealNameRequest;
import com.souche.bmgateway.model.request.CreateBankCardRequest;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.ModifyEnterpriseInfoRequest;
import com.souche.bmgateway.model.request.QueryBalanceRequest;
import com.souche.bmgateway.model.request.QueryBankAccountRequest;
import com.souche.bmgateway.model.request.QueryVerifyInfoRequest;
import com.souche.bmgateway.model.request.QueryWalletMemberInfoRequest;
import com.souche.bmgateway.model.request.RemoveBankCardRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.bmgateway.model.request.member.ModifyPersonalInfoRequest;
import com.souche.bmgateway.model.request.member.OpenAccountRequest;
import com.souche.bmgateway.model.request.member.QueryAccountsRequest;
import com.souche.bmgateway.model.request.member.QueryWitnessInfoRequest;
import com.souche.bmgateway.model.response.AccountBalanceInfo;
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.BankAccountListResponse;
import com.souche.bmgateway.model.response.BankAccountRecordInfo;
import com.souche.bmgateway.model.response.BankCardResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.bmgateway.model.response.UserVerifyInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.bmgateway.model.response.member.QueryAccountsResponse;
import com.souche.bmgateway.model.response.member.QueryWitnessInfoResponse;
import com.souche.bmgateway.model.response.member.WalletAccountInfo;
import com.souche.optimus.common.util.DateTimeUtil;
import com.souche.optimus.common.util.Exceptions;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员服务实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Service("walletMemberService")
@Slf4j(topic = "service")
public class WalletMemberServiceImpl implements WalletMemberService {

    @Resource
    private MemberManager memberManager;

    @Resource
    private UesService uesService;

    @Resource
    private AuthRealNameManager authRealNameManager;
    @Resource
    private WitnessManager witnessManager;
    @Resource
    private ONSProducer idCardBillProducer;

    /**
     * 查询绑卡信息
     *
     * @param memberSimpleDTO 维金id/会员id/钱包id
     */
    @Override
    public Result<BankCardInfoBO> queryBankCardInfo(MemberSimpleDTO memberSimpleDTO) {
        log.info("<查询绑卡信息>请求参数->{}", memberSimpleDTO);
        BankAccountRequest bankAcctReq = new BankAccountRequest();
        bankAcctReq.setMemberId(memberSimpleDTO.getMemberId());
        // 对公
        bankAcctReq.setCardAttribute(0);
        // 有效
        bankAcctReq.setStatus(1);
        // 借记卡
        bankAcctReq.setCardType(1);
        // 请求接口
        BankAccountResponse bankAcctResp;
        try {
            bankAcctResp = memberManager.queryBankAccount(bankAcctReq);

        } catch (ManagerException e) {
            return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), e.getMessage());
        }
        // 请求成功校验
        if (bankAcctResp == null || !Constants.ZERO.equals(bankAcctResp.getResponseCode())) {
            return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>无查询结果");
        }
        BankAccountInfo bankAccountInfo = bankAcctResp.getBankAccountInfos().get(0);
        // 响应字段非空校验
        if (StringUtil.isBlank(bankAccountInfo.getRealName())) {
            return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>银行账户户名为空");
        }
        if (StringUtil.isBlank(bankAccountInfo.getBranchNo())) {
            return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>联行号为空");
        }
        // 银行卡号校验
        if (StringUtil.isEmpty(bankAccountInfo.getExtention())
                || JSONObject.parseObject(bankAccountInfo.getExtention()).isEmpty()
                || !JSONObject.parseObject(bankAccountInfo.getExtention()).containsKey(Constants.BANK_ACCOUNT_NO)
                || StringUtil.isBlank(JSONObject.parseObject(bankAccountInfo.getExtention()).getString(Constants.BANK_ACCOUNT_NO))) {
            return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>返回银行卡号为空");
        }
        // 银行卡号去mask
        JSONObject jsonAcctNo = JSONObject.parseObject(bankAccountInfo.getExtention());
        String dataByTicket;
        try {
            dataByTicket = uesService.getDataByTicket(jsonAcctNo.getString(Constants.BANK_ACCOUNT_NO));
            if (StringUtil.isBlank(dataByTicket)) {
                return Result.fail(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>解密银行卡号为空");
            }

        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode(), "<查询绑卡信息>获取银行卡号后调用ues解密失败");
        }
        // 构建返回参数
        BankCardInfoBO bankCardInfo = WalletMemberBuilder.buildBankCardInfo(bankAccountInfo, dataByTicket);
        log.info("<查询绑卡信息>响应->{}", bankCardInfo.toString());
        return Result.success(bankCardInfo);
    }

    @Override
    @TradeWitness
    public WalletMemberResponse createAndActivateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest) {
        log.info("创建并激活企业会员，请求参数=>{}", createEnterpriseMemberRequest);
        String memberId;
        try {
            //1、创建企业会员
            CreateMemberInfoResponse createMemberInfoResponse = memberManager.createEnterpriseMember(createEnterpriseMemberRequest);

            //2、修改企业信息
            memberManager.updateEnterpriseInfo(convertCompanyInfo(createEnterpriseMemberRequest, createMemberInfoResponse.getMemberId()));

            //3、添加手机号实名信息，联系电话优先级大于法人手机号码
            if (StringUtil.isNotBlank(createEnterpriseMemberRequest.getTelephone()) ||
                    StringUtil.isNotBlank(createEnterpriseMemberRequest.getLegalPersonPhone())) {
                String verifyEntity = StringUtil.isNotBlank(createEnterpriseMemberRequest.getTelephone()) ?
                        createEnterpriseMemberRequest.getTelephone() : createEnterpriseMemberRequest.getLegalPersonPhone();
                updateVerifyInfo(createMemberInfoResponse.getMemberId(), VerifyTypeEnums.CELL_PHONE.getCode(), verifyEntity);
            }

            //4、激活企业会员
            memberManager.activeMemberAccount(createMemberInfoResponse.getMemberId(), MemberTypeEnums.ENTERPRISE);
            memberId = createMemberInfoResponse.getMemberId();
        } catch (ManagerException e) {
            log.error("创建并激活企业会员失败, 请求参数=>{}, 异常：", createEnterpriseMemberRequest, e);

            if (!ErrorCodeEnums.REPEAT_REQUEST_ERROR.getCode().equals(e.getCode())) {
                return WalletMemberResponse.createFailResp(new WalletMemberResponse(),
                        ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), "创建并激活企业会员失败" + e.getMessage());
            }
            try {
                MemberIntegratedRequest memberIntegratedRequest = new MemberIntegratedRequest();
                memberIntegratedRequest.setPlatformType(createEnterpriseMemberRequest.getPlatformType());
                memberIntegratedRequest.setMemberIdentity(createEnterpriseMemberRequest.getLoginName());
                MemberIntegratedResponse memberIntegratedResponse = memberManager.queryMemberInfoByIdentity(memberIntegratedRequest);
                memberId = memberIntegratedResponse.getBaseMemberInfo().getMemberId();
            } catch (ManagerException e1) {
                log.error("创建并激活企业会员-重复创建查询企业信息失败" + e1.getMessage());
                return WalletMemberResponse.createFailResp(new WalletMemberResponse(),
                        ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), "创建并激活企业会员-重复创建查询企业信息失败" + e1.getMessage());
            }
        }
        WalletMemberResponse successResp = WalletMemberResponse.createSuccessResp(new WalletMemberResponse());
        successResp.setMemberId(memberId);
        return successResp;
    }

    /**
     * 转换企业信息
     *
     * @param enterpriseMemberRequest
     * @param memberId
     * @return
     */
    private CompanyInfo convertCompanyInfo(CreateEnterpriseMemberRequest enterpriseMemberRequest, String memberId) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setAddress(enterpriseMemberRequest.getAddress());
        companyInfo.setBusinessScope(enterpriseMemberRequest.getBusinessScope());
        companyInfo.setCompanyName(enterpriseMemberRequest.getEnterpriseName());
        companyInfo.setLegalPerson(enterpriseMemberRequest.getLegalPerson());
        companyInfo.setLegalPersonPhone(enterpriseMemberRequest.getLegalPersonPhone());
        companyInfo.setLicenseAddress(enterpriseMemberRequest.getLicenseAddress());
        companyInfo.setLicenseExpireDate(enterpriseMemberRequest.getLicenseExpireDate());
        companyInfo.setLicenseNo(enterpriseMemberRequest.getLicenseNo());
        companyInfo.setMemberId(memberId);
        companyInfo.setMemberName(enterpriseMemberRequest.getMemberName());
        companyInfo.setOrganizationNo(enterpriseMemberRequest.getOrganizationNo());
        companyInfo.setSummary(enterpriseMemberRequest.getSummary());
        companyInfo.setTelephone(enterpriseMemberRequest.getTelephone());
        companyInfo.setWebsite(enterpriseMemberRequest.getWebsite());
        companyInfo.setExtention(enterpriseMemberRequest.getExtension());
        return companyInfo;
    }

    @Override
    public CommonResponse modifyEnterpriseInfo(ModifyEnterpriseInfoRequest modifyEnterpriseInfoRequest) {
        log.info("修改企业会员信息，请求参数=>{}", modifyEnterpriseInfoRequest);
        try {
            memberManager.updateEnterpriseInfo(convertCompanyInfo(modifyEnterpriseInfoRequest));
        } catch (ManagerException e) {
            log.error("修改企业会员信息失败", e);
            return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    "修改企业会员信息失败" + e.getMessage());
        }
        return CommonResponse.createSuccessResp();
    }

    /**
     * 转换企业信息
     *
     * @param modifyEnterpriseInfoRequest
     * @return
     */
    private CompanyInfo convertCompanyInfo(ModifyEnterpriseInfoRequest modifyEnterpriseInfoRequest) {
        CompanyInfo companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(modifyEnterpriseInfoRequest, companyInfo);
        return companyInfo;
    }

    @Override
    public WalletMemberInfoResponse queryWalletMemberInfo(QueryWalletMemberInfoRequest queryWalletMemberInfoRequest) {
        log.info("查询用户激活信息，请求参数=>{}", queryWalletMemberInfoRequest);
        MemberIntegratedResponse memberIntegratedResponse = null;
        try {
            MemberIntegratedIdRequest memberIntegratedIdRequest = new MemberIntegratedIdRequest();
            memberIntegratedIdRequest.setMemberId(queryWalletMemberInfoRequest.getMemberId());
            memberIntegratedResponse = memberManager.queryMemberInfoByMemberId(memberIntegratedIdRequest);
        } catch (ManagerException e) {
            log.error("查询用户激活信息失败", e);
            return WalletMemberInfoResponse.createFailResp(new WalletMemberInfoResponse(), ErrorCodeEnums
                    .MANAGER_SERVICE_ERROR.getCode(), "查询用户激活信息失败" + e.getMessage());
        }
        WalletMemberInfoResponse walletMemberInfoResponse = new WalletMemberInfoResponse();
        BeanUtils.copyProperties(memberIntegratedResponse.getBaseMemberInfo(), walletMemberInfoResponse);
        return walletMemberInfoResponse;
    }

    @Override
    public BalanceResponse queryAccountBalance(QueryBalanceRequest queryBalanceRequest) {
        log.info("查询账户余额，请求参数=>{}", queryBalanceRequest);
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setMemberId(queryBalanceRequest.getMemberId());
        accountRequest.setAccountType(Long.parseLong(queryBalanceRequest.getAccountType()));
        // TODO 账户标识查询
        AccountResponse accountResponse;
        try {
            accountResponse = memberManager.queryBalanceByMemberId(accountRequest);
        } catch (ManagerException e) {
            log.error("查询账户余额失败" + e.getMessage());
            return BalanceResponse.createFailResp(new BalanceResponse(), ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    "查询账户余额失败" + e.getMessage());
        }
        return convertBalanceResponse(accountResponse);
    }

    /**
     * 转换余额查询返回结果
     *
     * @param accountResponse
     * @return
     */
    private BalanceResponse convertBalanceResponse(AccountResponse accountResponse) {
        BalanceResponse balanceResponse = new BalanceResponse();
        List<AccountBalanceInfo> accountList = Lists.newArrayList();
        List<Account> accounts = accountResponse.getAccounts();
        for (Account account : accounts) {
            AccountBalanceInfo accountBalanceInfo = new AccountBalanceInfo();
            accountBalanceInfo.setAccountType(account.getAccountType().toString());
            if (StringUtils.isNotBlank(account.getExtention())) {
                accountBalanceInfo.setAccountIdentity(((JSONObject)JSONObject.parse(account.getExtention()))
                        .getString(Constants.ACCT_IDENTITY));
            }
            accountBalanceInfo.setAccountNo(account.getAccountId());
            accountBalanceInfo.setAvailableBalance(account.getAvailableBalance().getAmount().toString());
            accountBalanceInfo.setBalance(account.getBalance().getAmount().toString());
            accountBalanceInfo.setFrozenBalance(account.getFrozenBalance().getAmount().toString());
            accountList.add(accountBalanceInfo);
        }
        balanceResponse.setAccountList(accountList);
        return balanceResponse;
    }

    @Override
    public BankCardResponse createBankCard(CreateBankCardRequest createBankCardRequest) {
        log.info("创建银行卡，请求参数=>{}", createBankCardRequest);
        BankAccInfoResponse bankCard;
        try {
            BankAccInfoRequest bankAccInfoRequest = new BankAccInfoRequest();
            bankAccInfoRequest.setBankInfo(convertBankAcctInfo(createBankCardRequest));
            bankCard = memberManager.createBankCard(bankAccInfoRequest);
        } catch (ManagerException e) {
            log.error("创建银行卡失败", e);
            return BankCardResponse.createFailResp(new BankCardResponse(), ErrorCodeEnums.MANAGER_SERVICE_ERROR
                    .getCode(), "创建银行卡失败" + e.getMessage());
        }
        BankCardResponse bankCardResponse = new BankCardResponse();
        bankCardResponse.setBankcardId(bankCard.getBankcardId());
        return bankCardResponse;
    }

    @Override
    public CommonResponse removeBankCard(RemoveBankCardRequest removeBankCardRequest) {
        log.info("会员解绑银行卡，请求参数=>{}", removeBankCardRequest);
        try {
            BankAccRemoveRequest bankAccRemoveRequest = new BankAccRemoveRequest();
            bankAccRemoveRequest.setMemberId(removeBankCardRequest.getMemberId());
            bankAccRemoveRequest.setBankcardId(removeBankCardRequest.getBankcardId());
            return memberManager.removeBankCard(bankAccRemoveRequest);
        } catch (ManagerException e) {
            log.error("解绑银行卡失败", e);
            return CommonResponse.createFailResp(new CommonResponse(), ErrorCodeEnums.MANAGER_SERVICE_ERROR
                    .getCode(), "解绑银行卡失败" + e.getMessage());
        }
    }

    /**
     * 转换创建绑卡信息
     *
     * @param createBankCardRequest
     * @return
     */
    private BankAcctInfo convertBankAcctInfo(CreateBankCardRequest createBankCardRequest) {
        BankAcctInfo bankAcctInfo = new BankAcctInfo();
        bankAcctInfo.setBankAccountNum(createBankCardRequest.getBankAccountNo());
        bankAcctInfo.setBranchNo(createBankCardRequest.getBankBranchNo());
        bankAcctInfo.setPayAttribute(PayAttrEnums.NORMAL.getCode());
        bankAcctInfo.setMemberId(createBankCardRequest.getMemberId());
        bankAcctInfo.setBankCode(createBankCardRequest.getBankCode());
        bankAcctInfo.setBankName(createBankCardRequest.getBankName());
        bankAcctInfo.setBankBranch(createBankCardRequest.getBankBranch());
        bankAcctInfo.setRealName(createBankCardRequest.getAccountName());
        bankAcctInfo.setProvince(createBankCardRequest.getProvince());
        bankAcctInfo.setCity(createBankCardRequest.getCity());
        bankAcctInfo.setCardType(CardTypeEnums.getByCode(createBankCardRequest.getCardType()).getInsCode());
        bankAcctInfo.setCardAttribute(CardAttrEnums.getByCode(createBankCardRequest.getCardAttribute()).getInsCode());
        Integer isVerified = StringUtils.isBlank(createBankCardRequest.getIsVerified()) ? 0 :
                Integer.parseInt(createBankCardRequest.getIsVerified());
        bankAcctInfo.setIsVerified(isVerified);
        bankAcctInfo.setStatus(1);
        bankAcctInfo.setExtention(createBankCardRequest.getExtension());
        if (StringUtils.isNotBlank(createBankCardRequest.getSignNum())) {
            bankAcctInfo.setSignNum(createBankCardRequest.getSignNum());
        }
        return bankAcctInfo;
    }

    @Override
    public BankAccountListResponse queryBankAccountList(QueryBankAccountRequest queryBankAccountRequest) {
        log.info("查询银行卡数据，请求参数=>{}", queryBankAccountRequest);
        BankAccountResponse bankAccountResponse;
        try {
            bankAccountResponse = memberManager.queryBankAccount(convertQueryBankRequest(queryBankAccountRequest));
        } catch (ManagerException e) {
            log.error("查询绑卡数据异常", e);
            return BankAccountListResponse.createFailResp(new BankAccountListResponse(),
                    ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), "查询绑卡数据异常" + e.getMessage());
        }
        return convertBankAccountListResponse(bankAccountResponse);
    }

    /**
     * 拼接查询参数
     *
     * @param queryBankAccountRequest
     * @return
     */
    private BankAccountRequest convertQueryBankRequest(QueryBankAccountRequest queryBankAccountRequest) {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setMemberId(queryBankAccountRequest.getMemberId());
        bankAccountRequest.setStatus(1);
        if (CardTypeEnums.getByCode(queryBankAccountRequest.getCardType()) != null) {
            bankAccountRequest.setCardType(CardTypeEnums.getByCode(queryBankAccountRequest.getCardType()).getInsCode());
        }
        if (CardAttrEnums.getByCode(queryBankAccountRequest.getCardAttribute()) != null) {
            bankAccountRequest.setCardAttribute(CardAttrEnums.getByCode(queryBankAccountRequest.getCardAttribute())
                    .getInsCode());
        }
        bankAccountRequest.setBankAccountNum(queryBankAccountRequest.getBankAccountNum());
        return bankAccountRequest;
    }

    /**
     * 返回结果转换
     *
     * @param bankAccountResponse
     * @return
     */
    private BankAccountListResponse convertBankAccountListResponse(BankAccountResponse bankAccountResponse) {
        List<BankAccountRecordInfo> bankAccountRecordInfoList = Lists.newArrayList();
        List<BankAccountInfo> bankAccountInfos = bankAccountResponse.getBankAccountInfos();
        if (bankAccountInfos != null && !bankAccountInfos.isEmpty()) {
            for (BankAccountInfo bankAccountInfo : bankAccountInfos) {
                BankAccountRecordInfo bankAccountRecordInfo = new BankAccountRecordInfo();
                BeanUtils.copyProperties(bankAccountInfo, bankAccountRecordInfo);
                bankAccountRecordInfoList.add(bankAccountRecordInfo);
            }
        }
        BankAccountListResponse bankAccountListResponse = new BankAccountListResponse();
        bankAccountListResponse.setBankAccountRecordInfoList(bankAccountRecordInfoList);
        return bankAccountListResponse;
    }

    @Override
    public CommonResponse syncIdCard(SyncIdCardDTO syncIdCardDTO) {
        log.info("同步身份证信息，syncIdCardDTO=>{}", syncIdCardDTO);
        try {
            updateVerifyInfo(syncIdCardDTO.getMemberId(), VerifyTypeEnums.ID_CARD.getCode(), syncIdCardDTO.getIdCard());
        } catch (ManagerException e) {
            log.error("同步身份证信息失败", e);
            return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    "同步身份证信息失败" + e.getMessage());
        }
        return CommonResponse.createSuccessResp();
    }

    /**
     * 更新实名信息
     * PS：如果不存在则创建，存在则更新
     * @param memberId
     * @param verifyType
     * @param verifyEntity
     * @throws ManagerException
     */
    private void updateVerifyInfo(String memberId, Integer verifyType, String verifyEntity) throws ManagerException {
        VerifyQueryRequest verifyQueryRequest = new VerifyQueryRequest();
        verifyQueryRequest.setMemberId(memberId);
        verifyQueryRequest.setVerifyType(verifyType);
        VerifyInfoResponse verifyInfoResponse = memberManager.queryVerify(verifyQueryRequest);
        if (verifyInfoResponse.getVerifyInfos() == null) {
            VerifyInputRequest createVerifyInputRequest = new VerifyInputRequest();
            VerifyInfo verifyInfo = new VerifyInfo();
            verifyInfo.setVerifyType(verifyType);
            verifyInfo.setVerifyEntity(verifyEntity);
            verifyInfo.setMemberId(memberId);
            verifyInfo.setStatus(VerifyStatusEnums.AUTHENTICATED.getCode());
            verifyInfo.setVerifiedTime(new Date());
            createVerifyInputRequest.setVerifyInfo(verifyInfo);
            memberManager.createVerifyInfo(createVerifyInputRequest);
        } else {
            VerifyInputRequest updateVerifyInputRequest = new VerifyInputRequest();
            VerifyInfo verifyInfo = verifyInfoResponse.getVerifyInfos().get(0);
            verifyInfo.setVerifyEntity(verifyEntity);
            updateVerifyInputRequest.setVerifyInfo(verifyInfo);
            memberManager.updateVerify(updateVerifyInputRequest);
        }
    }

    @Override
    public UserVerifyInfoResponse queryVerifyInfo(QueryVerifyInfoRequest queryVerifyInfoRequest) {
        log.info("查询用户解密实名信息，请求参数=>{}", queryVerifyInfoRequest);
        try {
            UserVerifyInfoResponse userVerifyInfoResponse = new UserVerifyInfoResponse();
            // 查询解密信息
            List<DecipherResult> decipherResults = queryMemberDecipherInfo(new MemberSimpleDTO(queryVerifyInfoRequest.getMemberId()));
            if (decipherResults == null) {
                return UserVerifyInfoResponse.createFailResp(new UserVerifyInfoResponse(),
                        ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(), "查询用户解密实名信息为空");
            }
            for (DecipherResult rs : decipherResults) {
                if (rs.getDecipheredType() == 6) {
                    userVerifyInfoResponse.setAccName(rs.getPrimitiveValue());
                }
                if (rs.getDecipheredType() == 1) {
                    userVerifyInfoResponse.setIdCard(rs.getPrimitiveValue());
                }
                if (rs.getDecipheredType() == 2) {
                    userVerifyInfoResponse.setPhone(rs.getPrimitiveValue());
                }
            }
            return userVerifyInfoResponse;
        } catch (ManagerException e) {
            log.error("查询用户解密实名信息失败", e);
            return UserVerifyInfoResponse.createFailResp(new UserVerifyInfoResponse(), ErrorCodeEnums
                    .MANAGER_SERVICE_ERROR.getCode(), "查询用户解密实名信息失败" + e.getMessage());
        }
    }

    /**
     * 查询解密信息
     *
     * @param memberSimpleDTO 会员ID/钱包ID/维金ID
     * @return List<DecipherResult>
     * @throws ManagerException 自定义Manager异常
     */
    @Override
    public List<DecipherResult> queryMemberDecipherInfo(MemberSimpleDTO memberSimpleDTO) throws ManagerException {
        DecipherInfoRequest request = new DecipherInfoRequest();
        request.setMemberId(memberSimpleDTO.getMemberId());
        List<DecipherItem> columnList = new ArrayList<>();
        // 真实姓名 queryFlag=6
        DecipherItem accNameItem = new DecipherItem();
        accNameItem.setDecipheredType(6);
        accNameItem.setQueryFlag(1);
        // 身份证 queryFlag=1
        DecipherItem identityItem = new DecipherItem();
        identityItem.setDecipheredType(1);
        identityItem.setQueryFlag(1);
        // 手机号 queryFlag=2
        DecipherItem phoneItem = new DecipherItem();
        phoneItem.setDecipheredType(2);
        phoneItem.setQueryFlag(1);
        columnList.add(accNameItem);
        columnList.add(identityItem);
        columnList.add(phoneItem);
        request.setColumnList(columnList);
        DecipherInfoResponse queryResult = memberManager.querytMemberDecipherInfo(request);
        List<DecipherResult> rs = queryResult.getDecipheredResult();
        if (rs != null && rs.size() > 0) {
            return rs;
        }
        return null;
    }

    @Override
    public CommonResponse umpayAuthRealName(AuthRealNameRequest authRealNameRequest) {
        AuthRealNameResponse authRealNameResponse = new AuthRealNameResponse();
        try {
            authRealNameResponse = authRealNameManager.realNameAuth(authRealNameRequest.getRequestNo(),
                    authRealNameRequest.getRealName(), authRealNameRequest.getIdCard());
        } catch (ManagerException e) {
            log.error("联动实名认证失败" + e.getMessage());
            authRealNameResponse.setSuccess(false);
            authRealNameResponse.setMsg("联动实名认证失败" + e.getMessage());
            return CommonResponse.createFailResp(e.getCode(), "联动实名认证失败" + e.getMessage());
        } finally {
            sendBill(authRealNameRequest, authRealNameResponse);
        }
        return CommonResponse.createSuccessResp();
    }

    @Override
    public CreateAccountResponse openAccount(OpenAccountRequest openAccountRequest) {
        log.info("开账户，请求参数：{}", openAccountRequest);
        CreateAccountResponse response = new CreateAccountResponse();
        try {
            OpenAccountResponse result = memberManager.openAccount(openAccountRequest.getMemberId(),
                    Long.parseLong(openAccountRequest.getAccountType()));
            if (StringUtils.isBlank(result.getAccountId())) {
                return CreateAccountResponse.createFailResp(response, ErrorCodeEnums
                        .MANAGER_SERVICE_ERROR.getCode(), "开通账户失败，账户号返回为空");
            }
            response.setAccountId(result.getAccountId());
        } catch (ManagerException e) {
            log.error("查询用户解密实名信息失败", e);
            return CreateAccountResponse.createFailResp(response, ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    e.getMessage());
        }
        return response;
    }

    @Override
    public QueryAccountsResponse queryWalletAccountsInfo(QueryAccountsRequest queryAccountsRequest) {
        log.info("查询会员账户信息, 请求参数：{}", queryAccountsRequest);
        MemberIntegratedIdRequest memberIntegratedIdRequest = new MemberIntegratedIdRequest();
        memberIntegratedIdRequest.setMemberId(queryAccountsRequest.getMemberId());
        AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
        accountQueryRequest.setRequireAccountInfos(true);
        if (StringUtils.isNotBlank(queryAccountsRequest.getAccountType())) {
            accountQueryRequest.setAccountTypes(Lists.newArrayList(Long.parseLong(queryAccountsRequest.getAccountType())));
        }
        memberIntegratedIdRequest.setAccountRequest(accountQueryRequest);
        MemberIntegratedResponse memberIntegratedResponse;
        try {
            memberIntegratedResponse = memberManager.queryMemberInfoByMemberId(memberIntegratedIdRequest);
        } catch (ManagerException e) {
            log.error("查询会员账户信息失败", e);
            return QueryAccountsResponse.createFailResp(new QueryAccountsResponse(), ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    "查询会员账户信息异常" + e.getMessage());
        }
        QueryAccountsResponse queryAccountsResponse = QueryAccountsResponse.createSuccessResp(new QueryAccountsResponse());
        queryAccountsResponse.setMemberId(memberIntegratedResponse.getBaseMemberInfo().getMemberId());

        //账户列表不为空, 设置账号列表
        List<AccountInfo> accountInfoList = memberIntegratedResponse.getAccountInfos();
        if (accountInfoList != null && !accountInfoList.isEmpty()) {
            queryAccountsResponse.setWalletAccountInfoList(setWalletAccountInfos(queryAccountsRequest, accountInfoList));
        }
        return queryAccountsResponse;
    }

    /**
     * 设置账户列表，如果设置了账户标识，需要根据账户标识筛选
     *
     * @param queryAccountsRequest
     * @param accountInfoList
     * @return
     */
    private List<WalletAccountInfo> setWalletAccountInfos(QueryAccountsRequest queryAccountsRequest, List<AccountInfo>
            accountInfoList) {
        List<WalletAccountInfo> walletAccountInfoList = Lists.newArrayList();
        for (AccountInfo accountInfo : accountInfoList) {
            WalletAccountInfo walletAccountInfo = new WalletAccountInfo();
            if (StringUtils.isBlank(queryAccountsRequest.getMemberIdentity())) {
                BeanUtils.copyProperties(accountInfo, walletAccountInfo);
            } else {
                if (StringUtils.isNotBlank(accountInfo.getExtention()) && queryAccountsRequest.getMemberIdentity()
                        .equals(JSONObject.parseObject(accountInfo.getExtention()).getString(Constants.ACCT_IDENTITY))) {
                    BeanUtils.copyProperties(accountInfo, walletAccountInfo);
                }
            }
            walletAccountInfoList.add(walletAccountInfo);
        }
        return walletAccountInfoList;
    }

    @Override
    public Account queryAccountInfo(AccountSimpleDTO accountSimpleDTO) {
        Account account;
        try {
            account = memberManager.queryAccountById(accountSimpleDTO.getAccountNo());
        } catch (ManagerException e) {
            log.error("查询账户信息异常", e);
            return null;
        }
        return account;
    }

    @Override
    public QueryWitnessInfoResponse queryReceiveSubAccount(QueryWitnessInfoRequest queryWitnessInfoRequest) {
        log.info("查询用户收款子账户，请求参数：{}", queryWitnessInfoRequest);
        WitnessMemberInfoResponse witnessMemberInfoResponse;
        try {
            witnessMemberInfoResponse = witnessManager.queryWitnessMemberInfo(queryWitnessInfoRequest.getPartnerId(),
                    queryWitnessInfoRequest.getMemberId());
        } catch (ManagerException e) {
            log.error("查询用户收款子账户失败", e);
            return QueryWitnessInfoResponse.createFailResp(new QueryWitnessInfoResponse(),
                    ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), e.getMessage());
        }
        QueryWitnessInfoResponse queryWitnessInfoResponse =
                QueryWitnessInfoResponse.createSuccessResp(new QueryWitnessInfoResponse());
        queryWitnessInfoResponse.setMemberId(witnessMemberInfoResponse.getMemberId());
        queryWitnessInfoResponse.setReceiveSubAccount(Constants.RECEIVE_ACCOUNT_PREFIX + witnessMemberInfoResponse.getWitnessSubActNo());
        return queryWitnessInfoResponse;
    }

    /**
     * 发送账单记录MQ
     *
     * @param authRealNameRequest
     * @param authRealNameResponse
     */
    private void sendBill(AuthRealNameRequest authRealNameRequest, AuthRealNameResponse authRealNameResponse) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            Date date = new Date();
            map.put("clientId", "bmgateway");
            map.put("bizType", "B001");
            map.put("bizNo", authRealNameRequest.getRequestNo());
            map.put("submitTime", DateTimeUtil.parseDateTime(date, DateTimeUtil.PATTERN_DEFAULT));
            String identityCode = StringUtils.substring(authRealNameRequest.getIdCard(), 0,
                    authRealNameRequest.getIdCard().length() - 4) + "****";
            String accountName = StringUtils.substring(authRealNameRequest.getRealName(), 0, 1) + "*" +
                    StringUtils.substring(authRealNameRequest.getRealName(), 2, authRealNameRequest.getRealName().length());
            map.put("data", "{\"orderId\":\"" + authRealNameRequest.getRequestNo() + "\"," +
                    "\"name\":\"" + accountName + "\"," +
                    "\"idCard\":\"" + identityCode + "\"," +
                    "\"time\":\"" + DateTimeUtil.parseDateTime(date, DateTimeUtil.PATTERN_DEFAULT) + "\"," +
                    "\"result\":\"" + authRealNameResponse.isSuccess() +
                    "\"}");
            map.put("memo", "实名认证=>" + authRealNameResponse.getMsg());
            log.info("联动实名认证发送账单记录MQ" + map);
            idCardBillProducer.send(map, authRealNameRequest.getRequestNo());
        } catch (Exception e) {
            log.error("idCardBillProducer send mq error:", e);
        }
    }

    /**
     * 创建个人会员
     *
     * @param createPersonalMemberRequest
     * @return
     */
    @Override
    public WalletMemberResponse creatPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest) {
        log.info("创建个人会员，请求参数=>{}", createPersonalMemberRequest);
        String memberId = null;
        try {
            // 创建个人会员
            IntegratedPersonalResponse response = memberManager.createPersonalMember(createPersonalMemberRequest);
            memberId = response.getMemberId();
        } catch (ManagerException e) {
            log.error("创建个人会员失败", e);
            return WalletMemberResponse.createFailResp(new WalletMemberResponse(),
                    ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), "创建个人会员失败" + e.getMessage());
        }
        WalletMemberResponse successResp = WalletMemberResponse.createSuccessResp(new WalletMemberResponse());
        successResp.setMemberId(memberId);
        return successResp;
    }

    /**
     * 修改个人会员信息
     *
     * @param modifyPersonalInfoRequest
     * @return
     */
    @Override
    public CommonResponse modifyPersonalInfo(ModifyPersonalInfoRequest modifyPersonalInfoRequest) {

        log.info("修改个人会员信息，请求参数=>{}", modifyPersonalInfoRequest);
        try {
            memberManager.updatePersonalMember(convertPersonalInfo(modifyPersonalInfoRequest));
        } catch (ManagerException e) {
            log.error("修改个人会员信息失败", e);
            return CommonResponse.createFailResp(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(),
                    "修个人会员信息失败" + e.getMessage());
        }
        return CommonResponse.createSuccessResp();

    }

    /**
     * 构造个人会员请求参数
     *
     * @param modifyPersonalInfoRequest
     * @return
     */
    private PersonalMemberInfoRequest convertPersonalInfo(ModifyPersonalInfoRequest modifyPersonalInfoRequest) {
        PersonalMemberInfoRequest personalMemberInfoRequest = new PersonalMemberInfoRequest();
        personalMemberInfoRequest.setMemberId(modifyPersonalInfoRequest.getMemberId());
        personalMemberInfoRequest.setMemberName(modifyPersonalInfoRequest.getMemberName());
        return personalMemberInfoRequest;
    }

}
