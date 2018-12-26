package com.souche.bmgateway.core.service.bill.service.impl;

import com.souche.bmgateway.core.repo.BillSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.atomic.AtomicInteger;

public class PayResultStatsService{

    //目前废弃，还是在每次消息回调的时候查询一次统计
//    @Autowired
//    BillSummaryRepository billSummaryRepository;
//
//    //总条数
//    public static int Total_Num = 0;
//
//    //实际代发条数
//    public static int Pay_Num = 0;
//
//    public static AtomicInteger Success_Num = new AtomicInteger(0);
//
//    public static AtomicInteger Fail_Num = new AtomicInteger(0);
//
//    //mbs回调条数
//    public static AtomicInteger CallBack_Num = new AtomicInteger(0);
//
//    //账户配置错误条数
//    public static int AccountError_Num = 0;

}
