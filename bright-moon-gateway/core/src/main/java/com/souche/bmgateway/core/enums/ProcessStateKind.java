package com.souche.bmgateway.core.enums;

/**
 *
 * <p>接授商户平台请求的处理状态</p>
 * @author sirk
 */
public enum ProcessStateKind {
    // 验签通过
    SIGN_PASS,
    //参数验证通过
    VERIFY_PASS,
    //业务可接受
    ACCEPT,
    //没通过
    REFUND,

}
