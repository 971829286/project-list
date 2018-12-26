package com.souche.bmgateway.core.service.bill;

import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.dao.BillSummaryMapper;
import com.souche.bmgateway.core.enums.BillTypeEnums;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class BillServiceTest {

    @Autowired
    BillSummaryMapper billSummaryMapper;

    @Autowired
    BillSummaryRepository billSummaryRepository;

    @Test
    public void test() {
        billSummaryRepository.queryAndSaveBillSummary("20180620", BillTypeEnums.Grand.getCode());
    }

    @Test
    public void test1()
    {
        System.out.println(12);
    }
}
