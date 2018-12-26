package com.souche.bmgateway.core.job;

import com.google.common.collect.Maps;
import com.souche.bmgateway.core.dao.OrderShareApplyMapper;
import com.souche.bmgateway.core.domain.OrderShareApply;
import com.souche.bmgateway.core.dto.request.OrderShareQueryRequest;
import com.souche.bmgateway.core.dto.response.OrderShareQueryResponse;
import com.souche.bmgateway.core.enums.ShareResult;
import com.souche.bmgateway.core.manager.share.OrderShareQueryService;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;


/**
 * @Author: huangbin
 * @Description:
 * @Date: Created in
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class SingleOrdershareQueryJobTest {

    public static final Logger logger = LoggerFactory.getLogger(SingleOrdershareQueryJobTest.class);

    private static final String SHARE_TAG = OptimusConfig.getValue("ons.notify.tag");

    @Autowired
    private OrderShareQueryService orderShareQueryService;

    @Autowired
    private OrderShareApplyRepository orderShareApplyRepository;

    @Autowired
    private ONSProducer orderShareNotifyProducer;

    @Test
    public void doQuery(){

        OrderShareApply orderShareApply = orderShareApplyRepository.selectByOutTradeNo("20180810153850969670967988-innerSplit");

        OrderShareQueryRequest request = buildShareQueryRequest(orderShareApply);

        logger.info("request=>{}",request);

        OrderShareQueryResponse response = orderShareQueryService.doQuery(request);

        logger.info("response=>{}",response);
    }

    @Test
    public void execute() {

        logger.info("查询分账没有通知结果的数据...");
        try {
            List<OrderShareApply> orderShareApplylist = orderShareApplyRepository.selectNotSuccessStatus(10, 30);

            for (OrderShareApply orderShareApply : orderShareApplylist) {

                logger.info("orderShareApply=>{}开始查询分账结果...", orderShareApply);

                OrderShareQueryRequest request = buildShareQueryRequest(orderShareApply);

                OrderShareQueryResponse response = orderShareQueryService.doQuery(request);

                logger.info("分帐结果=>{}", response);

                Assert.assertEquals("SUCCESS",response.getStatus());

                //单笔查询分账成功MQ通知上游
                if (ShareResult.SUCCESS.getCode().equals(response.getStatus())) {
                    //更新分帐结果信息
                    updateShareResult(orderShareApply, response);

                    try {
                        Map<String, Object> producerMap = Maps.newHashMap();
                        producerMap.put("outTradeNo", response.getOutTradeNo());
                        producerMap.put("shareStatus", response.getStatus());
                        String key = UUIDUtil.getID();
                        logger.info("单笔查询发送分账成功MQ,producerMap=>{},key=>{},SHARE_TAG=>{}", producerMap, key, SHARE_TAG);
                        orderShareNotifyProducer.send(producerMap, key, SHARE_TAG);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("单笔查询分账成功MQ通知上游失败,response=>{}，异常=>{}", response, e);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("任务:SingleOrdershareQueryJob error,异常=>{}", e);
        }
    }

    /**
     * 更新分帐结果信息
     *
     * @param orderShareApply
     * @param response
     */
    private void updateShareResult(OrderShareApply orderShareApply, OrderShareQueryResponse response) {

        orderShareApply.setNotifyStatus(ShareResult.getShareResultByCode(response.getStatus()).getNitifyStatus());

        orderShareApplyRepository.updateByPrimaryKey(orderShareApply);
    }

    /**
     * 构建分账查询请求
     *
     * @param orderShareApply
     * @return
     */
    private OrderShareQueryRequest buildShareQueryRequest(OrderShareApply orderShareApply) {
        OrderShareQueryRequest orderShareQueryRequest = new OrderShareQueryRequest();
        orderShareQueryRequest.setReqMsgId(orderShareApply.getReqMsgId());
        orderShareQueryRequest.setMerchantId(orderShareApply.getMerchantId());
        orderShareQueryRequest.setOutTradeNo(orderShareApply.getOutTradeNo());
        orderShareQueryRequest.setRelateOrderNo(orderShareApply.getRelateOrderNo());
        orderShareQueryRequest.setShareOrderNo(orderShareApply.getShareOrderNo());
        return orderShareQueryRequest;
    }
}