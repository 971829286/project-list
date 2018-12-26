package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBO;
import com.souche.map.service.api.location.Area;
import com.souche.map.service.api.location.service.AreaService;
import com.souche.optimus.core.web.Result;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺服务
 *
 * @author chenwj
 * @since 2018/7/26
 */
public class ShopServiceTest extends BaseTest {

    @Resource
    private ShopService shopService;

    @Resource
    private AreaService areaService;

    @Test
    public void queryShopTest() {
        Result<ShopInfoBO> rsShop = shopService.queryShopInfo("01134564");
        Assert.assertTrue(rsShop.isSuccess());
    }

    @Test
    public void test () {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
//        list.remove(1);
        System.out.println(list.toString());
    }

    @Test
    public void matchArea(){
        Area area = areaService.matchArea("浙江省", "杭州市", "");
        Assert.assertEquals("330000000000", area.getProvinceCode());
    }

    @Test
    public void queryAddress() {
        Result<String> result = shopService.queryAddress("GH00000036");
        Assert.assertEquals("上海市长宁区延安东路8866号", result.getData());
    }
}
