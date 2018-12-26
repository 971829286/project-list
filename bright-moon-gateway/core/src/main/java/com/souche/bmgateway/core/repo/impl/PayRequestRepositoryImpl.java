package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.PayRequsetMapper;
import com.souche.bmgateway.core.domain.PayRequest;
import com.souche.bmgateway.core.repo.PayRequestRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author  wangkanlong
 */
@Repository
public class PayRequestRepositoryImpl implements PayRequestRepository {

    @Resource
    private PayRequsetMapper payRequsetMapper;

    @Override
    public int insertSelective(PayRequest record) {
        return payRequsetMapper.insertSelective(record);
    }

    @Override
    public int updateVersionByID(PayRequest record) {
        return payRequsetMapper.updateVersionByID(record);
    }

    @Override
    public int updateStatusBytaskCode(String taskCode, String businessDate, Integer status) {
        return payRequsetMapper.updateStatusBytaskCode(taskCode, businessDate, status);
    }

    @Override
    public PayRequest selectByTaskCode(String taskCode, String businessDate) {
        return payRequsetMapper.selectByTaskCode(taskCode, businessDate);
    }

    @Override
    public int updateBytaskCode(PayRequest record) {
        return payRequsetMapper.updateByTaskcode(record);
    }

    /**
     * 上一批任务发送时间
     */
    @Override
    public String getLastJobBusinessDate(String businessDate) {
        return payRequsetMapper.getLastJobBusinessDate(businessDate);
    }
}
