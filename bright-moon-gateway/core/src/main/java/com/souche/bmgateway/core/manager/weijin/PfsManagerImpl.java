package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.pfs.service.payment.EntryAccountFacade;
import com.netfinworks.pfs.service.payment.FundsControlFacade;
import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.pfs.service.payment.request.FundsFreezeRequest;
import com.netfinworks.pfs.service.payment.request.FundsUnFreezeRequest;
import com.netfinworks.pfs.service.payment.response.PaymentResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Service("pfsManager")
@Slf4j(topic = "manager")
public class PfsManagerImpl extends CurrentOperationEnvironment implements PfsManager {

    @Resource
    private EntryAccountFacade entryAccountFacade;

    @Resource
    private FundsControlFacade fundsControlFacade;

    @Override
    public PaymentResponse freeze(FundsFreezeRequest freezeRequest) throws ManagerException {
        try {
            log.info("资金冻结，请求参数：{}", freezeRequest);
            long beginTime = System.currentTimeMillis();
            PaymentResponse paymentResponse = fundsControlFacade.freeze(freezeRequest, this.getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("资金冻结， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if (!paymentResponse.isSuccess() || !"S".equals(paymentResponse.getBizPaymentState())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用支付前置资金冻结异常" + paymentResponse
                        .getReturnMessage() + paymentResponse.getUnityResultMessage());
            }
            return paymentResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    public PaymentResponse unfreeze(FundsUnFreezeRequest unFreezeRequest) throws ManagerException {
        try {
            log.info("资金解冻，请求参数：{}", unFreezeRequest);
            long beginTime = System.currentTimeMillis();
            PaymentResponse paymentResponse = fundsControlFacade.unfreeze(unFreezeRequest, this
                    .getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("资金解冻， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if (!paymentResponse.isSuccess() || !"S".equals(paymentResponse.getBizPaymentState())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用支付前置资金解冻异常" + paymentResponse
                        .getReturnMessage() + paymentResponse.getUnityResultMessage());
            }
            return paymentResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    @Override
    public PaymentResponse enter(EntryAcountRequest entryAcountRequest) throws ManagerException {
        try {
            log.info("登帐，请求参数：{}", entryAcountRequest);
            long beginTime = System.currentTimeMillis();
            PaymentResponse paymentResponse = entryAccountFacade.enter(entryAcountRequest, getOperationEnvironment());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("登帐， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if (!paymentResponse.isSuccess() || !"S".equals(paymentResponse.getBizPaymentState())) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用支付前置登帐异常" + paymentResponse
                        .getReturnMessage() + paymentResponse.getUnityResultMessage());
            }
            return paymentResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }
}
