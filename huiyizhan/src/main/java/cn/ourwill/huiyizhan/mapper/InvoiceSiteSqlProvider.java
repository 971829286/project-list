package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.InvoiceSite;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:44
 * @Version1.0
 */
public class InvoiceSiteSqlProvider {
    //保存
    public String save(final InvoiceSite invoiceSite){
        return new SQL(){
            {
                INSERT_INTO("invoice_site");
                if(invoiceSite.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(invoiceSite.getReceiver()!=null){
                    VALUES("receiver","#{receiver}");
                }
                if(invoiceSite.getPhone()!=null){
                    VALUES("phone","#{phone}");
                }
                if(invoiceSite.getAddress()!=null){
                    VALUES("address","#{address}");
                }
                if(invoiceSite.getIsDefault()!=null){
                    VALUES("is_default","#{isDefault}");
                }
                if(invoiceSite.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(invoiceSite.getUTime()!=null){
                    VALUES("u_time","#{uTime}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final InvoiceSite invoiceSite){
        return new SQL(){
            {
                UPDATE("invoice_site");
                if(invoiceSite.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(invoiceSite.getReceiver()!=null){
                    SET("receiver=#{receiver}");
                }
                if(invoiceSite.getPhone()!=null){
                    SET("phone=#{phone}");
                }
                if(invoiceSite.getAddress()!=null){
                    SET("address=#{address}");
                }
                if(invoiceSite.getIsDefault()!=null){
                    SET("is_default=#{isDefault}");
                }
                if(invoiceSite.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(invoiceSite.getUTime()!=null){
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
                SELECT("id,user_id,receiver,phone,address,is_default,c_time,u_time");
                FROM("invoice_site");
                if(param.get("userId")!=null){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("receiver")!=null){
                    WHERE("receiver=#{receiver}");
                }
                if(param.get("phone")!=null){
                    WHERE("phone=#{phone}");
                }
                if(param.get("address")!=null){
                    WHERE("address=#{address}");
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
                SELECT("id,user_id,receiver,phone,address,is_default,c_time,u_time");
                FROM("invoice_site");
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //根据ID查找
    public String selectByUserId(Integer userId){
        return new SQL(){
            {
                SELECT("id,user_id,receiver,phone,address,is_default,c_time,u_time");
                FROM("invoice_site");
                WHERE("user_id=#{userId}");
            }
        }.toString();
    }
    //根据id删除
    public String deleteById(Integer id){
        return new SQL(){
            {
                DELETE_FROM("invoice_site");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
