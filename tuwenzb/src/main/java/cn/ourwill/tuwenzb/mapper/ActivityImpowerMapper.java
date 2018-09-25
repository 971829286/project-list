package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityImpower;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 14:50
 * @Version1.0
 */
@Repository
public interface ActivityImpowerMapper extends IBaseMapper<ActivityImpower> {

    @InsertProvider(type = ActivityImpowerMapperProvider.class,method ="save")
    Integer save(ActivityImpower activityImpower);
    @Select("select a.id,a.activity_id,a.album_id,a.user_id,a.type,a.status,a.c_time,u.username,u.nickname,u.avatar from activity_impower a left join user u on a.user_id = u.id where a.activity_id = #{activityId} and a.status = 1")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "type",property = "type"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "c_time",property = "cTime")
    })
    List<ActivityImpower> selectByActivityId(@Param("activityId") Integer activityId);

    @Select("select id,activity_id,album_id,user_id,type,status,c_time from activity_impower where id = #{id}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "type",property = "type"),
            @Result( column = "c_time",property = "cTime")
    })
    ActivityImpower getById(@Param("id") Integer id);

    @Select("select a.id,a.activity_id,a.album_id,a.user_id,a.type,a.status,a.c_time,u.username,u.nickname,u.avatar from activity_impower a left join user u on a.user_id = u.id where a.activity_id = #{activityId} and a.album_id = #{albumId} and a.status = 1")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "type",property = "type"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "c_time",property = "cTime")
    })
    List<ActivityImpower> getByAlbumIdAndActivityId(@Param("activityId") Integer activityId,@Param("albumId") Integer albumId);

    @Select("select a.id,a.activity_id,a.album_id,a.user_id,a.type,a.status,a.c_time,u.username,u.nickname,u.avatar from activity_impower a left join user u on a.user_id = u.id where a.activity_id = #{activityId} and a.type=2 and a.status = 1")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "type",property = "type"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "c_time",property = "cTime")
    })
    List<ActivityImpower> getByAdminActivityId(@Param("activityId") Integer activityId);

    @Select("select count(id) from activity_impower where activity_id = #{activityId} and status = 1")
    Integer selectCountByActivityId(@Param("activityId") Integer activityId);

    @Select("select count(id) from activity_impower where activity_id = #{activityId} and user_id = #{userId} and status = 1")
    Integer selectCountByActivityIdUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId);

    @Select("select count(id) from activity_impower where album_id = #{albumId} and type = #{type} and status = 1")
    Integer selectCountByAlbumId(@Param("albumId") Integer albumId,@Param("type") Integer type);

    @Select("select count(id) from activity_impower where album_id = #{albumId} and type = #{type} and user_id = #{userId} and status = 1")
    Integer selectCountByAlbumIdUserId(@Param("albumId") Integer albumId,@Param("type") Integer type,@Param("userId") Integer userId);

    @Select("select count(id) from activity_impower where activity_id = #{activityId} and album_id = #{albumId} and user_id = #{userId} and status = 1")
    Integer selectCountByActivityIdAlbumIdUserId(@Param("activityId") Integer activityId,@Param("albumId") Integer albumId,@Param("userId") Integer userId);

    @Update("update activity_impower set status = 0 where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select count(id) from activity_impower where activity_id = #{activityId} and user_id = #{userId} and status = 1 and type = 2")
    Integer selectCountByActivityIdAlbumIdUserIdAdmin(@Param("activityId") Integer activityId,@Param("userId") Integer userId);
}
