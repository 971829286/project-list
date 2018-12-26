package com.souche.bmgateway.core.manager.share;

import com.souche.bmgateway.core.dto.request.OrderShareQueryRequest;
import com.souche.bmgateway.core.dto.response.OrderShareQueryResponse;

/**
 * @Author: huangbin
 * @Description: 单笔分帐结果查询服务
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
public interface OrderShareQueryService {
    OrderShareQueryResponse doQuery(OrderShareQueryRequest orderShareQueryRequest);
}
