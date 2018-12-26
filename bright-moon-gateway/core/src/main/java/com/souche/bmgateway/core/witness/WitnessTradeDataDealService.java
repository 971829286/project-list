package com.souche.bmgateway.core.witness;

import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;

/**
 * 交易同步交易见证
 *
 * @author zs. Created on 17/12/4.
 */
public interface WitnessTradeDataDealService {

    /**
     * 充值交易同步交易见证
     *
     * @param depositRequest
     * @param depositResponse
     */
    void saveDepositDataToWitness(DepositRequest depositRequest, DepositResponse depositResponse);

    /**
     * 即时交易下单并支付OR转账同步交易见证
     *
     * @param tradeRequest
     * @param paymentResponse
     */
    void saveInstantTradeDataToWitness(TradeRequest tradeRequest, PaymentResponse paymentResponse);

    /**
     * 基于交易单支付同步交易见证
     *
     * @param paymentRequest
     * @param paymentResponse
     */
    void saveInstantPayDataToWitness(PaymentRequest paymentRequest, PaymentResponse paymentResponse);

    /**
     * 退款同步交易见证
     *
     * @param refundRequest
     * @param refundResponse
     */
    void saveRefundToWitness(RefundRequest refundRequest, RefundResponse refundResponse);

    /**
     * 提现同步交易见证
     *
     * @param fundoutRequest
     * @param fundoutResponse
     */
    void saveFundoutToWitness(FundoutRequest fundoutRequest, FundoutResponse fundoutResponse);
}
