package com.souche.bmgateway.core.repo;

/**
 * Created by huang_sq on 2018/9/10.
 */
public interface HolidayRepository {

    /**
     * 返回某年某月的节假日
     *
     * @param year  y
     * @param month M
     * @return 1, 2, 3, 4...
     */
    String getHolidayDaysByYearAndMonth(Integer year, Integer month);


}
