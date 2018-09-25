package com.souche.niu.spi;

import com.souche.shop.model.Shop;

/**
 * Created by sid on 2018/9/5.
 */
public interface ShopSPI {


    /**
     * 判断用户是否认证接口
     *
     * @author ZhangHui
     * @since 2018-09-12
     */
    boolean checkShopAuthentication(String shopCode);

    /**
     * 获取店铺信息
     *
     * @author ZhangHui
     * @since 2018-09-12
     */
    Shop loadShopByCode(String shopCode) ;
}
