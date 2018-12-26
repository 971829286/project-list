package com.souche.bmgateway.core.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @Author: huangbin
 * @Description: 开放平台http请求返回参数
 * @Date: Created in 2018/07/11
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class HttpBaseResponse {
    private boolean success;
    private String code;
    private String msg;
    private Map<String, Object> data;
}
