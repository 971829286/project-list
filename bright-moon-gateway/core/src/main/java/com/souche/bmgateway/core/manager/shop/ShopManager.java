package com.souche.bmgateway.core.manager.shop;

import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * 店铺服务
 *
 * @author chenwj
 * @since 2018/7/19
 */
public interface ShopManager {

    /**
     * 查询企业信息
     *
     * @param shopCode 内部商户号
     * @return HttpBaseResponse
     * @throws ManagerException 自定义Manager异常
     */
    HttpBaseResponse queryShopInfo(String shopCode) throws ManagerException;

}
