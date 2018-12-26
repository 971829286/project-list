package com.souche.bmgateway.vo;

import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.core.web.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商户入驻统一业务返回
 *
 * @author chenwj
 * @since 2018/7/25
 */
@Setter
@Getter
@ToString
public class MerchantVO implements Serializable {

    private Boolean success;

    private String code;

    private String msg;

    private String data;

    public static Result<String> fail(String code, String msg) {
        MerchantVO retVO = new MerchantVO();
        retVO.setSuccess(false);
        retVO.setCode(code);
        retVO.setMsg(msg);
        return Result.success(JsonUtils.toJson(retVO));
    }

    public static Result<String> success(String msg, String data) {
        MerchantVO retVO = new MerchantVO();
        retVO.setCode("200");
        retVO.setSuccess(true);
        retVO.setMsg(msg);
        retVO.setData(data);
        return Result.success(JsonUtils.toJson(retVO));
    }

}
