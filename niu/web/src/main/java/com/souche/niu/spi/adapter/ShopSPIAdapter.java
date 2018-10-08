package com.souche.niu.spi.adapter;

import com.souche.niu.spi.ShopSPI;
import com.souche.shop.api.ShopAuditService;
import com.souche.shop.api.ShopService;
import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopAudit;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.shop.model.ShopExtra;
import com.souche.shop.model.ShopRelation;
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

    @Autowired
    private ShopAuditService shopAuditService;
    /**
     * 判断用户是否认证接口
     *
     * @author ZhangHui
     * @since 2018-09-12
     */
    @Override
    public boolean checkShopAuthentication(String shopCode) {
        try {
            ShopAudit shop = shopAuditService.loadAuditByShopCode(shopCode);
            if (shop == null) {
                return false;
            }
            //判断是否需要
            if (shop.getReviewStatus() != null && shop.getReviewStatus().intValue() == 1) {
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

    @Override
    public void saveOrUpdateShop(ShopExtra shop) {
        if (shop == null) {
            logger.error("修改店铺信息失败 参数为空");
            throw new OptimusExceptionBase("修改店铺信息失败 参数为空");
        }
        if (StringUtil.isEmpty(shop.getCode())) {
            logger.error("修改店铺信息失败 shopCode为空");
            throw new OptimusExceptionBase("修改店铺信息失败 shopCode为空");
        }
        try {
            //todo 修改店铺扩展信息
            this.shopService.saveShopExtra(shop);
            //todo 修改授权店基础信息
            this.shopService.saveShop(shop);
        } catch (OptimusExceptionBase e) {
            logger.error("修改店铺信息失败 {}",e.toString());
            throw e;
        }
    }

    @Override
    public ShopRelation loadRelationByShopCode(String shopCode) {
        try {
            return this.shopService.loadRelationByShopCode(shopCode);
        } catch (OptimusExceptionBase e) {
            logger.error("获得店铺所属站关系的信息失败 shopCode=[{}]",shopCode);
            throw e;
        }
    }

    @Override
    public ShopExtra loadShopExtraByCode(String shopCode) {
        try {
            ShopExtra extra = this.shopService.loadShopExtraByCode(shopCode);
            return extra;
        } catch (OptimusExceptionBase e) {
            logger.info("获取店铺extra信息错误 {}",e);
            throw e;
        }
    }
}
