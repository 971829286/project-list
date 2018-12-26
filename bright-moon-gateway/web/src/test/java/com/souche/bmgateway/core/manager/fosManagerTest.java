package com.souche.bmgateway.core.manager;

import javax.annotation.Resource;

import org.junit.Test;

import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.weijin.FosManager;

/**
 * 
 * @author luobing 18/11/21
 *
 */
public class fosManagerTest extends BaseTest {

	@Resource
	private FosManager fosManager;

	@Test
	public void walletFundout() {
		FundoutRequest fundoutRequestDO = new FundoutRequest();
		try {
			
//			fundoutRequestDO.setAccountNo("");
//			fundoutRequestDO.setAmount(amount);
//			fundoutRequestDO.set
			FundoutResponse submit = fosManager.submit(fundoutRequestDO);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
	}

}
