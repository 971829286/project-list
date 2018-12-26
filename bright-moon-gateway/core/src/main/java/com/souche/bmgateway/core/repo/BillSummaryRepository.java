package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.BillSummary;
import com.souche.bmgateway.core.domain.SettleTranSumInfo;

import java.util.List;

public interface BillSummaryRepository {

    int queryAndSaveBillSummary(String businessDate, String bizType);

    /**
     * date+type获取记录
     * position,status非必填
     * position目的分页查询
     *
     */
    List<BillSummary> getBillSummaryByType(Integer lastPosition,String bizType, Integer position, Integer requestNum, Integer... status);

    boolean updateBillSummaryStatus(String businessDate, String bizType, Integer status);

    boolean updateBillSummaryStatus(Integer id, Integer status);

    boolean updateBillSummaryStatus(List<Integer> list, Integer status);

    /**
     * 统计从lastPosition往后的所有记录
     */
    SettleTranSumInfo getSettleTranSumInfo(Integer lastPosition, String type);

    boolean updateByPrimaryKey(BillSummary billSummary);

    /**
     * 获取上次代发的id
     *
     * @param businessDate 20180908
     */
    Integer getLastTaskPosition(String businessDate);


    /**
     * 根据批次号统计
     */
    SettleTranSumInfo getSettleTranSumInfoByDeductionDate(String businessDate, String type);

}
