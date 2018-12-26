package com.souche.bmgateway.core.service.bill.service;

/**
 * 处理通联对账单
 *
 * @author zhaojian
 * @date 18/7/13
 */
public interface AllinPayBillHandleService {

    /**
     * @param bankDate 清算日期(yyyyMMdd)
     */
    void handle(String bankDate);
}
