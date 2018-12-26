package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhaojian
 */

@Getter
@AllArgsConstructor
public enum FilePathEnums {

    // 文件生成模板
    TEMPLATE("/template", "template"),

    // 临时文件
    TEMP("/temp", "temp"),

    // 文件解析脚本
    SCRIPT("/script", "script");

    private String code;

    private String message;


    public static FilePathEnums getByCode(Byte code) {

        if (code != null) {
            FilePathEnums[] $enums = FilePathEnums.values();

            for (FilePathEnums $enum : $enums) {
                if ($enum.getCode().equals(code)) {
                    return $enum;
                }
            }
        }

        return null;
    }
}
