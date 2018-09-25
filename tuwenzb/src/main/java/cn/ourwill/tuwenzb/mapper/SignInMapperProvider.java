package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.SignIn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:26
 * @Description
 */
public class SignInMapperProvider {
    //表名
    private String tableName="sign_in";

    //表名
    private String tableNameAlias="sign_in s";

    //所有的列名
    private String columns="id,activity_id,user_id,c_time";

    //所有的列名
    private String columnsAlias="s.id,s.activity_id,s.user_id,s.c_time";

    //保存
    public String save(final SignIn signIn){
        return new SQL(){
            {
                INSERT_INTO(tableName);
                if(signIn.getActivityId()!=null){
                    VALUES("activity_id","#{activityId}");
                }
                if(signIn.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(signIn.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final SignIn signIn){
        return new SQL(){
            {
                UPDATE(tableName);
                if(signIn.getActivityId()!=null){
                    SET("activity_id=#{activityId}");
                }
                if(signIn.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(signIn.getCTime()!=null){
                    SET("c_time=#{cTime}");
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
                FROM(tableName);
            }
        }.toString();
    }

    //根据属性查找（blackList）
    public String findByProperty(final SignIn signIn){
        return new SQL(){
            {
                SELECT(columns);
                FROM(tableName);
                if(signIn.getId()!=null){
                    WHERE("id=#{id}");
                }
                if(signIn.getActivityId()!=null){
                    WHERE("activity_id=#{activityId}");
                }
                if(signIn.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
                if(signIn.getCTime()!=null){
                    WHERE("c_time=#{cTime}");
                }
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT(columns);
                FROM(tableName);
                if(param.get("id")!=null&&param.get("id")!=""){
                    WHERE("id=#{id}");
                }
                if(param.get("activityId")!=null&&param.get("activityId")!=""){
                    WHERE("activity_id=#{activityId}");
                }
                if(param.get("userId")!=null&&param.get("userId")!=""){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("cTime")!=null&&param.get("cTime")!=""){
                    WHERE("c_time=#{cTime}");
                }
            }
        }.toString();
    }
    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT(columnsAlias+",u.avatar,u.nickname");
                FROM(tableNameAlias);
                LEFT_OUTER_JOIN("user u on s.user_id = u.id");
                WHERE("s.id=#{id}");
            }
        }.toString();
    }

    //根据房间ID查找
    public String selectByActivityId(@Param("activityId") Integer activityId, @Param("time") Date time){
        return new SQL(){
            {
                SELECT(columnsAlias+",u.avatar,u.nickname");
                FROM(tableNameAlias);
                LEFT_OUTER_JOIN("user u on s.user_id = u.id");
                WHERE("s.activity_id=#{activityId}");
                if(time!=null){
                    WHERE("s.c_time > #{time}");
                }
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Integer id){
        return new SQL(){
            {
                DELETE_FROM(tableName);
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
