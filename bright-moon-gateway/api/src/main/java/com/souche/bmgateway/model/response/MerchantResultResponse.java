package com.souche.bmgateway.model.response;

import com.souche.optimus.common.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 商户入驻结果查询响应
 *
 * @author chenwj
 * @since 2018/07/10
 */
@Getter
@Setter
public class MerchantResultResponse extends CommonResponse {

    /**
     * 网商商户号
     */
    private String outMerchantId;

    /**
     * 入驻成功标识 S：成功 F：失败 P：处理中 U：未知
     */
    private String status;

    public static MerchantResultResponse fail(String code, String msg) {
        MerchantResultResponse response = new MerchantResultResponse();
        response.setSuccess(false);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static MerchantResultResponse success(String status, String outMerchantId) {
        MerchantResultResponse response = new MerchantResultResponse();
        response.setCode("200");
        response.setSuccess(true);
        response.setStatus(status);
        response.setOutMerchantId(outMerchantId);
        return response;
    }

}
