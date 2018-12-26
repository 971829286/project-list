package com.souche.bmgateway.core.service.bill.service.impl;

import com.souche.bmgateway.core.repo.HolidayRepository;
import com.souche.bmgateway.core.service.bill.service.HolidayService;
import com.souche.bmgateway.core.util.DingTalkClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author huang_sq
 * @date 2018/9/10
 */
@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {

    @Resource
    private HolidayRepository holidayRepository;

    /**
     * 下一天是不是节假日
     *
     * @return boolean
     */
    @Override
    public boolean isNextDayHoliday(String businessDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(businessDate);
        } catch (ParseException e) {
            log.error("手续费代发异常, 传入的日期格式不正确. string-->date error\n" +
                    "businessDate = {}", businessDate);
        }
        Calendar calendar = Calendar.getInstance();
        Assert.notNull(date, "手续费代发 date不能为空");
        calendar.setTime(date);
        //这里判断的是businessDate 的 后一日(今天) 是不是节假日
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.get(Calendar.YEAR);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        //这里看的是今天是不是节假日 是对账单的后一天
        int day = calendar.get(Calendar.DATE);

        //查询那年那月的节假日信息
        String holidays = holidayRepository.getHolidayDaysByYearAndMonth(year, month);
        if (StringUtils.isEmpty(holidays)) {
            log.error("手续费代发,节假日信息缺失,请配置节假日t_holiday");
            DingTalkClient.sendMsg("手续费代发,节假日信息缺失,请配置节假日t_holiday");
            return false;
        }

        String[] holidayArr = StringUtils.split(holidays,",");
        List<String> holidayList = Arrays.asList(holidayArr);
        if (holidayList.contains(String.valueOf(day))) {
            log.info("手续费节假日代发暂停YEAR={} , MONTH={} , DAY={}",
                    year, month, day);
            return true;
        }

        return false;
    }

}
