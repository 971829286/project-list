package com.souche.bmgateway.core.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author luobing 2018/12/7
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletFundoutDTO extends WalletBaseDTO {
	private static final long serialVersionUID = 8048017393386023070L;

	/*** 外部订单号 ***/
	private String outTradeNo;

	/*** 会员id ***/
	private String memberId;

	/*** 产品编码 ***/
	private String bizProductCode;

	/*** 储值账户类型 ***/
	private String accountType;

	/*** 储值账户号 ***/
	private String accountNo;

	/*** 绑卡id ***/
	private String bankCardId;

	/*** 卡号 ***/
	private String cardNo;

	/*** 户名 ***/
	private String bankAccountName;

	/*** 扩展信息 ***/
	private String extension;

	/*** 异步通知地址 ***/
	private String notifyUrl;

	/*** 请求时间 ***/
	private String requestTime;

	/** 分支行编号 **/
	private String bankLineNo;

	/*** 银行编码 ***/
	private String bankCode;

	/*** 银行信息 ***/
	private String bankName;

	/*** 分支行信息 ***/
	private String bankBranch;

	/*** 省市信息 ***/
	private String Prov;

	/*** 城市 ***/
	private String city;

	/*** 卡类型(DC/CC) ***/
	private Integer cardTye;
	
	/*** 对公/对私 ***/
	private Integer companyOrPersonal;

	/** 金额 **/
	private String amount;

	/** 交易凭证号 **/
	private String tradeVoucherNo;

	/** 支付凭证号 **/
	private String paymentVoucherNo;
	
	/***银行卡账户名***/
	private String name;

}
