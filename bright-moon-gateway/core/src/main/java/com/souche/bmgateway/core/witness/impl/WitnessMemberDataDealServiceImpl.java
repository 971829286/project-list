package com.souche.bmgateway.core.witness.impl;

import com.alibaba.fastjson.JSON;
import com.netfinworks.dts.service.facade.enums.BusinessType;
import com.netfinworks.dts.service.facade.request.member.BankCardBindRequest;
import com.netfinworks.dts.service.facade.request.member.BankCardUnBindRequest;
import com.netfinworks.dts.service.facade.request.member.EnterpriseMemberCreateRequest;
import com.netfinworks.dts.service.facade.request.member.EnterpriseMemberModifyRequest;
import com.netfinworks.dts.service.facade.request.member.OpenAccountRequest;
import com.netfinworks.dts.service.facade.request.member.PersonalMemberCreateRequest;
import com.netfinworks.dts.service.facade.request.member.PersonalMemberModifyRequest;
import com.netfinworks.ma.service.base.model.BankAcctInfo;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
import com.netfinworks.ma.service.request.BankAccRemoveRequest;
import com.netfinworks.ma.service.request.PersonalMemberInfoRequest;
import com.souche.bmgateway.core.witness.WitnessMemberDataDealService;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.witness.api.service.WitnessMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luobing Created on 18/11/26.
 */
@Service("witnessMemberDataDealService")
@Slf4j(topic = "witness")
public class WitnessMemberDataDealServiceImpl implements WitnessMemberDataDealService {

	@Resource
	private WitnessMqSendService witnessMqSendService;

	// 开通账户
	@Override
	public void witnessAccountOpen(String memberId, long accountTyp) {
		log.info("同步见证服务--开通账户 请求参数：memberId：{},accountType:{}", memberId, accountTyp);
		OpenAccountRequest openAccountRequest = new OpenAccountRequest();
		openAccountRequest.setUid(memberId);
		openAccountRequest.setAccount_type(accountTyp + "");
		openAccountRequest.setBusiness_type(BusinessType.OPENACCOUNT.getCode());
		witnessMqSendService.openAccountMQ(openAccountRequest);
	}

	// 同步见证服务-创建企业会员信息
	@Override
	public void witnessCreateEnterpriseMember(CreateEnterpriseMemberRequest createEnterpriseMemberRequest) {
		log.info("同步见证服务-创建企业会员信息请求参数：" + JSON.toJSONString(createEnterpriseMemberRequest));
		EnterpriseMemberCreateRequest enterpriseMemberCreateRequest = new EnterpriseMemberCreateRequest();
		enterpriseMemberCreateRequest.setUid(createEnterpriseMemberRequest.getUid());
		enterpriseMemberCreateRequest.setEnterprise_name(createEnterpriseMemberRequest.getEnterpriseName());
		enterpriseMemberCreateRequest.setMember_name(createEnterpriseMemberRequest.getMemberName());
		enterpriseMemberCreateRequest.setLegal_person(createEnterpriseMemberRequest.getLegalPerson());
		if (StringUtils.isNotBlank(createEnterpriseMemberRequest.getLegalPersonPhone())
				&& isPhoneFormatMatches(createEnterpriseMemberRequest.getLegalPersonPhone())) {
			enterpriseMemberCreateRequest.setLegal_person_phone(createEnterpriseMemberRequest.getLegalPersonPhone());
		}
		enterpriseMemberCreateRequest.setWebsite(createEnterpriseMemberRequest.getWebsite());
		enterpriseMemberCreateRequest.setAddress(createEnterpriseMemberRequest.getAddress());
		// 营业执照号必须为15位
		if (StringUtils.isNotBlank(createEnterpriseMemberRequest.getLicenseNo())
				&& createEnterpriseMemberRequest.getLicenseNo().length() == 15) {
			enterpriseMemberCreateRequest.setLicense_no(createEnterpriseMemberRequest.getLicenseNo());
		}
		enterpriseMemberCreateRequest.setLicense_address(createEnterpriseMemberRequest.getLicenseAddress());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		if (createEnterpriseMemberRequest.getLicenseExpireDate() != null) {
			enterpriseMemberCreateRequest
					.setLicense_expire_date(format.format(createEnterpriseMemberRequest.getLicenseExpireDate()));
		}
		enterpriseMemberCreateRequest.setBusiness_scope(createEnterpriseMemberRequest.getBusinessScope());
		if (StringUtils.isNotBlank(createEnterpriseMemberRequest.getTelephone())
				&& isPhoneFormatMatches(createEnterpriseMemberRequest.getTelephone())) {
			enterpriseMemberCreateRequest.setTelephone(createEnterpriseMemberRequest.getTelephone());
		}
		enterpriseMemberCreateRequest.setOrganization_no(createEnterpriseMemberRequest.getOrganizationNo());
		enterpriseMemberCreateRequest.setSummary(createEnterpriseMemberRequest.getSummary());
		enterpriseMemberCreateRequest.setIs_verify("N");
		enterpriseMemberCreateRequest.setLogin_name(createEnterpriseMemberRequest.getLoginName());
		enterpriseMemberCreateRequest.setIs_active("T");
		enterpriseMemberCreateRequest.setBusiness_type(BusinessType.ENTERPRISEMEMBERCREATE.getCode());
		witnessMqSendService.createEnterpriseMemberMQ(enterpriseMemberCreateRequest);
	}

	@Override
	public void witnessModifyEnterpriseInfo(CompanyInfo info) {
		log.info("同步见证服务-修改企业会员信息请求参数：" + JSON.toJSONString(info));

		EnterpriseMemberModifyRequest memberModifyRequest = new EnterpriseMemberModifyRequest();
		memberModifyRequest.setUid(info.getMemberId());
		if (StringUtils.isNotBlank(info.getCompanyName())) {
			memberModifyRequest.setEnterprise_name(info.getCompanyName());
		}
		if (StringUtils.isNotBlank(info.getMemberName())) {
			memberModifyRequest.setMember_name(info.getMemberName());
		}
		if (StringUtils.isNotBlank(info.getLegalPerson())) {
			memberModifyRequest.setLegal_person(info.getLegalPerson());
		}
		if (StringUtils.isNotBlank(info.getLegalPersonPhone()) && isPhoneFormatMatches(info.getLegalPersonPhone())) {
			memberModifyRequest.setLegal_person_phone(info.getLegalPersonPhone());
		}
		if (StringUtils.isNotBlank(info.getWebsite())) {
			memberModifyRequest.setWebsite(info.getWebsite());
		}
		if (StringUtils.isNotBlank(info.getAddress())) {
			memberModifyRequest.setAddress(info.getAddress());
		}
		if (StringUtils.isNotBlank(info.getLicenseNo()) && info.getLicenseNo().length() == 15) {
			memberModifyRequest.setLicense_no(info.getLicenseNo());
		}
		if (StringUtils.isNotBlank(info.getLicenseAddress())) {
			memberModifyRequest.setLicense_address(info.getLicenseAddress());
		}
		if (info.getLicenseExpireDate() != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			memberModifyRequest.setLicense_expire_date(format.format(info.getLicenseExpireDate()));
		}
		if (StringUtils.isNotBlank(info.getBusinessScope())) {
			memberModifyRequest.setBusiness_scope(info.getBusinessScope());
		}
		if (StringUtils.isNotBlank(info.getTelephone()) && isPhoneFormatMatches(info.getTelephone())) {
			memberModifyRequest.setTelephone(info.getTelephone());
		}
		if (StringUtils.isNotBlank(info.getOrganizationNo())) {
			memberModifyRequest.setOrganization_no(info.getOrganizationNo());
		}
		if (StringUtils.isNotBlank(info.getSummary())) {
			memberModifyRequest.setSummary(info.getSummary());
		}
		memberModifyRequest.setBusiness_type(BusinessType.ENTERPRISEMEMBERMODIFY.getCode());
		witnessMqSendService.modifyEnterpriseMemberMQ(memberModifyRequest);

	}

	@Override
	public void witnessCreateBankCard(BankAccInfoRequest bankAccInfoRequest) {
		log.info("同步见证服务-绑定银行卡：" + JSON.toJSONString(bankAccInfoRequest));

		BankCardBindRequest bankCardBindRequest = new BankCardBindRequest();
		BankAcctInfo bankInfo = bankAccInfoRequest.getBankInfo();
		if (null != bankInfo) {
			bankCardBindRequest.setUid(bankInfo.getMemberId());
			bankCardBindRequest.setCard_type(bankInfo.getCardType() + "");
			bankCardBindRequest.setAccount_name(bankInfo.getRealName());
			bankCardBindRequest.setBank_account_no(bankInfo.getBankAccountNum());
			bankCardBindRequest.setBank_name(bankInfo.getBankName());
			bankCardBindRequest.setCard_attribute(bankInfo.getCardAttribute() + "");
			bankCardBindRequest.setBank_branch(bankInfo.getBankBranch());
			bankCardBindRequest.setBranch_no(bankInfo.getBranchNo());
			bankCardBindRequest.setProvince(bankInfo.getProvince());
			bankCardBindRequest.setCity(bankInfo.getCity());
			bankCardBindRequest.setPay_attribute(bankInfo.getPayAttribute());
			bankCardBindRequest.setBusiness_type(BusinessType.BANKCARDBIND.getCode());
			witnessMqSendService.bindBankCardMQ(bankCardBindRequest);
		}

	}

	@Override
	public void witnessUnbindBankCard(BankAccRemoveRequest removeBankCardRequest) {
		BankCardUnBindRequest bankCardUnBindRequest = new BankCardUnBindRequest();
		bankCardUnBindRequest.setUid(removeBankCardRequest.getMemberId());
		
		bankCardUnBindRequest.setBank_id(removeBankCardRequest.getBankcardId());
		bankCardUnBindRequest.setBusiness_type(BusinessType.BANKCARDUNBIND.getCode());
		witnessMqSendService.unBindBankCardMQ(bankCardUnBindRequest);
	}

	@Override
	public void witnessCreatePersonalMember(CreatePersonalMemberRequest personal, String memberId) {
		log.info("同步见证服务-创建个人会员：" + JSON.toJSONString(personal));
		PersonalMemberCreateRequest personalMemberCreateRequest = new PersonalMemberCreateRequest();
		personalMemberCreateRequest.setUid(memberId);
		if (StringUtils.isNotBlank(personal.getMobile()) && isPhoneFormatMatches(personal.getMobile())) {
			personalMemberCreateRequest.setMobile(personal.getMobile());
		} else {
			personalMemberCreateRequest.setEmail(personal.getMobile() + "@souche1.com");
		}
		personalMemberCreateRequest.setReal_name(personal.getRealName());
		personalMemberCreateRequest.setMember_name(personal.getMemberName());
		if (StringUtils.isNotBlank(personal.getIdCardNo())) {
			personalMemberCreateRequest.setCertificate_type("ID_CARD");
			personalMemberCreateRequest.setCertificate_no(personal.getIdCardNo());
		}
		personalMemberCreateRequest.setIs_verify(personal.getIsVerify());
		personalMemberCreateRequest.setIs_active("T");
		personalMemberCreateRequest.setBusiness_type(BusinessType.PERSONALMEMBERCREATE.getCode());
		witnessMqSendService.createPersonalMemberMQ(personalMemberCreateRequest);

	}

	@Override
	public void witnessModifyPersonalInfo(PersonalMemberInfoRequest personalMemberInfoRequest) {
		log.info("同步见证服务-修改个人会员信息：" + JSON.toJSONString(personalMemberInfoRequest));
		PersonalMemberModifyRequest personalMemberModifyRequest = new PersonalMemberModifyRequest();
		personalMemberModifyRequest.setUid(personalMemberInfoRequest.getMemberId());
		if (StringUtils.isNotBlank(personalMemberInfoRequest.getMemberName())) {
			personalMemberModifyRequest.setMember_name(personalMemberInfoRequest.getMemberName());
		}
		personalMemberModifyRequest.setBusiness_type(BusinessType.PERSONALMEMBERMODIFY.getCode());
		witnessMqSendService.modifyPersonalMemberMQ(personalMemberModifyRequest);
	}

	private boolean isPhoneFormatMatches(String phone) {
		// 手机验证规则
		String regEx = "^1[3|4|5|7|8][0-9]\\d{8}$";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}
}
