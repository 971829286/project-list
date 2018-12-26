package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;
import com.netfinworks.tradeservice.facade.response.TradeDetailQueryResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * @author zs.
 * Created on 18/11/15.
 */
public interface TssManager {

    /**
     * 创建支付单并支付
     * PS：目前只支持单个交易类目以及单个支付方式；
     *
     * @param tradeRequest
     * @return
     */
    PaymentResponse createAndPay(TradeRequest tradeRequest) throws ManagerException;

    /**
     * 基于之前创建的支付单进行支付
     * PS：目前只支持单种支付方式
     *
     * @param paymentRequest
     * @return
     * @throws ManagerException
     */
    PaymentResponse pay(PaymentRequest paymentRequest) throws ManagerException;

    /**
     * 退款
     *
     * @param refundRequest
     * @return
     * @throws ManagerException
     */
    RefundResponse refund(RefundRequest refundRequest) throws ManagerException;

    /**
     * 查询交易详情
     *
     * @param tradeVoucherNo
     * @return
     * @throws ManagerException
     */
    TradeDetailQueryResponse queryTradeDetail(String tradeVoucherNo) throws ManagerException;

}
