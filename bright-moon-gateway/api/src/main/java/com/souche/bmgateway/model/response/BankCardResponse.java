package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class BankCardResponse extends CommonResponse {
    private static final long serialVersionUID = -3727334718229835189L;

    /*** 绑卡返回id ***/
    private String bankcardId;
}
