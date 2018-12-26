package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.tradeservice.facade.api.TradeProcessFacade;
import com.netfinworks.tradeservice.facade.api.TradeQueryFacade;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;
import com.netfinworks.tradeservice.facade.response.TradeDetailQueryResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.witness.aspect.TradeWitness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/11/15.
 */
@Service("tssManager")
@Slf4j(topic = "manager")
public class TssManagerImpl extends CurrentOperationEnvironment implements TssManager {

    @Resource
    private TradeProcessFacade tradeProcessFacade;

    @Resource
    private TradeQueryFacade tradeQueryFacade;

    @Override
    @TradeWitness
    public PaymentResponse createAndPay(TradeRequest tradeRequest) throws ManagerException {
        try {
            log.info("创建订单并支付，请求参数：{}", tradeRequest);
            long beginTime = System.currentTimeMillis();
            PaymentResponse paymentResponse = tradeProcessFacade.createAndPay(tradeRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("创建订单并支付， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if(!paymentResponse.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用交易服务创建订单并支付异常" + paymentResponse.getResultMessage());
            }
            return paymentResponse;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    @TradeWitness
    public PaymentResponse pay(PaymentRequest paymentRequest) throws ManagerException {
        try {
            log.info("基于支付单支付，请求参数：{}", paymentRequest);
            long beginTime = System.currentTimeMillis();
            PaymentResponse paymentResponse = tradeProcessFacade.pay(paymentRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("基于支付单支付， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if(!paymentResponse.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用交易服务基于支付单支付异常" + paymentResponse.getResultMessage());
            }
            return paymentResponse;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    @TradeWitness
    public RefundResponse refund(RefundRequest refundRequest) throws ManagerException {
        try {
            log.info("退款，请求参数：{}", refundRequest);
            long beginTime = System.currentTimeMillis();
            RefundResponse refundResponse = tradeProcessFacade.refund(refundRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("退款， 耗时:{} (ms); 响应结果:{} ", consumeTime, refundResponse);
            if (!refundResponse.isSuccess() || !"S".equals(refundResponse.getStatus())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用交易服务退款异常" + refundResponse.getResultMessage());
            }
            return refundResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    public TradeDetailQueryResponse queryTradeDetail(String tradeVoucherNo) throws ManagerException {
        try {
            log.info("查询交易信息， 请求参数：{}", tradeVoucherNo);
            long beginTime = System.currentTimeMillis();
            TradeDetailQueryResponse queryResponse = tradeQueryFacade.queryDetail(tradeVoucherNo, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("查询交易信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, queryResponse);
            if (!queryResponse.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "查询交易信息" + queryResponse.getResultMessage());
            }
            return queryResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }
}
