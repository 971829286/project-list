package com.souche.bmgateway.core.witness;

import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
import com.netfinworks.ma.service.request.BankAccRemoveRequest;
import com.netfinworks.ma.service.request.PersonalMemberInfoRequest;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;

/**
 * @author luobing. Created on 2018/11/26.
 */
public interface WitnessMemberDataDealService {
	/**
	 * 同步见证服务-开通账户
	 * 
	 * @param memberId
	 * @param accountTyp
	 */
	public void witnessAccountOpen(String memberId, long accountTyp);

	/**
	 * 同步见证服务-创建个人会员信息
	 * 
	 * @param createPersonalMemberRequest
	 * @param memberId
	 */
	public void witnessCreatePersonalMember(CreatePersonalMemberRequest createPersonalMemberRequest, String memberId);

	/**
	 * 同步见证服务-修改个人会员信息
	 * 
	 * @param personalMemberInfoRequest
	 */
	public void witnessModifyPersonalInfo(PersonalMemberInfoRequest personalMemberInfoRequest);

	/**
	 * 同步见证服务-创建企业会员信息
	 * 
	 * @param createEnterpriseMemberRequest
	 */
	public void witnessCreateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest);

	/**
	 * 同步见证服务-修改企业会员信息
	 * 
	 * @param companyInfo
	 */
	public void witnessModifyEnterpriseInfo(CompanyInfo companyInfo);

	/**
	 * 同步见证服务参数-绑定银行卡
	 * 
	 * @param bankAccInfoRequest
	 */
	public void witnessCreateBankCard(BankAccInfoRequest bankAccInfoRequest);

	/**
	 * 同步见证服务参数-解绑银行卡
	 * 
	 * @param bankAccRemoveRequest
	 */
	public void witnessUnbindBankCard(BankAccRemoveRequest bankAccRemoveRequest);
}
