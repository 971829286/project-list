package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.OrderShareApplyMapper;
import com.souche.bmgateway.core.domain.OrderShareApply;
import com.souche.bmgateway.core.repo.OrderShareApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: huangbin
 * @Description: 分账申请数据仓库服务实现
 * @Date: Created in 2018/07/17
 * @Modified By:
 */
@Repository
public class OrderShareApplyRepositoryImpl implements OrderShareApplyRepository {

    @Autowired
    private OrderShareApplyMapper orderShareApplyMapper;

    @Override
    public boolean saveOne(OrderShareApply orderShareApply) {
        int num = orderShareApplyMapper.insert(orderShareApply);
        return num > 0 ? true : false;
    }

    @Override
    public boolean updateApplyStatus(String outTradeNo, String applyStatus) {
        int num = orderShareApplyMapper.updateApplyStatusByOutTradeNo(outTradeNo, applyStatus);
        return num > 0 ? true : false;
    }

    @Override
    public boolean updateNotifyResult(String outTradeNo, String notifyStatus, String shareDate) {
        int num = orderShareApplyMapper.updateShareNotifyResult(outTradeNo, notifyStatus, shareDate);
        return num > 0 ? true : false;
    }

    @Override
    public boolean updateShareOrderNo(String outTradeNo, String shareOrderNo) {
        int num = orderShareApplyMapper.updateShareOrderNo(outTradeNo, shareOrderNo);
        return num > 0 ? true : false;
    }

    @Override
    public boolean updateShareApplyResult(String outTradeNo, String shareOrderNo, String applyStatus, String extension) {
        int num = orderShareApplyMapper.updateShareApplyResult(outTradeNo, shareOrderNo, applyStatus, extension);
        return num > 0 ? true : false;
    }

    @Override
    public List<OrderShareApply> selectNotSuccessStatus(int queryTimes, int count) {
        return orderShareApplyMapper.selectNotSuccessStatus(queryTimes, count);
    }

    @Override
    public boolean updateByPrimaryKey(OrderShareApply orderShareApply) {
        int num = orderShareApplyMapper.updateByPrimaryKey(orderShareApply);
        return num > 0 ? true : false;
    }

    @Override
    public OrderShareApply selectByOutTradeNo(String outTradeNo) {
        return orderShareApplyMapper.selectByOutTradeNo(outTradeNo);
    }
}
