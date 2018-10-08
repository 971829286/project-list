package com.souche.niu.spi.adapter;

import com.souche.niu.spi.SiteSPI;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.shop.api.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/21
 **/
@Service("siteSPI")
public class SiteSPIAdapter implements SiteSPI {

    private static final Logger logger = LoggerFactory.getLogger(SiteSPIAdapter.class);

    @Resource
    private SiteService siteService;

    @Override
    public void bindCheniuShopDomain(String shopCode, String domain) {
        if (StringUtil.isEmpty(shopCode)) {
            logger.error("更新车牛店铺的domain信息失败 shopCode为空");
            throw new OptimusExceptionBase("更新车牛店铺的domain信息失败 shopCode为空");
        }
        if (StringUtil.isEmpty(domain)) {
            logger.error("更新车牛店铺的domain信息失败 domain为空");
            throw new OptimusExceptionBase("更新车牛店铺的domain信息失败 domain为空");
        }
        try {
            this.siteService.bindCheniuShopDomain(shopCode, domain);
            logger.info("更新车牛店铺domain信息 shopCode=[{}],domain=[{}]",shopCode,domain);
        } catch (OptimusExceptionBase e) {
            logger.error("更新车牛店铺的domain信息失败 {}", e.toString());
            throw e;
        }
    }
}
