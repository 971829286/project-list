package com.souche.bmgateway.core.enums;

/**
 * @author WKL
 */

public enum StatusEnums {

    INITIAL(0, "初始"),
    HANDING(1, "处理中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败"),
    PART_FAIL(4, "部分失败"),
    CONFIG_ERROR(5, "配置错误");

    private Integer code;
    private String name;

    StatusEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static StatusEnums findByCode(Integer code){
        for (StatusEnums statusEnum : StatusEnums.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
