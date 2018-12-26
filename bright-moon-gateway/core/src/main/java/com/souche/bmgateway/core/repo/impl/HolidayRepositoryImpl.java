package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.HolidayMapper;
import com.souche.bmgateway.core.repo.HolidayRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author huang_sq
 * @date 2018/9/10
 */
@Repository
public class HolidayRepositoryImpl implements HolidayRepository {

    @Resource
    private HolidayMapper holidayMapper;

    /**
     * 返回某年某月的节假日
     *
     * @param year  y
     * @param month M
     * @return 1, 2, 3, 4...
     */
    @Override
    public String getHolidayDaysByYearAndMonth(Integer year, Integer month) {
        return holidayMapper.getHolidayDaysByYearAndMonth(year, month);
    }
}
