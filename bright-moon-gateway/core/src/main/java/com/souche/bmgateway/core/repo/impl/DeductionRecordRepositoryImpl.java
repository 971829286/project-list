package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.DeductionRecordMapper;
import com.souche.bmgateway.core.domain.DeductionRecord;
import com.souche.bmgateway.core.repo.DeductionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DeductionRecordRepositoryImpl implements DeductionRecordRepository {

    @Autowired
    DeductionRecordMapper deductionRecordMapper;

    @Override
    public int save(List<DeductionRecord> list) {
        return deductionRecordMapper.batchInsertRecords(list);
    }

    @Override
    public int updateStatusByBatchId(String requestNo, String orderNo, Integer tradeStatus) {
        return deductionRecordMapper.updateStatusByBatchId(requestNo, orderNo, tradeStatus);
    }

    @Override
    public DeductionRecord selectByOrderNo(String requestNo, String orderNo) {
        return deductionRecordMapper.selectByOrderNo(requestNo, orderNo);
    }

    @Override
    public int updateByPrimaryKey(DeductionRecord record) {
        return deductionRecordMapper.updateByPrimaryKey(record);
    }
}
