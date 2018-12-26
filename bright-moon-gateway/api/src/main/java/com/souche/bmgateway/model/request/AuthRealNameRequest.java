package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 *         Created on 18/8/4.
 */
@Getter
@Setter
@ToString
public class AuthRealNameRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -2067768782174912268L;

    /*** 请求号 ***/
    private String requestNo;

    /*** 真实姓名 ***/
    private String realName;

    /*** 身份证号 ***/
    private String idCard;
}
