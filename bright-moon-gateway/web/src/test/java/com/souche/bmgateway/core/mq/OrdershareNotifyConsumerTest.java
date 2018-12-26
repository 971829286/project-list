package com.souche.bmgateway.core.mq;

import com.google.common.collect.Maps;
import com.souche.bmgateway.core.domain.OrderShareNotifyDetail;
import com.souche.bmgateway.core.enums.ShareResult;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 接收分账异步通知消息测试
 * @Date: Created in 2018/07/14
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class OrdershareNotifyConsumerTest {

    private static final Logger logger = LoggerFactory.getLogger(OrdershareNotifyConsumerTest.class);

    private static final String SHARE_TAG = OptimusConfig.getValue("ons.notify.tag");

    @Autowired
    private ONSProducer orderShareNotifyProducer;

    @Autowired
    private OrderShareApplyRepository orderShareApplyRepository;

    @Test
    public void onRecived() {
//记录分账异步通知流水
        OrderShareNotifyDetail orderShareNotifyDetail = new OrderShareNotifyDetail();

        orderShareNotifyDetail.setOutTradeNo("20180810122609961508729609-innerSplit");
        orderShareNotifyDetail.setStatus("SUCCESS");
        orderShareNotifyDetail.setShareDate("20180810125925");
        //更新网商分账通知结果
        orderShareApplyRepository.updateNotifyResult(orderShareNotifyDetail.getOutTradeNo(), ShareResult.getShareResultByCode(orderShareNotifyDetail.getStatus()).getNitifyStatus(), orderShareNotifyDetail.getShareDate());

        //更新网商分账通知结果
       // orderShareApplyRepository.updateNotifyResult("20180809111425084974102230-innerSplit",ShareResult.getShareResultByCode("SUCCESS").getNitifyStatus(),"20180809211036");

    }

    @Test
    public void producterTest() {
        try {
            Map<String, Object> producerMap = Maps.newHashMap();
            producerMap.put("outTradeNo", "20180811105736219937429063-innerSplit");
            producerMap.put("shareStatus", "SUCCESS");
            String key = UUIDUtil.getID();
            logger.info("发送分账成功MQ,producerMap=>{},key=>{},SHARE_TAG=>{}", producerMap, key, SHARE_TAG);
            orderShareNotifyProducer.send(producerMap, key, SHARE_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("分账成功MQ通知上游失败，异常=>{}", e);
        }
    }
}
