package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询实名信息
 * @author zs.
 *         Created on 18/7/23.
 */
@Getter
@Setter
@ToString
public class QueryVerifyInfoRequest extends CommonBaseRequest {
    private static final long serialVersionUID = 2609029756184944922L;

    /*** 会员编号 ***/
    private String memberId;
}