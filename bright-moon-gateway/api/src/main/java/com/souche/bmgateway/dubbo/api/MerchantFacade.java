package com.souche.bmgateway.dubbo.api;

import com.souche.bmgateway.model.request.MerchantQueryRequest;
import com.souche.bmgateway.model.response.MerchantResultResponse;

import com.alibaba.dubbo.doc.annotation.InterfaceDesc;
import com.alibaba.dubbo.doc.annotation.MethodDesc;
/**
 * 商户服务
 *
 * @author: chenwj
 * @since: 2018/7/24
 */
@InterfaceDesc(value="商户服务", url="http://null///git.souche-inc.com/spay/bright-moon-gateway/tree/master/api/src/main/java/com/souche/bmgateway/dubbo/api/MerchantFacade.java")
public interface MerchantFacade {

    /**
     * 商户入驻结果查询
     *
     * @param merchantQueryRequest
     * @return
     */
    @MethodDesc("商户入驻结果查询")
    MerchantResultResponse queryMerchantResult(MerchantQueryRequest merchantQueryRequest);

}
