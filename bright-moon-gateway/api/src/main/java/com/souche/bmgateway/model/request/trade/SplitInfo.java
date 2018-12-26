package com.souche.bmgateway.model.request.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 分润集合
 *
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString
public class SplitInfo implements Serializable {

    /*** 分润钱包id ***/
    @NotBlank
    private String memberId;

    /*** 账户类型 ***/
    @NotBlank
    private String accountType;

    /*** 账户标识 ***/
    private String accountIdentity;

    /*** 分润金额 ***/
    @NotBlank
    private String amount;

    /*** 分润规则，保留字段 ***/
    private String royaltyRule;
}
