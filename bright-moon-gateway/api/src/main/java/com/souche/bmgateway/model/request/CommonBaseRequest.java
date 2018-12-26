package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString
public class CommonBaseRequest implements Serializable {

    private static final long serialVersionUID = -2224699914655175823L;

    /*** 接口名称 ***/
    private String service;

    /*** 版本号 ***/
    private String version;

    /*** 合作者身份id ***/
    private String partnerId;

    /*** 机构方id ***/
    private String instId;

    /*** 参数编码字符集 ***/
    private String inputCharset;

    /*** 签名 ***/
    private String sign;

    /*** 签名类型 ***/
    private String signType;

    /*** 备注 ***/
    private String memo;
    
}
