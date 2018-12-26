package com.souche.bmgateway.core.service.bill.service;

import com.souche.bmgateway.core.domain.DeductionRecord;

import java.util.List;

public interface PayRequestService {

    /**
     * 获取请求版本号,防止并发
     * @return
     */
    Integer getVersion(String taskCode, String businessDate);

    /**
     * 更新每次job调用请求的状态
     * @return
     */
    boolean updateStatus(String taskCode, String businessDate, Integer status);


    /**
     * 获取上次任务发送的billSummary位置  (现在不是每日发送)
     * @return lastPosition
     */
    Integer getLastJobBillSummaryPosition(String businessDate);
}
