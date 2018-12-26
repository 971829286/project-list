package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FundoutTradeRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -4990405321139718723L;

    /*** 外部订单号 ***/
    @NotBlank(message = "outTradeNo不能为空")
    private String outTradeNo;

    /*** 产品编码 ***/
    @NotBlank
    private String bizProductCode;

    /** 金额 **/
    @NotBlank
    private String amount;

    /*** 会员id ***/
    @NotBlank(message = "memberId不能为空")
    private String memberId;

    /*** 储值账户类型 ***/
    private String accountType;
    
    /*** 账户标识 ***/
    private String accountIdentity;

    /*** 绑卡id ***/
    private String bankCardId;

    /*** 卡号 ***/
    private String cardNo;

    /*** 异步通知地址 ***/
    private String notifyUrl;

    /*** 扩展信息 ***/
    private String extension;
}
