package com.souche.niu.spi;

import com.souche.shop.model.ShopAudit;

/**
 * @Description：店铺审核服务
 *
 * @remark: Created by wujingtao in 2018/9/25
 **/
public interface ShopAuditSPI {

    /**
     * 根据shop code加载车行认证信息
     * @param shopCode
     * @return
     */
    ShopAudit loadAuditByShopCode(String shopCode);
}
