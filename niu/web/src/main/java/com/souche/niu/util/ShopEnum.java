package com.souche.niu.util;

public enum ShopEnum {
	PARAM_ERROR("400", "param error"),
	SYSTEM_ERROR("500", "system error"),
	SUCCESS("200", "ok"),
	NOT_FOUND("404", "not found");

	private String code;
	private String desc;

	ShopEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
