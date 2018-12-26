package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.pbs.service.context.vo.PayPricingReq;
import com.netfinworks.pbs.service.context.vo.PaymentPricingResponse;
import com.netfinworks.pbs.service.facade.PayPartyFeeFacade;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 计费服务
 *
 * @author zs. Created on 17/12/6.
 */
@Service("pbsManager")
@Slf4j(topic = "manager")
public class PbsManagerImpl extends CurrentOperationEnvironment implements PbsManager {

	@Resource
	private PayPartyFeeFacade payPartyFeeFacade;

    @Override
    public PaymentPricingResponse getFeeInfo(PayPricingReq payPricingReq) throws ManagerException {
        try {
            log.info("查询手续费，请求参数：{}", payPricingReq);
            long beginTime = System.currentTimeMillis();
            payPricingReq.setSourceCode(getOperationEnvironment().getClientId());
            PaymentPricingResponse paymentResponse = payPartyFeeFacade.getFee(payPricingReq);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("查询手续费， 耗时:{} (ms); 响应结果:{} ", consumeTime, paymentResponse);
            if (!paymentResponse.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "调用算费服务查询手续费异常" + paymentResponse
                        .getResultMessage());
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
