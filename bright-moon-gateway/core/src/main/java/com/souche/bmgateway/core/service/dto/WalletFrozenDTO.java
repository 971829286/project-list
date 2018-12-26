package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 *         Created on 18/12/6.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletFrozenDTO extends WalletBaseDTO {
	private static final long serialVersionUID = -8850367132543532146L;

	private String outerTradeNo;
	private String paymentVoucherNo;
	private String bizProductCode;
	private String memberId;
	private String accountNo;
	private Money  freezeAmount;
	private String extension;
	private String memo;
}
