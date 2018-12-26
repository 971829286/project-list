package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 即时交易返回
 * @author zs.
 *         Created on 18/11/16.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstantTradeResponse extends CommonResponse {

    /*** 交易凭证号 支付时需携带 ***/
    private String tradeVoucherNo;

    /*** 支付状态 P-支付中, PS-付款成功, F-失败, S-成功, 对于调用方而言,只要关心 S- 成功和 F-失败即可,其他都是处理中 ***/
    private String paymentStatus;

    /*** 支付渠道结果 ***/
    private PayChannelResult payMethodResult;

    /*** 扩展参数 ***/
    private String extension;
}
