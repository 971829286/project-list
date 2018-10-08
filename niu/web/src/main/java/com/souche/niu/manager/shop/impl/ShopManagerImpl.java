package com.souche.niu.manager.shop.impl;

import com.alibaba.fastjson.JSON;
import com.souche.niu.manager.shop.ShopManager;
import com.souche.niu.spi.ShangHuShopSPI;
import com.souche.niu.spi.ShopSPI;
import com.souche.niu.spi.SiteSPI;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopExtra;
import com.souche.shop.model.ShopRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description：车牛店铺逻辑实现类
 *
 * @remark: Created by wujingtao in 2018/9/21
 **/
@Service("shopManager")
public class ShopManagerImpl implements ShopManager {

    private boolean test = OptimusConfig.getValue("test", Boolean.class, false);

    private static final Logger logger = LoggerFactory.getLogger(ShopManagerImpl.class);

    @Autowired
    private ShopSPI shopSPI;
    @Autowired
    private SiteSPI siteSPI;
    @Autowired
    private ShangHuShopSPI shangHuShopSPI;


    @Override
    public void addOrUpdateShop(ShopExtra shopExtra) {
        if (shopExtra == null || StringUtil.isEmpty(shopExtra.getCode())) {
            logger.error("添加/更新车牛店铺信息失败 店铺code不能为空");
            throw new OptimusExceptionBase("店铺code不能为空");
        }
        shopExtra.setType(Shop.TYPE_CheNiu);
        if (StringUtil.isEmpty(shopExtra.getShortname())) {
            shopExtra.setShortname(shopExtra.getName());
        }
        shopSPI.saveOrUpdateShop(shopExtra);
        logger.info("添加/修改车牛店铺信息 data={}",JSON.toJSONString(shopExtra));

        ShopRelation shopRelation = this.shopSPI.loadRelationByShopCode(shopExtra.getCode());
        if (shopRelation == null) {
            String domain = shopExtra.getCode() + ".souche.com";
            if(test) {
                domain = shopExtra.getCode() + ".weidian.souche.com";
            }
            Shop shop = shopSPI.loadShopByCode(shopExtra.getCode());
            if (shop == null) {
                logger.error("修改车牛店铺信息，更新店铺domain信息失败，当前shopCode未加载到店铺 shopCode=[{}]",shopExtra.getCode());
                throw new OptimusExceptionBase("修改车牛店铺信息，更新店铺domain信息失败，当前shopCode未加载到店铺 shopCode=" + shopExtra.getCode());
            }
            siteSPI.bindCheniuShopDomain(shopExtra.getCode(),domain);
            logger.info("添加/修改车牛店铺信息 [bindCheniuShopDomain(shopExtra.getCode(),domain)] shopCode:{},domain:{}",shopExtra.getCode(),domain);
        }

        //todo 修改店铺名称时修改店铺logo
        try {
            if (StringUtil.isNotEmpty(shopExtra.getName()) && shopRelation != null) {
                boolean flag = shangHuShopSPI.updateLogo(shopRelation.getSiteId(), shopExtra.getCode(), shopExtra.getName());
                if (flag == true) {
                    logger.info("修改店铺logo siteId:{},shopCode:{},name:{}", shopRelation.getSiteId(), shopExtra.getCode(), shopExtra.getName());
                } else {
                    logger.error("修改店铺logo失败 siteId:{},shopCode:{},name:{}", shopRelation.getSiteId(), shopExtra.getCode(), shopExtra.getName());
                }
            }
        } catch (OptimusExceptionBase e) {
            logger.error("修改店铺logo错误 {}",e.toString());
        }
    }
}
