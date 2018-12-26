package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.service.dto.MemberSimpleDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.member.builder.BankCardInfoBO;
import com.souche.optimus.core.web.Result;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 会员服务
 *
 * @author chenwj
 * @since 2018/8/2
 */
public class MemberServiceTest extends BaseTest {

    @Resource
    private WalletMemberService walletMemberService;

    @Test
    public void bankInfo() {
        Result<BankCardInfoBO> rsBankInfo = walletMemberService.queryBankCardInfo(new MemberSimpleDTO("200001130458"));
        Assert.assertTrue(rsBankInfo.isSuccess());
    }

}
