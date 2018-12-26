package com.souche.bmgateway.dubbo.api;

import com.souche.bmgateway.model.request.*;
import com.souche.bmgateway.model.response.EntryResponse;
import com.souche.bmgateway.model.response.FundoutTradeResponse;
import com.souche.bmgateway.model.response.TradeResponse;

import com.alibaba.dubbo.doc.annotation.InterfaceDesc;
import com.alibaba.dubbo.doc.annotation.MethodDesc;

/**
 * 交易服务-负责对接维金交易系统并对外提供支付能力
 *
 * @author zs.
 * Created on 18/7/12.
 */
@Deprecated
@InterfaceDesc(value="交易服务-负责对接维金交易系统并对外提供支付能力", url="http://null///git.souche-inc.com/spay/bright-moon-gateway/tree/master/api/src/main/java/com/souche/bmgateway/dubbo/api/TradeFacade.java")
public interface TradeFacade {

    /**
     * 即时到账交易网关接口
     *
     * @param createInstantTradeRequest
     * @return
     */
    @MethodDesc("即时到账交易网关接口")
    TradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest);

    /**
     * 提现接口
     *
     * @param fundoutRequestDO
     * @return
     */
    @MethodDesc("提现接口")
    FundoutTradeResponse fundoutTrade(FundoutTradeRequest fundoutRequestDO);

    /**
     * 转账网关接口
     * @param transferAccountRequest
     * @return
     */
    @MethodDesc("转账网关接口")
    TradeResponse transferAccount(TransferAccountRequest transferAccountRequest);

    /**
     * 登账接口
     *
     * @param entryRequest
     * @return
     */
    @MethodDesc("登账接口")
    EntryResponse entry(EntryRequest entryRequest);


    /**
     * 交易查询网关接口
     * @param queryTrade
     * @return
     */
    @MethodDesc("交易查询网关接口")
    public TradeResponse queryPay(QueryPayRequest queryTrade);

}
