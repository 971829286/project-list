package com.souche.bmgateway.core.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: huangbin
 * @Description: 返回码组件对象
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class ResponseInfo {
    /**
     * 处理状态 S：成功，F：失败，U：未知
     */
    private String resultStatus;

    /**
     * 返回码(当resultStatus为S时，该字段必定为0000)
     */
    private String resultCode;

    /**
     * 返回码信息
     * 当resultStatus为S时，该字段可为空
     * 当resultStatus为F或U时，需要描述该错误的原因
     */
    private String resultMsg;

}
