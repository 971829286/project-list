package com.souche.bulbous.exception;

import org.apache.commons.lang3.StringUtils;

public class CustomException extends BulbousCoreException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
        this.msg = message;
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    @Override
    public String getErrorMessage() {
        return StringUtils.isEmpty(msg) ? "系统内部异常" : msg;
    }
}
