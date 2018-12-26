package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.request.trade.SplitInfo;
import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zs.
 * Created on 18/7/12.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TradeQueryResponse extends CommonResponse {
    private static final long serialVersionUID = 194957435001049828L;

    /*** 外部请求号 ***/
    private String outerTradeNo;

    /*** 交易凭证号 ***/
    private String tradeVoucherNo;

    /*** 产品码 ***/
    private String bizProductCode;

    /*** 商品信息 ***/
    private String subject;

    /*** 买家id ***/
    private String buyerId;

    /*** 卖家id ***/
    private String sellerId;

    /*** 买家手续费 ***/
    private String payerFee;

    /*** 卖家手续费 ***/
    private String payeeFee;
    
    /*** 分润集合 ***/
    private List<SplitInfo> splitInfoList;

    /*** 卖家账号 ***/
    private String sellerAccountNo;

    /*** 交易金额 ***/
    private String tradeAmount;

    /*** 交易状态 ***/
    private String tradeStatus;

    /*** 支付结果 ***/
    private PayChannelResult payMethodResult;
}
