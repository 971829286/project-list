package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author zs. Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletDepositRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -6701656980360449487L;

    /*** 外部订单号 ***/
    @NotBlank(message = "外部订单号不能为空")
    private String outTradeNo;

    /*** 充值产品号 ***/
    @NotBlank(message = "充值产品码不能为空")
    private String bizProductCode;

	/*** 会员id ***/
	@NotBlank(message = "会员id不能为空")
	private String memberId;

    /*** 账户类型 ***/
    @NotBlank
    private String accountType;

    /*** 账户标识 ***/
    private String accountIdentity;

    /*** 充值金额 ***/
    @NotBlank
    private String amount;

    /*** 支付方式 目前只支持网银和POS充值 ***/
    @NotNull
    private PayMethodInfo payMethod;

    /*** 服务器异步通知地址 用于通过GatewayAccessInfo传入pns，如果不传，则不会通知，业务系统也可以监听MQ获取交易结果 ***/
    private String notifyUrl;
	
    /*** 充值说明 ***/
    private String remark;

    /*** 扩展参数 ***/
    private String extension;

}
