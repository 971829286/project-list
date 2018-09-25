package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.BlackList;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 12:02
 * @Version1.0
 */
public class BlackListMapperProvider {
    //保存
    public String save(final BlackList blackList){
        return new SQL(){
            {
                INSERT_INTO("black_list");
                if(blackList.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(blackList.getStatus()!=null){
                    VALUES("status","#{status}");
                }
                if(blackList.getStartDate()!=null){
                    VALUES("start_date","#{startDate}");
                }
                if(blackList.getEndDate()!=null){
                    VALUES("end_date","#{endDate}");
                }
                if(blackList.getReason()!=null){
                    VALUES("reason","#{reason}");
                }
                if(blackList.getCId()!=null){
                    VALUES("c_id","#{cId}");
                }
                if(blackList.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(blackList.getUId()!=null){
                    VALUES("u_id","#{uId}");
                }
                if(blackList.getUTime()!=null){
                    VALUES("u_time","#{uTime}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final BlackList blackList){
        return new SQL(){
            {
                UPDATE("black_list");
                if(blackList.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(blackList.getStatus()!=null){
                    SET("status=#{status}");
                }
                if(blackList.getStartDate()!=null){
                    SET("start_date=#{startDate}");
                }
                if(blackList.getEndDate()!=null){
                    SET("end_date=#{endDate}");
                }
                if(blackList.getReason()!=null){
                    SET("reason=#{reason}");
                }
                if(blackList.getCId()!=null){
                    SET("c_id=#{cId}");
                }
                if(blackList.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(blackList.getUId()!=null){
                    SET("u_id=#{uId}");
                }
                if(blackList.getUTime()!=null){
                    SET("u_time=#{uTime}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT("id,user_id,status,start_date,end_date,reason,c_id,c_time,u_id,u_time");
                FROM("black_list");
            }
        }.toString();
    }

    //根据属性查找（blackList）
    public String findByProperty(final BlackList blackList){
        return new SQL(){
            {
                SELECT("id,user_id,status,start_date,end_date,reason,c_id,c_time,u_id,u_time");
                FROM("black_list");
                if(blackList.getId()!=null){
                    WHERE("id=#{id}");
                }
                if(blackList.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
                if(blackList.getStatus()!=null){
                    WHERE("status=#{status}");
                }
                if(blackList.getStartDate()!=null){
                    WHERE("start_date=#{startDate}");
                }
                if(blackList.getEndDate()!=null){
                    WHERE("end_date=#{endDate}");
                }
                if(blackList.getReason()!=null){
                    WHERE("reason=#{reason}");
                }
                if(blackList.getCId()!=null){
                    WHERE("c_id=#{cId}");
                }
                if(blackList.getCTime()!=null){
                    WHERE("c_time=#{cTime}");
                }
                if(blackList.getUId()!=null){
                    WHERE("u_id=#{uId}");
                }
                if(blackList.getUTime()!=null){
                    WHERE("u_time=#{uTime}");
                }
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("b.id,b.user_id,b.status,b.start_date,b.end_date,b.reason,b.c_id,b.c_time,b.u_id,b.u_time,u.nickname");
                FROM("black_list b ");
                LEFT_OUTER_JOIN("user u on u.id = b.user_id");
                if(param.get("id")!=null&&param.get("id")!=""){
                    WHERE("b.id=#{id}");
                }
                if(param.get("userId")!=null&&param.get("userId")!=""){
                    WHERE("b.user_id=#{userId}");
                }
                if(param.get("status")!=null&&param.get("status")!=""){
                    WHERE("b.status=#{status}");
                }
                if(param.get("startDate")!=null&&param.get("startDate")!=""){
                    WHERE("b.start_date=#{startDate}");
                }
                if(param.get("endDate")!=null&&param.get("endDate")!=""){
                    WHERE("b.end_date=#{endDate}");
                }
                if(param.get("reason")!=null&&param.get("reason")!=""){
                    WHERE("b.reason=#{reason}");
                }
                if(param.get("cId")!=null&&param.get("cId")!=""){
                    WHERE("b.c_id=#{cId}");
                }
                if(param.get("cTime")!=null&&param.get("cTime")!=""){
                    WHERE("b.c_time=#{cTime}");
                }
                if(param.get("uId")!=null&&param.get("uId")!=""){
                    WHERE("b.u_id=#{uId}");
                }
                if(param.get("uTime")!=null&&param.get("uTime")!=""){
                    WHERE("b.u_time=#{uTime}");
                }
            }
        }.toString();
    }
    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,user_id,status,start_date,end_date,reason,c_id,c_time,u_id,u_time");
                FROM("black_list");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("black_list");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
