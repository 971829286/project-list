package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.DeductionRecord;

import java.util.List;

public interface DeductionRecordRepository {

    int save(List<DeductionRecord> list);

    int updateStatusByBatchId(String requestNo, String orderNo, Integer tradeStatus);

    DeductionRecord selectByOrderNo(String requestNo, String orderNo);

    int updateByPrimaryKey(DeductionRecord record);

}
