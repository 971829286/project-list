package com.souche.bmgateway.core.repo;


import com.souche.bmgateway.core.domain.OrderShareApply;

import java.util.List;

/**
 * @Author: huangbin
 * @Description: 分账申请数据仓库服务
 * @Date: Created in 2018/07/17
 * @Modified By:
 */
public interface OrderShareApplyRepository {
    boolean saveOne(OrderShareApply orderShareApply);

    boolean updateApplyStatus(String outTradeNo, String applyStatus);

    boolean updateNotifyResult(String outTradeNo, String notifyStatus, String shareDate);

    boolean updateShareOrderNo(String outTradeNo, String shareOrderNo);

    boolean updateShareApplyResult(String outTradeNo, String shareOrderNo, String applyStatus, String extension);

    List<OrderShareApply> selectNotSuccessStatus(int queryTimes, int count);

    boolean updateByPrimaryKey(OrderShareApply orderShareApply);

    OrderShareApply selectByOutTradeNo(String outTradeNo);
}
