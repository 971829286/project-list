package com.souche.bmgateway.core.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.souche.bmgateway.core.service.bill.service.AllinPayBillHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaojian
 * @date 18/7/13
 */
@Slf4j(topic = "job")
@Component
public class AllinPayBillHandleJob implements SimpleJob {


    @Autowired
    private AllinPayBillHandleService allinPayBillHandleService;


    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("开始通联对账单下载解析入库任务,jobParameter:{}", shardingContext.getJobParameter());
        allinPayBillHandleService.handle(shardingContext.getJobParameter());
        log.info("通联对账单下载解析入库任务执行完成,jobParameter:{}", shardingContext.getJobParameter());
    }
}
