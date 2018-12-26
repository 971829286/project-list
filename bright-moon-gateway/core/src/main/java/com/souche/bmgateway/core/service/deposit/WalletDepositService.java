package com.souche.bmgateway.core.service.deposit;

import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
public interface WalletDepositService {

    /**
     * 钱包充值
     *
     * @param walletDepositDTO
     * @return
     */
    WalletDepositResponse walletDeposit(WalletDepositDTO walletDepositDTO);

}
