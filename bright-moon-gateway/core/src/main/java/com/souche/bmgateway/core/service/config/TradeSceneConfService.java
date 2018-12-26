package com.souche.bmgateway.core.service.config;

import com.souche.bmgateway.core.domain.TradeSceneConfDO;

/**
 * 交易场景配置
 *
 * @author zs.
 *         Created on 18/12/4.
 */
public interface TradeSceneConfService {

    /**
     * 更加业务产品编号查询账户配置信息
     *
     * @param bizProductCode
     * @return
     */
    TradeSceneConfDO queryTradeConf(String bizProductCode);
}
