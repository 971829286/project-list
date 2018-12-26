package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author zs.
 *         Created on 18/11/29.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class EntryDTO extends WalletBaseDTO {
    private static final long serialVersionUID = 6042477002289669190L;

    private String paymentVoucherNo;
    private String bizProductCode;
    private Date gmtPaymentInitiate;
    private Money amount;

    private String drMemberId;
    private String drAccountNo;
    private String drFundType;

    private String crMemberId;
    private String crAccountNo;
    private String crFundType;

    private String memo;
    private String extension;
}
