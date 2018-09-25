package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.InvoiceContent;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:44
 * @Version1.0
 */
public class InvoiceContentSqlProvider {
    //保存
    public String save(final InvoiceContent invoiceContent){
        return new SQL(){
            {
                INSERT_INTO("invoice_content");
                if(invoiceContent.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(invoiceContent.getInvoiceType()!=null){
                    VALUES("invoice_type","#{invoiceType}");
                }
                if(invoiceContent.getTitleType()!=null){
                    VALUES("title_type","#{titleType}");
                }
                if(invoiceContent.getInvoiceTitle()!=null){
                    VALUES("invoice_title","#{invoiceTitle}");
                }
                if(invoiceContent.getRegistrationNum()!=null){
                    VALUES("registration_num","#{registrationNum}");
                }
                if(invoiceContent.getRegistrationSite()!=null){
                    VALUES("registration_site","#{registrationSite}");
                }
                if(invoiceContent.getRegistrationPhone()!=null){
                    VALUES("registration_phone","#{registrationPhone}");
                }
                if(invoiceContent.getBank()!=null){
                    VALUES("bank","#{bank}");
                }
                if(invoiceContent.getBankAccount()!=null){
                    VALUES("bank_account","#{bankAccount}");
                }
                if(invoiceContent.getIsDefault()!=null){
                    VALUES("is_default","#{isDefault}");
                }
                if(invoiceContent.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(invoiceContent.getUTime()!=null){
                    VALUES("u_time","#{uTime}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final InvoiceContent invoiceContent){
        return new SQL(){
            {
                UPDATE("invoice_content");
                if(invoiceContent.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(invoiceContent.getInvoiceType()!=null){
                    SET("invoice_type=#{invoiceType}");
                }
                if(invoiceContent.getTitleType()!=null){
                    SET("title_type=#{titleType}");
                }
                if(invoiceContent.getInvoiceTitle()!=null){
                    SET("invoice_title=#{invoiceTitle}");
                }
                if(invoiceContent.getRegistrationNum()!=null){
                    SET("registration_num=#{registrationNum}");
                }
                if(invoiceContent.getRegistrationSite()!=null){
                    SET("registration_site=#{registrationSite}");
                }
                if(invoiceContent.getRegistrationPhone()!=null){
                    SET("registration_phone=#{registrationPhone}");
                }
                if(invoiceContent.getBank()!=null){
                    SET("bank=#{bank}");
                }
                if(invoiceContent.getBankAccount()!=null){
                    SET("bank_account=#{bankAccount}");
                }
                if(invoiceContent.getIsDefault()!=null){
                    SET("is_default=#{isDefault}");
                }
                if(invoiceContent.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(invoiceContent.getUTime()!=null){
                    SET("u_time=#{uTime}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //根据属性查找(使用Map参数)
    public String selectByParams(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("id,user_id,invoice_type,title_type,invoice_title,registration_num,registration_site,registration_phone,bank,bank_account,is_default,c_time,u_time");
                FROM("invoice_content");
                if(param.get("userId")!=null){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("invoiceType")!=null){
                    WHERE("invoice_type=#{invoiceType}");
                }
                if(param.get("titleType")!=null){
                    WHERE("title_type=#{titleType}");
                }
                if(param.get("invoiceTitle")!=null){
                    WHERE("invoice_title=#{invoiceTitle}");
                }
                if(param.get("registrationNum")!=null){
                    WHERE("registration_num=#{registrationNum}");
                }
                if(param.get("registrationSite")!=null){
                    WHERE("registration_site=#{registrationSite}");
                }
                if(param.get("registrationPhone")!=null){
                    WHERE("registration_phone=#{registrationPhone}");
                }
                if(param.get("bank")!=null){
                    WHERE("bank=#{bank}");
                }
                if(param.get("bankAccount")!=null){
                    WHERE("bank_account=#{bankAccount}");
                }
                if(param.get("isDefault")!=null){
                    WHERE("is_default=#{isDefault}");
                }
                if(param.get("cTime")!=null){
                    WHERE("c_time=#{cTime}");
                }
                if(param.get("uTime")!=null){
                    WHERE("u_time=#{uTime}");
                }
            }
        }.toString();
    }
    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,user_id,invoice_type,title_type,invoice_title,registration_num,registration_site,registration_phone,bank,bank_account,is_default,c_time,u_time");
                FROM("invoice_content");
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //根据用户ID查找
    public String selectByUserId(Integer userId){
        return new SQL(){
            {
                SELECT("id,user_id,invoice_type,title_type,invoice_title,registration_num,registration_site,registration_phone,bank,bank_account,is_default,c_time,u_time");
                FROM("invoice_content");
                WHERE("user_id=#{userId}");
            }
        }.toString();
    }
    //根据id删除
    public String deleteById(Integer id){
        return new SQL(){
            {
                DELETE_FROM("invoice_content");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
