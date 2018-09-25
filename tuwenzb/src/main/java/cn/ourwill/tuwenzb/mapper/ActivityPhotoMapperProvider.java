package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 17:42
 * @Version1.0
 */
public class ActivityPhotoMapperProvider {
    //保存
    public String save(final ActivityPhoto activityPhoto){
        return new SQL(){
            {
                INSERT_INTO("activity_photo");
                if(activityPhoto.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(activityPhoto.getAlbumId()!=null){
                    VALUES("album_id","#{albumId}");
                }
                if(activityPhoto.getPhotoPath()!=null){
                    VALUES("photo_path","#{photoPath}");
                }
                if(activityPhoto.getPhotoStatus()!=null){
                    VALUES("photo_status","#{photoStatus}");
                }
                if(activityPhoto.getPhotoSize()!=null){
                    VALUES("photo_size","#{photoSize}");
                }
                if(activityPhoto.getPhotoHeight()!=null){
                    VALUES("photo_height","#{photoHeight}");
                }
                if(activityPhoto.getPhotoWidth()!=null){
                    VALUES("photo_width","#{photoWidth}");
                }
                if(activityPhoto.getPhotoOrientation()!=null){
                    VALUES("photo_orientation","#{photoOrientation}");
                }
                if(activityPhoto.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
                if(activityPhoto.getUTime()!=null){
                    VALUES("u_time","#{uTime}");
                }
                if(activityPhoto.getLikeNum()!=null){
                    VALUES("like_num","#{likeNum}");
                }
                if(activityPhoto.getDownloadName()!=null){
                    VALUES("download_name","#{downloadName}");
                }
                if(activityPhoto.getPhotoName()!=null) {
                    VALUES("photo_name", "#{photoName}");
                }
                if(activityPhoto.getShootingTime()!=null){
                    VALUES("shooting_time","#{shootingTime}");
                }
                if(activityPhoto.getDownloadStatus()!=null){
                    VALUES("download_status","#{downloadStatus}");
                }
                if(activityPhoto.getReplaceStatus()!=null){
                    VALUES("replace_status","#{replaceStatus}");
                }
                if(activityPhoto.getIsSaveToFaceSet()!=null){
                    VALUES("is_save_to_faceset","#{isSaveToFaceSet}");
                }
            }
        }.toString();
    }
    //修改
    public String update(final ActivityPhoto activityPhoto){
        return new SQL(){
            {
                UPDATE("activity_photo");
                if(activityPhoto.getUserId()!=null){
                    SET("user_id=#{userId}");
                }
                if(activityPhoto.getAlbumId()!=null){
                    SET("album_id=#{albumId}");
                }
                if(activityPhoto.getPhotoPath()!=null){
                    SET("photo_path=#{photoPath}");
                }
                if(activityPhoto.getPhotoStatus()!=null){
                    SET("photo_status=#{photoStatus}");
                }
                if(activityPhoto.getPhotoSize()!=null){
                    SET("photo_size=#{photoSize}");
                }
                if(activityPhoto.getPhotoHeight()!=null){
                    SET("photo_height=#{photoHeight}");
                }
                if(activityPhoto.getPhotoWidth()!=null){
                    SET("photo_width=#{photoWidth}");
                }
                if(activityPhoto.getPhotoOrientation()!=null){
                    SET("photo_orientation=#{photoOrientation}");
                }
                if(activityPhoto.getCTime()!=null){
                    SET("c_time=#{cTime}");
                }
                if(activityPhoto.getUTime()!=null){
                    SET("u_time=#{uTime}");
                }
                if(activityPhoto.getLikeNum()!=null){
                    SET("like_num=#{likeNum}");
                }
                if(activityPhoto.getDownloadName()!=null){
                    SET("download_name=#{downloadName}");
                }
                if(activityPhoto.getPhotoName()!=null){
                    SET("photo_name=#{photoName}");
                }
                if(activityPhoto.getShootingTime()!=null){
                    SET("shooting_time=#{shootingTime}");
                }
                if(activityPhoto.getDownloadStatus()!=null){
                    SET("download_status=#{downloadStatus}");
                }
                if(activityPhoto.getReplaceStatus()!=null){
                    SET("replace_status=#{replaceStatus}");
                }
                if(activityPhoto.getIsSaveToFaceSet()!=null){
                    SET("is_save_to_faceset=#{isSaveToFaceSet}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT("id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset");
                FROM("activity_photo");
            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT("id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset");
                FROM("activity_photo");
                if(param.get("id")!=null){
                    WHERE("id=#{id}");
                }
                if(param.get("userId")!=null){
                    WHERE("user_id=#{userId}");
                }
                if(param.get("albumId")!=null){
                    WHERE("album_id=#{albumId}");
                }
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset");
                FROM("activity_photo");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("activity_photo");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
