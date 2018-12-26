package com.souche.bmgateway.core.enums;

public enum SourceEnums {
    //10位
    TLBILL("TLBILL", "通联对账文件");

    private String code;
    private String name;

    SourceEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SourceEnums findByCode(String code){
        for (SourceEnums sourceEnum : SourceEnums.values()) {
            if (sourceEnum.getCode().equals(code)) {
                return sourceEnum;
            }
        }
        return null;
    }
}
