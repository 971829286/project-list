package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 实名信息
 *
 * @author zs.
 *         Created on 18/7/20.
 */
@Getter
@Setter
@ToString
public class UserVerifyInfoResponse extends CommonResponse {
    private static final long serialVersionUID = 1233512514273962731L;

    /*** 真实姓名 ***/
    private String accName;

    /*** 身份证号 ***/
    private String idCard;

    /*** 手机号 ***/
    private String phone;
}
