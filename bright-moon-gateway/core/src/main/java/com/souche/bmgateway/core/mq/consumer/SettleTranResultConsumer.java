package com.souche.bmgateway.core.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.souche.bmgateway.core.domain.DeductionRecord;
import com.souche.bmgateway.core.domain.PayRequest;
import com.souche.bmgateway.core.domain.SettleTranSumInfo;
import com.souche.bmgateway.core.enums.StatusEnums;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.repo.DeductionRecordRepository;
import com.souche.bmgateway.core.repo.impl.PayRequestRepositoryImpl;
import com.souche.bmgateway.core.util.DingTalkClient;
import com.souche.optimus.mq.ConsumeResult;
import com.souche.optimus.mq.MQConsumer;
import com.souche.tfb.fin.settle.sdk.bean.SettleTranNotifyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

import static com.souche.bmgateway.core.util.DingTalkClient.formatMessage;

/**
 * @author wangkanlong
 */
@Slf4j
public class SettleTranResultConsumer implements MQConsumer {

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

    @Override
    public ConsumeResult onRecived(Map<String, Object> map) {
        log.info("接收到的mq消息,msgId={}, src={}", map.get("_msgId"), map);
        String data = map.get("data") + "";
        SettleTranNotifyDTO settleTranNotify;
        try {
            settleTranNotify = JSON.parseObject(data, SettleTranNotifyDTO.class);
        } catch (Exception e) {
            log.error("解析json串失败,data={}", data);
            return ConsumeResult.CommitMessage;
        }
        if (!(FAILED.equals(settleTranNotify.getStatus()) ||SUCCESS.equals(settleTranNotify.getStatus()))) {
            log.error("接收到的订单状态非法，data={}", JSON.toJSONString(settleTranNotify));
            return ConsumeResult.CommitMessage;
        }
        String requestNo = settleTranNotify.getBatchId();
        String orderNo = settleTranNotify.getOrderNo();
        if (StringUtils.isEmpty(requestNo) || StringUtils.isEmpty(orderNo)) {
            log.error("接收到的订单数据不正确，data={}", JSON.toJSONString(settleTranNotify));
            return ConsumeResult.CommitMessage;
        }

        DeductionRecord record = deductionRecordRepository.selectByOrderNo(requestNo, orderNo);
        if(record == null) {
            return ConsumeResult.CommitMessage;
        }

        if(!StatusEnums.HANDING.getCode().equals(record.getTradeStatus())) {
            log.error("接收到的订单数据重复，data={}", JSON.toJSONString(settleTranNotify));
            return ConsumeResult.CommitMessage;
        }

        doHandle(record, settleTranNotify);
        return ConsumeResult.CommitMessage;
    }

    private void doHandle(DeductionRecord record, SettleTranNotifyDTO settleTranNotify) {
        Integer status;
        if(FAILED.equals(settleTranNotify.getStatus())) {
            status = StatusEnums.FAIL.getCode();
            record.setRemark(settleTranNotify.getRemark());
            DingTalkClient.sendMsg(formatMessage(record, settleTranNotify.getRemark()));
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
