package com.souche.bmgateway.dubbo.api;

import com.alibaba.dubbo.doc.annotation.InterfaceDesc;
import com.alibaba.dubbo.doc.annotation.MethodDesc;
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

/**
 * 钱包会员服务，开通维金会员等
 *
 * @author zs.
 *         Created on 18/7/12.
 */
@InterfaceDesc(value="钱包会员服务，开通维金会员等", url="http://null///git.souche-inc.com/spay/bright-moon-gateway/tree/master/api/src/main/java/com/souche/bmgateway/dubbo/api/WalletMemberFacade.java")
public interface WalletMemberFacade {

    /**
     * 开通企业钱包并且自动激活钱包
     *
     * @param createEnterpriseMemberRequest
     * @return
     */
    @MethodDesc("开通企业钱包并且自动激活钱包")
    WalletMemberResponse createAndActivateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest);

    /**
     * 开通个人会员
     * @param createPersonalMemberRequest
     * @return
     */
    @MethodDesc("开通个人会员")
    WalletMemberResponse createPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest);

    /**
     * 查询用户激活信息
     *
     * @param queryWalletMemberInfoRequest
     * @return
     */
    @MethodDesc("查询用户激活信息")
    WalletMemberInfoResponse queryWalletMemberInfo(QueryWalletMemberInfoRequest queryWalletMemberInfoRequest);

    /**
     * 查询余额接口
     *
     * @param queryBalanceRequest
     * @return
     */
    @MethodDesc("查询余额接口")
    BalanceResponse queryBalance(QueryBalanceRequest queryBalanceRequest);

    /**
     * 开通账号
     *
     * @param openAccountRequest
     * @return
     */
    @MethodDesc("开通账号")
    CreateAccountResponse openAccount(OpenAccountRequest openAccountRequest);

    /**
     * 创建银行卡
     *
     * @param createBankCardRequest
     * @return
     */
    @MethodDesc("创建银行卡")
    BankCardResponse createBankCard(CreateBankCardRequest createBankCardRequest);

    /**
     * 会员解绑银行卡
     *
     * @param removeBankCardRequest
     * @return
     */
    @MethodDesc("会员解绑银行卡")
    CommonResponse removeBankAccount(RemoveBankCardRequest removeBankCardRequest);

    /**
     * 查询绑定的银行卡列表
     *
     * @param queryBankAccountRequest
     * @return
     */
    @MethodDesc("查询绑定的银行卡列表")
    BankAccountListResponse queryBankAccountList(QueryBankAccountRequest queryBankAccountRequest);

    /**
     * 查询用户实名信息
     * @param queryVerifyInfoRequest
     * @return
     */
    @MethodDesc("查询用户实名信息")
    UserVerifyInfoResponse queryVerifyInfo(QueryVerifyInfoRequest queryVerifyInfoRequest);

    /**
     * 联动实名认证
     * @param authRealNameRequest
     * @return
     */
    @MethodDesc("联动实名认证")
    CommonResponse authRealName(AuthRealNameRequest authRealNameRequest);

    /**
     * 查询线下转账汇入 用户收款子账户
     * @param queryWitnessInfoRequest
     * @return
     */
    @MethodDesc("查询线下转账汇入 用户收款子账户")
    QueryWitnessInfoResponse queryReceiveSubAccount(QueryWitnessInfoRequest queryWitnessInfoRequest);

}
