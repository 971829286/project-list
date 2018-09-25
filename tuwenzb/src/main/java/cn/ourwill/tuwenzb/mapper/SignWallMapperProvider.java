package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.SignWall;
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
public class SignWallMapperProvider {
    //表名
    private String tableName="sign_wall";

    //所有的列名
    private String columns="id,room_id,user_id,nickname,avatar,c_time";

    //保存
    public String save(final SignWall signWall){
        return new SQL(){
            {
                INSERT_INTO(tableName);
                if(signWall.getRoomId()!=null){
                    VALUES("room_id","#{roomId}");
                }
                if(signWall.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(signWall.getNickname()!=null){
                    VALUES("nickname","#{nickname}");
                }
                if(signWall.getAvatar()!=null){
                    VALUES("avatar","#{avatar}");
                }
                if(signWall.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final SignWall signWall){
        return new SQL(){
            {
                UPDATE(tableName);
                if(signWall.getRoomId()!=null){
                    SET("room_id=#{roomId}");
                }
                if(signWall.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(signWall.getNickname()!=null){
                    SET("nickname=#{nickname}");
                }
                if(signWall.getAvatar()!=null){
                    SET("avatar=#{avatar}");
                }
                if(signWall.getCTime()!=null){
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
    public String findByProperty(final SignWall signWall){
        return new SQL(){
            {
                SELECT(columns);
                FROM(tableName);
                if(signWall.getId()!=null){
                    WHERE("id=#{id}");
                }
                if(signWall.getRoomId()!=null){
                    WHERE("room_id=#{roomId}");
                }
                if(signWall.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
                if(signWall.getNickname()!=null){
                    WHERE("nickname=#{nickname}");
                }
                if(signWall.getAvatar()!=null){
                    WHERE("avatar=#{avatar}");
                }
                if(signWall.getCTime()!=null){
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
                if(param.get("roomId")!=null&&param.get("roomId")!=""){
                    WHERE("room_id=#{roomId}");
                }
                if(param.get("userId")!=null&&param.get("userId")!=""){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("nickname")!=null&&param.get("nickname")!=""){
                    WHERE("nickname=#{nickname}");
                }
                if(param.get("avatar")!=null&&param.get("avatar")!=""){
                    WHERE("avatar=#{avatar}");
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
                SELECT(columns);
                FROM(tableName);
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据房间ID查找
    public String selectByRoomId(@Param("roomId") Integer roomId, @Param("time") Date time){
        return new SQL(){
            {
                SELECT(columns);
                FROM(tableName);
                WHERE("room_id=#{roomId}");
                if(time!=null){
                    WHERE("c_time > #{time}");
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
