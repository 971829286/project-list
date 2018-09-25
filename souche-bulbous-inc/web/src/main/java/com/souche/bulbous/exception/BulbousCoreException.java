package com.souche.bulbous.exception;

/**
 * 核心业务异常
 */
public class BulbousCoreException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BulbousCoreException() {
        super();
    }

    public BulbousCoreException(String message) {
        super(message);
    }

    public BulbousCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BulbousCoreException(Throwable cause) {
        super(cause);
    }

    public String getErrorMessage() {
        return "系统内部异常";
    }
}
