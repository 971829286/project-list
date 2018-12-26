package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.BillFlow;
import com.souche.bmgateway.core.exception.DaoException;

import java.util.List;

/**
 * @author zhaojian
 * @date 18/7/23
 */
public interface BillFlowRepository {

    /**
     * 根据条件查询
     *
     * @param record
     * @return
     * @throws DaoException
     */
    BillFlow selectSelective(BillFlow record) throws DaoException;


    /**
     * 插入
     *
     * @param record
     * @return
     * @throws DaoException
     */
    void insertSelective(BillFlow record) throws DaoException;


    List<BillFlow> getBillFlowList(String businessDate, String bizType);

    int insert(BillFlow billFlow);

}
