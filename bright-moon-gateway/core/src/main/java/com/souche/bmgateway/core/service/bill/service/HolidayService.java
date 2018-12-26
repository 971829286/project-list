package com.souche.bmgateway.core.service.bill.service;

/**
 *
 * @author huang_sq
 * @date 2018/9/10
 * 节假日service
 */
public interface HolidayService {

    /**
     * 下一天是不是节假日
     * @return boolean
     */
    boolean isNextDayHoliday(String businessDate);

}
