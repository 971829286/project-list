package com.souche.bmgateway.core.manager;

import com.netfinworks.ma.service.base.model.BankAcctInfo;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.base.model.DecipherItem;
import com.netfinworks.ma.service.base.model.VerifyInfo;
import com.netfinworks.ma.service.base.response.Response;
import com.netfinworks.ma.service.request.AccountRequest;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
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
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.CardAttrEnums;
import com.souche.bmgateway.core.enums.CardTypeEnums;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.PayAttrEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.enums.MemberTypeEnums;
import com.souche.bmgateway.core.manager.enums.PlatFormTypeEnums;
import com.souche.bmgateway.core.manager.model.AuthRealNameResponse;
import com.souche.bmgateway.core.manager.model.MemberActiveResponse;
import com.souche.bmgateway.core.manager.umpay.AuthRealNameManager;
import com.souche.bmgateway.core.manager.weijin.MemberManager;
import com.souche.bmgateway.enums.ActivateStatus;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import com.souche.optimus.common.util.UUIDUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zs.
 *         Created on 18/7/18.
 */
public class MemberManagerTest extends BaseTest {

    @Resource
    private MemberManager memberManager;

    @Resource
    private AuthRealNameManager authRealNameManager;

    @Test
	public void CreatePersonalMemeberTest() throws ManagerException {
		CreatePersonalMemberRequest req = new CreatePersonalMemberRequest(); // memberId=100000161021,operatorId=7000000161020,accountId=200100200110000016102100001
		 req.setEmail("1529199924@qq.com");
		 req.setIdCardNo("532126299404222319");
		req.setIsActive(ActivateStatus.ACTIVATED_ALL);
		// req.setIs_verify("T");
		req.setMemberName("测试杨");
		req.setMobile("18712700732");
		req.setUid("dfrjehguhf33Ïwbeg");
		req.setRealName("lb1");
		req.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
		IntegratedPersonalResponse createPersonalMember = memberManager.createPersonalMember(req);
		System.out.println(createPersonalMember);
	}

	@Test
	public void updatePersonalMemberTest() throws ManagerException {
		PersonalMemberInfoRequest req = new PersonalMemberInfoRequest();
		req.setMemberId("100000161021");
		req.setMemberName("ydcyc");
		Response updatePersonalMember = memberManager.updatePersonalMember(req);
		System.out.println(updatePersonalMember);
	}
	
    @Test
    public void testCreateEnterpriseMember() throws ManagerException {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
        //uid可空
//        createEnterpriseMemberRequest.setUid(UUIDUtil.getID());
        createEnterpriseMemberRequest.setLoginName(UUIDUtil.getID());
        createEnterpriseMemberRequest.setTelephone("13777807633");
        CreateMemberInfoResponse enterpriseMember = memberManager.createEnterpriseMember(createEnterpriseMemberRequest);
        Assert.assertEquals("0", enterpriseMember.getResponseCode());
        Assert.assertNotNull(enterpriseMember.getMemberId());
    }

    @Test
    public void testCreateEnterpriseMemberRepeatUnActive() throws ManagerException {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
        createEnterpriseMemberRequest.setLoginName("郑氏集团3");
        createEnterpriseMemberRequest.setTelephone("13777807616");
        CreateMemberInfoResponse enterpriseMember = memberManager.createEnterpriseMember(createEnterpriseMemberRequest);
        Assert.assertEquals("0", enterpriseMember.getResponseCode());
        Assert.assertEquals("200000021039", enterpriseMember.getMemberId());
    }

    @Test
    public void testCreateEnterpriseMemberRepeatActive() {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
        createEnterpriseMemberRequest.setLoginName("郑氏集团");
        createEnterpriseMemberRequest.setTelephone("13777807616");
        try {
            memberManager.createEnterpriseMember(createEnterpriseMemberRequest);
            Assert.fail("系统异常，没有抛出指定异常");
        } catch (ManagerException e) {
            Assert.assertEquals(ErrorCodeEnums.REPEAT_REQUEST_ERROR.getCode(), e.getCode());
            Assert.assertEquals("创建企业会员失败会员标识已存在[会员标识已存在:郑氏集团,平台类型:3]", e.getMessage());
        }
    }

    @Test
    public void testUpdateEnterpriseInfo() throws ManagerException {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setMemberId("200000000012");
        companyInfo.setCompanyName("郑氏集团");
        companyInfo.setLegalPerson("郑森");
        companyInfo.setLegalPersonPhone("13777807615");
        Response response = memberManager.updateEnterpriseInfo(companyInfo);
        Assert.assertEquals("0", response.getResponseCode());
    }

    @Test
    public void testQueryMemberInfo() throws ManagerException {
        MemberIntegratedIdRequest memberIntegratedIdRequest = new MemberIntegratedIdRequest();
        memberIntegratedIdRequest.setMemberId("200000000012");
        MemberIntegratedResponse memberIntegratedResponse = memberManager.queryMemberInfoByMemberId(memberIntegratedIdRequest);
        Assert.assertEquals("0", memberIntegratedResponse.getResponseCode());
        Assert.assertEquals("200000000012", memberIntegratedResponse.getBaseMemberInfo().getMemberId());
        Assert.assertEquals(Optional.of(1l).get(), memberIntegratedResponse.getBaseMemberInfo().getStatus());
    }

    @Test
    public void testQueryMemberInfoByIdentity() throws ManagerException {
        MemberIntegratedRequest memberIntegratedRequest = new MemberIntegratedRequest();
        memberIntegratedRequest.setPlatformType(PlatFormTypeEnums.LOGIN_NAME.getLoginNamePlatformType());
        memberIntegratedRequest.setMemberIdentity("郑氏集团");
        MemberIntegratedResponse memberIntegratedResponse = memberManager.queryMemberInfoByIdentity
                (memberIntegratedRequest);
        Assert.assertEquals("0", memberIntegratedResponse.getResponseCode());
        Assert.assertEquals("200000000012", memberIntegratedResponse.getBaseMemberInfo().getMemberId());
        Assert.assertEquals(Optional.of(1l).get(), memberIntegratedResponse.getBaseMemberInfo().getStatus());
    }

    @Test
    public void testQueryMemberInfoByIdentityNotExist() {
        MemberIntegratedRequest memberIntegratedRequest = new MemberIntegratedRequest();
        memberIntegratedRequest.setPlatformType(PlatFormTypeEnums.LOGIN_NAME.getLoginNamePlatformType());
        memberIntegratedRequest.setMemberIdentity("fdsffsd");
        try {
            memberManager.queryMemberInfoByIdentity(memberIntegratedRequest);
            Assert.fail("外部调用异常，没有抛出指定异常");
        } catch (ManagerException e) {
            Assert.assertEquals(ErrorCodeEnums.MEMBER_NOT_EXIST.getCode(), e.getCode());
            Assert.assertEquals("通过标识查询会员信息失败会员不存在", e.getMessage());
        }
    }

    @Test
    public void testActiveMemberAccount() throws ManagerException {
        MemberActiveResponse memberActiveResponse = memberManager.activeMemberAccount("200000021049", MemberTypeEnums
                .ENTERPRISE);
        Assert.assertEquals("200000021049", memberActiveResponse.getMemberId());
    }

    @Test
    public void testOpenAccount() throws ManagerException {
        OpenAccountResponse openAccountResponse = memberManager.openAccount("200000000056", 204l);
        Assert.assertEquals("0", openAccountResponse.getResponseCode());
        Assert.assertNotNull(openAccountResponse.getAccountId());
    }

    @Test
    public void testQueryBankAccount() throws ManagerException {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setMemberId("200000000012");
        bankAccountRequest.setStatus(1);
        BankAccountResponse bankAccountResponse = memberManager.queryBankAccount(bankAccountRequest);
        Assert.assertEquals("0", bankAccountResponse.getResponseCode());
        Assert.assertNotNull(bankAccountResponse.getBankAccountInfos().get(0).getBankcardId());
    }

    @Test
    public void testCreateBankCard() throws ManagerException {
        BankAccInfoRequest bankAccInfoRequest = new BankAccInfoRequest();
        BankAcctInfo bankAcctInfo = new BankAcctInfo();
        bankAcctInfo.setBranchNo("102100099996");
        bankAcctInfo.setPayAttribute(PayAttrEnums.NORMAL.getCode());
        bankAcctInfo.setMemberId("200000000012");
        bankAcctInfo.setBankCode("ICBC");
        bankAcctInfo.setBankName("中国工商银行");
        bankAcctInfo.setBankBranch("中国工商银行高沙支行");
        bankAcctInfo.setRealName("郑森");
        bankAcctInfo.setProvince("浙江省");
        bankAcctInfo.setCity("杭州市");
        bankAcctInfo.setCardType(CardTypeEnums.DEBIT_CARD.getInsCode());
        bankAcctInfo.setCardAttribute(CardAttrEnums.PERSONAL.getInsCode());
        bankAcctInfo.setIsVerified(1);
        bankAcctInfo.setStatus(1);
        bankAcctInfo.setExtention("");
        bankAccInfoRequest.setBankInfo(bankAcctInfo);
        BankAccInfoResponse bankCard = memberManager.createBankCard(bankAccInfoRequest);
        Assert.assertEquals("0", bankCard.getResponseCode());
        Assert.assertNotNull(bankCard.getBankcardId());
    }

    @Test
    public void testQueryBalanceByMemberId() throws ManagerException {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setMemberId("200000000012");
        AccountResponse accountResponse = memberManager.queryBalanceByMemberId(accountRequest);
        Assert.assertEquals("0", accountResponse.getResponseCode());
        Assert.assertEquals("200100100120000000001200001", accountResponse.getAccounts().get(0).getAccountId());
    }

    @Test
    public void testCreateVerifyInfo() {
        VerifyInputRequest verifyInputRequest = new VerifyInputRequest();
        VerifyInfo verifyInfo = new VerifyInfo();
        verifyInfo.setVerifyEntity("13062919920321083X");
        verifyInfo.setVerifyType(1);
        verifyInfo.setMemberId("200000000012");
        verifyInfo.setVerifiedTime(new Date());
        verifyInputRequest.setVerifyInfo(verifyInfo);
        try {
            memberManager.createVerifyInfo(verifyInputRequest);
            Assert.fail("没有抛出异常，测试失败");
        } catch (ManagerException e) {
            Assert.assertEquals(ErrorCodeEnums.SYSTEM_ERROR.getCode(), e.getCode());
            Assert.assertEquals("新增实名信息失败会员认证信息重复[会员已经添加了相同类型的认证信息:memberId=200000000012,verifyType=1]", e.getMessage());
        }
    }

    @Test
    public void testQueryVerify() throws ManagerException {
        VerifyQueryRequest verifyQueryRequest = new VerifyQueryRequest();
        verifyQueryRequest.setMemberId("200000000012");
        verifyQueryRequest.setVerifyType(1);
        verifyQueryRequest.setSecurity(false);
        VerifyInfoResponse verifyInfoResponse = memberManager.queryVerify(verifyQueryRequest);
        Assert.assertEquals("0", verifyInfoResponse.getResponseCode());
        Assert.assertEquals(Optional.of(15l).get(), verifyInfoResponse.getVerifyInfos().get(0).getVerifyId());
    }

    @Test
    public void testQueryVerifyNotExist() throws ManagerException {
        VerifyQueryRequest verifyQueryRequest = new VerifyQueryRequest();
        verifyQueryRequest.setMemberId("200000021050");
        verifyQueryRequest.setVerifyType(1);
        verifyQueryRequest.setSecurity(false);
        VerifyInfoResponse verifyInfoResponse = memberManager.queryVerify(verifyQueryRequest);
        Assert.assertNull(verifyInfoResponse.getVerifyInfos());
    }

    @Test
    public void testUpdateVerify() throws ManagerException {
        VerifyInputRequest verifyInputRequest = new VerifyInputRequest();
        VerifyInfo verifyInfo = new VerifyInfo();
        verifyInfo.setMemberId("200000000035");
        verifyInfo.setVerifyType(1);
        verifyInfo.setVerifyEntity("130629199108090033");
        verifyInfo.setVerifyId(25l);
        verifyInputRequest.setVerifyInfo(verifyInfo);
        VerifyResponse verifyResponse = memberManager.updateVerify(verifyInputRequest);
        Assert.assertEquals("0", verifyResponse.getResponseCode());
        Assert.assertEquals("25", verifyResponse.getVerifyId());
    }

    @Test
    public void testQuerytMemberDecipherInfo() throws ManagerException {
        DecipherInfoRequest decipherInfoRequest = new DecipherInfoRequest();
        decipherInfoRequest.setMemberId("200000000035");
        List<DecipherItem> columnList = new ArrayList<DecipherItem>();
        DecipherItem accNameItem = new DecipherItem();
        accNameItem.setDecipheredType(2);//6真实姓名,1身份证,2手机号
        accNameItem.setQueryFlag(1);
        columnList.add(accNameItem);
        decipherInfoRequest.setColumnList(columnList);
        DecipherInfoResponse decipherInfoResponse = memberManager.querytMemberDecipherInfo(decipherInfoRequest);
        Assert.assertEquals("0", decipherInfoResponse.getResponseCode());
        Assert.assertEquals("13777807633", decipherInfoResponse.getDecipheredResult().get(0).getPrimitiveValue());
    }

    @Test
    public void testBankID() throws ManagerException {
        BankAccountInfoResponse response = memberManager.queryBankAccountDetail("11421");

        System.out.println(response.getExtention());
        System.out.println(response);
    }
//[BankAccountInfoResponse[
// bankAcctInfo=BankAccountInfo [bankcardId=11421,
// memberId=200000890607, bankCode=ICBC, bankName=工商银行,
// bankBranch=null, bankAccountNumMask=***************8072,
// realName=摇锴, province=null, city=null, cardType=1,
// cardAttribute=1, isVerified=1, alias=null, cardSkin=null,
// isSigning=N, status=1, extention=2610, payAttribute=normal,
// certType=null, certNum=null, mobileNum=P478590, activateDate=Tue
// Sep 26 19:56:01 CST 2017, channelCode=null, branchNo=null
// ]BankAcctDetailInfo [signId=null, agreementValidDate=Tue Sep 26 19:56:00 CST 2017],responseCode=0,responseMessage=处理成功,extention=<null>]]






    @Test
    public void testRealNameAuthError() throws ManagerException {
        AuthRealNameResponse authRealNameResponse = authRealNameManager.realNameAuth(UUIDUtil.getID(), "郑森",
                "13062919920321083Y");
        Assert.assertFalse(authRealNameResponse.isSuccess());
        Assert.assertNotEquals(Constants.UMPAY_SUCCESS_CODE, authRealNameResponse.getCode());
    }

    @Test
    public void testCreateAccount() throws ManagerException{
        OpenAccountResponse response = memberManager.openAccount("200000000012", 204);
        Assert.assertNotNull(response.getAccountId());
    }

}
