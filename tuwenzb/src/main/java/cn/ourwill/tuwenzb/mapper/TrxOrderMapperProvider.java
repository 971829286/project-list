package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.TrxOrder;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/10 0010 14:44
 * @Version1.0
 */
public class TrxOrderMapperProvider {

    //所有的列名
    private String columns="id, order_no, user_id, open_id, transaction_id,prepay_id,prepayid_duetime, from_type,create_ip, type, price, number, amount, fee_type, bank_type, create_time, " +
            "finish_time, transaction_status, order_status, trade_type,code_url,photo_live, version,deal_type";
    private String columnsAlis="trx.id, trx.order_no, trx.user_id, trx.open_id, trx.transaction_id, trx.prepay_id, trx.prepayid_duetime, trx.from_type, trx.create_ip, trx.type, trx.price, " +
            "trx.number, trx.amount, trx.fee_type, trx.bank_type, trx.create_time, trx.finish_time, trx.transaction_status, trx.order_status, trx.trade_type, trx.code_url, trx.photo_live,trx.version,trx.deal_type";
    //保存
    public String save(final TrxOrder trxOrder){
        return new SQL(){
            {
                INSERT_INTO("trx_order");
                if(trxOrder.getOrderNo()!=null){
                    VALUES("order_no","#{orderNo}");
                }
                if(trxOrder.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(trxOrder.getOpenId()!=null){
                    VALUES("open_id","#{openId}");
                }
                if(trxOrder.getTransactionId()!=null){
                    VALUES("transaction_id","#{transactionId}");
                }
                if(trxOrder.getPrepayId()!=null){
                    VALUES("prepay_id","#{prepayId}");
                }
                if(trxOrder.getPrepayIdDueTime()!=null){
                    VALUES("prepayid_duetime","#{prepayIdDueTime}");
                }
                if(trxOrder.getFromType()!=null){
                    VALUES("from_type","#{fromType}");
                }
                if(trxOrder.getCreateIp()!=null){
                    VALUES("create_ip","#{createIp}");
                }
                if(trxOrder.getType()!=null){
                    VALUES("type","#{type}");
                }
                if(trxOrder.getPrice()!=null){
                    VALUES("price","#{price}");
                }
                if(trxOrder.getNumber()!=null){
                    VALUES("number","#{number}");
                }
                if(trxOrder.getAmount()!=null){
                    VALUES("amount","#{amount}");
                }
                if(trxOrder.getFeeType()!=null){
                    VALUES("fee_type","#{feeType}");
                }
                if(trxOrder.getBankType()!=null){
                    VALUES("bank_type","#{bankType}");
                }
                if(trxOrder.getCreateTime()!=null){
                    VALUES("create_time","#{createTime}");
                }
                if(trxOrder.getFinishTime()!=null){
                    VALUES("finish_time","#{finishTime}");
                }
                if(trxOrder.getTransactionStatus()!=null){
                    VALUES("transaction_status","#{transactionStatus}");
                }
                if(trxOrder.getOrderStatus()!=null){
                    VALUES("order_status","#{orderStatus}");
                }
                if(trxOrder.getTradeType()!=null){
                    VALUES("trade_type","#{tradeType}");
                }
                if(trxOrder.getCodeUrl()!=null){
                    VALUES("code_url","#{codeUrl}");
                }
                if(trxOrder.getPhotoLive()!=null){
                    VALUES("photo_live","#{photoLive}");
                }
                if(trxOrder.getVersion()!=null){
                    VALUES("version","#{version}");
                }
                if(trxOrder.getDealType()!=null){
                    VALUES("deal_type","#{dealType}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final TrxOrder trxOrder){
        return new SQL(){
            {
                UPDATE("trx_order");
                if(trxOrder.getOrderNo()!=null){
                    SET("order_no=#{orderNo}");
                }
                if(trxOrder.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(trxOrder.getOpenId()!=null){
                    SET("open_id=#{openId}");
                }
                if(trxOrder.getTransactionId()!=null){
                    SET("transaction_id=#{transactionId}");
                }
                if(trxOrder.getPrepayId()!=null){
                    SET("prepay_id=#{prepayId}");
                }
                if(trxOrder.getPrepayIdDueTime()!=null){
                    SET("prepayid_duetime=#{prepayIdDueTime}");
                }
                if(trxOrder.getFromType()!=null){
                    SET("from_type=#{fromType}");
                }
                if(trxOrder.getCreateIp()!=null){
                    SET("create_ip=#{createIp}");
                }
                if(trxOrder.getType()!=null){
                    SET("type=#{type}");
                }
                if(trxOrder.getPrice()!=null){
                    SET("price=#{price}");
                }
                if(trxOrder.getNumber()!=null){
                    SET("number=#{number}");
                }
                if(trxOrder.getAmount()!=null){
                    SET("amount=#{amount}");
                }
                if(trxOrder.getFeeType()!=null){
                    SET("fee_type=#{feeType}");
                }
                if(trxOrder.getBankType()!=null){
                    SET("bank_type=#{bankType}");
                }
                if(trxOrder.getCreateTime()!=null){
                    SET("create_time=#{createTime}");
                }
                if(trxOrder.getFinishTime()!=null){
                    SET("finish_time=#{finishTime}");
                }
                if(trxOrder.getTransactionStatus()!=null){
                    SET("transaction_status=#{transactionStatus}");
                }
                if(trxOrder.getOrderStatus()!=null){
                    SET("order_status=#{orderStatus}");
                }
                if(trxOrder.getTradeType()!=null){
                    SET("trade_type=#{tradeType}");
                }
                if(trxOrder.getCodeUrl()!=null){
                    SET("code_url=#{codeUrl}");
                }
                if(trxOrder.getVersion()!=null){
                    SET("version=#{version}");
                }
                if(trxOrder.getDealType()!=null){
                    SET("deal_type=#{dealType}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT(columns);
                FROM("trx_order");
            }
        }.toString();
    }

    //根据属性查找（使用user参数）
    public String findByProperty(final TrxOrder trxOrder){
        return new SQL(){
            {
                SELECT(columns);
                FROM("trx_order");
                if(trxOrder.getOrderNo()!=null){
                    WHERE("order_no=#{orderNo}");
                }
                if(trxOrder.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
                if(trxOrder.getOpenId()!=null){
                    WHERE("open_id=#{openId}");
                }
                if(trxOrder.getTransactionId()!=null){
                    WHERE("transaction_id=#{transactionId}");
                }
                if(trxOrder.getPrepayId()!=null){
                    WHERE("prepay_id=#{prepayId}");
                }
                if(trxOrder.getPrepayIdDueTime()!=null){
                    WHERE("prepayid_duetime=#{prepayIdDueTime}");
                }
                if(trxOrder.getFromType()!=null){
                    WHERE("from_type=#{fromType}");
                }
                if(trxOrder.getCreateIp()!=null){
                    WHERE("create_ip=#{createIp}");
                }
                if(trxOrder.getType()!=null){
                    WHERE("type=#{type}");
                }
                if(trxOrder.getPrice()!=null){
                    WHERE("price=#{price}");
                }
                if(trxOrder.getNumber()!=null){
                    WHERE("number=#{number}");
                }
                if(trxOrder.getAmount()!=null){
                    WHERE("amount=#{amount}");
                }
                if(trxOrder.getFeeType()!=null){
                    WHERE("fee_type=#{feeType}");
                }
                if(trxOrder.getBankType()!=null){
                    WHERE("bank_type=#{bankType}");
                }
                if(trxOrder.getCreateTime()!=null){
                    WHERE("create_time=#{createTime}");
                }
                if(trxOrder.getFinishTime()!=null){
                    WHERE("finish_time=#{finishTime}");
                }
                if(trxOrder.getTransactionStatus()!=null){
                    WHERE("transaction_status=#{transactionStatus}");
                }
                if(trxOrder.getOrderStatus()!=null){
                    WHERE("order_status=#{orderStatus}");
                }
                if(trxOrder.getTradeType()!=null){
                    WHERE("trade_type=#{tradeType}");
                }
                if(trxOrder.getCodeUrl()!=null){
                    WHERE("code_url=#{codeUrl}");
                }
                if(trxOrder.getPhotoLive()!=null){
                    WHERE("photo_live=#{photoLive}");
                }
                if(trxOrder.getVersion()!=null){
                    WHERE("version=#{version}");
                }

            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParams(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT(columnsAlis+",u.username,u.mob_phone");
                FROM("trx_order trx");
                LEFT_OUTER_JOIN("user u on trx.user_id = u.id");
                if(param.get("id")!=null&&param.get("id")!=""){
                    WHERE("trx.id=#{id}");
                }
                if(param.get("orderNo")!=null&&param.get("orderNo")!=""){
                    WHERE("trx.order_no=#{orderNo}");
                }
                if(param.get("userId")!=null&&param.get("userId")!=""){
                    WHERE("trx.user_id=#{userId}");
                }
                if(param.get("openId")!=null&&param.get("openId")!=""){
                    WHERE("trx.open_id=#{openId}");
                }
                if(param.get("transactionId")!=null&&param.get("transactionId")!=""){
                    WHERE("trx.transaction_id=#{transactionId}");
                }
                if(param.get("prepayId")!=null&&param.get("prepayId")!=""){
                    WHERE("trx.prepay_id=#{prepayId}");
                }
                if(param.get("prepayIdDueTime")!=null&&param.get("prepayIdDueTime")!=""){
                    WHERE("trx.prepayid_duetime=#{prepayIdDueTime}");
                }
                if(param.get("fromType")!=null&&param.get("fromType")!=""){
                    WHERE("trx.from_type=#{fromType}");
                }
                if(param.get("createIp")!=null&&param.get("createIp")!=""){
                    WHERE("trx.create_ip=#{createIp}");
                }
                if(param.get("type")!=null&&param.get("type")!=""){
                    WHERE("trx.type=#{type}");
                }
                if(param.get("price")!=null&&param.get("price")!=""){
                    WHERE("trx.price=#{price}");
                }
                if(param.get("number")!=null&&param.get("number")!=""){
                    WHERE("trx.number=#{number}");
                }
                if(param.get("amount")!=null&&param.get("amount")!=""){
                    WHERE("trx.amount=#{amount}");
                }
                if(param.get("feeType")!=null&&param.get("feeType")!=""){
                    WHERE("trx.fee_type=#{feeType}");
                }
                if(param.get("bankType")!=null&&param.get("bankType")!=""){
                    WHERE("trx.bank_type=#{bankType}");
                }
                if(param.get("createTime")!=null&&param.get("createTime")!=""){
                    WHERE("trx.create_time=#{createTime}");
                }
                if(param.get("finishTime")!=null&&param.get("finishTime")!=""){
                    WHERE("trx.finish_time=#{finishTime}");
                }
                if(param.get("transactionStatus")!=null&&param.get("transactionStatus")!=""){
                    WHERE("trx.transaction_status=#{transactionStatus}");
                }
                WHERE("trx.order_status in (0,1,2)");
                if(param.get("tradeType")!=null&&param.get("tradeType")!=""){
                    WHERE("trx.trade_type=#{tradeType}");
                }
                if(param.get("codeUrl")!=null&&param.get("codeUrl")!=""){
                    WHERE("trx.code_url=#{codeUrl}");
                }
                if(param.get("version")!=null&&param.get("version")!=""){
                    WHERE("trx.version=#{version}");
                }
                if(param.get("username")!=null&&param.get("username")!=""){
                    WHERE("u.username=#{username}");
                }
                if(param.get("mobPhone")!=null&&param.get("mobPhone")!=""){
                    WHERE("u.mob_phone=#{mobPhone}");
                }
                if(param.get("startTime")!=null&&param.get("startTime")!=""){
                    WHERE("trx.create_time>=#{startTime}");
                }
                if(param.get("endTime")!=null&&param.get("endTime")!=""){
                    WHERE("trx.create_time<=#{endTime}");
                }
                if(param.get("photoLive")!=null&&param.get("photoLive")!=""){
                    WHERE("trx.photo_live=#{photoLive}");
                }
                if(param.get("orderBy")!=null&&param.get("orderBy")!=""){
                    if(param.get("orderBy").equals(1))
                        ORDER_BY(" trx.create_time desc");
                    if(param.get("orderBy").equals(0))
                        ORDER_BY(" trx.create_time asc");
                }
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT(columns);
                FROM("trx_order");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("trx_order");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
