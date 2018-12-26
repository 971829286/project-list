package com.souche.bmgateway.core.service.bill.builder;

import com.google.common.collect.Lists;
import com.souche.bmgateway.core.domain.ShopInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author zhaojian
 * @date 18/8/6
 */
public class ShopInfoBuilder {

    public static List<ShopInfo> buildAllinPayShopInfo(List<String[]> src) {
        List<ShopInfo> result = Lists.newArrayListWithCapacity(src.size());
        if (CollectionUtils.isNotEmpty(src)) {
            for (String[] data : src) {
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setShopCode(data[0]);
                shopInfo.setShopName(data[1]);
                shopInfo.setAccountNo(data[2]);
                shopInfo.setAccountName(data[3]);
                shopInfo.setRemark(data[14]);
                result.add(shopInfo);
            }

        }
        return result;
    }
}
