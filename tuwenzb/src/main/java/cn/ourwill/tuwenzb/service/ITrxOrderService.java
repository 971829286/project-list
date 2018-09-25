package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/11 0011 15:19
 * 订单
 * @Version1.0
 */
public interface ITrxOrderService extends IBaseService<TrxOrder>{

    Map addOrder(TrxOrder trxOrder);

    List<Contract> getContractList(Integer userId);

    List<Contract> getPhotoContractList(Integer userId);

    Contract getContract(Integer activityNum, Integer type, Integer userId);

    Contract getPhotoContract(Integer activityNum, Integer type, Integer userId);

    Contract getContract(Integer activityNum, PriceType priceType, Integer userId);

    Contract getPhotoContract(Integer activityNum, PriceType priceType, Integer userId);

    Map queryOrder(String orderNo);

    Map<String,String> callback(String strXml);

    Map closeOrder(String orderNo);

    Map addOrderTest(TrxOrder trxOrder);

    List<TrxOrder> selectByParams(Map params);

    TrxOrder selectById(Integer id);

    void refreshStatus();

    Map callbackReward(String strXml);

    Map addReward(TrxOrder trxOrder,Integer userId,Integer activityId);

    Map withdrawDeposit(WithdrawalOrder withdrawalOrder);

    FundStatistics getTotalAmount (Integer userId);

    List<FundDetail> selectFundListById(Integer userId);

    List<FundDetail> getRewardList(Integer activityId);
}
