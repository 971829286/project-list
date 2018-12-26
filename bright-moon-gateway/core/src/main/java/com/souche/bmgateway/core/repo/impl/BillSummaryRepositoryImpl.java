package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.BillSummaryMapper;
import com.souche.bmgateway.core.domain.BillSummary;
import com.souche.bmgateway.core.domain.SettleTranSumInfo;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class BillSummaryRepositoryImpl implements BillSummaryRepository {

    @Resource
    private BillSummaryMapper billSummaryMapper;

    @Override
    public int queryAndSaveBillSummary(String businessDate, String bizType) {
        //insert into on duplicate key update返回的是尝试插入+更新的影响行数
        String batchId = com.souche.bmgateway.core.util.UUIDUtil.getTimeID();
        return billSummaryMapper.insertBillSummary(businessDate,bizType, batchId);
    }

    @Override
    public List<BillSummary> getBillSummaryByType(Integer lastPosition, String type, Integer position, Integer requestNum,
                                                  Integer... status) {
        List<Integer> statusList = new ArrayList<>();
        Collections.addAll(statusList, status);
        List<BillSummary> billSummaryList =
                billSummaryMapper.getBillSummaryByType(lastPosition, type, position, requestNum, statusList);
        if (CollectionUtils.isEmpty(billSummaryList)) {
            return new ArrayList<>(0);
        }
        return billSummaryList;
    }

    @Override
    public boolean updateBillSummaryStatus(String date, String type, Integer status) {
        return billSummaryMapper.updateStatusByType(date, type, status) > 0;
    }

    @Override
    public boolean updateBillSummaryStatus(Integer id, Integer status) {
        BillSummary summary = new BillSummary();
        summary.setId(id);
        summary.setTradeStatus(status);
        return billSummaryMapper.updateByPrimaryKeySelective(summary) > 0;
    }

    @Override
    public boolean updateBillSummaryStatus(List<Integer> list, Integer status) {
        return billSummaryMapper.updateStatusByIDList(list, status) > 0;
    }

    @Override
    public SettleTranSumInfo getSettleTranSumInfo(Integer lastPosition, String type) {
        return billSummaryMapper.getSettleTranSumInfo(lastPosition, type);
    }

    @Override
    public boolean updateByPrimaryKey(BillSummary billSummary) {
        return billSummaryMapper.updateByPrimaryKey(billSummary) > 0;
    }

    /**
     * 获取上次代发的id
     *
     * @param businessDate 20180908
     */
    @Override
    public Integer getLastTaskPosition(String businessDate) {
        return billSummaryMapper.getLastTaskPosition(businessDate);
    }

    /**
     * 根据批次号统计
     */
    @Override
    public SettleTranSumInfo getSettleTranSumInfoByDeductionDate(String businessDate, String type) {
        return billSummaryMapper.getSettleTranSumInfoByDeductionDate(businessDate, type);
    }

}
