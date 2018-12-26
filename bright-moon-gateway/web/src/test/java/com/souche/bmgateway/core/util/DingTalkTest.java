package com.souche.bmgateway.core.util;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.domain.DeductionRecord;
import com.souche.bmgateway.core.repo.DeductionRecordRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

import static com.souche.bmgateway.core.util.DingTalkClient.formatMessage;

public class DingTalkTest extends BaseTest {

    @Autowired
    DeductionRecordRepository deductionRecordRepository;
    @Test
    public void test() {
        DingTalkClient.sendMsg(formatMessage("123", new BigDecimal(123), new Date(), "月月鸟"));
        DeductionRecord record = deductionRecordRepository.selectByOrderNo("123", "123");
        DingTalkClient.sendMsg(formatMessage(record, "月月鸟"));
        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
