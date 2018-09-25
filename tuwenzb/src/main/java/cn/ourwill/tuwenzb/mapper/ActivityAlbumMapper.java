package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 17:22
 * @Version1.0
 */
@Repository
public interface ActivityAlbumMapper extends IBaseMapper<ActivityAlbum> {

    String tableName = "activity_album";
    String columnNoId = "activity_id,user_id,album_name,description,default_flag,c_time";
    String columns = "id," + columnNoId;

    @InsertProvider(type = ActivityAlbumMapperProvider.class, method = "save")
    public Integer save(ActivityAlbum activityAlbum);

    @UpdateProvider(type = ActivityAlbumMapperProvider.class, method = "update")
    public Integer update(ActivityAlbum activityAlbum);

    @SelectProvider(type = ActivityAlbumMapperProvider.class, method = "findAll")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_name", property = "albumName"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "default_flag", property = "defaultFlag"),
            @Result(column = "description", property = "description"),
    })
    public List<ActivityAlbum> findAll();

    @SelectProvider(type = ActivityAlbumMapperProvider.class, method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_name", property = "albumName"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "default_flag", property = "defaultFlag"),
            @Result(column = "description", property = "description"),
    })
    public ActivityAlbum getById(Integer id);

    @DeleteProvider(type = ActivityAlbumMapperProvider.class, method = "deleteById")
    public Integer delete(Map param);

    @Select("select id,album_name,activity_id,user_id,description,default_flag,c_time from activity_album where activity_id = #{activityId} AND delete_status = 0")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_name", property = "albumName"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "default_flag", property = "defaultFlag"),
            @Result(column = "description", property = "description"),
            @Result(column = "id", property = "photoNumber",
                    one = @One(select = "cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper.selectCountByAlbumIdForActivity", fetchType = FetchType.EAGER)
            )
    })
    List<ActivityAlbum> selectByActivity(Integer activityId);

    @Select({"select DISTINCT a.id,a.album_name,a.activity_id,a.user_id,a.description,a.default_flag,a.c_time from activity_album a",
            "left join activity_impower b on a.id = b.album_id",
            "where a.activity_id = #{activityId} and b.type = #{type} and b.status = 1 and (b.user_id = #{userId} or a.user_id = #{userId})  AND delete_status = 0"})
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "album_name", property = "albumName"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "default_flag", property = "defaultFlag"),
            @Result(column = "description", property = "description"),
    })
    List<ActivityAlbum> selectByActivityImpower(@Param("activityId") Integer activityId, @Param("userId") Integer userId, @Param("type") Integer type);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.activityId},#{item.userId},#{item.albumName},#{item.description},#{item.defaultFlag},#{item.cTime})" +
            "</foreach> " +
            "</script>")
    int batchSave(@Param("items") List<ActivityAlbum> activityAlbums);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columns + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.activityId},#{item.userId},#{item.albumName},#{item.description},#{item.defaultFlag},#{item.cTime})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),activity_id = VALUES(activity_id),user_id = VALUES(user_id),album_name = VALUES(album_name),description = VALUES(description),default_flag = VALUES(default_flag),c_time = VALUES(c_time)" +
            "</script>")
    int batchUpdate(@Param("items") List<ActivityAlbum> activityAlbums);

    @Select(" select id from activity_album where activity_id = #{activityId,jdbcType=INTEGER}")
    List<Integer> findAllId(@Param("activityId") Integer activityId);

    @Select({"<script>" +
            "delete  from " + tableName +
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    void batchDelete(@Param("items") List<Integer> items);

    @Update({"<script>" +
            "update  " + tableName +
            " set delete_status = 1" +
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    Integer updateDeleteStatus(@Param("items") ArrayList<Integer> items);

    @Select(" select id from activity_album where activity_id = #{activityId,jdbcType=INTEGER} AND default_flag = 1")
    Integer getIdByActivityIdAndDefaultFlag(Integer activityId);

    @Select("select id from activity_album where activity_id = #{activityId,jdbcType=INTEGER}")
    List<Integer> getActivityAlbumIdsByActivityID(Integer id);

    @Update({"update activity_album set default_flag = #{defaultFlag,jdbcType=INTEGER}",
            " where activity_id = #{activityId,jdbcType=INTEGER} AND id = #{id,jdbcType=INTEGER}",
    })
    Integer updateDefaultFlag(@Param("activityId") Integer activityId,@Param("id") Integer id, @Param("defaultFlag")Integer defaultFlag);
}
