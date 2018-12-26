package com.souche.bmgateway.core.exception;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;

/**
 * 参数校验异常类
 *
 * @author zs.
 *         Created on 18/11/26.
 */
public class ParamValidateException extends Exception {

    private static final long serialVersionUID = 4879753396726753790L;

    private ErrorCodeEnums code;

    public ParamValidateException(ErrorCodeEnums code) {
        super(code.getMessage());
        this.code = code;
    }

    public ParamValidateException(ErrorCodeEnums code, String message) {
        super(code.getMessage() + message);
        this.code = code;
    }

    public ParamValidateException(Throwable cause, ErrorCodeEnums code) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    public String getCode() {
        return code.getCode();
    }
}