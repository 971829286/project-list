package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author yyx
 */
@Setter
@Getter
@ToString
@Deprecated
public class QueryPayRequest extends CommonBaseRequest{

    private static final long serialVersionUID = -8555641534682449788L;
    /**商户网站请求号**/
    private String requestNo;

    /**操作员 Id**/
    private String operatorId;
}
