package com.souche.bmgateway.core.dubbo.api.impl;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.manager.enums.PlatFormTypeEnums;
import com.souche.bmgateway.dubbo.api.WalletMemberFacade;
import com.souche.bmgateway.enums.ActivateStatus;
import com.souche.bmgateway.model.request.CreateBankCardRequest;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.QueryBalanceRequest;
import com.souche.bmgateway.model.request.QueryBankAccountRequest;
import com.souche.bmgateway.model.request.QueryVerifyInfoRequest;
import com.souche.bmgateway.model.request.QueryWalletMemberInfoRequest;
import com.souche.bmgateway.model.request.RemoveBankCardRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.BankCardResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.optimus.common.util.UUIDUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/7/23.
 */
public class WalletMemberFacadeTest extends BaseTest {
	@Autowired
	private WalletMemberFacade walletMemberFacade;

	// 创建个人会员
	@Test
	public void creatPersonal() {
		CreatePersonalMemberRequest createPersonalMemberRequest = new CreatePersonalMemberRequest();
		createPersonalMemberRequest.setEmail("1529192974@qq.com");
		createPersonalMemberRequest.setIdCardNo("532229929249393586");
		createPersonalMemberRequest.setIsActive(ActivateStatus.ACTIVATED_ALL);
		createPersonalMemberRequest.setMemberName("影响中国");
		createPersonalMemberRequest.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
		createPersonalMemberRequest.setIsVerify("T");
		createPersonalMemberRequest.setMobile("17321075170");
		createPersonalMemberRequest.setUid(UUIDUtil.getID());
		WalletMemberResponse createPersonalMember = walletMemberFacade
				.createPersonalMember(createPersonalMemberRequest);
		System.out.println(createPersonalMember);// memberId=100000201001,operatorId=7000000201001,accountId=200100200110000020100100001
	}

	// 创建企业会员并激活
	@Test
	public void createAndActivateEnterpriseMemberTest() {
		CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
		createEnterpriseMemberRequest.setAddress("北平");
		createEnterpriseMemberRequest.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
		createEnterpriseMemberRequest.setEnterpriseName("北平律师事务所");
		createEnterpriseMemberRequest.setLegalPerson("张北平");
		createEnterpriseMemberRequest.setLegalPersonPhone("18712777032");
		createEnterpriseMemberRequest.setLicenseAddress("北平市中心");
		createEnterpriseMemberRequest.setLicenseExpireDate(new Date(2022, 3, 5));
		createEnterpriseMemberRequest.setLicenseNo("123456789009876");
		createEnterpriseMemberRequest.setLoginName("luobing@pb.com");
		createEnterpriseMemberRequest.setMemberName("张北平小号");
		WalletMemberResponse createAndActivateEnterpriseMember = walletMemberFacade
				.createAndActivateEnterpriseMember(createEnterpriseMemberRequest);
		System.out.println(createAndActivateEnterpriseMember);
	}

	// 查询会员信息
	@Test
	public void queryWalletMemberInfoTest() {
		QueryWalletMemberInfoRequest queryWalletMemberInfoRequest = new QueryWalletMemberInfoRequest();
		queryWalletMemberInfoRequest.setMemberId("200000201002");
		walletMemberFacade.queryWalletMemberInfo(queryWalletMemberInfoRequest);
	}

	// 查询余额
	@Test
	public void queryBalanceTest() {
		QueryBalanceRequest queryBalanceRequest = new QueryBalanceRequest();
		queryBalanceRequest.setMemberId("200000201002");
		BalanceResponse queryBalance = walletMemberFacade.queryBalance(queryBalanceRequest);
		System.out.println(queryBalance);
	}

	@Test
	public void createBankCardTest() {
		CreateBankCardRequest createBankCardRequest = new CreateBankCardRequest();
		createBankCardRequest.setAccountName("lb");
		createBankCardRequest.setBankAccountNo("12322332");
		createBankCardRequest.setBankBranchNo("102100099996");
		createBankCardRequest.setBankCode("ICBC");
		createBankCardRequest.setBankName("中国工商银行");
		createBankCardRequest.setCardAttribute("C");
		createBankCardRequest.setCardType("DEBIT");
		createBankCardRequest.setCity("杭州市");
		createBankCardRequest.setMemberId("200000201002");
		createBankCardRequest.setProvince("浙江省");
		// createBankCardRequest.setSignNum(signNum);
		createBankCardRequest.setIsVerified("1");

		BankCardResponse createBankCard = walletMemberFacade.createBankCard(createBankCardRequest);
		System.out.println(createBankCard);
	}

	// 解除绑银行卡
	@Test
	public void removeBankAccountTest() {
		RemoveBankCardRequest removeBankCardRequest = new RemoveBankCardRequest();
		removeBankCardRequest.setBankcardId("48");
		removeBankCardRequest.setMemberId("200000201002");
		CommonResponse removeBankAccount = walletMemberFacade.removeBankAccount(removeBankCardRequest);
		System.out.println(removeBankAccount);
	}

	// 查询绑定的银行卡列表
	@Test
	public void queryBankAccountListTest() {
		QueryBankAccountRequest queryBankAccountRequest = new QueryBankAccountRequest();
		queryBankAccountRequest.setMemberId("200000201002");
		walletMemberFacade.queryBankAccountList(queryBankAccountRequest);
	}

	@Test
	public void queryVerifyInfoTest() {
		QueryVerifyInfoRequest queryVerifyInfoRequest = new QueryVerifyInfoRequest();
		queryVerifyInfoRequest.setMemberId("200000201002");
		walletMemberFacade.queryVerifyInfo(queryVerifyInfoRequest);
	}

}
