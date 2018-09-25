package com.souche.niu.spi.adapter;

import com.souche.niu.spi.ShopSPI;
import com.souche.shop.api.ShopService;
import com.souche.shop.model.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sid on 2018/9/5.
 */
@Component
public class ShopSPIAdapter implements ShopSPI {

    private static final Logger logger = LoggerFactory.getLogger(ShopSPIAdapter.class);

    @Autowired
    private ShopService shopService;

    /**
     * 判断用户是否认证接口
     *
     * @author ZhangHui
     * @since 2018-09-12
     */
    @Override
    public boolean checkShopAuthentication(String shopCode) {
        try {
            Shop shop = shopService.loadShopByCode(shopCode);
            if (shop == null) {
                return false;
            }
            //判断是否需要
            if (shop.getCertStatus() != null && shop.getCertStatus().intValue() == 1) {
                return true;
            }
        } catch (Exception e) {
            logger.error("认证信息 判断用户是否认证接口 异常e={}",e);
        }
        return false;
    }

    /**
     * 根据shopCode获取店铺信息
     *
     * @author ZhangHui
     * @since 2018-09-12
     */
    @Override
    public Shop loadShopByCode(String shopCode) {
        Shop shop = null;
        try {
            shop = shopService.loadShopByCode(shopCode);
        } catch (Exception e) {
            logger.error("认证信息 根据shopCode获取店铺信息 异常e={}",e);
        }
        return shop;
    }
}
