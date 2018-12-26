package com.souche.bmgateway.core.service.bill.service.impl;

import com.souche.bmgateway.core.domain.PayRequest;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.StatusEnums;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.repo.PayRequestRepository;
import com.souche.bmgateway.core.service.bill.service.PayRequestService;
import com.souche.optimus.common.util.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wkl
 */
@Slf4j(topic = "service")
@Service
public class PayRequestServiceImpl implements PayRequestService {

    @Resource
    private PayRequestRepository payRequestRepository;

    @Resource
    private BillSummaryRepository billSummaryRepository;

    @Override
    public Integer getVersion(String taskCode, String businessDate) {

        PayRequest payRequest;
        payRequest = payRequestRepository.selectByTaskCode(taskCode, businessDate);
        if (payRequest == null) {
            PayRequest newPayRequest = new PayRequest();
            newPayRequest.setBusinessDate(businessDate);
            newPayRequest.setTaskCode(taskCode);
            newPayRequest.setTradeStatus(StatusEnums.HANDING.getCode());
            newPayRequest.setVersion(1);
            payRequestRepository.insertSelective(newPayRequest);
            return 1;
        }
        //已经有任务在执行或者已完成
        if (StatusEnums.HANDING.getCode().equals(payRequest.getTradeStatus())
                || StatusEnums.SUCCESS.getCode().equals(payRequest.getTradeStatus())) {
            log.error("执行数据费代发异常——已经有任务正在执行中");
            throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "手续费代发异常，已经有任务正在执行中");
        }
        payRequest.setTradeStatus(StatusEnums.HANDING.getCode());

        //说明有其他线程在执行这个任务，防止并发，
        if (payRequestRepository.updateVersionByID(payRequest) < 1) {
            log.error("执行数据费代发异常——已经有任务正在执行中");
            throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "手续费代发异常，已经有任务正在执行中");
        }

        return payRequest.getVersion() + 1;
    }

    @Override
    public boolean updateStatus(String taskCode, String businessDate, Integer status) {
        Assert.hasText(taskCode, "更新条件任务code不能为空");
        Assert.hasText(businessDate, "更新条件业务日期不能为空");
        Assert.notNull(status, "更新状态不能为空");
        return payRequestRepository.updateStatusBytaskCode(taskCode, businessDate, status) > 0;
    }

    /**
     * 获取上次任务发送的billSummary位置  (现在不是每日发送)
     *
     * @return lastPosition
     */
    @Override
    public Integer getLastJobBillSummaryPosition(String businessDate) {
        String lastJobBusinessDate = payRequestRepository.getLastJobBusinessDate(businessDate);
        return billSummaryRepository.getLastTaskPosition(lastJobBusinessDate);
    }
}
