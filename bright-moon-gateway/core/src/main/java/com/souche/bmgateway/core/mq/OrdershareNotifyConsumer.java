package com.souche.bmgateway.core.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.souche.bmgateway.core.dao.OrderShareNotifyDetailMapper;
import com.souche.bmgateway.core.domain.OrderShareNotifyDetail;
import com.souche.bmgateway.core.enums.ShareResult;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.mq.ConsumeResult;
import com.souche.optimus.mq.MQConsumer;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 分帐异步通知MQ消费并通知上游
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
public class OrdershareNotifyConsumer implements MQConsumer {

    private final static Logger logger = LoggerFactory.getLogger(OrdershareNotifyConsumer.class);

    private static final String SHARE_TAG = OptimusConfig.getValue("ons.notify.tag");

    @Autowired
    private ONSProducer orderShareNotifyProducer;

    @Autowired
    private OrderShareNotifyDetailMapper orderShareNotifyDetailMapper;

    @Autowired
    private OrderShareApplyRepository orderShareApplyRepository;

    @Override
    public ConsumeResult onRecived(Map<String, Object> map) {
        logger.info("start to revice mq...");
        if (map == null || map.size() == 0) {
            logger.error("消息为空");
            return ConsumeResult.CommitMessage;
        }
        String msgId = map.get("_msgId") + "";
        logger.info("开始接受开放平台分帐异步通知消息msgId=>{}, 消息内容=>{}", msgId, map);

        String type = map.get("type") + "";
        //接收分账通知MQ
        if (!"ant.mybank.bkcloudfunds.ordershare.notify".equals(type)) {
            return ConsumeResult.CommitMessage;
        }

        String data = map.get("data") + "";

        //记录分账异步通知流水
        OrderShareNotifyDetail orderShareNotifyDetail = saveShareNotifyDetail(data);

        //更新网商分账通知结果
        orderShareApplyRepository.updateNotifyResult(orderShareNotifyDetail.getOutTradeNo(), ShareResult.getShareResultByCode(orderShareNotifyDetail.getStatus()).getNitifyStatus(), orderShareNotifyDetail.getShareDate());

        //分账成功MQ通知上游
        if (ShareResult.SUCCESS.getCode().equals(orderShareNotifyDetail.getStatus())) {
            try {

                Map<String, Object> producerMap = Maps.newHashMap();
                producerMap.put("outTradeNo", orderShareNotifyDetail.getOutTradeNo());
                producerMap.put("shareStatus", orderShareNotifyDetail.getStatus());
                String key = UUIDUtil.getID();
                logger.info("发送分账成功MQ,producerMap=>{},key=>{},SHARE_TAG=>{}", producerMap, key, SHARE_TAG);
                orderShareNotifyProducer.send(producerMap, key, SHARE_TAG);

                return ConsumeResult.CommitMessage;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("分账成功MQ通知上游失败,map=>{}，异常=>{}", map, e);
            }
        } else {
            logger.error("注意!!!网商分账异步通知返回失败=>{}", map);
            return ConsumeResult.CommitMessage;
        }
        return ConsumeResult.ReconsumeLater;
    }

    /**
     * 记录分账异步通知流水
     *
     * @param data
     */
    private OrderShareNotifyDetail saveShareNotifyDetail(String data) {
        OrderShareNotifyDetail orderShareNotifyDetail = new OrderShareNotifyDetail();
        try {
            JSONObject jsonObject = JSON.parseObject(data);
            orderShareNotifyDetail.setIsvOrgId(jsonObject.getString("IsvOrgId"));
            orderShareNotifyDetail.setMerchantId(jsonObject.getString("MerchantId"));
            orderShareNotifyDetail.setRelateOrderNo(jsonObject.getString("RelateOrderNo"));
            orderShareNotifyDetail.setOutTradeNo(jsonObject.getString("OutTradeNo"));
            orderShareNotifyDetail.setShareOrderNo(jsonObject.getString("ShareOrderNo"));
            orderShareNotifyDetail.setTotalAmount(jsonObject.getString("TotalAmount"));
            orderShareNotifyDetail.setCurrency(jsonObject.getString("Currency"));
            orderShareNotifyDetail.setShareDate(jsonObject.getString("ShareDate"));
            orderShareNotifyDetail.setStatus(jsonObject.getString("Status"));
            orderShareNotifyDetail.setErrorCode(jsonObject.getString("ErrorCode"));
            orderShareNotifyDetail.setErrorDesc(jsonObject.getString("ErrorDesc"));
            orderShareNotifyDetail.setExtInfo(jsonObject.getString("ExtInfo"));
            orderShareNotifyDetail.setMemo(jsonObject.getString("Memo"));
            orderShareNotifyDetail.setGmtCreate(new Date());
            orderShareNotifyDetail.setGmtModified(new Date());
            orderShareNotifyDetailMapper.insert(orderShareNotifyDetail);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("记录分账异步通知流水失败，data=>{},异常=>{}", data, e);
        }
        return orderShareNotifyDetail;
    }
}
