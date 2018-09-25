package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.BulletScreen;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:09
 * @Description
 */
public class BulletScreenMapperProvider {

    //表名
    private String tableName="bullet_screen";

    //所有的列名
    private String columns="id,room_id,user_id,nickname,avatar,content,img,c_time,check_status";

    //保存
    public String save(final BulletScreen bulletScreen){
        return new SQL(){
            {
                INSERT_INTO(tableName);
                if(bulletScreen.getRoomId()!=null){
                    VALUES("room_id","#{roomId}");
                }
                if(bulletScreen.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(bulletScreen.getNickname()!=null){
                    VALUES("nickname","#{nickname}");
                }
                if(bulletScreen.getAvatar()!=null){
                    VALUES("avatar","#{avatar}");
                }
                if(bulletScreen.getContent()!=null){
                    VALUES("content","#{content}");
                }
                if(bulletScreen.getImg()!=null){
                    VALUES("img","#{img}");
                }
                if(bulletScreen.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(bulletScreen.getCheckStatus()!=null){
                    VALUES("check_status","#{checkStatus}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final BulletScreen bulletScreen){
        return new SQL(){
            {
                UPDATE(tableName);
                if(bulletScreen.getRoomId()!=null){
                    SET("room_id=#{roomId}");
                }
                if(bulletScreen.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(bulletScreen.getNickname()!=null){
                    SET("nickname=#{nickname}");
                }
                if(bulletScreen.getAvatar()!=null){
                    SET("avatar=#{avatar}");
                }
                if(bulletScreen.getContent()!=null){
                    SET("content=#{content}");
                }
                if(bulletScreen.getImg()!=null){
                    SET("img=#{img}");
                }
                if(bulletScreen.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(bulletScreen.getCheckStatus()!=null){
                    SET("check_status=#{checkStatus}");
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
    public String findByProperty(final BulletScreen bulletScreen){
        return new SQL(){
            {
                SELECT(columns);
                FROM(tableName);
                if(bulletScreen.getId()!=null){
                    WHERE("id=#{id}");
                }
                if(bulletScreen.getRoomId()!=null){
                    WHERE("room_id=#{roomId}");
                }
                if(bulletScreen.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
                if(bulletScreen.getNickname()!=null){
                    WHERE("nickname=#{nickname}");
                }
                if(bulletScreen.getAvatar()!=null){
                    WHERE("avatar=#{avatar}");
                }
                if(bulletScreen.getContent()!=null){
                    WHERE("content=#{content}");
                }
                if(bulletScreen.getImg()!=null){
                    WHERE("img=#{img}");
                }
                if(bulletScreen.getCTime()!=null){
                    WHERE("c_time=#{cTime}");
                }
                if(bulletScreen.getCheckStatus()!=null){
                    WHERE("check_status=#{checkStatus}");
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
                if(param.get("content")!=null&&param.get("content")!=""){
                    WHERE("content=#{content}");
                }
                if(param.get("img")!=null&&param.get("img")!=""){
                    WHERE("img=#{img}");
                }
                if(param.get("cTime")!=null&&param.get("cTime")!=""){
                    WHERE("c_time=#{cTime}");
                }
                if(param.get("checkStatus")!=null&&param.get("checkStatus")!=""){
                    WHERE("check_status=#{checkStatus}");
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
                WHERE("check_status = 1");
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
