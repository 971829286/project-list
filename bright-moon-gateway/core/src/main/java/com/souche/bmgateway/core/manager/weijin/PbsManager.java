package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.pbs.service.context.vo.PayPricingReq;
import com.netfinworks.pbs.service.context.vo.PaymentPricingResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * @author zs.
 *         Created on 17/12/6.
 */
public interface PbsManager {

    /**
     * 获取手续费信息
     *
     * @param payPricingReq
     * @return
     * @throws ManagerException
     */
    PaymentPricingResponse getFeeInfo(PayPricingReq payPricingReq) throws ManagerException;
}
