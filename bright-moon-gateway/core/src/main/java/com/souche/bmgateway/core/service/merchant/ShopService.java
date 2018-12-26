package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBO;
import com.souche.optimus.core.web.Result;

/**
 * 企业（商户）服务
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface ShopService {

    /**
     * 查询企业信息
     *
     * @param shopCode 内部商户号
     * @return Result
     */
    Result<ShopInfoBO> queryShopInfo(String shopCode);

    /**
     * 查询商户地址
     *
     * @param shopCode 店铺code
     */
    Result<String> queryAddress(String shopCode);

}
