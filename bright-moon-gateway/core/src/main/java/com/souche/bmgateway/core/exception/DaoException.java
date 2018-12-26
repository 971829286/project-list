package com.souche.bmgateway.core.exception;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import lombok.Getter;

import java.io.Serializable;

/**
 * Dao异常
 *
 * @author: chenwj
 * @since: 2018/7/19
 */
@Getter
public class DaoException extends Exception implements Serializable {

    private ErrorCodeEnums code;

    public DaoException(ErrorCodeEnums code) {
        super(code.getMessage());
        this.code = code;
    }

    public DaoException(ErrorCodeEnums code, String message) {
        super(message);
        this.code = code;
    }

    public DaoException(Throwable cause, ErrorCodeEnums code) {
        super(code.getMessage(), cause);
        this.code = code;
    }

}

