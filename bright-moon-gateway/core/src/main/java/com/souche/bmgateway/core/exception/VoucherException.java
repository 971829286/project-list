package com.souche.bmgateway.core.exception;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;

/**
 * @author XuJinNiu
 * @since 2018-12-18
 */
public class VoucherException extends Exception {

	private static final long serialVersionUID = 4879753396726753790L;


	private ErrorCodeEnums code;

	public VoucherException(ErrorCodeEnums code) {
		this.code = code;
	}

	public VoucherException(ErrorCodeEnums code, String message) {
		super(message);
		this.code = code;
	}

	public VoucherException(Throwable cause, ErrorCodeEnums code) {
		super(cause);
		this.code = code;
	}

	public ErrorCodeEnums getCode() {
		return code;
	}
}
