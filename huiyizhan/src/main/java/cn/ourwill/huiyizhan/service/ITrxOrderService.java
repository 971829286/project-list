package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.Contract;
import cn.ourwill.huiyizhan.entity.PriceType;
import cn.ourwill.huiyizhan.entity.TrxOrder;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/11 0011 15:19
 * 订单
 * @Version1.0
 */
public interface ITrxOrderService extends IBaseService<TrxOrder>{
    String generateOrderNo(String type);

    Map addOrder(TrxOrder trxOrder);

    List<Contract> getContractList(Integer userId);

    List<Contract> getPhotoContractList(Integer userId);

    Contract getContract(Integer activityNum, Integer userId);

    Contract getPhotoContract(Integer activityNum, Integer userId);

    Contract getContract(Integer activityNum, PriceType priceType, Integer userId);

    Contract getPhotoContract(Integer activityNum, PriceType priceType, Integer userId);

    Map queryOrder(String orderNo);

    Map<String,String> callback(String strXml);

    Map closeOrder(String orderNo);

    Map addOrderTest(TrxOrder trxOrder);

    List<TrxOrder> selectByParams(Map params);

    List<TrxOrder> selectByParamsWithTicket(Map params);

    TrxOrder selectById(Integer id);

    Map addTicketOrder(TrxOrder trxOrder) throws Exception;

    TrxOrder getByNo(String orderNo);

    Integer updateOrderOpenIdByNO(TrxOrder order);

    List<TrxOrder> selectByOpenId(String fromUserName);
}
