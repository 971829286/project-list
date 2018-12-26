package com.souche.bmgateway.core.dubbo.api.impl;

import com.alibaba.fastjson.JSON;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import com.souche.bmgateway.dubbo.api.OrderShareApplyFacade;
import com.souche.bmgateway.model.FundDetail;
import com.souche.bmgateway.model.request.OrderShareApplyRequest;
import com.souche.bmgateway.model.response.OrderShareApplyResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangbin
 * @Description: 分帐请求服务测试类
 * @Date: Created in 2018/07/13
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class OrderShareApplyServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger("OrderShareApplyServiceImplTest");
    @Autowired
    OrderShareApplyFacade orderShareApplyFacade;

    @Autowired
    OrderShareApplyRepository orderShareApplyRepository;

    @Test
    public void doApply() {

        OrderShareApplyRequest request = new OrderShareApplyRequest();
        request.setMerchantId("226801000000121943873");
        request.setOutTradeNo("20180811105736219937429063-innerSplit");
        request.setRelateOrderNo("201808871000009728");
        request.setTotalAmount("1400");
        request.setCurrency("CNY");
//        request.setMemberId("hb12345");
//        request.setExtension("Extension");
        List<FundDetail> list = new ArrayList<FundDetail>();
        FundDetail fundDetail = new FundDetail();
        fundDetail.setAmount("60");
        fundDetail.setCurrency("CNY");
        fundDetail.setParticipantId("202210000000000001275");
        fundDetail.setParticipantType("PLATFORM");
        fundDetail.setPurpose("FEE");
        list.add(fundDetail);

        request.setPayeeFundDetails(list);
        // request.setPayerFundDetails(list);
//        request.setExtInfo("详情");
//        request.setMemo("备注");
        logger.info(JSON.toJSONString(request));
        OrderShareApplyResponse orderShareApplyResponse = orderShareApplyFacade.doApply(request);
        logger.info("orderShareApplyResponse=>{}", orderShareApplyResponse);
        Assert.assertEquals(true, orderShareApplyResponse.isSuccess());

    }

    @Test
    public void test() {
        HttpBaseResponse httpBaseResponse = new HttpBaseResponse();
        OrderShareApplyResponse orderShareApplyResponse = new OrderShareApplyResponse();
        orderShareApplyResponse.setSuccess(httpBaseResponse.isSuccess());
        orderShareApplyResponse.setCode(httpBaseResponse.getCode());
        orderShareApplyResponse.setMsg(httpBaseResponse.getMsg());
    }

    @Test
    public void update() {
        orderShareApplyRepository.updateShareApplyResult("20180804001", "111", "1", "");
//        orderShareApplyRepository.updateApplyStatus("20180804001","S");
    }


}