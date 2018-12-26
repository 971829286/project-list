package com.souche.bmgateway.core.share.impl;

import com.souche.bmgateway.core.dto.request.OrderShareQueryRequest;
import com.souche.bmgateway.core.manager.share.OrderShareQueryService;
import com.souche.optimus.common.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangbin
 * @Description:
 * @Date: Created in
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class OrderShareQueryServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderShareQueryServiceImplTest.class);

    @Autowired
    OrderShareQueryService orderShareQueryService;

    @Test
    public void doQuery() {
        OrderShareQueryRequest orderShareQueryRequest = new OrderShareQueryRequest();
        orderShareQueryRequest.setOutTradeNo("");
        orderShareQueryRequest.setMerchantId("");
        orderShareQueryRequest.setRelateOrderNo("");
        orderShareQueryRequest.setShareOrderNo("");
        orderShareQueryService.doQuery(orderShareQueryRequest);

    }

    @Test
    public void buildJsonTest(){
        OrderShareQueryRequest request = new OrderShareQueryRequest();
        request.setOutTradeNo("111");
        Map<String, String> map = new HashMap<String, String>();
        map.put("api", "");
        Map<String, String> mapInner = new HashMap<String, String>();
        mapInner.put("body",JsonUtils.toJson(request));
        map.put("date", JsonUtils.toJson(mapInner));
        String data = JsonUtils.toJson(map);
        data = data.replaceAll("\\\\", "");
        data = data.replaceAll("\"\"", "\"");
        data = data.replaceAll("\"\\{", "{");
        data = data.replaceAll("\\}\"", "}");
        logger.info("data:"+data);

    }
}
