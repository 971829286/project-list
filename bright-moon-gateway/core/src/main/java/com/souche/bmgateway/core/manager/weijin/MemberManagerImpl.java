package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.ma.service.base.model.Account;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.base.model.VerifyInfo;
import com.netfinworks.ma.service.base.response.Response;
import com.netfinworks.ma.service.facade.IAccountFacade;
import com.netfinworks.ma.service.facade.IBankAccountFacade;
import com.netfinworks.ma.service.facade.IMemberFacade;
import com.netfinworks.ma.service.facade.IVerifyFacade;
import com.netfinworks.ma.service.request.AccountRequest;
import com.netfinworks.ma.service.request.ActivateCompanyRequest;
import com.netfinworks.ma.service.request.ActivatePersonalRequest;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
import com.netfinworks.ma.service.request.BankAccRemoveRequest;
import com.netfinworks.ma.service.request.BankAccountRequest;
import com.netfinworks.ma.service.request.CreateMemberInfoRequest;
import com.netfinworks.ma.service.request.DecipherInfoRequest;
import com.netfinworks.ma.service.request.IntegratedPersonalRequest;
import com.netfinworks.ma.service.request.MemberIntegratedIdRequest;
import com.netfinworks.ma.service.request.MemberIntegratedRequest;
import com.netfinworks.ma.service.request.OpenAccountRequest;
import com.netfinworks.ma.service.request.PersonalMemberInfoRequest;
import com.netfinworks.ma.service.request.PersonalMemberRequest;
import com.netfinworks.ma.service.request.VerifyInputRequest;
import com.netfinworks.ma.service.request.VerifyQueryRequest;
import com.netfinworks.ma.service.response.AccountInfoResponse;
import com.netfinworks.ma.service.response.AccountResponse;
import com.netfinworks.ma.service.response.ActivateCompanyResponse;
import com.netfinworks.ma.service.response.ActivatePersonalResponse;
import com.netfinworks.ma.service.response.BankAccInfoResponse;
import com.netfinworks.ma.service.response.BankAccountInfoResponse;
import com.netfinworks.ma.service.response.BankAccountResponse;
import com.netfinworks.ma.service.response.BaseMemberInfo;
import com.netfinworks.ma.service.response.CreateMemberInfoResponse;
import com.netfinworks.ma.service.response.DecipherInfoResponse;
import com.netfinworks.ma.service.response.IntegratedPersonalResponse;
import com.netfinworks.ma.service.response.MemberIntegratedResponse;
import com.netfinworks.ma.service.response.OpenAccountResponse;
import com.netfinworks.ma.service.response.VerifyInfoResponse;
import com.netfinworks.ma.service.response.VerifyResponse;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.enums.AccountTypeEnums;
import com.souche.bmgateway.core.manager.enums.LoginNameTypeEnums;
import com.souche.bmgateway.core.manager.enums.MemberStatusEnums;
import com.souche.bmgateway.core.manager.enums.MemberTypeEnums;
import com.souche.bmgateway.core.manager.enums.PlatFormTypeEnums;
import com.souche.bmgateway.core.manager.enums.VerifyStatusEnums;
import com.souche.bmgateway.core.manager.enums.VerifyTypeEnums;
import com.souche.bmgateway.core.manager.model.MemberActiveResponse;
import com.souche.bmgateway.core.witness.aspect.MemberWitness;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.bmgateway.model.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zs
 */

@Service("memberManager")
@Slf4j(topic = "manager")
public class MemberManagerImpl extends CurrentOperationEnvironment implements MemberManager {

	@Resource
	private IMemberFacade memberFacade;

	@Resource
	private IAccountFacade accountFacade;

	@Resource
	private IVerifyFacade verifyFacade;

	@Resource
	private IBankAccountFacade bankAccountFacade;

	@Value("${accounts.personal}")
	private String personalAccountTypeDesc;

	@Value("${accounts.enterprise}")
	private String enterpriseAccountTypeDesc;

	@Override
	@MemberWitness
	public CreateMemberInfoResponse createEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest)
			throws ManagerException {
		try {
			log.info("创建企业会员，请求参数：{}", createEnterpriseMemberRequest);
			long beginTime = System.currentTimeMillis();
			CreateMemberInfoResponse createMemberInfoResponse = memberFacade.createMemberInfo(getOperationEnvironment(),
					convertEnterpriseMemberRequest(createEnterpriseMemberRequest));
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("创建企业会员， 耗时:{} (ms); 响应结果:{} ", consumeTime, createMemberInfoResponse);
			if (WeijinResponseEnums.MEMBER_IDENTITY_EXIST.getCode()
					.equals(createMemberInfoResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.REPEAT_REQUEST_ERROR,
						"创建企业会员失败" + createMemberInfoResponse.getResponseMessage());
			}
			if (!Constants.ZERO.equals(createMemberInfoResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"创建企业会员失败" + createMemberInfoResponse.getResponseMessage());
			}
			return createMemberInfoResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	@MemberWitness
	public Response updateEnterpriseInfo(CompanyInfo companyInfo) throws ManagerException {
		try {
			log.info("修改企业信息，请求参数：{}", companyInfo);
			long beginTime = System.currentTimeMillis();
			Response response = memberFacade.setCompanyMember(getOperationEnvironment(), companyInfo);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("修改企业信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, response);
			if (!Constants.ZERO.equals(response.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "修改企业会员失败" + response.getResponseMessage());
			}
			return response;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 转换创建会员请求参数
	 *
	 * @param memberRequest
	 * @return
	 */
	private CreateMemberInfoRequest convertEnterpriseMemberRequest(CreateEnterpriseMemberRequest memberRequest) {
		CreateMemberInfoRequest createMemberInfoRequest = new CreateMemberInfoRequest();
		createMemberInfoRequest.setLoginName(memberRequest.getLoginName());
		createMemberInfoRequest.setLoginNameType(LoginNameTypeEnums.COMMON_CHAR.getLoginNameType());
		createMemberInfoRequest.setLoginNamePlatformType(memberRequest.getPlatformType());

		if (StringUtils.isNotBlank(memberRequest.getUid())) {
			createMemberInfoRequest.setPlatformUserId(memberRequest.getUid());
			createMemberInfoRequest.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
		}
		createMemberInfoRequest.setMemberType(new Integer(MemberTypeEnums.ENTERPRISE.getCode()));
		createMemberInfoRequest.setMemberName(memberRequest.getMemberName());
		return createMemberInfoRequest;
	}

	@Override
	public MemberIntegratedResponse queryMemberInfoByIdentity(MemberIntegratedRequest memberIntegratedRequest)
			throws ManagerException {
		try {
			log.info("通过标识查询会员信息，请求参数：{}", memberIntegratedRequest);
			long beginTime = System.currentTimeMillis();
			MemberIntegratedResponse memberIntegratedResponse = memberFacade
					.queryMemberIntegratedInfo(getOperationEnvironment(), memberIntegratedRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("通过标识查询会员信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, memberIntegratedResponse);
			if (WeijinResponseEnums.MEMBER_NOT_EXIST.getCode().equals(memberIntegratedResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.MEMBER_NOT_EXIST,
						"通过标识查询会员信息失败" + memberIntegratedResponse.getResponseMessage());
			}
			if (!Constants.ZERO.equals(memberIntegratedResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"通过标识查询会员信息失败" + memberIntegratedResponse.getResponseMessage());
			}
			return memberIntegratedResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public MemberIntegratedResponse queryMemberInfoByMemberId(MemberIntegratedIdRequest memberIntegratedIdRequest)
			throws ManagerException {
		try {
			log.info("通过会员id查询会员信息，请求参数：{}", memberIntegratedIdRequest);
			long beginTime = System.currentTimeMillis();
			MemberIntegratedResponse memberIntegratedResponse = memberFacade
					.queryMemberIntegratedInfoById(getOperationEnvironment(), memberIntegratedIdRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("通过会员id查询会员信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, memberIntegratedResponse);
			if (WeijinResponseEnums.MEMBER_NOT_EXIST.getCode().equals(memberIntegratedResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.MEMBER_NOT_EXIST,
						"通过会员id查询会员信息失败" + memberIntegratedResponse.getResponseMessage());
			}
			if (!Constants.ZERO.equals(memberIntegratedResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"通过会员id查询会员信息失败" + memberIntegratedResponse.getResponseMessage());
			}
			return memberIntegratedResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public MemberActiveResponse activeMemberAccount(String memberId, MemberTypeEnums memberType)
			throws ManagerException {
		try {
			MemberActiveResponse memberActiveResponse = new MemberActiveResponse();
			memberActiveResponse.setMemberId(memberId);

			MemberIntegratedIdRequest memberIntegratedIdRequest = new MemberIntegratedIdRequest();
			memberIntegratedIdRequest.setMemberId(memberId);
			BaseMemberInfo baseMemberInfo = queryMemberInfoByMemberId(memberIntegratedIdRequest).getBaseMemberInfo();
			MemberStatusEnums memberStatus = MemberStatusEnums.getByCode(baseMemberInfo.getStatus().toString());
			// 未激活
			if (MemberStatusEnums.UNACTIVE.equals(memberStatus)) {
				if (MemberTypeEnums.PERSONAL.equals(memberType)) {
					ActivatePersonalRequest activatePersonalRequest = convertActivatePersonalRequest(memberId);
					log.info("激活个人会员，请求参数：{}", activatePersonalRequest);
					long beginTime = System.currentTimeMillis();
					ActivatePersonalResponse activatePersonalResponse = memberFacade
							.activatePersonalMemberInfo(getOperationEnvironment(), activatePersonalRequest);
					long consumeTime = System.currentTimeMillis() - beginTime;
					log.info("激活个人会员， 耗时:{} (ms); 响应结果:{} ", consumeTime, activatePersonalResponse);
					if (!Constants.ZERO.equals(activatePersonalResponse.getResponseCode())) {
						throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
								"激活个人会员失败" + activatePersonalResponse.getResponseMessage());
					}
					memberActiveResponse.setAccountId(activatePersonalResponse.getAccountId());
					// 如果是个人会员，需要同步开个人需要激活的账户
					List<String> accountTypeCodes = AccountTypeEnums.parseAccountTypes(personalAccountTypeDesc);
					if (null != accountTypeCodes && 0 < accountTypeCodes.size()) {
						for (String accountTypeCode : accountTypeCodes) {
							openAccount(memberId, Long.parseLong(accountTypeCode));
						}
					}
				} else {
					ActivateCompanyRequest activateCompanyRequest = new ActivateCompanyRequest();
					activateCompanyRequest.setMemberId(memberId);
					// 没有支付密码也激活
					activateCompanyRequest.setActivateAccount(true);
					log.info("激活企业会员，请求参数：{}", activateCompanyRequest);
					long beginTime1 = System.currentTimeMillis();
					ActivateCompanyResponse activateCompanyResponse = memberFacade
							.activateCompanyMember(getOperationEnvironment(), activateCompanyRequest);
					long consumeTime1 = System.currentTimeMillis() - beginTime1;
					log.info("激活企业会员， 耗时:{} (ms); 响应结果:{} ", consumeTime1, activateCompanyResponse);
					if (!Constants.ZERO.equals(activateCompanyResponse.getResponseCode())) {
						throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
								"激活企业会员失败" + activateCompanyResponse.getResponseMessage());
					}
					memberActiveResponse.setAccountId(activateCompanyResponse.getAccountId());
					// 如果是企业会员，需要同步开企业需要激活的账户
					List<String> accountTypeCodes = AccountTypeEnums.parseAccountTypes(enterpriseAccountTypeDesc);
					if (null != accountTypeCodes && 0 < accountTypeCodes.size()) {
						for (String accountTypeCode : accountTypeCodes) {
							openAccount(memberId, Long.parseLong(accountTypeCode));
						}
					}
				}
			}
			return memberActiveResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 转换激活个人会员请求
	 *
	 * @param memberId
	 * @return
	 */
	private ActivatePersonalRequest convertActivatePersonalRequest(String memberId) {
		ActivatePersonalRequest activatePersonalRequest = new ActivatePersonalRequest();
		PersonalMemberInfoRequest person = new PersonalMemberInfoRequest();
		person.setMemberId(memberId);
		activatePersonalRequest.setPersonalMemberInfo(person);
		// 没有支付密码也激活
		activatePersonalRequest.setActivateAccount(true);
		return activatePersonalRequest;
	}

	//TODO 支持开账户时传账户标识
	@Override
	@MemberWitness
	public OpenAccountResponse openAccount(String memberId, long accountType) throws ManagerException {
		try {
			log.info("开通账户，请求参数：{}", memberId + "|" + accountType);
			long beginTime = System.currentTimeMillis();
			OpenAccountRequest openAccountRequest = new OpenAccountRequest();
			openAccountRequest.setAccountType(accountType);
			openAccountRequest.setMemberId(memberId);
			openAccountRequest.setActivateStatus(1);
			OpenAccountResponse openAccountResponse = accountFacade.openAccount(getOperationEnvironment(),
					openAccountRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("开通账户， 耗时:{} (ms); 响应结果:{} ", consumeTime, openAccountResponse);
			if (!Constants.ZERO.equals(openAccountResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"开通账户失败" + openAccountResponse.getResponseMessage());
			}
			return openAccountResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public BankAccountResponse queryBankAccount(BankAccountRequest bankAccountRequest) throws ManagerException {
		try {
			log.info("查询绑卡列表，请求参数：{}", bankAccountRequest);
			long beginTime = System.currentTimeMillis();
			BankAccountResponse bankAccountResponse = bankAccountFacade.queryBankAccount(getOperationEnvironment(),
					bankAccountRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("查询绑卡列表， 耗时:{} (ms); 响应结果:{} ", consumeTime, bankAccountResponse);
			if (!WeijinResponseEnums.QUERY_NO_RESULT.getCode().equals(bankAccountResponse.getResponseCode())
					&& !Constants.ZERO.equals(bankAccountResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"银行卡查询失败" + bankAccountResponse.getResponseMessage());
			}
			return bankAccountResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	@MemberWitness
	public BankAccInfoResponse createBankCard(BankAccInfoRequest bankAccInfoRequest) throws ManagerException {
		try {
			log.info("创建银行卡，请求参数：{}", bankAccInfoRequest);
			long beginTime = System.currentTimeMillis();
			BankAccInfoResponse bankAccInfoResponse = bankAccountFacade.addBankAccount(getOperationEnvironment(),
					bankAccInfoRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("创建银行卡， 耗时:{} (ms); 响应结果:{} ", consumeTime, bankAccInfoResponse);
			if (!Constants.ZERO.equals(bankAccInfoResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"创建银行卡失败" + bankAccInfoResponse.getResponseMessage());
			}
			return bankAccInfoResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	@MemberWitness
	public CommonResponse removeBankCard(BankAccRemoveRequest bankAccRemoveRequest) throws ManagerException {
		try {
			log.info("解绑银行卡，请求参数：{}", bankAccRemoveRequest);
			long beginTime = System.currentTimeMillis();
			Response response = bankAccountFacade.removeBankAccount(getOperationEnvironment(), bankAccRemoveRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("解绑银行卡， 耗时:{} (ms); 响应结果:{} ", consumeTime, response);
			if (!Constants.ZERO.equals(response.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "解绑银行卡失败" + response.getResponseMessage());
			}
			return CommonResponse.createSuccessResp();
		} catch (ManagerException me) {
			throw me;
		} catch (Exception e) {
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public AccountResponse queryBalanceByMemberId(AccountRequest accountRequest) throws ManagerException {
		try {
			log.info("查询账户余额，请求参数：{}", accountRequest);
			long beginTime = System.currentTimeMillis();
			AccountResponse accountResponse = accountFacade.queryAccountByMemberId(getOperationEnvironment(),
					accountRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("查询账户余额， 耗时:{} (ms); 响应结果:{} ", consumeTime, accountResponse);
			if (!Constants.ZERO.equals(accountResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"查询账户余额失败" + accountResponse.getResponseMessage());
			}
			return accountResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public VerifyResponse createVerifyInfo(VerifyInputRequest verifyInputRequest) throws ManagerException {
		try {
			log.info("新增实名信息，请求参数：{}", verifyInputRequest);
			long beginTime = System.currentTimeMillis();
			VerifyResponse verifyResponse = verifyFacade.createVerify(getOperationEnvironment(), verifyInputRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("新增实名信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, verifyResponse);
			if (!Constants.ZERO.equals(verifyResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"新增实名信息失败" + verifyResponse.getResponseMessage());
			}
			return verifyResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public VerifyResponse updateVerify(VerifyInputRequest verifyInputRequest) throws ManagerException {
		try {
			log.info("更新实名信息，请求参数：{}", verifyInputRequest);
			long beginTime = System.currentTimeMillis();
			VerifyResponse verifyResponse = verifyFacade.updateVerify(getOperationEnvironment(), verifyInputRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("更新实名信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, verifyResponse);
			if (!Constants.ZERO.equals(verifyResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"更新实名信息失败" + verifyResponse.getResponseMessage());
			}
			return verifyResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public VerifyInfoResponse queryVerify(VerifyQueryRequest verifyQueryRequest) throws ManagerException {
		try {
			log.info("查询实名信息，请求参数：{}", verifyQueryRequest);
			long beginTime = System.currentTimeMillis();
			VerifyInfoResponse verifyInfoResponse = verifyFacade.queryVerify(getOperationEnvironment(),
					verifyQueryRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("查询实名信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, verifyInfoResponse);
			if (!WeijinResponseEnums.QUERY_NO_RESULT.getCode().equals(verifyInfoResponse.getResponseCode())
					&& !Constants.ZERO.equals(verifyInfoResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"查询实名信息失败" + verifyInfoResponse.getResponseMessage());
			}
			return verifyInfoResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public DecipherInfoResponse querytMemberDecipherInfo(DecipherInfoRequest decipherInfoRequest)
			throws ManagerException {
		try {
			log.info("查询用户解密信息，请求参数：{}", decipherInfoRequest);
			long beginTime = System.currentTimeMillis();
			DecipherInfoResponse decipherInfoResponse = memberFacade.querytMemberDecipherInfo(getOperationEnvironment(),
					decipherInfoRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("查询用户解密信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, decipherInfoResponse);
			if (!Constants.ZERO.equals(decipherInfoResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"查询用户解密信息失败" + decipherInfoResponse.getResponseMessage());
			}
			return decipherInfoResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public Account queryAccountById(String accountId) throws ManagerException {
		try {
			log.info("获取会员账户信息，请求参数：{}", accountId);
			long beginTime = System.currentTimeMillis();
			AccountInfoResponse response = this.accountFacade.queryAccountById(getOperationEnvironment(), accountId);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("获取会员账户信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, response);
			if ("999".equalsIgnoreCase(response.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "获取会员账户信息失败" + response.getResponseMessage());
			}
			return response.getAccount();
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public BankAccountInfoResponse queryBankAccountDetail(String bankId) throws ManagerException {
		try {
			log.info("获取绑卡详细信息，请求参数：{}", bankId);
			long beginTime = System.currentTimeMillis();
			BankAccountInfoResponse response = bankAccountFacade.queryBankAccountDetail(getOperationEnvironment(),
					bankId);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("获取绑卡详细信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, response);
			if (!"0".equals(response.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "获取绑卡详细信息失败" + response.getResponseMessage());
			}
			return response;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

	@Override
    @MemberWitness
	public IntegratedPersonalResponse createPersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest)
			throws ManagerException {
		try {
			log.info("创建个人会员，请求参数：{}", createPersonalMemberRequest);
			long beginTime = System.currentTimeMillis();
			IntegratedPersonalResponse integratedPersonalResponse = memberFacade.createIntegratedPersonalMember(
					getOperationEnvironment(), convertPersonalMemberRequest(createPersonalMemberRequest));
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("创建个人会员， 耗时:{} (ms); 响应结果:{} ", consumeTime, integratedPersonalResponse);
			if (WeijinResponseEnums.MEMBER_IDENTITY_EXIST.getCode()
					.equals(integratedPersonalResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.REPEAT_REQUEST_ERROR,
						"创建个人会员失败" + integratedPersonalResponse.getResponseMessage());
			}
			if (!Constants.ZERO.equals(integratedPersonalResponse.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR,
						"创建个人会员失败" + integratedPersonalResponse.getResponseMessage());
			}
			return integratedPersonalResponse;
		} catch (ManagerException e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}

	}

    /**
     * 构造创建个人会员请求参数
     *
     * @param createPersonalMemberRequest
     * @return
     */
	private IntegratedPersonalRequest convertPersonalMemberRequest(CreatePersonalMemberRequest createPersonalMemberRequest) {
		IntegratedPersonalRequest personalRequest = new IntegratedPersonalRequest();
		PersonalMemberRequest info = new PersonalMemberRequest();
        if (StringUtils.isNotBlank(createPersonalMemberRequest.getEmail())) {
			// EMAIL 不为空，则优先用EMAIL做identity
			info.setLoginName(createPersonalMemberRequest.getEmail());
			info.setLoginNameType(LoginNameTypeEnums.EMAIL.getLoginNameType());
		} else {
			info.setLoginName(createPersonalMemberRequest.getMobile());
			info.setLoginNameType(LoginNameTypeEnums.CELL_PHONE.getLoginNameType());
		}
        info.setLoginNamePlatformType(createPersonalMemberRequest.getPlatformType());
        info.setMemberName(createPersonalMemberRequest.getMemberName());
        info.setRealName(createPersonalMemberRequest.getRealName());
		personalRequest.setPersonalRequest(info);
		if (StringUtils.isNotBlank(createPersonalMemberRequest.getUid())) {
			personalRequest.setPlatformUserId(createPersonalMemberRequest.getUid());
            personalRequest.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
        }
		personalRequest.setExtention(createPersonalMemberRequest.getExtention());
		personalRequest.setMemberAccountFlag(createPersonalMemberRequest.getIsActive().getCode());

		List<VerifyInfo> verifyInfos = new ArrayList<>();
		// 认证手机号
		if (StringUtils.isNotBlank(createPersonalMemberRequest.getMobile())) {
			VerifyInfo mobile = new VerifyInfo();
			mobile.setVerifyEntity(createPersonalMemberRequest.getMobile());
			mobile.setVerifyType(VerifyTypeEnums.CELL_PHONE.getCode());
			mobile.setStatus(VerifyStatusEnums.AUTHENTICATED.getCode());
			verifyInfos.add(mobile);
		}
		// 绑定邮箱
		if (StringUtils.isNotBlank(createPersonalMemberRequest.getEmail())) {
			VerifyInfo email = new VerifyInfo();
			email.setVerifyEntity(createPersonalMemberRequest.getEmail());
			email.setVerifyType(VerifyTypeEnums.EMAIL.getCode());
			email.setStatus(VerifyStatusEnums.AUTHENTICATED.getCode());
			verifyInfos.add(email);
		}
		// 添加身份证未绑定
		if (StringUtils.isNotBlank(createPersonalMemberRequest.getIdCardNo())) {
			VerifyInfo idcard = new VerifyInfo();
			idcard.setVerifyEntity(createPersonalMemberRequest.getIdCardNo());
			idcard.setVerifyType(VerifyTypeEnums.ID_CARD.getCode());
			if ("T".equalsIgnoreCase(createPersonalMemberRequest.getIsVerify())) {
				idcard.setStatus(VerifyStatusEnums.AUTHENTICATED.getCode());
			} else {
				idcard.setStatus(VerifyStatusEnums.UNAUTHENTICATED.getCode());
			}
			verifyInfos.add(idcard);
		}
		personalRequest.setVerifys(verifyInfos);
		return personalRequest;
	}

	@Override
	@MemberWitness
	public Response updatePersonalMember(PersonalMemberInfoRequest personalMemberInfoRequest) throws ManagerException {
		try {
			log.info("修个人业信息，请求参数：{}", personalMemberInfoRequest);
			long beginTime = System.currentTimeMillis();
			Response response = memberFacade.setPersonalMemberInfo(getOperationEnvironment(),
					personalMemberInfoRequest);
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("修改个人信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, response);
			if (!Constants.ZERO.equals(response.getResponseCode())) {
				throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "修改个人会员失败" + response.getResponseMessage());
			}
			return response;
		} catch (ManagerException e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}
}
