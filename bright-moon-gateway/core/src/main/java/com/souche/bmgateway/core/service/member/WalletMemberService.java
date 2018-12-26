package com.souche.bmgateway.core.service.member;

import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.ma.service.base.model.DecipherResult;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.service.dto.AccountSimpleDTO;
import com.souche.bmgateway.core.service.dto.MemberSimpleDTO;
import com.souche.bmgateway.core.service.dto.SyncIdCardDTO;
import com.souche.bmgateway.core.service.member.builder.BankCardInfoBO;
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
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.BankAccountListResponse;
import com.souche.bmgateway.model.response.BankCardResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.bmgateway.model.response.UserVerifyInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.bmgateway.model.response.member.QueryAccountsResponse;
import com.souche.bmgateway.model.response.member.QueryWitnessInfoResponse;
import com.souche.optimus.core.web.Result;

import java.util.List;

/**
 * 会员服务 注：维金的请求参数只能有一个，并且包含partnerId
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface WalletMemberService {

    /**
     * 查询绑卡信息
     *
     * @param memberSimpleDTO 会员号
     * @return Result<BankCardInfoBO>
     */
    Result<BankCardInfoBO> queryBankCardInfo(MemberSimpleDTO memberSimpleDTO);

    /**
     * 创建会员
     *
     * @param createEnterpriseMemberRequest
     * @return
     */
    WalletMemberResponse createAndActivateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest);

    /**
     * 修改企业会员信息
     *
     * @param modifyEnterpriseInfoRequest
     * @return
     */
    CommonResponse modifyEnterpriseInfo(ModifyEnterpriseInfoRequest modifyEnterpriseInfoRequest);

    /**
     * 创建个人会员
     *
     * @param createPersonalMemberRequest
     * @return
     */
    WalletMemberResponse creatPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest);

    /**
     * 修改个人会员信息
     *
     * @param modifyPersonalInforequest
     * @return
     */
    CommonResponse modifyPersonalInfo(ModifyPersonalInfoRequest modifyPersonalInforequest);

    /**
     * 查询会员激活信息
     *
     * @param queryWalletMemberInfoRequest
     * @return
     */
    WalletMemberInfoResponse queryWalletMemberInfo(QueryWalletMemberInfoRequest queryWalletMemberInfoRequest);

    /**
     * 查询用户余额
     *
     * @param queryBalanceRequest
     * @return
     */
    BalanceResponse queryAccountBalance(QueryBalanceRequest queryBalanceRequest);

    /**
     * 创建银行卡
     *
     * @param createBankCardRequest
     * @return
     */
    BankCardResponse createBankCard(CreateBankCardRequest createBankCardRequest);

    /**
     * 会员解绑银行卡
     *
     * @param removeBankCardRequest
     * @return
     */
    CommonResponse removeBankCard(RemoveBankCardRequest removeBankCardRequest);

    /**
     * 查询有效的银行卡列表
     *
     * @param queryBankAccountRequest
     * @return
     */
    BankAccountListResponse queryBankAccountList(QueryBankAccountRequest queryBankAccountRequest);

    /**
     * 同步实名身份证信息
     *
     * @param syncIdCardDTO
     * @return
     */
    CommonResponse syncIdCard(SyncIdCardDTO syncIdCardDTO);

    /**
     * 查询企业实名信息
     *
     * @param queryVerifyInfoRequest
     * @return
     */
    UserVerifyInfoResponse queryVerifyInfo(QueryVerifyInfoRequest queryVerifyInfoRequest);

    /**
     * 查询解密信息
     *
     * @param memberSimpleDTO
     * @return List<DecipherResult>
     * @throws ManagerException 自定义Manager异常
     */
    List<DecipherResult> queryMemberDecipherInfo(MemberSimpleDTO memberSimpleDTO) throws ManagerException;

    /**
     * 联动实名认证
     *
     * @param authRealNameRequest
     * @return
     */
    CommonResponse umpayAuthRealName(AuthRealNameRequest authRealNameRequest);

    /**
     * 开通账户
     *
     * @param openAccountRequest
     * @return CreateAccountResponse
     */
    CreateAccountResponse openAccount(OpenAccountRequest openAccountRequest);

    /**
     * 查询钱包账户列表
     *
     * @param queryAccountsRequest
     * @return
     */
    QueryAccountsResponse queryWalletAccountsInfo(QueryAccountsRequest queryAccountsRequest);

    /**
     * 根据账号查询账户信息
     *
     * @param accountSimpleDTO
     * @return
     */
    Account queryAccountInfo(AccountSimpleDTO accountSimpleDTO);

    /**
     * 查询线下转账汇入 用户收款子账户
     *
     * @param queryWitnessInfoRequest
     * @return
     */
    QueryWitnessInfoResponse queryReceiveSubAccount(QueryWitnessInfoRequest queryWitnessInfoRequest);

}
