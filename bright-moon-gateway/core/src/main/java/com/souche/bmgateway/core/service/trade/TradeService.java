package com.souche.bmgateway.core.service.trade;

import com.souche.bmgateway.model.request.*;
import com.souche.bmgateway.model.response.EntryResponse;
import com.souche.bmgateway.model.response.FundoutTradeResponse;
import com.souche.bmgateway.model.response.TradeResponse;

/**
 * @author yyx
 */
@Deprecated
public interface TradeService {

    /**
     * 及时到账
     * @param createInstantTradeRequest
     * @return
     */
    TradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest);

    /**
     * 提现
     * @param fundoutRequestDO
     * @return
     */
    FundoutTradeResponse submit(FundoutTradeRequest fundoutRequestDO);


    /**
     * 登账
     * @param entryRequest
     * @return
     */
    EntryResponse entry(EntryRequest entryRequest);

    /**
     * 转账
     * @param transferAccountRequest
     * @return
     */
    TradeResponse transferAccount(TransferAccountRequest transferAccountRequest);

    /**
     * 查询交易接口
     * @param queryTrade
     * @return
     */
    TradeResponse queryTrade(QueryPayRequest queryTrade);
}
