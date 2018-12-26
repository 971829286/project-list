package com.souche.bmgateway.core.service.trade;

import com.souche.bmgateway.core.service.dto.BalanceTransferDTO;
import com.souche.bmgateway.core.service.dto.EntryDTO;
import com.souche.bmgateway.core.service.dto.InstantPayDTO;
import com.souche.bmgateway.core.service.dto.TradeFeeDTO;
import com.souche.bmgateway.core.service.dto.TradeInfoDTO;
import com.souche.bmgateway.core.service.dto.TradeQueryDTO;
import com.souche.bmgateway.core.service.dto.WalletFrozenDTO;
import com.souche.bmgateway.core.service.dto.WalletRefundDTO;
import com.souche.bmgateway.core.service.dto.WalletUnFrozenDTO;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.trade.InstantTradeResponse;
import com.souche.bmgateway.model.response.trade.TradeFeeQueryResponse;
import com.souche.bmgateway.model.response.trade.TradeQueryResponse;

/**
 * 即时交易&转账&冻结&解冻服务
 *
 * @author zs.
 *         Created on 18/11/16.
 */
public interface WalletTradeService {

    /**
     * 创建交易单并支付接口
     * PS：如果没有支付信息则只是下交易单，需要调用doPayWithPayOrder进行后续的支付
     *
     * @param tradeInfoDTO
     * @return
     */
    InstantTradeResponse createInstantAndPay(TradeInfoDTO tradeInfoDTO);

    /**
     * 基于交易单支付
     *
     * @param instantPayDTO
     * @return
     */
    InstantTradeResponse doPayWithPayOrder(InstantPayDTO instantPayDTO);

    /**
     * 转账
     *
     * @param balanceTransferDTO
     * @return
     */
    CommonResponse transferAccount(BalanceTransferDTO balanceTransferDTO);

    /**
     * 退款
     *
     * @param walletRefundDTO
     * @return
     */
    CommonResponse refund(WalletRefundDTO walletRefundDTO);

    /**
     * 登账
     *
     * @param entryDTO
     * @return
     */
    CommonResponse entry(EntryDTO entryDTO);

    /**
     * 钱包资金冻结
     *
     * @param walletFrozenDTO
     * @return
     */
    CommonResponse walletFrozen(WalletFrozenDTO walletFrozenDTO);

	/**
	 * 钱包资金解冻
	 *
	 * @param walletUnFrozenDTO
	 * @return
	 */
	CommonResponse walletUnFrozen(WalletUnFrozenDTO walletUnFrozenDTO);

    /**
     * 查询交易接口
     *
     * @param tradeQueryDTO
     * @return
     */
    TradeQueryResponse queryTrade(TradeQueryDTO tradeQueryDTO);

    /**
     * 查询交易手续费
     *
     * @param tradeFeeDTO
     * @return
     */
    TradeFeeQueryResponse queryFee(TradeFeeDTO tradeFeeDTO);

    // TODO 子账户汇入接收通知

}
