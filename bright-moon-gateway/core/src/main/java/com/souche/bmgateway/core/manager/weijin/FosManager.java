package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * 出款
 * 
 * @author luobing Create on 2018/11/20
 *
 */
public interface FosManager {
	/**
	 * 提现
	 * 
	 * @param fundoutRequestDO
	 * @return
	 * @throws ManagerException
	 */
	FundoutResponse submit(FundoutRequest fundoutRequestDO) throws ManagerException;

}
