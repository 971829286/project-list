package com.souche.niu.spi.adapter;

import com.souche.niu.spi.ShopAuditSPI;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.shop.api.ShopAuditService;
import com.souche.shop.model.ShopAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/25
 **/
@Service("shopAuditSPI")
public class ShopAuditSPIAdapter implements ShopAuditSPI {

    private static final Logger logger = LoggerFactory.getLogger(ShopAuditSPIAdapter.class);

    @Resource
    private ShopAuditService shopAuditService;


    @Override
    public ShopAudit loadAuditByShopCode(String shopCode) {
        try {
            return shopAuditService.loadAuditByShopCode(shopCode);
        } catch (OptimusExceptionBase e) {
            logger.error("获取店铺认证信息错误 {}",e.toString());
            throw e;
        }
    }
}
