package com.souche.bmgateway.core.exception;


import com.souche.bmgateway.core.enums.ErrorCodeEnums;

/**
 * 外部调用异常类
 * @author zs.
 *         Created on 18/7/18.
 */
public class ManagerException extends Exception {

    private static final long serialVersionUID = 4879753396726753790L;

    private ErrorCodeEnums code;

    public ManagerException(ErrorCodeEnums code) {
        super(code.getMessage());
        this.code = code;
    }

    public ManagerException(ErrorCodeEnums code, String message) {
        super(message);
        this.code = code;
    }

    public ManagerException(Throwable cause, ErrorCodeEnums code) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    public String getCode() {
        return code.getCode();
    }
}
