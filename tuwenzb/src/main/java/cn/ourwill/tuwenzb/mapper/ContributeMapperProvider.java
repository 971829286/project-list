package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Contribute;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Description:投稿dao层
 * @Author: zhaoqing
 * @Date: 2018/6/20 ❤❤ 14:16
 */
public class ContributeMapperProvider {

    String columns ="id,user_id,f_user_id,name,sex,mob_phone,email,address,work_title,pic_url,check_status,sub_time,update_time,check_time,feedback";

    public String save (final Contribute contribute){
        return new SQL(){
            {
                INSERT_INTO("contribute");
                if (contribute.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if (contribute.getFUserId()!=null){
                    VALUES("f_user_id","#{fUserId}");
                }
                if (contribute.getName()!=null){
                    VALUES("name","#{name}");
                }
                if (contribute.getSex()!=null){
                    VALUES("sex","#{sex}");
                }
                if (contribute.getMobPhone()!=null){
                    VALUES("mob_phone","#{mobPhone}");
                }
                if (contribute.getEmail()!=null){
                    VALUES("email","#{email}");
                }
                if (contribute.getAddress()!=null){
                    VALUES("address","#{address}");
                }
                if (contribute.getWorkTitle()!=null){
                    VALUES("work_title","#{workTitle}");
                }
                if (contribute.getPicUrl()!=null){
                    VALUES("pic_url","#{picUrl}");
                }
                if (contribute.getCheckStatus()!=null){
                    VALUES("check_status","#{checkStatus}");
                }
                if(contribute.getSubTime()!=null){
                    VALUES("sub_time","#{subTime}");
                }
                if(contribute.getUpdateTime()!=null){
                    VALUES("update_time","#{updateTime}");
                }
                if(contribute.getCheckTime()!=null){
                    VALUES("check_time","#{checkTime}");
                }
            }
        }.toString();
    }

    public String update (final Contribute contribute){
        return new SQL(){
            {
                UPDATE("contribute");
                if (contribute.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if (contribute.getName()!=null){
                    SET("name=#{name}");
                }
                if (contribute.getSex()!=null){
                    SET("sex=#{sex}");
                }
                if (contribute.getMobPhone()!=null){
                    SET("mob_phone=#{mobPhone}");
                }
                if (contribute.getEmail()!=null){
                    SET("email=#{email}");
                }
                if (contribute.getAddress()!=null){
                    SET("address=#{address}");
                }
                if (contribute.getWorkTitle()!=null){
                    SET("work_title=#{workTitle}");
                }
                if (contribute.getPicUrl()!=null){
                    SET("pic_url=#{picUrl}");
                }
                if (contribute.getCheckStatus()!=null){
                    SET("check_status=#{checkStatus}");
                }
                if(contribute.getSubTime()!=null){
                    SET("sub_time=#{subTime}");
                }
                if(contribute.getUpdateTime()!=null){
                    SET("update_time=#{updateTime}");
                }
                if(contribute.getCheckTime()!=null){
                    SET("check_time=#{checkTime}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)id,user_id,name,sex,mob_phone,email,address,pic_url,check_status,sub_time,update_time,check_time
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT(columns);

                if(param.get("checkStatus")!=null){
                    FROM("(select "+columns+" from contribute where check_status = #{checkStatus}) tt ");
                    WHERE("1=1");
                }else {
                    FROM("contribute");
                }
                if(param.get("name")!=null){
                    OR();
                    WHERE("name=#{name}");
                }
                if(param.get("mobPhone")!=null){
                    OR();
                    WHERE("mob_phone=#{mobPhone}");
                }
                if(param.get("email")!=null){
                    OR();
                    WHERE("email=#{email}");
                }
                if(param.get("address")!=null){
                    OR();
                    WHERE("address=#{address}");
                }
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)id,user_id,name,sex,mob_phone,email,address,pic_url,check_status,sub_time,update_time,check_time
    public String selectUserByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("distinct user_id,name,sex,mob_phone,email,address,sub_time,update_time,check_time");

                if(param.get("checkStatus")!=null){
                    FROM("(select user_id,name,sex,mob_phone,email,address,sub_time,update_time,check_time from contribute where check_status = #{checkStatus}) tt ");
                    WHERE("1=1");
                }else {
                    FROM("contribute");
                }
                if(param.get("name")!=null){
                    OR();
                    WHERE("name=#{name}");
                }
                if(param.get("mobPhone")!=null){
                    OR();
                    WHERE("mob_phone=#{mobPhone}");
                }
                if(param.get("email")!=null){
                    OR();
                    WHERE("email=#{email}");
                }
                if(param.get("address")!=null){
                    OR();
                    WHERE("address=#{address}");
                }
            }
        }.toString();
    }


    public String selectUserByParamNew(final Map<String,Object> param){
        return new SQL(){
            {
//                SELECT(" a.user_id, a.name, a.sex, a.mob_phone, a.email, a.address, a.sub_time, a.update_time, a.check_time");
//
//                if(param.get("checkStatus")!=null){
//                    FROM("(select min(id) id,user_id from contribute where check_status = #{checkStatus} group BY user_id)tt  ");
//                    LEFT_OUTER_JOIN("(select * from contribute where check_status = #{checkStatus}) a  on a.id = tt.id");
//                    WHERE("1=1");
//                }else {
//                    FROM(" contribute a ");
//                    RIGHT_OUTER_JOIN(" (select min(id) id,user_id from contribute group BY user_id)tt on a.id = tt.id ");
//                    WHERE("1=1");
//                }
                SELECT(columns);
                FROM("contribute");
                if(param.get("checkStatus")!=null){
                    WHERE("check_status=#{checkStatus}");
                }
                if(param.get("searchText")!=null){
                    WHERE("name=#{searchText} or mob_phone=#{searchText} or email=#{searchText} or address=#{searchText} or work_title=#{searchText}");
                }
//                if(param.get("mobPhone")!=null){
//                    OR();
//                    WHERE("a.mob_phone=#{mobPhone}");
//                }
//                if(param.get("email")!=null){
//                    OR();
//                    WHERE("a.email=#{email}");
//                }
//                if(param.get("address")!=null){
//                    OR();
//                    WHERE("a.address=#{address}");
//                }
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT(columns);
                FROM("contribute");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
