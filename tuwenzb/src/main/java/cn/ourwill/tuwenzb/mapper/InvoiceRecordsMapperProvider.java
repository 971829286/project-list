package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.InvoiceRecords;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:12
 * @Version1.0
 */
public class InvoiceRecordsMapperProvider {
    //保存
    public String save(final InvoiceRecords invoiceRecords){
        return new SQL(){
            {
                INSERT_INTO("invoice_records");
                if(invoiceRecords.getApplyTime()!=null){
                    VALUES("apply_time","#{applyTime}");
                }
                if(invoiceRecords.getOrderNos()!=null){
                    VALUES("order_nos","#{orderNos}");
                }
                if(invoiceRecords.getAuthorizationIds()!=null){
                    VALUES("authorization_ids","#{authorizationIds}");
                }
                if(invoiceRecords.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(invoiceRecords.getInvoiceAmount()!=null){
                    VALUES("invoice_amount","#{invoiceAmount}");
                }
                if(invoiceRecords.getInvoiceStatus()!=null){
                    VALUES("invoice_status","#{invoiceStatus}");
                }
                if(invoiceRecords.getInvoiceContentId()!=null){
                    VALUES("invoice_content_id","#{invoiceContentId}");
                }
                if(invoiceRecords.getInvoiceSiteId()!=null){
                    VALUES("invoice_site_id","#{invoiceSiteId}");
                }
                if(invoiceRecords.getReceiver()!=null){
                    VALUES("receiver","#{receiver}");
                }
                if(invoiceRecords.getPhone()!=null){
                    VALUES("phone","#{phone}");
                }
                if(invoiceRecords.getExpressCompany()!=null){
                    VALUES("express_company","#{expressCompany}");
                }
                if(invoiceRecords.getExpressNo()!=null){
                    VALUES("express_no","#{expressNo}");
                }
                if(invoiceRecords.getInvoiceType()!=null){
                    VALUES("invoice_type","#{invoiceType}");
                }
                if(invoiceRecords.getInvoiceContentStr()!=null){
                    VALUES("invoice_content","#{invoiceContentStr}");
                }
                if(invoiceRecords.getInvoiceSiteStr()!=null){
                    VALUES("invoice_site","#{invoiceSiteStr}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final InvoiceRecords invoiceRecords){
        return new SQL(){
            {
                UPDATE("invoice_records");
                if(invoiceRecords.getApplyTime()!=null){
                    SET("apply_time=#{applyTime}");
                }
                if(invoiceRecords.getOrderNos()!=null){
                    SET("order_nos=#{orderNos}");
                }
                if(invoiceRecords.getAuthorizationIds()!=null){
                    SET("authorization_ids=#{authorizationIds}");
                }
                if(invoiceRecords.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(invoiceRecords.getInvoiceAmount()!=null){
                    SET("invoice_amount=#{invoiceAmount}");
                }
                if(invoiceRecords.getInvoiceStatus()!=null){
                    SET("invoice_status=#{invoiceStatus}");
                }
                if(invoiceRecords.getInvoiceContentId()!=null){
                    SET("invoice_content_id=#{invoiceContentId}");
                }
                if(invoiceRecords.getInvoiceSiteId()!=null){
                    SET("invoice_site_id=#{invoiceSiteId}");
                }
                if(invoiceRecords.getReceiver()!=null){
                    SET("receiver=#{receiver}");
                }
                if(invoiceRecords.getPhone()!=null){
                    SET("phone=#{phone}");
                }
                if(invoiceRecords.getExpressCompany()!=null){
                    SET("express_company=#{expressCompany}");
                }
                if(invoiceRecords.getExpressNo()!=null){
                    SET("express_no=#{expressNo}");
                }
                if(invoiceRecords.getInvoiceType()!=null){
                    SET("invoice_type=#{invoiceType}");
                }
                if(invoiceRecords.getInvoiceContentStr()!=null){
                    SET("invoice_content=#{invoiceContentStr}");
                }
                if(invoiceRecords.getInvoiceSiteStr()!=null){
                    SET("invoice_site=#{invoiceSiteStr}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //根据属性查找(使用Map参数)
    public String selectByParams(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("id,apply_time,order_nos,authorization_ids,user_id,invoice_amount,invoice_status,invoice_content_id,invoice_site_id,receiver,phone,express_company,express_no,invoice_type,invoice_content,invoice_site");
                FROM("invoice_records");
//                LEFT_OUTER_JOIN("invoice_site s on r.invoice_site_id = s.id");
//                LEFT_OUTER_JOIN("invoice_content c on r.invoice_content_id = c.id");
                if(param.get("applyTime")!=null){
                    WHERE("apply_time=#{applyTime}");
                }
                if(param.get("orderNos")!=null){
                    WHERE("order_nos=#{orderNos}");
                }
                if(param.get("userId")!=null){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("invoiceAmount")!=null){
                    WHERE("invoice_amount=#{invoiceAmount}");
                }
                if(param.get("invoiceStatus")!=null){
                    WHERE("invoice_status=#{invoiceStatus}");
                }
                if(param.get("invoiceContentId")!=null){
                    WHERE("invoice_content_id=#{invoiceContentId}");
                }
                if(param.get("invoiceSiteId")!=null){
                    WHERE("invoice_site_id=#{invoiceSiteId}");
                }
                if(param.get("startTime")!=null&&param.get("startTime")!=""){
                    WHERE("apply_time >= #{startTime}");
                }
                if(param.get("endTime")!=null&&param.get("endTime")!=""){
                    WHERE("apply_time <= #{endTime}");
                }
                if(param.get("receiver")!=null&&param.get("receiver")!=""){
                    WHERE("receiver like concat('%',#{receiver},'%')");
                }
                if(param.get("phone")!=null&&param.get("phone")!=""){
                    WHERE("phone = #{phone}");
                }
            }
        }.toString();
    }
    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,apply_time,order_nos,authorization_ids,user_id,invoice_amount,invoice_status,invoice_content_id,invoice_site_id,receiver,phone,express_company,express_no,invoice_type,invoice_content,invoice_site");
                FROM("invoice_records");
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //根据id删除
    public String deleteById(Integer id){
        return new SQL(){
            {
                DELETE_FROM("invoice_records");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
