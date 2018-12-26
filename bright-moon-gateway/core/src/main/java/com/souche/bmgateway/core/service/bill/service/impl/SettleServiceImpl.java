package com.souche.bmgateway.core.service.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.domain.BillSummary;
import com.souche.bmgateway.core.domain.DeductionRecord;
import com.souche.bmgateway.core.domain.SettleTranSumInfo;
import com.souche.bmgateway.core.domain.TaskInfo;
import com.souche.bmgateway.core.dto.response.SettleResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.PubFlagEnums;
import com.souche.bmgateway.core.enums.SettleTypeEnums;
import com.souche.bmgateway.core.enums.StatusEnums;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.repo.DeductionRecordRepository;
import com.souche.bmgateway.core.service.bill.service.PayRequestService;
import com.souche.bmgateway.core.service.bill.service.SettleService;
import com.souche.bmgateway.core.util.DingTalkClient;
import com.souche.bmgateway.core.util.UUIDUtil;
import com.souche.optimus.common.util.Exceptions;
import com.souche.tfb.fin.settle.sdk.bean.SettleTranDetail;
import com.souche.tfb.fin.settle.sdk.common.PayerBankEnum;
import com.souche.tfb.fin.settle.sdk.facade.SettleTranServiceFacade;
import com.souche.tfb.fin.settle.sdk.request.SettleTranRequest;
import com.souche.tfb.fin.settle.sdk.response.SettleTranResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.souche.bmgateway.core.util.DingTalkClient.formatMessage;
import static com.souche.bmgateway.core.util.DingTalkClient.formatMessageForAccount;

/**
 * @author wangkanlong
 */
@Slf4j(topic = "service")
@Service
public class SettleServiceImpl implements SettleService {

    private static final Integer BATCH_NUM = 100;
    private final static String FAIL_REASON = "店铺关联账户信息不存在";
    private final static String ILLEGAL_ACCOUNT_NO = "收款账号异常 , 请联系通联核对";
    /**
     * demo :  8888888008448399
     */
    private final static String ACCOUNT_PATTERN = "^8888888\\d{9}$";
    @Resource
    private SettleTranServiceFacade settleTranServiceFacade;
    @Resource
    private BillSummaryRepository billSummaryRepository;
    @Resource
    private DeductionRecordRepository deductionRecordRepository;
    @Resource
    private PayRequestService payRequestService;

    @Override
    public SettleResponse settle(String date, String type, TaskInfo taskInfo) {
        //获取上次任务执行到哪个位置
        Integer lastTaskPosition = payRequestService.getLastJobBillSummaryPosition(date);
        SettleTranSumInfo sumInfo = billSummaryRepository.getSettleTranSumInfo(lastTaskPosition, type);
        if (sumInfo == null) {
            log.error("手续费代发没有查询到数据date={}, type={}", date, type);
            throw Exceptions.fault(ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(), "手续费代发异常，没有查询到可代发数据");
        }

        if (Objects.equals(sumInfo.getFailNum(),0) && Objects.equals(sumInfo.getInitailNum(),0)) {
            log.error("手续费代发没有查询到数据date={}, type={}", date, type);
            throw Exceptions.fault(ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(), "手续费代发异常，没有查询到可代发数据");
        }

        Boolean result = batchSettleTran(lastTaskPosition, date, type, taskInfo);

        return SettleResponse.createResponse(result,
                ErrorCodeEnums.SYSTEM_ERROR.getCode(), "手续费代发异常, 执行手续费代发出错");
    }

    private Boolean batchSettleTran(Integer lastTaskPosition, String date, String type, TaskInfo taskInfo) {

        Integer position = 0;
        //这一次循环查询到的listSize
        Integer gotListSize;
        Boolean isSuccess;
        Integer requestNum = BATCH_NUM + 1;
        //是否还有下一页查询
        boolean nextQueryFlag = true;
        do {
            // 2018/9/10 改动:不再以日期为查询条件
            List<BillSummary> list = billSummaryRepository.getBillSummaryByType(lastTaskPosition, type, position, requestNum,
                    StatusEnums.FAIL.getCode(),
                    StatusEnums.INITIAL.getCode());
            gotListSize = list.size();
            if (gotListSize.equals(requestNum)) {
                position = list.get(gotListSize - 2).getId();
                list.remove(gotListSize - 1);
            } else {
                //没有下一批数据了
                nextQueryFlag = false;
            }
            //手续费代发处理
            List<Integer> idList = new ArrayList<>();
            String batchId = UUIDUtil.getTimeID();
            SettleTranRequest request = buildAndSaveTranRequest(list, taskInfo, date, idList, batchId);
            Integer requestSize = request.getDataList().size();
            SettleTranResponse response = tran(request);
            isSuccess = response.getSuccess() ;
            if (isSuccess) {
                //总条数与实际发送 可能是金额为0 可能是账户信息不全
                log.info("本批次手续费代发涉及总条数 size={}, 实际代发条数size={}", list.size(), requestSize);
            }
            //结果处理
            resultHandle(response, idList, batchId);
            /*判断是否存在下一页*/
        } while (nextQueryFlag);
        return isSuccess;
    }

    private SettleTranResponse tran(SettleTranRequest request) {
        SettleTranResponse response;
        List<String> orderNoList = request.getDataList().stream()
                .map(SettleTranDetail::getOrderNo).collect(Collectors.toList());
        try {
            log.info("请求出款交易，请求参数：{}", orderNoList);
            long beginTime = System.currentTimeMillis();
            response = settleTranServiceFacade.tran(request);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("请求出款交易， 耗时:{} (ms); 响应 结果:{}", consumeTime, JSONObject.toJSONString(response));
            if (!response.getSuccess()) {
                DingTalkClient.sendMsg(formatMessage(request.getBatchId(), request.getTotal(), new Date(), response.getMsg()));
            }
        } catch (Exception e) {
            log.error("请求出款交易异常：请求参数：{}, e: {}", orderNoList, e);
            DingTalkClient.sendMsg(formatMessage(request.getBatchId(), request.getTotal(), new Date(), "调用出款接口失败"));
            response = new SettleTranResponse();
            response.setSuccess(false);
            response.setCode(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode());
            response.setMsg(ErrorCodeEnums.MANAGER_SERVICE_ERROR.getMessage());
        }
        return response;
    }

    private void resultHandle(SettleTranResponse response, List<Integer> idList, String batchId) {
        Integer status = response.getSuccess() ? StatusEnums.HANDING.getCode() : StatusEnums.FAIL.getCode();
        billSummaryRepository.updateBillSummaryStatus(idList, status);
        if (!response.getSuccess()) {
            deductionRecordRepository.updateStatusByBatchId(batchId, null, status);
        }
    }

    private SettleTranDetail buildSettleDetail(TaskInfo taskInfo, BillSummary summary, String batchId) {
        SettleTranDetail detail = new SettleTranDetail();
        detail.setOrderNo(batchId);
        //对公
        detail.setPubFlag(PubFlagEnums.PUB.getCode());
        detail.setPayeeAccount(summary.getAccountNo());
        detail.setPayeeName(summary.getAccountName());
        detail.setPayeeBankCode(summary.getBankCode());
        detail.setPayeeBankName(summary.getBankName());
        detail.setCurrency(summary.getCurrency());
        detail.setAmount(summary.getTotalFee());
        detail.setPayerAccount(taskInfo.getPayerAccount());
        detail.setPayerName(taskInfo.getPayerName());
        detail.setRemark(summary.getRemark());
        detail.setRemark2(taskInfo.getRemark());
        //2018/9/11 wkl need
        detail.setPayerBank(PayerBankEnum.SPDB.getCode());
        //无需审核
        detail.setIsAudit(false);
        //2018/9/11 wkl need
        detail.setPayerBank(PayerBankEnum.SPDB.getCode());
        return detail;
    }

    private DeductionRecord buildDeductionRecord(SettleTranDetail detail, BillSummary summary, String batchId, String date) {
        DeductionRecord record = new DeductionRecord();
        record.setBusinessDate(date);
        record.setRequestNo(batchId);
        record.setOrderNo(detail.getOrderNo());
        record.setCurrency(detail.getCurrency());
        record.setPayeeAccount(detail.getPayeeAccount());
        record.setPayeeName(detail.getPayeeName());
        record.setPayerAccount(detail.getPayerAccount());
        record.setPayerName(detail.getPayerName());
        record.setSummaryId(summary.getId());
        record.setTradeAmount(detail.getAmount());
        record.setTradeStatus(StatusEnums.HANDING.getCode());
        record.setTradeType(SettleTypeEnums.PAY.getCode().toString());
        record.setShopCode(summary.getShopCode());
        record.setShopName(summary.getShopName());
        return record;
    }


    private SettleTranRequest buildAndSaveTranRequest(List<BillSummary> list, TaskInfo taskInfo, String date
            , List<Integer> idList, String batchId) {
        SettleTranRequest request = new SettleTranRequest();
        request.setBatchId(batchId);
        request.setPayerAccount(taskInfo.getPayerAccount());
        request.setPayerName(taskInfo.getPayerName());
        //1-付款
        request.setTradeType(SettleTypeEnums.PAY.getCode());
        request.setRemark(taskInfo.getTaskCode());

        List<SettleTranDetail> dataList = new ArrayList<>();
        List<DeductionRecord> recordList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(0);
        Integer i = 0;
        for (BillSummary summary : list) {
            if (summary.getTotalAmount() == null || (summary.getTotalFee().compareTo(BigDecimal.ZERO) <= 0)) {
                continue;
            }
            if (emptyAccountNo(summary)) {
                continue;
            }
            if (illegalAccount(summary)) {
                continue;
            }
            SettleTranDetail detail = buildSettleDetail(taskInfo, summary, batchId + "-" + i++);
            DeductionRecord record = buildDeductionRecord(detail, summary, batchId, date);
            dataList.add(detail);
            recordList.add(record);
            idList.add(summary.getId());
            totalAmount = totalAmount.add(detail.getAmount());
        }

        if (CollectionUtils.isEmpty(dataList) || CollectionUtils.isEmpty(recordList)) {
            log.error("手续费代发异常，查询后没有可代发数据");
            throw Exceptions.fault(ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(), "手续费代发异常，没有可代发数据");
        }
        request.setDataList(dataList);
        request.setTotal(totalAmount);
        deductionRecordRepository.save(recordList);
        return request;
    }

    private boolean illegalAccount(BillSummary summary) {
        if (!Pattern.matches(ACCOUNT_PATTERN, summary.getAccountNo())) {
            log.error("手续费代发异常，收款账号异常 , 联系通联核对 shopCode={}, accountNo={} 跳过该商户",
                    summary.getShopCode(), summary.getAccountNo());
            if (!StatusEnums.FAIL.getCode().equals(summary.getTradeStatus())) {
                billSummaryRepository.updateBillSummaryStatus(summary.getId(), StatusEnums.FAIL.getCode());
            }
            DingTalkClient.sendMsg(formatMessageForAccount(summary, new Date(), ILLEGAL_ACCOUNT_NO, summary.getAccountNo()));
            return true;
        }
        return false;
    }

    private boolean emptyAccountNo(BillSummary summary) {
        if (!StringUtils.hasText(summary.getAccountName()) || !StringUtils.hasText(summary.getAccountNo())) {
            log.error("手续费代发异常，账户信息不存在 shop_code={}, biz_type={} 跳过该商户",
                    summary.getShopCode(), summary.getBizType());
            if (!StatusEnums.FAIL.getCode().equals(summary.getTradeStatus())) {
                billSummaryRepository.updateBillSummaryStatus(summary.getId(), StatusEnums.FAIL.getCode());
            }
            DingTalkClient.sendMsg(formatMessage(summary, new Date(), FAIL_REASON));
            return true;
        }
        return false;
    }

}
