package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.TrxOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/10 0010 14:44
 * @Version1.0
 */
@Repository
public interface TrxOrderMapper extends IBaseMapper<TrxOrder>{
    String columns="id, order_no, user_id, open_id, transaction_id,prepay_id,prepayid_duetime, from_type,create_ip, type, price, number, amount, fee_type, bank_type, create_time, " +
            "finish_time, transaction_status, order_status,trade_type,code_url,photo_live, version,deal_type";

    @InsertProvider(type = TrxOrderMapperProvider.class,method ="save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer save(TrxOrder trxOrder);

    @UpdateProvider(type = TrxOrderMapperProvider.class,method = "update")
    public Integer update(TrxOrder trxOrder);

    @SelectProvider(type = TrxOrderMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "open_id",property = "openId"),
            @Result( column = "transaction_id",property = "transactionId"),
            @Result( column = "prepay_id",property = "prepayId"),
            @Result( column = "prepayid_duetime",property = "prepayIdDueTime"),
            @Result( column = "from_type",property = "fromType"),
            @Result( column = "create_ip",property = "createIp"),
            @Result( column = "type",property = "type"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "fee_type",property = "feeType"),
            @Result( column = "bank_type",property = "bankType"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "transaction_status",property = "transactionStatus"),
            @Result( column = "order_status",property = "orderStatus"),
            @Result( column = "trade_type",property = "tradeType"),
            @Result( column = "code_url",property = "codeUrl"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "version",property = "version"),
            @Result( column = "deal_type",property = "dealType")
    })
    public List<TrxOrder> findAll();

    @SelectProvider(type = TrxOrderMapperProvider.class,method = "selectByParams")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "username",property = "username"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "open_id",property = "openId"),
            @Result( column = "transaction_id",property = "transactionId"),
            @Result( column = "prepay_id",property = "prepayId"),
            @Result( column = "prepayid_duetime",property = "prepayIdDueTime"),
            @Result( column = "from_type",property = "fromType"),
            @Result( column = "create_ip",property = "createIp"),
            @Result( column = "type",property = "type"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "fee_type",property = "feeType"),
            @Result( column = "bank_type",property = "bankType"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "transaction_status",property = "transactionStatus"),
            @Result( column = "order_status",property = "orderStatus"),
            @Result( column = "trade_type",property = "tradeType"),
            @Result( column = "code_url",property = "codeUrl"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "version",property = "version"),
            @Result( column = "deal_type",property = "dealType")
    })
    List<TrxOrder> selectByParams(Map params);

    @SelectProvider(type = TrxOrderMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "open_id",property = "openId"),
            @Result( column = "transaction_id",property = "transactionId"),
            @Result( column = "prepay_id",property = "prepayId"),
            @Result( column = "prepayid_duetime",property = "prepayIdDueTime"),
            @Result( column = "from_type",property = "fromType"),
            @Result( column = "create_ip",property = "createIp"),
            @Result( column = "type",property = "type"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "fee_type",property = "feeType"),
            @Result( column = "bank_type",property = "bankType"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "transaction_status",property = "transactionStatus"),
            @Result( column = "order_status",property = "orderStatus"),
            @Result( column = "trade_type",property = "tradeType"),
            @Result( column = "code_url",property = "codeUrl"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "version",property = "version"),
            @Result( column = "deal_type",property = "dealType")
    })
    public TrxOrder getById(Integer id);

    @DeleteProvider(type = TrxOrderMapperProvider.class,method = "deleteById")
    public Integer delete(Map param);

    @Select("select "+columns+" from trx_order where order_no = #{orderNo}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "open_id",property = "openId"),
            @Result( column = "transaction_id",property = "transactionId"),
            @Result( column = "prepay_id",property = "prepayId"),
            @Result( column = "prepayid_duetime",property = "prepayIdDueTime"),
            @Result( column = "from_type",property = "fromType"),
            @Result( column = "create_ip",property = "createIp"),
            @Result( column = "type",property = "type"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "fee_type",property = "feeType"),
            @Result( column = "bank_type",property = "bankType"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "transaction_status",property = "transactionStatus"),
            @Result( column = "order_status",property = "orderStatus"),
            @Result( column = "trade_type",property = "tradeType"),
            @Result( column = "code_url",property = "codeUrl"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "version",property = "version"),
            @Result( column = "deal_type",property = "dealType")
    })
    TrxOrder getByOrderNo(String orderNo);

    @Select("select "+columns+" from trx_order where order_no = #{orderNo} for update")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "open_id",property = "openId"),
            @Result( column = "transaction_id",property = "transactionId"),
            @Result( column = "prepay_id",property = "prepayId"),
            @Result( column = "prepayid_duetime",property = "prepayIdDueTime"),
            @Result( column = "from_type",property = "fromType"),
            @Result( column = "create_ip",property = "createIp"),
            @Result( column = "type",property = "type"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "fee_type",property = "feeType"),
            @Result( column = "bank_type",property = "bankType"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "transaction_status",property = "transactionStatus"),
            @Result( column = "order_status",property = "orderStatus"),
            @Result( column = "trade_type",property = "tradeType"),
            @Result( column = "code_url",property = "codeUrl"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "version",property = "version"),
            @Result( column = "deal_type",property = "dealType")
    })
    TrxOrder getByOrderNoWithClock(String orderNo);

    @Update("update trx_order trx set trx.order_status = 9 where trx.prepayid_duetime<NOW() and trx.order_status = 0 and trx.transaction_status = 0")
    int refreshStatus();
}
