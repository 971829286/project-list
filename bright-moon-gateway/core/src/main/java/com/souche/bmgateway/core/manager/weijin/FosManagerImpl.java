package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.fos.service.facade.FundoutFacade;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.witness.aspect.TradeWitness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("fosManager")
@Slf4j(topic="manager")
public class FosManagerImpl extends CurrentOperationEnvironment implements FosManager {

	@Resource
	private FundoutFacade fundoutFacade;

	@Override
	@TradeWitness
	public FundoutResponse submit(FundoutRequest fundoutRequest) throws ManagerException {
		try {
			log.info("提现，请求参数：{}", fundoutRequest);
			long beginTime = System.currentTimeMillis();
			FundoutResponse fundoutResponse = fundoutFacade.submit(fundoutRequest, getOperationEnvironment());
			long consumeTime = System.currentTimeMillis() - beginTime;
			log.info("提现， 耗时:{} (ms); 响应结果:{} ", consumeTime, fundoutResponse);
			if (null == fundoutResponse) {
				throw new ManagerException(ErrorCodeEnums.RET_OBJECT_IS_NULL, "出款失败，返回结果为空");
			}
			return fundoutResponse;
		} catch (Exception e) {
			if (e instanceof ManagerException) {
				throw e;
			}
			throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
		}
	}


}
