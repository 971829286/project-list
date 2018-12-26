package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.pfs.service.payment.response.PaymentResponse;
import com.souche.bmgateway.core.exception.ErrorCodeException;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.trade.PaymentToCardReq;

import java.util.Map;

/**
 * @author zs.
 *         Created on 18/7/20.
 */
@Deprecated
public interface TradeManager {

    /**
     * 提现
     * @param fundoutRequestDO
     * @return
     */
    FundoutResponse submit(FundoutRequest fundoutRequestDO) throws ManagerException;

    /**
     * 发送交易到交易网关
     * @param reqParams
     * @return
     */

    String sendTradeRequest(Map<String, String> reqParams);

    /**
     * 登账请求
     * @param entryAcountRequest
     * @return
     */
    PaymentResponse entryRequest(EntryAcountRequest entryAcountRequest) throws ManagerException;


    /**
     * 提现交易凭证
     * @param req
     * @throws ErrorCodeException.CommonException
     */
     void savePaymentToCardTradeRecord(PaymentToCardReq req) throws ManagerException;

    /**
     * 提现支付凭证
     * @param req
     * @throws ErrorCodeException.CommonException
     * @return
     */
     String savePayRecordByPaymentToCardReq(PaymentToCardReq req) throws ManagerException;
}
