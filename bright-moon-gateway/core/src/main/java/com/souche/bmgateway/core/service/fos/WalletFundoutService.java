package com.souche.bmgateway.core.service.fos;

import com.souche.bmgateway.core.service.dto.WalletFundoutDTO;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;

/**
 * @author zs. Created on 18/11/19.
 */
public interface WalletFundoutService {

	/**
	 * 钱包提现
	 * 
	 * @param walletFundoutDTO
	 * @return
	 */
	FundoutTradeResponse walletFundout(WalletFundoutDTO walletFundoutDTO);

}
