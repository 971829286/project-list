package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class AccountBalanceInfo implements Serializable {
    private static final long serialVersionUID = -3922497263295592238L;

    /*** 账户类型 ***/
    private String accountType;

    /*** 账户属性 ***/
    private String accountIdentity;

    /*** 账号 ***/
    private String accountNo;

    /*** 可用余额 ***/
    private String availableBalance;

    /*** 余额 ***/
    private String balance;

    /*** 冻结余额 ***/
    private String frozenBalance;
}
