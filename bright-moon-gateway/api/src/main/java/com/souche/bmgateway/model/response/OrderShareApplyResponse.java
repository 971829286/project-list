package com.souche.bmgateway.model.response;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: huangbin
 * @Description: 分帐请求返回
 * @Date: Created in 2018/07/23
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class OrderShareApplyResponse extends CommonResponse {

    /**
     * 外部交易号
     */
    private String OutTradeNo;
    /**
     * 网商分账订单号
     */
    private String ShareOrderNo;

}
