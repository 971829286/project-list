package com.souche.bmgateway.core.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/7/19.
 */
@Getter
@Setter
@ToString
public class MemberActiveResponse implements Serializable {
    private static final long serialVersionUID = -322501393946994903L;

    /*** 会员id ***/
    private String memberId;

    /*** 账户id ***/
    private String accountId;
}
