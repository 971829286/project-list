package com.souche.bmgateway.core.witness.aspect;

import com.alibaba.fastjson.JSON;
import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.netfinworks.tradeservice.facade.request.PaymentRequest;
import com.netfinworks.tradeservice.facade.request.RefundRequest;
import com.netfinworks.tradeservice.facade.request.TradeRequest;
import com.netfinworks.tradeservice.facade.response.PaymentResponse;
import com.netfinworks.tradeservice.facade.response.RefundResponse;
import com.souche.bmgateway.core.manager.weijin.CurrentOperationEnvironment;
import com.souche.bmgateway.core.witness.WitnessTradeDataDealService;
import com.souche.bmgateway.model.request.trade.FundoutTradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 选择manager来做交易见证，虽然参数封装可能稍微复杂一些，但是受业务影响会更小
 *
 * @author zs. Created on 17/12/4.
 */
@Component
@Aspect
@Slf4j(topic = "witness")
public class TradeWitnessAspectUtil {

	@Resource
	private WitnessTradeDataDealService witnessTradeDataDealService;

	@Pointcut("@annotation(com.souche.bmgateway.core.witness.aspect.TradeWitness)")
	public void tradeWitness() {}

	@Around("tradeWitness()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (!CurrentOperationEnvironment.isNeedWitness()) {
			return joinPoint.proceed();
		}

		log.info("交易见证网关请求参数：" + JSON.toJSONString(joinPoint.getArgs()));
		Object response = null;
		try {
			response = joinPoint.proceed();
			return response;
		} catch (Throwable throwable) {
			log.error("请求异常", throwable);
			throw throwable;
		} finally {
			try {
				doWitnessDataDeal(joinPoint.getArgs()[0], response);
			} catch (Exception e) {
				log.error("交易数据同步交易见证失败", e);
			}
		}
	}

    /**
     * 见证数据处理
     *
     * @param request
     * @param response
     */
    private void doWitnessDataDeal(Object request, Object response) {
        if (request instanceof DepositRequest) {
            //充值
            witnessTradeDataDealService.saveDepositDataToWitness((DepositRequest) request, (DepositResponse) response);
        } else if (request instanceof TradeRequest && ((TradeRequest) request).getPaymentInfo() != null) {
            //下单并支付OR转账
            witnessTradeDataDealService.saveInstantTradeDataToWitness((TradeRequest) request, (PaymentResponse) response);
        } else if (request instanceof PaymentRequest) {
            //基于交易单支付
            witnessTradeDataDealService.saveInstantPayDataToWitness((PaymentRequest) request, (PaymentResponse) response);
        } else if (request instanceof RefundRequest) {
            //退款
            witnessTradeDataDealService.saveRefundToWitness((RefundRequest) request, (RefundResponse) response);
        } else if (request instanceof FundoutTradeRequest) {
            //提现 用于做信息流对账使用，正常情况下无需同步
//            witnessTradeDataDealService.saveFundoutToWitness((FundoutRequest) request, (FundoutResponse) response);
        }
    }

}
