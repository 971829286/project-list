package com.souche.niu.spi.adapter;

import com.souche.niu.spi.ShangHuShopSPI;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.site.outer.api.service.ShangHuShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/25
 **/
@Service("shangHuShopSPI")
public class ShangHuShopSPIAdapter implements ShangHuShopSPI {

    private static final Logger logger = LoggerFactory.getLogger(ShangHuShopSPIAdapter.class);

    @Resource
    private ShangHuShopService shangHuShopService;


    @Override
    public Boolean updateLogo(int siteId, String shopCode, String shopName) {
        try {
            return shangHuShopService.updateLogo(siteId, shopCode, shopName);
        } catch (OptimusExceptionBase e) {
            logger.error("调取dubbo服务更新店铺logo错误 {}",e.toString());
            throw e;
        }
    }
}
