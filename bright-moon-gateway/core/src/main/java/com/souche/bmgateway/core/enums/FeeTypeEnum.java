package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 手续费类型
 *
 * @author chenwj
 * @since 2018/7/16
 */
@Getter
@AllArgsConstructor
public enum FeeTypeEnum {

    /**
     * t0收单手续费
     */
    T0("01", "t0收单手续费"),

    /**
     * t1收单手续费
     */
    T1("02", "t1收单手续费");

    private String feeCode;
    private String feeDesc;

}
