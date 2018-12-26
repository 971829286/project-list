/**
 * 
 */
package com.souche.bmgateway.enums;


/**
 * <p>注释</p>
 * @author fangjilue
 * @version $Id: ActivateStatus.java, v 0.1 2014-1-13 下午12:53:15 fangjilue Exp $
 */
public enum ActivateStatus {

    NOTACTIVATED(0, "会员未激活，不开赋值账户"), ACTIVATED(1, "会员激活，开赋值基本户"), ACTIVATED_ALL(2,"会员账户都激活");

    /** 代码 */
    private final Integer code;
    /** 信息 */
    private final String  message;

    ActivateStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过代码获取枚举项
     * @param code
     * @return
     */
    public static ActivateStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (ActivateStatus le : ActivateStatus.values()) {
            if (le.getCode().equals(code)) {
                return le;
            }
        }

        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
