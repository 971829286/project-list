package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.BaseTest;
import com.souche.optimus.core.web.Result;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 账务服务
 *
 * @author chenwj
 * @since 2018/7/26
 */
public class FinanceServiceTest extends BaseTest {

    @Resource
    private FinanceService financeService;

    @Test
    public void getVoucherPic() {
        Result<String> rs = financeService.queryAcctOpenLicense("200001130458");
        Assert.assertTrue(rs.isSuccess());
        Assert.assertEquals("http://img.souche.com/gkgz9osvuj47w1dn6c4j1rxnojhfsuko.jpg", rs.getData());
    }

}
