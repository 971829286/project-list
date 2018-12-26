package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商户入驻结果查询
 *
 * @author chenwj
 * @since 2018/7/25
 */
@Getter
@Setter
@ToString
public class MerchantResultQueryRequest extends CommonRequest {

    /**
     * 申请单号
     */
    private String orderNo;

}
