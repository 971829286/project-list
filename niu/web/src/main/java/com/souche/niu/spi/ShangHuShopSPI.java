package com.souche.niu.spi;

/**
 * @Description：更新店铺logo接口
 *
 * @remark: Created by wujingtao in 2018/9/25
 **/
public interface ShangHuShopSPI {

    Boolean updateLogo(int siteId, String shopCode, String shopName);
}
