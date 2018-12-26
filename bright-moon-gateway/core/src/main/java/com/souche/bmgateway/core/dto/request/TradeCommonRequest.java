package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: yeyinxian
 * @Date: 2018/7/31 下午6:00
 */
@Getter
@Setter
@ToString
public class TradeCommonRequest {
    //接口名称
    private String service;
    //版本号
    private String version;
    //合作者身份id
    private String partner_id;
    //参数编码字符集
    private String _input_charset;
    //签名
    private String sign;
    //签名类型
    private String sign_type;
    //页面跳转同步返回页面路径
    private String return_url;
    //备注
    private String memo;
    //扩展参数
    private String extension;
}
