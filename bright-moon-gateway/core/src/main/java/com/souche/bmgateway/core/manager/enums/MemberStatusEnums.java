package com.souche.bmgateway.core.manager.enums;

import lombok.Getter;

/**
 * @author zs.
 *         Created on 18/7/19.
 */
@Getter
public enum MemberStatusEnums {

    UNACTIVE("0", "未激活"), NORMAL("1", "正常"), SLEEP("2", "休眠"), CANCEL("3", "销户");

    /** 代码 */
    private final String code;
    /** 信息 */
    private final String message;

    MemberStatusEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过代码获取枚举项
     * @param code
     * @return
     */
    public static MemberStatusEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (MemberStatusEnums memberStatus : MemberStatusEnums.values()) {
            if (memberStatus.getCode().equals(code)) {
                return memberStatus;
            }
        }

        return null;
    }
}
