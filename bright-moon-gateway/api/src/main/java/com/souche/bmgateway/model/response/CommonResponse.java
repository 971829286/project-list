package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商户入驻结果查询响应
 *
 * @author zs
 * @since 2018/07/10
 */
@Getter
@Setter
@ToString
public class CommonResponse implements Serializable {

    /*** 结果标识 ***/
    private boolean isSuccess = true;

    /*** 结果代码 ***/
    private String code = "200";

    /*** 详细信息 ***/
    private String msg = "处理成功";

    public static CommonResponse createResp(boolean isSuccess, String code, String msg) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccess(isSuccess);
        commonResponse.setMsg(msg);
        commonResponse.setCode(code);
        return commonResponse;
    }

    public static CommonResponse createFailResp(String code, String msg) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccess(false);
        commonResponse.setMsg(msg);
        commonResponse.setCode(code);
        return commonResponse;
    }

    public static CommonResponse createSuccessResp() {
        return new CommonResponse();
    }

    public static <T extends CommonResponse> T createFailResp(T instance, String code, String msg) {
        instance.setSuccess(false);
        instance.setMsg(msg);
        instance.setCode(code);
        return instance;
    }

    public static <T extends CommonResponse> T createSuccessResp(T instance) {
        return instance;
    }

}
