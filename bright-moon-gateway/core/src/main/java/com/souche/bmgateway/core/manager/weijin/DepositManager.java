package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.deposit.api.domain.DepositRequest;
import com.netfinworks.deposit.api.domain.DepositResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * 充值
 * 
 * @author luobing Created on 18/11/20
 *
 */
public interface DepositManager {
	/**
	 * 提交充值请求
	 * 
	 * @param depositRequest
	 * @return
	 * @throws ManagerException
	 */
	DepositResponse deposit(DepositRequest depositRequest) throws ManagerException;
}
