package com.souche.bmgateway.core.enums;

public enum BillTypeEnums {
    //8位
    Grand("Grand", "广汇通联");

    private String code;
    private String name;

    BillTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BillTypeEnums findByCode(String code){
        for (BillTypeEnums billTypeEnum : BillTypeEnums.values()) {
            if (billTypeEnum.getCode().equals(code)) {
                return billTypeEnum;
            }
        }
        return null;
    }
}
