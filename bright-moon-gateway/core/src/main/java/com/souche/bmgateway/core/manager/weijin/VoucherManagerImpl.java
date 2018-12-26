package com.souche.bmgateway.core.manager.weijin;

import com.alibaba.fastjson.JSON;
import com.netfinworks.common.lang.StringUtil;
import com.netfinworks.voucher.service.facade.VoucherFacade;
import com.netfinworks.voucher.service.facade.domain.voucher.ControlVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.PaymentVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.RefundVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.TradeVoucherInfo;
import com.netfinworks.voucher.service.facade.request.BatchRecordingRequest;
import com.netfinworks.voucher.service.facade.request.RecordingRequest;
import com.netfinworks.voucher.service.facade.response.BatchRecordingResponse;
import com.netfinworks.voucher.service.facade.response.RecordingResponse;
import com.netfinworks.voucher.service.facade.response.VoucherQueryResponse;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zs.
 *         Created on 18/11/15.
 */

@Service("voucherManager")
@Slf4j(topic = "manager")
public class VoucherManagerImpl extends CurrentOperationEnvironment implements VoucherManager {

    @Resource
    private VoucherFacade voucherFacade;

    @Override
    public String recordUnifiedVoucher(SimpleOrderVoucherInfo simpleOrderVoucherInfo) throws ManagerException {
        return saveRecord(JSON.toJSONString(simpleOrderVoucherInfo), SimpleOrderVoucherInfo.voucherType());
    }

    @Override
    public String recordPaymentVoucher(PaymentVoucherInfo paymentVoucherInfo) throws ManagerException {
        return saveRecord(JSON.toJSONString(paymentVoucherInfo), PaymentVoucherInfo.voucherType());
    }

    @Override
    public String recordRefundVoucher(RefundVoucherInfo refundVoucherInfo) throws ManagerException {
        return saveRecord(JSON.toJSONString(refundVoucherInfo), RefundVoucherInfo.voucherType());
    }

    @Override
    public String saveControlRecord(ControlVoucherInfo controlVoucherInfo) throws ManagerException {
        return saveRecord(JSON.toJSONString(controlVoucherInfo), ControlVoucherInfo.voucherType());
    }

    @Override
    public String queryTradeBy(String outerTradeNo, String source, String voucherType) throws ManagerException {
        try {
            log.info("查询交易凭证信息,outerTradeNo:{},source:{},voucherType:{}", outerTradeNo, source, voucherType);
            long start = System.currentTimeMillis();
            VoucherQueryResponse resp = voucherFacade.queryBySource(outerTradeNo, source, voucherType);
            long end = System.currentTimeMillis();
            log.info("查询交易凭证信息,耗时:{}(ms); 响应结果:{}", start - end, resp);
            if (!"S0001".equalsIgnoreCase(resp.getReturnCode())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "查询交易凭证信息出错" + resp.getReturnMessage());
            }
            if (StringUtil.isEmpty(resp.getVoucher())) {
                throw new ManagerException(ErrorCodeEnums.ILLEGAL_OUTER_TRADE_NO, "原始商户订单号" +
                        JSON.toJSONString(outerTradeNo) + "不存在");
            }
            return resp.getVoucher();
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    public Map<String, String> batchRecordTradeVoucher(List<TradeVoucherInfo> tradeVoucherInfoList) throws ManagerException {
        List<String> infoJsonList = new ArrayList<String>();
        for (TradeVoucherInfo info : tradeVoucherInfoList) {
            String infoJson = JSON.toJSONString(info);
            infoJsonList.add(infoJson);
        }
        return batchSaveRecord(infoJsonList, TradeVoucherInfo.voucherType());
    }

    @Override
    public TradeVoucherInfo queryTradeByVoucherNo(String voucherNo) throws ManagerException {
        try {
            log.info("查询交易凭证信息，请求参数：{}", voucherNo);
            long beginTime = System.currentTimeMillis();
            VoucherQueryResponse voucherQueryResponse = voucherFacade.queryBy(voucherNo, TradeVoucherInfo.voucherType());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("查询交易凭证信息，耗时:{} (ms); 响应结果:{} ", consumeTime, voucherQueryResponse);
            if (!Constants.SUCCESS_RETURN_CODE.equalsIgnoreCase(voucherQueryResponse.getReturnCode())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "查询交易凭证信息异常" + voucherQueryResponse.getReturnMessage());
            }
            return JSON.parseObject(voucherQueryResponse.getVoucher(), TradeVoucherInfo.class);
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    /**
     * 保存到统一凭证
     * @param infoJson
     * @return
     * @throws ManagerException
     */
    private String saveRecord(String infoJson, String voucherType) throws ManagerException {
        try {
            RecordingRequest recordingRequest = new RecordingRequest();
            recordingRequest.setVoucher(infoJson);
            recordingRequest.setVoucherType(voucherType);
            log.info("记录统一凭证，请求参数：{}", infoJson);
            long beginTime = System.currentTimeMillis();
            RecordingResponse recordingResponse = voucherFacade.record(recordingRequest);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("记录统一凭证，耗时:{} (ms); 响应结果:{} ", consumeTime, recordingResponse);
            if (!Constants.SUCCESS_RETURN_CODE.equalsIgnoreCase(recordingResponse.getReturnCode())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "记录统一凭证异常" + recordingResponse.getReturnMessage());
            }
            return recordingResponse.getVoucherNo();
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    /**
     * 批量记录交易凭证
     * @param infoJsonList
     * @param voucherType
     * @return
     */
    private Map<String, String> batchSaveRecord(List<String> infoJsonList, String voucherType) throws ManagerException {
        try {
            BatchRecordingRequest batchRecordingRequest = new BatchRecordingRequest();
            batchRecordingRequest.setVoucherType(voucherType);
            batchRecordingRequest.setVouchers(infoJsonList);
            log.info("批量记录交易凭证，请求参数：{}", infoJsonList);
            long beginTime = System.currentTimeMillis();
            BatchRecordingResponse batchRecordingResponse = voucherFacade.batchRecord(batchRecordingRequest);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("批量记录交易凭证， 耗时:{} (ms); 响应结果:{} ", consumeTime, batchRecordingResponse);

            Map<String, String> resultMap = new HashMap<>(10);
            if (!Constants.SUCCESS_RETURN_CODE.equalsIgnoreCase(batchRecordingResponse.getReturnCode())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "批量记录交易凭证异常" +
                        batchRecordingResponse.getReturnMessage());
            }
            List<RecordingResponse> respList = batchRecordingResponse.getResponse();
            for (RecordingResponse recdResp : respList) {
                if ("F0011".equalsIgnoreCase(recdResp.getReturnCode())) {
                    throw new ManagerException(ErrorCodeEnums.REPEAT_REQUEST_ERROR, "批量记录交易凭证异常" +
                            batchRecordingResponse.getReturnMessage());
                }
                TradeVoucherInfo tradeVouchInfo = JSON.parseObject(recdResp.getVoucher(), TradeVoucherInfo.class);
                resultMap.put(tradeVouchInfo.getSourceVoucherNo(), recdResp.getVoucherNo());
            }
            return resultMap;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }
}
