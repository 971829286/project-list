package com.souche.bmgateway.core.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/8/4.
 */
@Getter
@Setter
@ToString
public class AuthRealNameResponse implements Serializable {

    /*** 实名结果 ***/
    private boolean isSuccess;

    /*** 实名认证code ***/
    private String code;

    /*** 实名认证信息 ***/
    private String msg;
}
