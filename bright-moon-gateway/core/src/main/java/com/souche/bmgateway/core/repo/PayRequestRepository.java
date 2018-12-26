package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.PayRequest;

public interface PayRequestRepository {

    int insertSelective(PayRequest record);

    int updateVersionByID(PayRequest record);

    int updateStatusBytaskCode(String taskCode, String businessDate, Integer status);

    PayRequest selectByTaskCode(String taskCode, String businessDate);

    int updateBytaskCode(PayRequest record);

    /**
     * 上一批任务发送时间
     */
    String getLastJobBusinessDate(String businessDate);
}
