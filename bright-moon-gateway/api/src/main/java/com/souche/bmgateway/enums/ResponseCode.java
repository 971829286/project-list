package com.souche.bmgateway.enums;

/**
 * 响应枚举集
 *
 * @since 2018/07/10
 */
public enum ResponseCode {

    SUCCESS("0", "处理成功"),
    NEW_SUCCESS("S0001", "处理成功"),
    NO_QUERY_RESULT("001", "无查询结果"),
    ARGUMENT_ERROR("002", "参数错误"),
    SIGN_CHECK_FAILURE("003", "参数签名错误"),
    TRANS_AUTH_FAILURE("004", "接口权限错误"),
    INTERFACE_COMPATIBALE_ERROR("005", "接口兼容错误"),
    OPERATION_FAILURE("006", "处理失败"),
    EXCEED_COUNT_LIMIT("007", "超过允许的最大个数"),
    DUPLICATE_RECORD("008", "重复记录"),
    NO_EXIST_RECORD("009", "不存在的记录"),
    INVOK_SUCCESS("S0001", "调用成功"),
    APPLY_SUCCESS("S0101", "提交申请成功"),
    INVOK_FAILURE("F0001", "调用失败");

    /**
     * 代码
     */
    private final String code;
    /**
     * 信息
     */
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static ResponseCode getByCode(String code) {
        if (code == null) {
            return null;
        }

        for (ResponseCode responseCode : ResponseCode.values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return null;
    }

    public boolean equalsByCode(String code) {
        return this.code.equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
