package com.souche.bmgateway.core.job;

import com.souche.bmgateway.core.dto.response.SettleResponse;
import com.souche.bmgateway.core.enums.*;
import com.souche.bmgateway.core.repo.TaskInfoRepository;
import com.souche.bmgateway.core.service.bill.service.PayRequestService;
import com.souche.bmgateway.core.service.bill.service.SettleService;
import com.souche.bmgateway.core.domain.TaskInfo;
import com.souche.bmgateway.core.exception.CommonDefinedException;
import com.souche.tfb.fin.settle.sdk.bean.SettleTranDetail;
import com.souche.tfb.fin.settle.sdk.facade.SettleTranServiceFacade;
import com.souche.tfb.fin.settle.sdk.request.SettleTranRequest;
import com.souche.tfb.fin.settle.sdk.response.SettleTranResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class CommissionPaymentJobTest {

    private static final Logger log = LoggerFactory.getLogger(CommissionPaymentJobTest.class);

    private static final String taskCode = "GrandPayment";

    @Autowired
    PayRequestService payRequestService;

    @Autowired
    TaskInfoRepository taskInfoRepository;

    @Autowired
    SettleService settleService;

    @Autowired
    SettleTranServiceFacade settleTranServiceFacade;

    @Test
    public void jobTest() {
        TaskInfo taskInfo = taskInfoRepository.getTaskInfo(taskCode);
        if(taskInfo == null || "0".equals(taskInfo.getIsLive())) {
            log.error("任务未配置或者任务不是启用状态", taskCode);
            throw CommonDefinedException.TASK_CONFIG_ERROR;
        }

        String businessDate = "20180620";
        try {
            payRequestService.getVersion(taskCode, businessDate);
        } catch (Exception e) {
            log.error("手续费代发异常taskCode={}, msg={}", taskCode, e.getMessage());
            return;
        }
        log.info("获取执行权限成功");
        try {
            //成功结果以消息回调为准
            SettleResponse response = settleService.settle(businessDate, BillTypeEnums.Grand.getCode(), taskInfo);
            Integer stauts = response.getSuccess() ? StatusEnums.HANDING.getCode() : StatusEnums.FAIL.getCode();
            log.info("手续费代发结果, response={}", response);
            payRequestService.updateStatus(taskCode, businessDate, stauts);
        } catch (Exception e) {
            log.error("手续费代发异常taskCode={}, e={}", taskCode, e);
            payRequestService.updateStatus(taskCode, businessDate, StatusEnums.FAIL.getCode());
        }
    }

    @Test
    public void tran() {
        SettleTranRequest request = new SettleTranRequest();
        List<SettleTranDetail> dataList = new ArrayList<>();
        //对公
        for(int i = 0 ; i < 500; i ++) {
            SettleTranDetail detail = new SettleTranDetail();
            detail.setOrderNo("wkldelete-1234567891"+i);
            detail.setPubFlag(PubFlagEnums.PUB.getCode());
            detail.setPayeeAccount("8888888457986160");
            detail.setPayeeName("浙江大搜车融资租赁有限公司");
            detail.setPayeeBankCode("323331000001");
            detail.setPayeeBankName("浙江网商银行股份有限公司");
            detail.setCurrency("RMB");
            detail.setAmount(new BigDecimal(0.01));
            detail.setPayerAccount("95200155300001827");
            detail.setPayerName("浙江网商银行股份有限公司");
            detail.setRemark("手续费代发");
            detail.setRemark2("手续费代发");
            dataList.add(detail);
        }
        request.setDataList(dataList);
        request.setTotal(new BigDecimal(0.01));
        request.setBatchId("wkltest-delete");
        request.setPayerAccount("95200155300001827");
        request.setPayerName("浙江大搜车融资租赁有限公司");
        //1-付款
        request.setTradeType(SettleTypeEnums.PAY.getCode());
        request.setRemark("手续费代发");

        SettleTranResponse response = settleTranServiceFacade.tran(request);
    }
}
