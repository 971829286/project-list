package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletRefundRequest extends CommonBaseRequest {

    /*** 商户请求号 ***/
    @NotBlank
    private String outerTradeNo;

    /*** 原始商户网站唯一交易订单号 ***/
    @NotBlank
    private String origOuterTradeNo;

    /*** 产品码 ***/
    @NotBlank
    private String bizProductCode;

    /*** 退款金额 ***/
    @NotBlank
    private String refundAmount;

    /*** 退金币金额 ***/
    private String goldCoinAmount;

    /*** 退分润集合 ***/
    @NotEmpty
    @Valid
    private List<SplitInfo> splitInfoList;

    /*** 异步通知地址 ***/
    private String notifyUrl;

    /*** 退款备注 ***/
    private String memo;

    /*** 扩展参数 ***/
    private String extension;

}
