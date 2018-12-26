package com.souche.bmgateway.core.dubbo.api.impl;

import com.souche.bmgateway.core.dubbo.api.aspect.DubboService;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.dubbo.api.WalletMemberFacade;
import com.souche.bmgateway.model.request.AuthRealNameRequest;
import com.souche.bmgateway.model.request.CreateBankCardRequest;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.QueryBalanceRequest;
import com.souche.bmgateway.model.request.QueryBankAccountRequest;
import com.souche.bmgateway.model.request.QueryVerifyInfoRequest;
import com.souche.bmgateway.model.request.QueryWalletMemberInfoRequest;
import com.souche.bmgateway.model.request.RemoveBankCardRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.bmgateway.model.request.member.OpenAccountRequest;
import com.souche.bmgateway.model.request.member.QueryWitnessInfoRequest;
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.BankAccountListResponse;
import com.souche.bmgateway.model.response.BankCardResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.bmgateway.model.response.UserVerifyInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.bmgateway.model.response.member.QueryWitnessInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/7/12.
 */
@Service("walletMemberFacade")
@Slf4j(topic = "dubbo.impl")
public class WalletMemberFacadeImpl implements WalletMemberFacade {

    @Resource
    private WalletMemberService walletMemberService;

    @Override
    @DubboService(desc = "创建并激活企业会员")
    public WalletMemberResponse createAndActivateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest) {
        return walletMemberService.createAndActivateEnterpriseMember(createEnterpriseMemberRequest);
    }

    @Override
    @DubboService(desc = "创建个人会员")
    public WalletMemberResponse createPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest) {
        return walletMemberService.creatPersonalMember(createPersonalMemberRequest);
    }

    @Override
    @DubboService(desc = "查询用户激活信息")
    public WalletMemberInfoResponse queryWalletMemberInfo(QueryWalletMemberInfoRequest queryWalletMemberInfoRequest) {
        return walletMemberService.queryWalletMemberInfo(queryWalletMemberInfoRequest);
    }

    @Override
    @DubboService(desc = "查询账户余额")
    public BalanceResponse queryBalance(QueryBalanceRequest queryBalanceRequest) {
        return walletMemberService.queryAccountBalance(queryBalanceRequest);
    }

    @Override
    @DubboService(desc = "开通账号")
    public CreateAccountResponse openAccount(OpenAccountRequest openAccountRequest) {
        return walletMemberService.openAccount(openAccountRequest);
    }

    @Override
    @DubboService(desc = "创建银行卡")
    public BankCardResponse createBankCard(CreateBankCardRequest createBankCardRequest) {
        return walletMemberService.createBankCard(createBankCardRequest);
    }

    @Override
    @DubboService(desc = "解绑银行卡")
    public CommonResponse removeBankAccount(RemoveBankCardRequest removeBankCardRequest) {
        return walletMemberService.removeBankCard(removeBankCardRequest);
    }

    @Override
    @DubboService(desc = "查询绑卡信息")
    public BankAccountListResponse queryBankAccountList(QueryBankAccountRequest queryBankAccountRequest) {
        return walletMemberService.queryBankAccountList(queryBankAccountRequest);
    }

    @Override
    @DubboService(desc = "查询企业实名信息")
    public UserVerifyInfoResponse queryVerifyInfo(QueryVerifyInfoRequest queryVerifyInfoRequest) {
        return walletMemberService.queryVerifyInfo(queryVerifyInfoRequest);
    }

    @Override
    @DubboService(desc = "联动实名认证")
    public CommonResponse authRealName(AuthRealNameRequest authRealNameRequest) {
        return walletMemberService.umpayAuthRealName(authRealNameRequest);
    }

    @Override
    @DubboService(desc = "查询会员见证信息")
    public QueryWitnessInfoResponse queryReceiveSubAccount(QueryWitnessInfoRequest queryWitnessInfoRequest) {
        return walletMemberService.queryReceiveSubAccount(queryWitnessInfoRequest);
    }
}
