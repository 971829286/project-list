package com.souche.bmgateway.core.mq;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.souche.bmgateway.core.repo.BillFlowRepository;
import com.souche.bmgateway.core.domain.BillFlow;
import com.souche.bmgateway.core.enums.BillTypeEnums;
import com.souche.bmgateway.core.dto.mq.BillChargeInfo;
import com.souche.bmgateway.core.service.bill.service.AllinPayBillHandleService;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
@Slf4j
public class CommissionNotifyProducerTest {

    private final String notifyTag = "charge-notify";

    @Resource(name = "billChargeProducer")
    private ONSProducer ssoProducer;

    @Autowired
    BillFlowRepository billFlowRepository;

    private ExecutorService executorService = new ThreadPoolExecutor(
            5,
            10,
            60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy());

    @Test
    public void notifyMQ() {
        System.out.println(Runtime.getRuntime().availableProcessors());
        List<BillFlow> list = billFlowRepository.getBillFlowList("20180620", BillTypeEnums.Grand.getCode());
            notifyCharge(list);
    }
    private void notifyCharge(List<BillFlow> billFlowList) {
        for (BillFlow billFlow : billFlowList) {
            executorService.execute(
                    () -> send(new BillChargeInfo(billFlow.getSerialNo(),
                            billFlow.getFee().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString()))
            );
        }
        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void send(BillChargeInfo info) {
        Map<String, Object> map = Maps.newHashMap();
        String msgKey = UUIDUtil.getID();
        map.put("data", JSONObject.toJSONString(info));
        log.info("send charge notify key="+msgKey+", data="+ map);
        ssoProducer.send(map, msgKey, notifyTag);
    }


    @Test
    public void test() {
        Long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
          if(i == 0)
              System.out.println(new BigDecimal("1.1").multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
        }
        System.out.println("1___" + (System.nanoTime() - start) );
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            if(i == 0)
                System.out.println( new BigDecimal("1.1").multiply(new BigDecimal(100)).toBigInteger());
        }
        System.out.println("2___" + (System.nanoTime() - start) );

        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            if(i == 0)
                System.out.println( new BigDecimal("1.1").multiply(new BigDecimal(100)).toString());
        }
        System.out.println("2___" + (System.nanoTime() - start) );
    }
}
