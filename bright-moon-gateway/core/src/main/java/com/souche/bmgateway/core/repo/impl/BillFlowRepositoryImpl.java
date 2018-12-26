package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.BillFlowMapper;
import com.souche.bmgateway.core.domain.BillFlow;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.DaoException;
import com.souche.bmgateway.core.repo.BillFlowRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaojian
 * @date 18/7/23
 */

@Repository
public class BillFlowRepositoryImpl implements BillFlowRepository {


    @Resource
    private BillFlowMapper billFlowMapper;

    @Override
    public BillFlow selectSelective(BillFlow record) throws DaoException {
        if (Objects.isNull(record)) {
            throw new DaoException(ErrorCodeEnums.DAO_OPERATION_ERROR, "查询通联对账数据参数不能为空!");
        }
        return billFlowMapper.selectSelective(record);
    }

    @Override
    public void insertSelective(BillFlow record) throws DaoException {
        if (Objects.isNull(record)) {
            throw new DaoException(ErrorCodeEnums.DAO_OPERATION_ERROR, "插入通联对账数据参数不能为空!");
        }
        billFlowMapper.insertSelective(record);
    }

    @Override
    public List<BillFlow> getBillFlowList(String businessDate, String bizType) {
        return billFlowMapper.selectListByBizType(businessDate, bizType, null);
    }

    @Override
    public int insert(BillFlow billFlow) {
        return billFlowMapper.insert(billFlow);
    }

}
