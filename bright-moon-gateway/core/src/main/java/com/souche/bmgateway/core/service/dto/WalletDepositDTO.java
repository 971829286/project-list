package com.souche.bmgateway.core.service.dto;

import com.souche.bmgateway.enums.PayMethodEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author zs. Created on 18/12/7.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletDepositDTO extends WalletBaseDTO {
	private static final long serialVersionUID = -6569466876373349516L;

	/*** 外部订单号 ***/
	private String outTradeNo;

	/*** 充值产品号 ***/
	private String bizProductCode;

	/*** 会员id ***/
	private String memberId;

	/*** 账户类型 ***/
	private String accountType;

	/*** 账号 ***/
	private String AccountNo;

	/*** 账户标识 ***/
	private String accountIdentity;

	/*** 充值金额 ***/
	private String amount;

	/*** 支付方式 ***/
	private PayMethodEnums payMethod;

	/*** 支付渠道 ***/
	private String paymentChannel;

	/*** 异步回调地址 ***/
	private String notifyUrl;

	/*** 充值说明 ***/
	private String remark;

	/*** 扩展参数 ***/
	private String extension;

	/*** 支付提交时间 ***/
	private Date gmtSubmit;

	/*** 统一交易凭证 ***/
	private String tradeVoucherNo;

	/*** 支付凭证号 ***/
	private String paymentVoucherNo;

	/*** 终端类型 ***/
	private String accessChannel;
	
	/*** 支付扩展参数 ***/
	private String payExtension;

}
