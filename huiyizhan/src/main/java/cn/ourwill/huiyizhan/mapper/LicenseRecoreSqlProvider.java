package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.LicenseRecord;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 12:00
 * @Version1.0
 */
public class LicenseRecoreSqlProvider {
    //保存
    public String save(final LicenseRecord licenseRecord){
        return new SQL(){
            {
                INSERT_INTO("license_record");
                if(licenseRecord.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(licenseRecord.getLicenseType()!=null){
                    VALUES("license_type","#{licenseType}");
                }
                if(licenseRecord.getSessionsTotal()!=null){
                    VALUES("sessions_total","#{sessionsTotal}");
                }
                if(licenseRecord.getDueDate()!=null){
                    VALUES("due_date","#{dueDate}");
                }
                if(licenseRecord.getAmount()!=null){
                    VALUES("amount","#{amount}");
                }
                if(licenseRecord.getPaymentType()!=null){
                    VALUES("payment_type","#{paymentType}");
                }
                if(licenseRecord.getTransactionDate()!=null){
                    VALUES("transaction_date","#{transactionDate}");
                }
                if(licenseRecord.getOrderNo()!=null){
                    VALUES("order_no","#{orderNo}");
                }
                if(licenseRecord.getCId()!=null){
                    VALUES("c_id","#{cId}");
                }
                if(licenseRecord.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(licenseRecord.getUId()!=null){
                    VALUES("u_id","#{uId}");
                }
                if(licenseRecord.getUTime()!=null){
                    VALUES("u_time","#{uTime}");
                }
                if(licenseRecord.getPhotoLive()!=null){
                    VALUES("photo_live","#{photoLive}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final LicenseRecord licenseRecord){
        return new SQL(){
            {
                UPDATE("license_record");
                if(licenseRecord.getUserId()!=null){
                    VALUES("user_id","userId");
                }
                if(licenseRecord.getLicenseType()!=null){
                    SET("license_type=#{licenseType}");
                }
                if(licenseRecord.getSessionsTotal()!=null){
                    SET("sessions_total=#{sessionsTotal}");
                }
                if(licenseRecord.getDueDate()!=null){
                    SET("due_date=#{dueDate}");
                }
                if(licenseRecord.getAmount()!=null){
                    SET("amount=#{amount}");
                }
                if(licenseRecord.getPaymentType()!=null){
                    SET("payment_type=#{paymentType}");
                }
                if(licenseRecord.getTransactionDate()!=null){
                    SET("transaction_date=#{transactionDate}");
                }
                if(licenseRecord.getOrderNo()!=null){
                    SET("order_no=#{orderNo}");
                }
                if(licenseRecord.getCId()!=null){
                    SET("c_id=#{cId}");
                }
                if(licenseRecord.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(licenseRecord.getUId()!=null){
                    SET("u_id=#{uId}");
                }
                if(licenseRecord.getUTime()!=null){
                    SET("u_time=#{uTime}");
                }
                if(licenseRecord.getPhotoLive()!=null){
                    SET("photo_live=#{photoLive}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT("l.id,l.user_id,l.license_type,l.sessions_total,l.due_date,l.amount,l.payment_type,l.transaction_date,l.order_no,l.invoice_status,l.c_id,l.c_time,l.u_id,l.u_time,l.photo_live,u.username");
                FROM("license_record l");
                LEFT_OUTER_JOIN("user u on u.id = l.user_id");
                WHERE("photo_live = #{photoLive}");
            }
        }.toString();
    }

    //根据属性查找（licenseRecord）
    public String findByProperty(final LicenseRecord licenseRecord){
        return new SQL(){
            {
                SELECT("id,user_id,license_type,sessions_total,due_date,amount,payment_type,transaction_date,order_no,invoice_status,c_id,c_time,u_id,u_time,photo_live");
                FROM("license_record");
                if(licenseRecord.getId()!=null){
                    WHERE("id=#{id}");
                }
                if(licenseRecord.getLicenseType()!=null){
                    WHERE("license_type=#{licenseType}");
                }
                if(licenseRecord.getSessionsTotal()!=null){
                    WHERE("sessions_total=#{sessionsTotal}");
                }
                if(licenseRecord.getDueDate()!=null){
                    WHERE("due_date=#{dueDate}");
                }
                if(licenseRecord.getAmount()!=null){
                    WHERE("amount=#{amount}");
                }
                if(licenseRecord.getPaymentType()!=null){
                    WHERE("payment_type=#{paymentType}");
                }
                if(licenseRecord.getTransactionDate()!=null){
                    WHERE("transaction_date=#{transactionDate}");
                }
                if(licenseRecord.getOrderNo()!=null){
                    WHERE("order_no=#{orderNo}");
                }
                if(licenseRecord.getCId()!=null){
                    WHERE("c_id=#{cId}");
                }
                if(licenseRecord.getCTime()!=null){
                    WHERE("c_time=#{cTime}");
                }
                if(licenseRecord.getUId()!=null){
                    WHERE("u_id=#{uId}");
                }
                if(licenseRecord.getUTime()!=null){
                    WHERE("u_time=#{uTime}");
                }
                if(licenseRecord.getPhotoLive()!=null){
                    WHERE("photo_live=#{photoLive}");
                }
            }
        }.toString();
    }
    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("u.remaining_days,l.id,l.user_id,l.license_type,l.sessions_total,l.due_date,l.amount,l.payment_type,l.transaction_date,l.order_no,l.invoice_status,l.c_id,l.c_time,l.u_id,l.u_time,l.photo_live,u.username,i.express_company,i.express_no");
                FROM("license_record l");
                LEFT_OUTER_JOIN("invoice_records i on find_in_set(l.id,i.authorization_ids)>0");
                LEFT_OUTER_JOIN("user u on u.id = l.user_id");
                if(param.get("id")!=null&&param.get("id")!=""){
                    WHERE("l.id=#{id}");
                }
                if(param.get("userId")!=null&&param.get("userId")!=""){
                    WHERE("l.user_id=#{userId}");
                }
                if(param.get("licenseType")!=null&&param.get("licenseType")!=""){
                    WHERE("l.license_type=#{licenseType}");
                }
                if(param.get("sessionsTotal")!=null&&param.get("sessionsTotal")!=""){
                    WHERE("l.sessions_total=#{sessionsTotal}");
                }
                if(param.get("dueDate")!=null&&param.get("dueDate")!=""){
                    WHERE("l.due_date=#{dueDate}");
                }
                if(param.get("amount")!=null&&param.get("amount")!=""){
                    WHERE("l.amount=#{amount}");
                }
                if(param.get("paymentType")!=null&&param.get("paymentType")!=""){
                    WHERE("l.payment_type=#{paymentType}");
                }
                if(param.get("transactionDate")!=null&&param.get("transactionDate")!=""){
                    WHERE("l.transaction_date=#{transactionDate}");
                }
                if(param.get("orderNo")!=null&&param.get("orderNo")!=""){
                    WHERE("l.order_no=#{orderNo}");
                }
                if(param.get("cId")!=null&&param.get("cId")!=""){
                    WHERE("l.c_id=#{cId}");
                }
                if(param.get("cTime")!=null&&param.get("cTime")!=""){
                    WHERE("l.c_time=#{cTime}");
                }
                if(param.get("uId")!=null&&param.get("uId")!=""){
                    WHERE("l.u_id=#{uId}");
                }
                if(param.get("uTime")!=null&&param.get("uTime")!=""){
                    WHERE("l.u_time=#{uTime}");
                }
                if(param.get("remainingDays")!=null&&param.get("remainingDays")!=""){
                    WHERE("l.remaining_days=#{remainingDays}");
                }
                if(param.get("startTime")!=null&&param.get("startTime")!=""){
                    WHERE("l.c_time>=#{startTime}");
                }
                if(param.get("endTime")!=null&&param.get("endTime")!=""){
                    WHERE("l.c_time<=#{endTime}");
                }
                if(param.get("photoLive")!=null&&param.get("photoLive")!=""){
                    WHERE("l.photo_live=#{photoLive}");
                }
            }
        }.toString();
    }
    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("u.remaining_days,l.id,l.user_id,l.license_type,l.sessions_total,l.due_date,l.amount,l.payment_type,l.transaction_date,l.order_no,l.invoice_status,l.c_id,l.c_time,l.u_id,l.u_time,l.photo_live,u.username");
                FROM("license_record l");
                LEFT_OUTER_JOIN("user u on u.id = l.user_id");
                WHERE("l.id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("license_record");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
