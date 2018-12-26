package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author XuJinNiu
 * @since 2018-12-06
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletUnFrozenDTO extends WalletBaseDTO {
	private static final long serialVersionUID = -2729767190621024036L;

	private String outerTradeNo;
	private String paymentVoucherNo;
	private String bizProductCode;
	private String origOuterTradeNo;
	private Money  unFreezeAmount;
	private String extension;
	private String memo;
}