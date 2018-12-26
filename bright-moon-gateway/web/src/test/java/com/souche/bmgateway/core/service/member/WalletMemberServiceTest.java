package com.souche.bmgateway.core.service.member;

import com.netfinworks.ma.service.base.model.DecipherResult;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.enums.PlatFormTypeEnums;
import com.souche.bmgateway.core.manager.weijin.MemberManager;
import com.souche.bmgateway.core.service.dto.MemberSimpleDTO;
import com.souche.bmgateway.core.service.dto.SyncIdCardDTO;
import com.souche.bmgateway.enums.ActivateStatus;
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
import com.souche.bmgateway.model.response.BalanceResponse;
import com.souche.bmgateway.model.response.BankAccountListResponse;
import com.souche.bmgateway.model.response.BankCardResponse;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.CreateAccountResponse;
import com.souche.bmgateway.model.response.UserVerifyInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberInfoResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.bmgateway.model.response.member.QueryAccountsResponse;
import com.souche.optimus.common.util.UUIDUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zs.
 *         Created on 18/8/2.
 */
public class WalletMemberServiceTest extends BaseTest {

    @Resource
    private WalletMemberService walletMemberService;

    @Resource
    private MemberManager memberManager;
    
    @Test
	public void testqueryWalletAccountsInfo() {
		QueryAccountsRequest queryAccountsRequest = new QueryAccountsRequest();
		queryAccountsRequest.setMemberId("200000201004");// 200000000056
		queryAccountsRequest.setAccountType("203");
		queryAccountsRequest.setMemberIdentity(null);
		QueryAccountsResponse queryAccountsResponse = walletMemberService.queryWalletAccountsInfo(queryAccountsRequest);
		System.out.println(queryAccountsResponse);
	}

	// 创建个人会员
	@Test
	public void CreatePersonalMemberTest() {
		CreatePersonalMemberRequest req = new CreatePersonalMemberRequest();
		req.setEmail("1529192474@qq.com"); // memberId=100000161027,operatorId=7000000161024,accountId=200100200110000016102700001
		req.setIdCardNo("536778388292999230");
		req.setIsActive(ActivateStatus.ACTIVATED_ALL);
		req.setIsVerify("T");
		req.setMemberName("测试杨");
		req.setMobile("18711770632");
		req.setUid("sdsssrerD");
		req.setRealName("lb");
		req.setPlatformType(PlatFormTypeEnums.COMPANY_ID.getLoginNamePlatformType());
		WalletMemberResponse creatPersonalMember = walletMemberService.creatPersonalMember(req);
		System.out.println(creatPersonalMember);
	}

	// 修改个人会员信息
	@Test
	public void modifyPersonalInfoTest() {
		ModifyPersonalInfoRequest modifyPersonalInfoRequest = new ModifyPersonalInfoRequest();
		// modifyPersonalInfoRequest.setIdCardNo(idCardNo);
		modifyPersonalInfoRequest.setMemberId("100000161027");
		modifyPersonalInfoRequest.setMemberName("xg");
		walletMemberService.modifyPersonalInfo(modifyPersonalInfoRequest);
	}

//    @Test
//    public void testQueryBankCardInfo() {
//        try {
//            String accName = walletMemberService.queryMemberDecipherInfo("200000000056");
//            System.out.println(accName);
//        } catch (ManagerException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Test
    public void testCreateAndActivateEnterpriseMember() {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
        createEnterpriseMemberRequest.setPartnerId("288888888888");
        createEnterpriseMemberRequest.setUid(UUIDUtil.getID());
        createEnterpriseMemberRequest.setLoginName(UUIDUtil.getID());
        createEnterpriseMemberRequest.setTelephone("13737964756");
        WalletMemberResponse andActivateEnterpriseMember = walletMemberService.createAndActivateEnterpriseMember
                (createEnterpriseMemberRequest);
        Assert.assertEquals("200", andActivateEnterpriseMember.getCode());
        Assert.assertNotNull(andActivateEnterpriseMember.getMemberId());
    }

    @Test
    public void testCreateAndActivateEnterpriseMemberExist() {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
        createEnterpriseMemberRequest.setUid("MROaBDIq6M");
        createEnterpriseMemberRequest.setLoginName("zk67Tk8Ftq");
        createEnterpriseMemberRequest.setTelephone("13737964756");
        WalletMemberResponse andActivateEnterpriseMember = walletMemberService.createAndActivateEnterpriseMember
                (createEnterpriseMemberRequest);
        Assert.assertEquals("200", andActivateEnterpriseMember.getCode());
        Assert.assertNotNull(andActivateEnterpriseMember.getMemberId());
    }

    @Test
    public void testCreateAndActivateEnterpriseMemberExistAndUnActive() {
        CreateEnterpriseMemberRequest createEnterpriseMemberRequest = new CreateEnterpriseMemberRequest();
//        createEnterpriseMemberRequest.setUid("MROaBDIq6M");
        createEnterpriseMemberRequest.setLoginName("3BcjkidtLa");
        createEnterpriseMemberRequest.setTelephone("13777807633");
        WalletMemberResponse andActivateEnterpriseMember = walletMemberService.createAndActivateEnterpriseMember
                (createEnterpriseMemberRequest);
        Assert.assertEquals("200", andActivateEnterpriseMember.getCode());
        Assert.assertNotNull(andActivateEnterpriseMember.getMemberId());
    }

    @Test
    public void testModifyEnterpriseInfo() {
        ModifyEnterpriseInfoRequest modifyEnterpriseInfoRequest = new ModifyEnterpriseInfoRequest();
        modifyEnterpriseInfoRequest.setMemberId("200000021055");
        modifyEnterpriseInfoRequest.setLegalPerson("郑森");
        modifyEnterpriseInfoRequest.setLegalPersonPhone("13777807615");
        CommonResponse commonResponse = walletMemberService.modifyEnterpriseInfo(modifyEnterpriseInfoRequest);
        Assert.assertEquals("200", commonResponse.getCode());
    }

    @Test
    public void testQueryWalletMemberInfo() {
        QueryWalletMemberInfoRequest queryWalletMemberInfoRequest = new QueryWalletMemberInfoRequest();
        queryWalletMemberInfoRequest.setMemberId("200000000038");
        WalletMemberInfoResponse walletMemberInfoResponse = walletMemberService.queryWalletMemberInfo
                (queryWalletMemberInfoRequest);
        Assert.assertEquals("200", walletMemberInfoResponse.getCode());
        Assert.assertEquals("200000000038", walletMemberInfoResponse.getMemberId());
        Assert.assertEquals("1", walletMemberInfoResponse.getStatus().toString());
    }

    @Test
    public void testQueryAccountBalance() {
        QueryBalanceRequest queryBalanceRequest = new QueryBalanceRequest();
        queryBalanceRequest.setMemberId("200000000038");
        BalanceResponse balanceResponse = walletMemberService.queryAccountBalance(queryBalanceRequest);
        Assert.assertEquals("200", balanceResponse.getCode());
        Assert.assertEquals("0.00", balanceResponse.getAccountList().get(0).getBalance());
    }

    @Test
    public void testCreateBankCard() {
        CreateBankCardRequest createBankCardRequest = new CreateBankCardRequest();
        createBankCardRequest.setMemberId("200000000038");
        createBankCardRequest.setBankCode("ICBC");
        createBankCardRequest.setBankName("中国工商银行");
        createBankCardRequest.setBankBranch("中国工商银行高沙支行");
        createBankCardRequest.setBankBranchNo("102100099996");
        createBankCardRequest.setBankAccountNo("6222021202039335071");
        createBankCardRequest.setAccountName("郑森");
        createBankCardRequest.setProvince("浙江省");
        createBankCardRequest.setCity("杭州市");
        createBankCardRequest.setCardType("DEBIT");
        createBankCardRequest.setCardAttribute("C");
        BankCardResponse bankCard = walletMemberService.createBankCard(createBankCardRequest);
        Assert.assertEquals("200", bankCard.getCode());
        Assert.assertNotNull(bankCard.getBankcardId());
    }

    @Test
    public void testRemoveBankCard() {
        RemoveBankCardRequest removeBankCardRequest = new RemoveBankCardRequest();
        removeBankCardRequest.setBankcardId("2");
        removeBankCardRequest.setMemberId("200000000012");
        CommonResponse commonResponse = walletMemberService.removeBankCard(removeBankCardRequest);
        Assert.assertEquals("200", commonResponse.getCode());
    }

    @Test
    public void testQueryBankAccountList() {
        QueryBankAccountRequest queryBankAccountRequest = new QueryBankAccountRequest();
        queryBankAccountRequest.setMemberId("200000000038");
        BankAccountListResponse bankAccountListResponse = walletMemberService.queryBankAccountList
                (queryBankAccountRequest);
        Assert.assertEquals("200", bankAccountListResponse.getCode());
        Assert.assertEquals("200000000038", bankAccountListResponse.getBankAccountRecordInfoList().get(0).getMemberId());
        Assert.assertEquals(Integer.valueOf("1"), bankAccountListResponse.getBankAccountRecordInfoList().get(0).getStatus());
    }

    @Test
    public void testSyncIdCard() {
        SyncIdCardDTO syncIdCardDTO = new SyncIdCardDTO();
        syncIdCardDTO.setMemberId("200000021052");
        syncIdCardDTO.setIdCard("139554442839994");
        CommonResponse commonResponse = walletMemberService.syncIdCard(syncIdCardDTO);
        Assert.assertEquals("200", commonResponse.getCode());
    }

    @Test
    public void testQueryVerifyInfo() {
        QueryVerifyInfoRequest queryVerifyInfoRequest = new QueryVerifyInfoRequest();
        queryVerifyInfoRequest.setMemberId("200000000038");
        UserVerifyInfoResponse userVerifyInfoResponse = walletMemberService.queryVerifyInfo(queryVerifyInfoRequest);
        Assert.assertEquals("200", userVerifyInfoResponse.getCode());
        Assert.assertEquals("郑森", userVerifyInfoResponse.getAccName());
        Assert.assertEquals("139554442839993", userVerifyInfoResponse.getIdCard());
        Assert.assertEquals("13737964756", userVerifyInfoResponse.getPhone());
    }

    @Test
    public void testQueryMemberDecipherInfo() throws ManagerException {
        List<DecipherResult> decipherResults = walletMemberService.queryMemberDecipherInfo(new MemberSimpleDTO("200000000038"));
        for (DecipherResult rs : decipherResults) {
            if (rs.getDecipheredType() == 6) {
                Assert.assertEquals("郑森", rs.getPrimitiveValue());
            }
            if (rs.getDecipheredType() == 1) {
                Assert.assertEquals("139554442839993", rs.getPrimitiveValue());
            }
            if (rs.getDecipheredType() == 2) {
                Assert.assertEquals("13737964756", rs.getPrimitiveValue());
            }
        }
    }

    @Test
    public void testUmpayAuthRealName() {
        AuthRealNameRequest authRealNameRequest = new AuthRealNameRequest();
        authRealNameRequest.setRequestNo(UUIDUtil.getID());
        authRealNameRequest.setIdCard("13062919920321083X");
        authRealNameRequest.setRealName("郑森");
        CommonResponse commonResponse = walletMemberService.umpayAuthRealName(authRealNameRequest);
        Assert.assertEquals("200", commonResponse.getCode());
        Assert.assertTrue(commonResponse.isSuccess());
    }

    @Test
    public void testCreateAccount() {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest();
        openAccountRequest.setMemberId("200000000012");
        openAccountRequest.setAccountType("204");
        CreateAccountResponse response = walletMemberService.openAccount(openAccountRequest);
        Assert.assertTrue(response.isSuccess());
    }

}
