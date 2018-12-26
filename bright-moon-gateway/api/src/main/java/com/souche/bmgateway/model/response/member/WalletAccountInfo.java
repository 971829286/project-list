package com.souche.bmgateway.model.response.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/11/20.
 */
@Getter
@Setter
@ToString
public class WalletAccountInfo implements Serializable {

    /*** 钱包id ***/
    private String memberId;

    /*** 账户类型 ***/
    private Long accountType;

    /*** 账户id ***/
    private String accountId;

    /*** 账户别名 ***/
    private String alias;

    /*** 账户属性 ***/
    private Long accountAttribute;

    /*** 扩展参数 ***/
    private String extention;
}
