package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公共请求参数
 *
 * @author chenwj
 * @since 2018/8/21
 */
@Getter
@Setter
@ToString
public class CommonRequest {

    /**
     * 接口名称
     */
    private String function;

}
