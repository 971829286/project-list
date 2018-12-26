package com.souche.bmgateway.core.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 转账DTO
 *
 * @author zs.
 *         Created on 18/11/26.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class BalanceTransferDTO extends WalletBaseDTO {
    private static final long serialVersionUID = 4967305273023887432L;

    private String outerTradeNo;
    private String bizOrderNo;
    private String payVoucherNo;
    private String tradeVoucherNo;

    private String productCode;
    private String transferAmount;

    private String fundoutMemberId;
    private String fundOutAccountId;

    private String fundinMemberId;
    private String fundInAccountId;

    private String notifyUrl;

    private String tradeMemo;
    private String extension;
}
