package com.souche.bmgateway.core.service.config.impl;

import com.souche.bmgateway.core.dao.TradeSceneConfDOMapper;
import com.souche.bmgateway.core.domain.TradeSceneConfDO;
import com.souche.bmgateway.core.service.config.TradeSceneConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/12/4.
 */
@Service("tradeSceneConfService")
@Slf4j(topic = "service")
public class TradeSceneConfServiceImpl implements TradeSceneConfService {

    @Resource
    private TradeSceneConfDOMapper tradeSceneConfDOMapper;

    @Override
    public TradeSceneConfDO queryTradeConf(String bizProductCode) {
        return tradeSceneConfDOMapper.queryByBizProductCode(bizProductCode);
    }
}
