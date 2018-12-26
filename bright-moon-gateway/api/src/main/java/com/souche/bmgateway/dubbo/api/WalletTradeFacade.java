package com.souche.bmgateway.dubbo.api;

import com.alibaba.dubbo.doc.annotation.InterfaceDesc;
import com.alibaba.dubbo.doc.annotation.MethodDesc;
import com.souche.bmgateway.model.request.trade.*;
import com.souche.bmgateway.model.request.trade.TradeQueryRequest;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.trade.*;

/**
 * 交易服务-负责对接维金交易系统并对外提供支付能力
 *
 * @author zs.
 *         Created on 18/7/12.
 */
@InterfaceDesc(value="交易服务-负责对接维金交易系统并对外提供支付能力", url="http://null///git.souche-inc.com/spay/bright-moon-gateway/tree/master/api/src/main/java/com/souche/bmgateway/dubbo/api/WalletTradeFacade.java")
public interface WalletTradeFacade {

    /**
     * 钱包充值
     *
     * @param walletDepositRequest
     * @return
     */
    @MethodDesc("钱包充值")
    WalletDepositResponse walletDeposit(WalletDepositRequest walletDepositRequest);


    /**
     * 创建交易订单
     *
     * @param createPayOrderRequest
     * @return
     */
    @MethodDesc("创建交易订单")
    CreatePayOrderResponse createPayOrder(CreatePayOrderRequest createPayOrderRequest);


    /**
     * 基于交易单支付
     *
     * @param instantPayRequest
     * @return
     */
    @MethodDesc("基于交易单支付")
    InstantTradeResponse doPayWithPayOrder(InstantPayRequest instantPayRequest);


    /**
     * 创建交易单并支付
     *
     * @param createInstantTradeRequest
     * @return
     */
    @MethodDesc("创建交易单并支付")
    InstantTradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest);


    /**
     * 转账
     *
     * @param transferAccountRequest
     * @return
     */
    @MethodDesc("转账")
    CommonResponse transferAccount(TransferAccountRequest transferAccountRequest);


    /**
     * 钱包扣款
     * PS：该扣款功能是转账的变种，主要针对用户违约，对用户钱包进行扣款，有多少扣多少
     *
     * @param walletDeductRequest
     * @return
     */
    @MethodDesc("钱包扣款 \nPS：该扣款功能是转账的变种，主要针对用户违约，对用户钱包进行扣款，有多少扣多少")
    CommonResponse walletDeduct(WalletDeductRequest walletDeductRequest);


    /**
     * 退款
     *
     * @param walletRefundRequest
     * @return
     */
    @MethodDesc("退款")
    CommonResponse refund(WalletRefundRequest walletRefundRequest);


    /**
     * 钱包提现
     *
     * @param fundoutRequestDO
     * @return
     */
    @MethodDesc("钱包提现")
    FundoutTradeResponse walletFundout(FundoutTradeRequest fundoutRequestDO);


    /**
     * 登账
     * 只能用于财务的登帐行为，不附带交易信息，不能根据业务码匹配账号信息
     *
     * @param entryRequest
     * @return
     */
    @MethodDesc("登账 \n只能用于财务的登帐行为，不附带交易信息，不能根据业务码匹配账号信息")
    CommonResponse entry(EntryRequest entryRequest);


    /**
     * 钱包资金冻结
     *
     * @param walletFrozenRequest
     * @return
     */
    @MethodDesc("钱包资金冻结")
    CommonResponse walletFrozen(WalletFrozenRequest walletFrozenRequest);


    /**
     * 钱包资金解冻
     *
     * @param walletUnFrozenRequest
     * @return
     */
    @MethodDesc("钱包资金解冻")
    CommonResponse walletUnFrozen(WalletUnFrozenRequest walletUnFrozenRequest);


    /**
     * 查询交易接口
     *
     * @param tradeQueryRequest
     * @return
     */
    @MethodDesc("查询交易接口")
    TradeQueryResponse queryTrade(TradeQueryRequest tradeQueryRequest);


    /**
     * 查询交易手续费
     *
     * @param tradeFeeQueryRequest
     * @return
     */
    @MethodDesc("查询交易手续费")
    TradeFeeQueryResponse queryTradeFee(TradeFeeQueryRequest tradeFeeQueryRequest);


    // TODO 提现审批接口

}
