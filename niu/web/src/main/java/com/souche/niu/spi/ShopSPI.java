package com.souche.niu.spi;

import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopExtra;
import com.souche.shop.model.ShopRelation;

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

    /**
     * 添加/修改店铺
     * @param shop
     *
     * @author wujingtao
     */
    void saveOrUpdateShop(ShopExtra shop);

    /**
     * 根据code获得店铺所属站关系的信息
     * @param shopCode
     * @return
     *
     * @author wujingtao
     */
    ShopRelation loadRelationByShopCode(String shopCode);


    /**
     * 根据shopcode加载店铺extra信息
     * @param shopCode
     * @return
     */
    ShopExtra loadShopExtraByCode(String shopCode);
}
