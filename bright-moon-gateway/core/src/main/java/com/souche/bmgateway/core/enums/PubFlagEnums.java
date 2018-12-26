package com.souche.bmgateway.core.enums;

public enum PubFlagEnums {

    PUB("0", "对公"),
    PRIVATE("1", "对私");

    private String code;
    private String name;

    PubFlagEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PubFlagEnums findByCode(String code){
        for (PubFlagEnums pubFlagEnum : PubFlagEnums.values()) {
            if (pubFlagEnum.getCode().equals(code)) {
                return pubFlagEnum;
            }
        }
        return null;
    }
}
