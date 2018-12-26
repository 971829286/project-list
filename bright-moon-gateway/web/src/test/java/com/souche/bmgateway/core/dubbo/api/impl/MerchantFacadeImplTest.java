package com.souche.bmgateway.core.dubbo.api.impl;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.dubbo.api.MerchantFacade;
import com.souche.bmgateway.model.request.MerchantQueryRequest;
import com.souche.bmgateway.model.response.MerchantResultResponse;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author chenwj
 * @since 2018/8/9
 */
public class MerchantFacadeImplTest extends BaseTest {

    @Resource
    private MerchantFacade merchantFacade;

    @Test
    public void queryTest() {
        MerchantQueryRequest request = new MerchantQueryRequest();
        request.setMemberId("200000000110");
        MerchantResultResponse response = merchantFacade.queryMerchantResult(request);
        Assert.assertTrue(response.isSuccess());
    }

}
