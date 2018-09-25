package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.TrxOrder;
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
    String columns="id, order_no, user_id, activity_id, open_id, transaction_id,prepay_id,prepayid_duetime, from_type,create_ip, type, price, number, amount, fee_type, bank_type, create_time, " +
            "finish_time, transaction_status, order_status,trade_type,code_url,version,sys_type,buyer,buyer_phone,buyer_email,QRCodeTicket_url,over_time";

    @InsertProvider(type = TrxOrderSqlProvider.class,method ="save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int  insertSelective(TrxOrder trxOrder);

    @UpdateProvider(type = TrxOrderSqlProvider.class,method = "update")
    public int updateByPrimaryKeySelective(TrxOrder trxOrder);

    @SelectProvider(type = TrxOrderSqlProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    public List<TrxOrder> findAll();

    @SelectProvider(type = TrxOrderSqlProvider.class,method = "selectByParams")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    List<TrxOrder> selectByParams(Map params);

    @SelectProvider(type = TrxOrderSqlProvider.class,method = "selectByParams")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result(property = "ticketsRecords", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.TicketsRecordMapper.getByOrderId")
            ),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    List<TrxOrder> selectByParamsWithTicket(Map params);

    @SelectProvider(type = TrxOrderSqlProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    public TrxOrder selectByPrimaryKey(Integer id);

    @DeleteProvider(type = TrxOrderSqlProvider.class,method = "deleteById")
    public int deleteByPrimaryKey(Integer id);

    @Select("select "+columns+" from trx_order where order_no = #{orderNo}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    TrxOrder getByOrderNo(String orderNo);

    @Select("select "+columns+" from trx_order where order_no = #{orderNo} for update")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    TrxOrder getByOrderNoWithClock(String orderNo);

    @Update("update trx_order trx set trx.order_status = 9 where trx.prepayid_duetime<NOW() and trx.order_status = 0 and trx.transaction_status = 0")
    int refreshStatus();

    @Update("update trx_order trx set trx.open_id = #{openId} where trx.order_no = #{orderNo}")
    Integer updateOrderOpenIdByNO(TrxOrder order);

    @Select("select "+columns+" from trx_order where open_id = #{fromUserName} order by create_time desc")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
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
            @Result( column = "sys_type",property = "sysType"),
            @Result( column = "buyer",property = "buyer"),
            @Result( column = "buyer_phone",property = "buyerPhone"),
            @Result( column = "buyer_email",property = "buyerEmail"),
            @Result( column = "version",property = "version"),
            @Result( column = "QRCodeTicket_url",property = "QRCodeTicketUrl"),
            @Result( column = "over_time",property = "overTime")
    })
    List<TrxOrder> selectByOpenId(String fromUserName);
}
