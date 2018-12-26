package com.souche.bmgateway.core.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
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
import com.souche.optimus.mq.ConsumeResult;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 单笔分账结果查询任务
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Slf4j(topic = "job")
@Component
public class SingleOrdershareQueryJob implements SimpleJob {

    private static final String SHARE_TAG = OptimusConfig.getValue("ons.notify.tag");

    @Autowired
    private OrderShareQueryService orderShareQueryService;

    @Autowired
    private OrderShareApplyRepository orderShareApplyRepository;

    @Autowired
    private ONSProducer orderShareNotifyProducer;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("查询分账没有通知结果的数据...");
        try {
            List<OrderShareApply> orderShareApplylist = orderShareApplyRepository.selectNotSuccessStatus(10, 30);

            for (OrderShareApply orderShareApply : orderShareApplylist) {

                log.info("orderShareApply=>{}开始查询分账结果...", orderShareApply);

                OrderShareQueryRequest request = buildShareQueryRequest(orderShareApply);

                OrderShareQueryResponse response = orderShareQueryService.doQuery(request);

                log.info("分帐结果=>{}", response);

                //更新分帐结果信息
                updateShareResult(orderShareApply, response);

                //单笔查询分账成功MQ通知上游
                if (ShareResult.SUCCESS.getCode().equals(response.getStatus())) {

                    try {
                        Map<String, Object> producerMap = Maps.newHashMap();
                        producerMap.put("outTradeNo", response.getOutTradeNo());
                        producerMap.put("shareStatus", response.getStatus());
                        String key = UUIDUtil.getID();
                        log.info("单笔查询发送分账成功MQ,producerMap=>{},key=>{},SHARE_TAG=>{}", producerMap, key, SHARE_TAG);
                        orderShareNotifyProducer.send(producerMap, key, SHARE_TAG);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("单笔查询分账成功MQ通知上游失败,response=>{}，异常=>{}", response, e);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("任务:SingleOrdershareQueryJob error,异常=>{}", e);
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
        orderShareApply.setQueryTimes(orderShareApply.getQueryTimes() + 1);
        orderShareApply.setShareDate(response.getShareDate());
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
