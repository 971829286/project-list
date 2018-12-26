package com.souche.bmgateway.core.enums;

public enum SettleTypeEnums {

    PAY(1,"付款"),
    ALLOCATION(2, "调拨"),
    AGENCY(3, "代发");

    private Integer code;
    private String name;

    SettleTypeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SettleTypeEnums findByCode(Integer code){
        for (SettleTypeEnums tradeTypeEnum : SettleTypeEnums.values()) {
            if (tradeTypeEnum.getCode().equals(code)) {
                return tradeTypeEnum;
            }
        }
        return null;
    }
}
