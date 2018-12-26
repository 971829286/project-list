package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.deposit.api.DepositService;
import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.witness.aspect.TradeWitness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * @author luobing 2018/11/20
 */
@Service("depositManager")
@Slf4j(topic = "manager")
public class DepositManagerImpl extends CurrentOperationEnvironment implements DepositManager {

	@Resource
	private DepositService depositService;

	@Override
    @TradeWitness
	public DepositResponse deposit(DepositRequest depositRequest) throws ManagerException {
		try {
			log.info("充值，请求参数：{}", depositRequest);
			long beginTime = System.currentTimeMillis();
			DepositResponse depositResponse = depositService.deposit(depositRequest, getOperationEnvironment());
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("查询手续费， 耗时:{} (ms); 响应结果:{} ", consumeTime, depositResponse);
			if (null == depositResponse) {
				throw new ManagerException(ErrorCodeEnums.RET_OBJECT_IS_NULL, "充值失败，返回结果为空");
			}
			return depositResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}

}
