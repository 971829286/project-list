package com.souche.bmgateway.core.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.souche.bmgateway.core.domain.TaskInfo;
import com.souche.bmgateway.core.dto.response.SettleResponse;
import com.souche.bmgateway.core.enums.BillTypeEnums;
import com.souche.bmgateway.core.enums.StatusEnums;
import com.souche.bmgateway.core.repo.TaskInfoRepository;
import com.souche.bmgateway.core.service.bill.service.HolidayService;
import com.souche.bmgateway.core.service.bill.service.PayRequestService;
import com.souche.bmgateway.core.service.bill.service.SettleService;
import com.souche.bmgateway.core.util.CheckUtil;
import com.souche.bmgateway.core.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 手续费代付job
 * @author wkl
 */
@Slf4j(topic = "job")
@Component
public class CommissionPaymentJob implements SimpleJob {

    private static final String TASK_CODE = "GrandPayment";

    private static final String DISABLE = "0";

    @Resource
    private PayRequestService payRequestService;

    @Resource
    private TaskInfoRepository taskInfoRepository;

    @Resource
    private SettleService settleService;

    @Resource
    private HolidayService holidayService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("手续费代付job开始, context:{}", shardingContext);

        TaskInfo taskInfo = taskInfoRepository.getTaskInfo(TASK_CODE);
        if(taskInfo == null || DISABLE.equals(taskInfo.getIsLive())) {
            log.error("任务未配置或者任务不是启用状态", TASK_CODE);
            return;
        }
        String businessDate = shardingContext.getJobParameter();
        Boolean hasText = StringUtils.hasText(businessDate);
        if(!hasText) {
            //普通情况 不传入参数 生成昨日businessDate
            businessDate = CommonUtil.getDate(-1);
        }

        if (hasText && !CheckUtil.isValidDate(businessDate)) {
            log.error("手续费代发异常, 传入的日期格式不正确. businessDate={}", businessDate);
            return;
        }

        //如果今天是节假日 不代发手续费
        String yesterday = CommonUtil.getDate(-1);
        if (holidayService.isNextDayHoliday(yesterday)) {
            return;
        }

        try {
            payRequestService.getVersion(TASK_CODE, businessDate);
        } catch (Exception e) {
            log.error("手续费代发异常TASK_CODE={}, msg={}", TASK_CODE, e.getMessage());
            return;
        }

        log.info("手续费代发获取版本号成功");

        try {
            SettleResponse response = settleService.settle(businessDate, BillTypeEnums.Grand.getCode(), taskInfo);
            log.info("手续费代发结果, response={}", response);
            Integer status = response.getSuccess() ? StatusEnums.HANDING.getCode() : StatusEnums.FAIL.getCode();
            payRequestService.updateStatus(TASK_CODE, businessDate, status);
        } catch (Exception e) {
            log.error("手续费代发异常TASK_CODE={}, e={}", TASK_CODE, e);
            payRequestService.updateStatus(TASK_CODE, businessDate, StatusEnums.FAIL.getCode());
        }
    }


}
