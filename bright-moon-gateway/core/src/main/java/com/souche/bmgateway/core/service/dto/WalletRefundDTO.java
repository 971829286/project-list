package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 钱包退款
 *
 * @author zs.
 *         Created on 18/11/27.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletRefundDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -3094301208519358855L;

    /*** 商户请求号 ***/
    private String outerTradeNo;

    /*** 原始商户网站唯一交易订单号 ***/
    private String origOuterTradeNo;
    
    /*** 交易原始凭证号 对应于原交易的交易凭证号 ***/
    private String tradeSourceVoucherNo;

    /*** 退款凭证号 ***/
    private String refundVoucherNo;

    /*** 产品码 ***/
    private String productCode;

    /*** 退款金额 ***/
    private Money refundAmount;

    /*** 退金币金额 ***/
    private Money goldCoinAmount;

    /*** 退分润集合 ***/
    private List<SplitParameter> splitParameterList;

    /*** 异步通知地址 ***/
    private String notifyUrl;
    
    /*** 退款备注 ***/
    private String memo;

    /*** 扩展参数 ***/
    private String extension;
}
