package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: yeyinxian
 * @Date: 2018/7/31 下午6:45
 */
@Getter
@Setter
@ToString
public class QueryTradeRequest extends TradeCommonRequest{

    /** 商户网站请求号 **/
    private String request_no;

    /** 操作员 Id **/
    private String operator_id;

}
