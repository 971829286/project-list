package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.base.response.Response;
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
import com.netfinworks.ma.service.response.AccountResponse;
import com.netfinworks.ma.service.response.BankAccInfoResponse;
import com.netfinworks.ma.service.response.BankAccountInfoResponse;
import com.netfinworks.ma.service.response.BankAccountResponse;
import com.netfinworks.ma.service.response.CreateMemberInfoResponse;
import com.netfinworks.ma.service.response.DecipherInfoResponse;
import com.netfinworks.ma.service.response.IntegratedPersonalResponse;
import com.netfinworks.ma.service.response.MemberIntegratedResponse;
import com.netfinworks.ma.service.response.OpenAccountResponse;
import com.netfinworks.ma.service.response.VerifyInfoResponse;
import com.netfinworks.ma.service.response.VerifyResponse;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.core.manager.enums.MemberTypeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.MemberActiveResponse;

/**
 * @author zs
 */
public interface MemberManager {

	/**
	 * 创建企业会员 PS：如果会员未激活则幂等，如果会员已激活则不幂等
	 *
	 * @param createEnterpriseMemberRequest
	 * @return
	 * @throws ManagerException
	 */
	CreateMemberInfoResponse createEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest)
			throws ManagerException;

	/**
	 * 创建个人会员
	 * 
	 * @param createPersonalMemberRequest
	 * @return
	 * @throws ManagerException
	 */
	IntegratedPersonalResponse createPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest)
			throws ManagerException;

	/**
	 * 修改个人会员
	 * 
	 * @param personalMemberInfoRequest
	 * @return
	 * @throws ManagerException
	 */
	Response updatePersonalMember(PersonalMemberInfoRequest personalMemberInfoRequest) throws ManagerException;

	/**
	 * 修改企业信息
	 *
	 * @param companyInfo
	 * @return
	 * @throws ManagerException
	 */
	Response updateEnterpriseInfo(CompanyInfo companyInfo) throws ManagerException;

	/**
	 * 通过标识查询会员信息
	 *
	 * @param memberIntegratedRequest
	 * @return
	 * @throws ManagerException
	 */
	MemberIntegratedResponse queryMemberInfoByIdentity(MemberIntegratedRequest memberIntegratedRequest)
			throws ManagerException;

	/**
	 * 通过会员ID查询会员信息
	 * 
	 * @param memberIntegratedIdRequest
	 * @return
	 * @throws ManagerException
	 */
	MemberIntegratedResponse queryMemberInfoByMemberId(MemberIntegratedIdRequest memberIntegratedIdRequest)
			throws ManagerException;

	/**
	 * 激活用户会员
	 *
	 * @param memberId
	 * @param memberType
	 * @return
	 * @throws ManagerException
	 */
	MemberActiveResponse activeMemberAccount(String memberId, MemberTypeEnums memberType) throws ManagerException;

	/**
	 * 开账户
	 *
	 * @param memberId
	 * @param accountType
	 * @return
	 * @throws ManagerException
	 */
	OpenAccountResponse openAccount(String memberId, long accountType) throws ManagerException;

	/**
	 * 查询会员绑定的银行卡信息
	 *
	 * @param bankAccountRequest
	 * @return
	 * @throws ManagerException
	 */
	BankAccountResponse queryBankAccount(BankAccountRequest bankAccountRequest) throws ManagerException;

	/**
	 * 新增绑卡
	 *
	 * @param bankAccInfoRequest
	 * @return
	 * @throws ManagerException
	 */
	BankAccInfoResponse createBankCard(BankAccInfoRequest bankAccInfoRequest) throws ManagerException;

	/**
	 * 会员解绑银行卡
	 *
	 * @param bankAccRemoveRequest
	 * @return
	 * @throws ManagerException
	 */
	CommonResponse removeBankCard(BankAccRemoveRequest bankAccRemoveRequest) throws ManagerException;

	/**
	 * 查询账户余额
	 *
	 * @param accountRequest
	 * @return
	 * @throws ManagerException
	 */
	AccountResponse queryBalanceByMemberId(AccountRequest accountRequest) throws ManagerException;

	/**
	 * 新增实名信息
	 *
	 * @param verifyInputRequest
	 * @return
	 * @throws ManagerException
	 */
	VerifyResponse createVerifyInfo(VerifyInputRequest verifyInputRequest) throws ManagerException;

	/**
	 * 更新实名信息
	 *
	 * @param verifyInputRequest
	 * @return
	 * @throws ManagerException
	 */
	VerifyResponse updateVerify(VerifyInputRequest verifyInputRequest) throws ManagerException;

	/**
	 * 查询实名信息
	 *
	 * @param verifyQueryRequest
	 * @return
	 * @throws ManagerException
	 */
	VerifyInfoResponse queryVerify(VerifyQueryRequest verifyQueryRequest) throws ManagerException;

	/**
	 * 查询用户解密信息
	 *
	 * @param decipherInfoRequest
	 * @return
	 * @throws ManagerException
	 */
	DecipherInfoResponse querytMemberDecipherInfo(DecipherInfoRequest decipherInfoRequest) throws ManagerException;

	/**
	 * 查询账号信息
	 *
	 * @param drAccountNo
	 * @return
	 * @throws ManagerException
	 */
	Account queryAccountById(String drAccountNo) throws ManagerException;

	/**
	 * 通过bankid查询账户信息
	 * 
	 * @param bankId
	 * @return
	 * @throws ManagerException
	 */
	BankAccountInfoResponse queryBankAccountDetail(String bankId) throws ManagerException;

}
