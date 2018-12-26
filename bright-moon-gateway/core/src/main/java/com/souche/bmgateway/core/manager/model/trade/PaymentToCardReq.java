package com.souche.bmgateway.core.manager.model.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.ws.rs.GET;

/**
 * 付款到卡
 * @author sirk
 *
 */
@Setter
@Getter
@ToString
public class PaymentToCardReq extends RequestBase {

	private String outerTradeNo;
	private String identityNo;
	private String identityType;
	private String payableAmount;

	private String cardNo;
	private String accountName;
	private String bankCode;
	private String bankName;
	private String branchName;
	private String bankLineNo;
	private String bankProv;
	private String bankCity;
	private String cardType;
	private String companyOrPersonal;

	private String notifyUrl;

	private String productCode;

	// 内部交易凭证号
	private String innerTradeNo;

	private String memberId;

	private String accountId;

}
