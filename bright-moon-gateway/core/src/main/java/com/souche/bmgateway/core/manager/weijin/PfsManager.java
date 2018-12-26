package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.pfs.service.payment.request.EntryAcountRequest;
import com.netfinworks.pfs.service.payment.request.FundsFreezeRequest;
import com.netfinworks.pfs.service.payment.request.FundsUnFreezeRequest;
import com.netfinworks.pfs.service.payment.response.PaymentResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
public interface PfsManager {

    /**
     * 钱包冻结
     *
     * @param freezeRequest
     * @return
     */
    PaymentResponse freeze(FundsFreezeRequest freezeRequest) throws ManagerException;


    /**
     * 钱包解冻
     *
     * @param unFreezeRequest
     * @return
     */
    PaymentResponse unfreeze(FundsUnFreezeRequest unFreezeRequest) throws ManagerException;


    /**
     * 登帐
     *
     * @param entryAcountRequest
     * @return
     */
    PaymentResponse enter(EntryAcountRequest entryAcountRequest) throws ManagerException;

}
