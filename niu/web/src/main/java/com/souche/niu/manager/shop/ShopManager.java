package com.souche.niu.manager.shop;

import com.souche.shop.model.ShopExtra;

/**
 * @Description：车牛店铺逻辑接口
 *
 * @remark: Created by wujingtao in 2018/9/21
 **/
public interface ShopManager {

    /**
     * 添加/更新车牛店铺信息
     * @param shopExtra
     */
    void addOrUpdateShop(ShopExtra shopExtra);

}
