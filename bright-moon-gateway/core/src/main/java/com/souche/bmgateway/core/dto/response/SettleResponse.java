package com.souche.bmgateway.core.dto.response;

import com.souche.bmgateway.enums.ResponseCode;

public class SettleResponse {
    private Boolean success;
    private String code;
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static SettleResponse createSuccessResponse() {
        SettleResponse response = new SettleResponse();
        response.setSuccess(true);
        response.setCode(ResponseCode.SUCCESS.getCode());
        return response;
    }

    public static SettleResponse createFailResponse(String code, String msg) {
        SettleResponse response = new SettleResponse();
        response.setSuccess(false);
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }

    public static SettleResponse createResponse(Boolean result, String code, String msg) {
        if(result) {
            return createSuccessResponse();
        }
        return createFailResponse(code, msg);
    }

    @Override
    public String toString() {
        return "SettleResponse{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
