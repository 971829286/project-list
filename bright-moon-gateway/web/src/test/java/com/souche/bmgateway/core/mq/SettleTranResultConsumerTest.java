package com.souche.bmgateway.core.mq;

import com.souche.bmgateway.core.domain.DeductionRecord;
import com.souche.bmgateway.core.domain.PayRequest;
import com.souche.bmgateway.core.domain.SettleTranSumInfo;
import com.souche.bmgateway.core.enums.StatusEnums;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.repo.DeductionRecordRepository;
import com.souche.bmgateway.core.repo.impl.PayRequestRepositoryImpl;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.tfb.fin.settle.sdk.bean.SettleTranNotifyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @Author: huangbin
 * @Description: 接收分账异步通知消息测试
 * @Date: Created in 2018/07/14
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class SettleTranResultConsumerTest {

    private static final Logger logger = LoggerFactory.getLogger(SettleTranResultConsumerTest.class);

    private static final String SHARE_TAG = OptimusConfig.getValue("ons.notify.tag");

    @Resource
    private BillSummaryRepository billSummaryRepository;

    @Resource
    private DeductionRecordRepository deductionRecordRepository;

    @Resource
    private PayRequestRepositoryImpl payRequestRepository;

    private static final String BIZ_TYPE = "Grand";

    private static final String TASK_CODE = "GrandPayment";

    private static final String FAILED = "FAILED";

    private static final String SUCCESS = "SUCCESS";

    @Test
    public void onRecived() {
        String requestNo = "bQukyJEOGu1539324940323";
        String orderNo = "bQukyJEOGu1539324940323-5";
        SettleTranNotifyDTO settleTranNotify = new SettleTranNotifyDTO();
        settleTranNotify.setBatchId(requestNo);
        settleTranNotify.setOrderNo(orderNo);
        settleTranNotify.setStatus("2");
        DeductionRecord record = deductionRecordRepository.selectByOrderNo(requestNo, orderNo);
        doHandle(record, settleTranNotify);
    }

    @SuppressWarnings("Duplicates")
    private void doHandle(DeductionRecord record, SettleTranNotifyDTO settleTranNotify) {
        Integer status;
        if(FAILED.equals(settleTranNotify.getStatus())) {
            status = StatusEnums.FAIL.getCode();
            record.setRemark(settleTranNotify.getRemark());
//            DingTalkClient.sendMsg(formatMessage(record, settleTranNotify.getRemark()));
        } else {
            status = StatusEnums.SUCCESS.getCode();
        }
        record.setTradeStatus(status);
        deductionRecordRepository.updateByPrimaryKey(record);
        billSummaryRepository.updateBillSummaryStatus(record.getSummaryId(), status);
        // 2018/9/11 根据批次号做统计(不再根据日期)
        SettleTranSumInfo sumInfo = billSummaryRepository.getSettleTranSumInfoByDeductionDate(record.getBusinessDate(), BIZ_TYPE);

        if(sumInfo != null && sumInfo.getHandingNum() == 0) {
            Integer taskStatus = sumInfo.getFailNum() > 0 ? StatusEnums.FAIL.getCode() :
                    StatusEnums.SUCCESS.getCode();
            //虽然summary中的businessDate不一定就是处理日期 但是 payRequest和deduction他们的businessDate是一致的
            PayRequest payRecord = buildPayRecord(record.getBusinessDate(), sumInfo, taskStatus);
            payRequestRepository.updateBytaskCode(payRecord);
        }
    }

    @SuppressWarnings("Duplicates")
    private PayRequest buildPayRecord(String businessDate, SettleTranSumInfo sumInfo, Integer taskStatus) {
        PayRequest payRecord = new PayRequest();
        payRecord.setBusinessDate(businessDate);
        payRecord.setTaskCode(TASK_CODE);
        payRecord.setTradeStatus(taskStatus);
        payRecord.setTotalNum(sumInfo.getTotalNum());
        payRecord.setFailNum(sumInfo.getFailNum());
        payRecord.setSuccessNum(sumInfo.getSuccessNum());
        return payRecord;
    }
}
