package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 16:01
 **/
public class ActivityPhotoTokenMapperProvider {
    String column = "id,album_id,user_id,face_token,photo_id,activity_id,same_person";
    String tableName = "activity_photo_token";
    public String save(final ActivityPhotoToken activityPhotoToken){
        return new SQL(){
            {
                INSERT_INTO(tableName);
                if(activityPhotoToken.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(activityPhotoToken.getAlbumId()!=null){
                    VALUES("album_id","#{albumId}");
                }
                if(activityPhotoToken.getFaceToken()!=null){
                    VALUES("face_token","#{faceToken}");
                }
                if(activityPhotoToken.getPhotoId()!=null){
                    VALUES("photo_id","#{photoId}");
                }
                if(activityPhotoToken.getActivityId()!=null){
                    VALUES("activity_id","#{activityId}");
                }
                if(activityPhotoToken.getSamePerson()!=null){
                    VALUES("same_person","#{samePerson}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final ActivityPhotoToken activityPhotoToken){
        return new SQL(){
            {
                UPDATE(tableName);
                if(activityPhotoToken.getAlbumId()!=null){
                    SET("album_id=#{albumId}");
                }
                if(activityPhotoToken.getFaceToken()!=null){
                    SET("face_token=#{faceToken}");
                }
                if(activityPhotoToken.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(activityPhotoToken.getActivityId()!=null){
                    SET("activity_id=#{activityId}");
                }
                if(activityPhotoToken.getSamePerson()!=null){
                    SET("same_person=#{samePerson}");
                }
                WHERE("photo_id=#{photoId}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT(column);
                FROM(tableName);
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT(column);
                FROM(tableName);
                if(param.get("id")!=null){
                    WHERE("id=#{id}");
                }
                if(param.get("albumId")!=null){
                    WHERE("album_id=#{albumId}");
                }
                if(param.get("userId")!=null){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("faceToken")!=null){
                    WHERE("face_token=#{faceToken}");
                }
                if(param.get("photoId")!=null){
                    WHERE("photo_id=#{photoId}");
                }
                if(param.get("activityId")!=null){
                    WHERE("activity_id=#{activityId}");
                }
                if(param.get("samePerson")!=null){
                    WHERE("same_person=#{samePerson}");
                }
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT(column);
                FROM(tableName);
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM(tableName);
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
