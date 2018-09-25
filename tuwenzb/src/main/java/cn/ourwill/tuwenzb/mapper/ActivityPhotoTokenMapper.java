package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 15:56
 **/
@Repository
public interface ActivityPhotoTokenMapper extends IBaseMapper<ActivityPhotoToken> {
    String column = "id,album_id,user_id,face_token,photo_id,activity_id,same_person";
    String tableName = "activity_photo_token";
    @InsertProvider(type = ActivityPhotoTokenMapperProvider.class, method = "save")
    Integer save(ActivityPhotoToken entity);

    @UpdateProvider(type = ActivityPhotoTokenMapperProvider.class,method = "update")
    Integer update(ActivityPhotoToken entity);

    @SelectProvider(type = ActivityPhotoTokenMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_id", property = "albumId"),
            @Result(column = "face_token", property = "faceToken"),
            @Result(column = "photo_id", property = "photoId"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "same_person", property = "samePerson")
    })
    List<ActivityPhotoToken> findAll();

    @SelectProvider(type = ActivityPhotoTokenMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_id", property = "albumId"),
            @Result(column = "face_token", property = "faceToken"),
            @Result(column = "photo_id", property = "photoId"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "same_person", property = "samePerson")
    })
    ActivityPhotoToken getById(Integer id);

    @DeleteProvider(type = ActivityPhotoTokenMapperProvider.class,method = "deleteById")
    Integer delete(Map map);

    @Delete({"delete from activity_photo_token where photo_id = #{photoId}"})
    Integer deleteByPhotoId(Integer photoId);

    @Select({"select "+column+" from activity_photo_token where photo_id = #{photoId}"})
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_id", property = "albumId"),
            @Result(column = "face_token", property = "faceToken"),
            @Result(column = "photo_id", property = "photoId"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "same_person", property = "samePerson"),
    })
     List<ActivityPhotoToken> getByPhotoId(Integer photoId);

    @Update({"update "+ tableName + " set same_person = #{samePerson} where id = #{id}"})
    Integer setSamePerson(@Param("samePerson") String samePerson,@Param("id") Integer id);

    @Select({"select "+ column+ " from activity_photo_token where same_person = #{samePerson}"})
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_id", property = "albumId"),
            @Result(column = "face_token", property = "faceToken"),
            @Result(column = "photo_id", property = "photoId"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "same_person", property = "samePerson"),
    })
    List<ActivityPhotoToken> findBySamePerson(@Param("samePerson") String samePerson);


    @Select({"select " + column + " from activity_photo_token where face_token = #{faceToken}" })
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_id", property = "albumId"),
            @Result(column = "face_token", property = "faceToken"),
            @Result(column = "photo_id", property = "photoId"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "same_person", property = "samePerson"),
    })
    ActivityPhotoToken findByFaceToken(@Param("faceToken") String faceToken);

}
