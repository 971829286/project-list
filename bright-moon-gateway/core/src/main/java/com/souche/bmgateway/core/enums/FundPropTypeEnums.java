package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 资金属性
 * @author XuJinNiu
 * @since 2018-11-23
 */
@Getter
@AllArgsConstructor
@ToString
public enum FundPropTypeEnums {
	DR("DR","借记资金"),
	CR("CR","贷记资金");

	private String code;

	private String msg;


	public static FundPropTypeEnums getByCode(String code) {
		for (FundPropTypeEnums ls : FundPropTypeEnums.values()) {
			if (ls.code.equalsIgnoreCase(code)) {
				return ls;
			}
		}
		return null;
	}


	public static FundPropTypeEnums getByMsg(String msg) {
		for (FundPropTypeEnums ls : FundPropTypeEnums.values()) {
			if (ls.msg.equalsIgnoreCase(msg)) {
				return ls;
			}
		}
		return null;
	}

	public boolean equals(String code) {
		return getCode().equals(code);
	}


}
