package com.souche.bmgateway.core.dto.response;

/**
 * 会员统一响应集
 *
 * @author: chenwj
 * @since: 2018/7/18
 */
public class MemberResponse {

    /**
     * 是否成功
     */
    private boolean success = false;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 结果返回信息
     */
    private String resultMessage;

    public MemberResponse(boolean success, String errorCode, String resultMessage) {
        super();
        this.success = success;
        this.resultMessage = resultMessage;
        this.errorCode = errorCode;
    }

    public static MemberResponse fail(String errorCode, String message) {
        return new MemberResponse(false, errorCode, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                '}';
    }

}
