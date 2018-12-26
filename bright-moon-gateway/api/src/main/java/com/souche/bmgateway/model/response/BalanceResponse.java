package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class BalanceResponse extends CommonResponse {
    private static final long serialVersionUID = 5853033317860634759L;

    /*** 会员各账户余额列表 ***/
    private List<AccountBalanceInfo> accountList;
}
