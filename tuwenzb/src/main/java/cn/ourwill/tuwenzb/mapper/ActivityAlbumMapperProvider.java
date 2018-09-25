package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 17:31
 * @Version1.0
 */
public class ActivityAlbumMapperProvider {
    //保存
    public String save(final ActivityAlbum activityAlbum){
        return new SQL(){
            {
                INSERT_INTO("activity_album");
                if(activityAlbum.getActivityId()!=null){
                    VALUES("activity_id","#{activityId}");
                }
                if(activityAlbum.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(activityAlbum.getAlbumName()!=null){
                    VALUES("album_name","#{albumName}");
                }
                if(activityAlbum.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(activityAlbum.getDefaultFlag()!=null){
                    VALUES("default_flag","#{defaultFlag}");
                }
                if(activityAlbum.getDescription()!=null){
                    VALUES("description","#{description}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final ActivityAlbum activityAlbum){
        return new SQL(){
            {
                UPDATE("activity_album");
//                if(activityAlbum.getActivityId()!=null){
//                    SET("activity_id=#{activityId}");
//                }
                if(activityAlbum.getAlbumName()!=null){
                    SET("album_name=#{albumName}");
                }
//                if(activityAlbum.getCTime()!=null){
//                    SET("c_time=#{cTime}");
//                }
                if(activityAlbum.getDescription()!=null){
                    SET("description=#{description}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT("id,activity_id,user_id,album_name,c_time,default_flag,description");
                FROM("activity_album");
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("id,activity_id,user_id,album_name,c_time,default_flag,description");
                FROM("activity_album");
                if(param.get("id")!=null){
                    WHERE("id=#{id}");
                }
                if(param.get("activityId")!=null){
                    WHERE("activity_id=#{activityId}");
                }
                if(param.get("albumName")!=null){
                    WHERE("album_name=#{albumName}");
                }
                if(param.get("cTime")!=null){
                    WHERE("c_time=#{cTime}");
                }
                if(param.get("description")!=null){
                    WHERE("description=#{description}");
                }

            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,activity_id,user_id,album_name,c_time,default_flag,description");
                FROM("activity_album");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("activity_album");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
