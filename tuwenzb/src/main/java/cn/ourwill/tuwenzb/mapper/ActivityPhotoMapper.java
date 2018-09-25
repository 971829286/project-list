package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.PhotoLog;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 17:23
 * @Version1.0
 */
@Repository
public interface ActivityPhotoMapper extends IBaseMapper<ActivityPhoto>{
    @InsertProvider(type = ActivityPhotoMapperProvider.class,method ="save")
    Integer save(ActivityPhoto activityPhoto);

    @UpdateProvider(type = ActivityPhotoMapperProvider.class,method = "update")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Integer update(ActivityPhoto activityPhoto);

    @SelectProvider(type = ActivityPhotoMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result(column = "is_save_to_faceset",property = "isSaveToFaceSet")

    })
    List<ActivityPhoto> findAll();

    @SelectProvider(type = ActivityPhotoMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result(column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    ActivityPhoto getById(Integer id);

    @DeleteProvider(type = ActivityPhotoMapperProvider.class,method = "deleteById")
    Integer delete(Map param);

    @Insert("<script> " +
            "insert activity_photo " +
            "(user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,download_name,photo_name,shooting_time,download_status,replace_status) "+
            "values "+
            "<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\"> "+
            " (#{item.userId},#{item.albumId},#{item.photoPath},#{item.photoStatus},#{item.photoSize},#{item.photoHeight},#{item.photoWidth},#{item.photoOrientation},#{item.cTime},#{item.uTime},#{item.downloadName},#{item.photoName},#{item.shootingTime},#{item.downloadStatus},#{item.replaceStatus})"+
            "</foreach> "+
            "</script>" )
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Integer batchSave(List<ActivityPhoto> list);

    @Delete("<script> " +
            "delete from activity_photo " +
            "where album_id = #{albumId} "+
            "and id in "+
            "<foreach collection=\"photoIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item}"+
            "</foreach> " +
            "and photo_status = 0 "+
            "</script>" )
    Integer batchDelete(@Param("photoIds") List<Integer> photoIds, @Param("albumId")Integer albumId);

    @Select("<script> " +
            " select p.id,p.user_id,p.album_id,p.photo_path,p.photo_status,p.photo_size,p.photo_height,p.photo_width,p.photo_orientation,p.c_time,p.u_time,p.like_num,p.download_name,p.photo_name,p.shooting_time,p.download_status,p.replace_status,p.is_save_to_faceset, " +
            " u.username,u.nickname,u.avatar" +
            " from activity_photo p " +
            " left join user u on u.id = p.user_id " +
            " <where> and album_id=#{albumId} " +
            "   <if test=\"photoStatus!=null\"> " +
            "    and photo_status=#{photoStatus} " +
            "   </if>" +
            " </where>" +
            " ${orderBy} " +
            "</script>")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result( column = "download_name",property = "downloadName"),
            @Result( column = "photo_name",property = "photoName"),
            @Result( column = "shooting_time",property = "shootingTime"),
            @Result( column = "download_status",property = "downloadStatus"),
            @Result( column = "replace_status",property = "replaceStatus"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet"),
    })
    List<ActivityPhoto> selectByAlbumId(@Param("albumId") Integer albumId,@Param("photoStatus") Integer photoStatus,@Param("orderBy") String orderBy);

    @Select("<script> " +
            "select id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset from activity_photo " +
            "where id in "+
            "<foreach collection=\"photoIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item} "+
            "</foreach> "+
            "</script>" )
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    List<ActivityPhoto> selectByIds(@Param("photoIds") List<Integer> photoIds);

    @Select("<script> " +
            "select photo_path from activity_photo " +
            "where id in "+
            "<foreach collection=\"photoIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item} "+
            "</foreach> "+
            "</script>" )
    List<String> selectPathByIds(@Param("photoIds") List<Integer> photoIds);

    @Update("<script> " +
            "update activity_photo " +
            "set photo_status = #{photoStatus},u_time = SYSDATE() "+
            "where album_id = #{albumId} and id in "+
            "<foreach collection=\"photoIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item}"+
            "</foreach> "+
            "</script>" )
    Integer batchPublish(@Param("photoIds") List<Integer> photoIds,@Param("albumId") Integer albumId, @Param("photoStatus")Integer photoStatus);

    @Select("select id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset from activity_photo where album_id = #{albumId} and download_name = #{downloadName}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    ActivityPhoto getByDownloadName(@Param("downloadName") String downloadName, @Param("albumId") Integer albumId);

    @Select("select id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset from activity_photo where album_id = #{albumId} and photo_name = #{photoName}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet"),
    })
    ActivityPhoto getByPhotoName(@Param("photoName") String photoName, @Param("albumId") Integer albumId);

    @Update("update activity_photo set download_status = 1 where id = #{photoId}")
    Integer updateDownLoadStatus(@Param("photoId") Integer photoId);

    @Update("update activity_photo set replace_status = 1 where id = #{photoId}")
    Integer updateReplaceStatus(@Param("photoId") Integer photoId);

    @Insert("insert into photo_log (album_id,number,opera_type,user_id) values (#{photoLog.albumId},#{photoLog.number},#{photoLog.operaType},#{photoLog.userId})")
    Integer insertLog(@Param("photoLog") PhotoLog photoLog);

    @Select("<script> " +
            "select id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset from activity_photo " +
            "where id in "+
            "<foreach collection=\"photoIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item} "+
            "</foreach> " +
            "and photo_status = 0"+
            "</script>" )
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result(column = "download_name",property = "downloadName"),
            @Result(column = "photo_name",property = "photoName"),
            @Result(column = "shooting_time",property = "shootingTime"),
            @Result(column = "download_status",property = "downloadStatus"),
            @Result(column = "replace_status",property = "replaceStatus"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    List<ActivityPhoto> selectNoPublishedByIds(@Param("photoIds") List<Integer> photoIds);

    @Select("select sum(number) from photo_log where album_id = #{albumId} and c_time>= #{time}")
    Integer selectCountLogAfterTime(@Param("albumId") Integer albumId, @Param("time") Date time);

    @Select({"<script> select count(1) as updateCount, max(u_time) as maxTime",
            "from activity_photo where album_id = #{albumId}",
            "<if test=\"photoStatus!=null\"> ",
            "and photo_status=#{photoStatus} ",
            "</if>",
            "and photo_status &lt;&gt; 2",
            "and u_time > #{time}</script>"})
    Map selectCountAfterUTime(@Param("albumId") Integer albumId,@Param("photoStatus") Integer photoStatus, @Param("time") Date time);

    @Select({"<script> select count(1) from activity_photo where album_id = #{albumId}",
            "<if test=\"photoStatus!=null\"> ",
            "and photo_status=#{photoStatus} ",
            "</if>",
            "and photo_status &lt;&gt; 2</script>"})
    Integer selectCountByAlbumId(@Param("albumId") Integer albumId,@Param("photoStatus") Integer photoStatus);

    @Select({"select count(1) from activity_photo where album_id = #{albumId}"})
    Integer selectCountByAlbumIdForActivity(@Param("albumId") Integer albumId);

    @Select("<script> " +
            " select p.id,p.user_id,p.album_id,p.photo_path,p.photo_status,p.photo_size,p.photo_height,p.photo_width,p.photo_orientation,p.c_time,p.u_time,p.like_num,p.download_name,p.photo_name,p.shooting_time,p.download_status,p.replace_status,p.is_save_to_faceset, " +
            " u.username,u.nickname,u.avatar" +
            " from activity_photo p " +
            " left join activity_album al on p.album_id = al.id " +
            " left join user u on u.id = p.user_id " +
            " <where> and al.activity_id=#{activityId} " +
            "   <if test=\"photoStatus!=null\"> " +
            "    and photo_status=#{photoStatus} " +
            "   </if>" +
            " </where>" +
            " ${orderBy} " +
            "</script>")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result( column = "download_name",property = "downloadName"),
            @Result( column = "photo_name",property = "photoName"),
            @Result( column = "shooting_time",property = "shootingTime"),
            @Result( column = "download_status",property = "downloadStatus"),
            @Result( column = "replace_status",property = "replaceStatus"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    List<ActivityPhoto> selectByActivityId(@Param("activityId") Integer activityId, @Param("photoStatus") Integer photoStatus, @Param("orderBy") String orderBy);

    @Select("select count(1) from activity_photo p left join activity_album a on p.album_id = a.id where a.activity_id = #{activityId} and p.photo_status <> 2")
    Integer getPhotoCount(@Param("activityId") Integer activityId);

//    @Update({"<script>",
//            "INSERT INTO activity_photo (id,user_id,album_id,photo_size,photo_width,photo_height,photo_orientation)",
//            "VALUES",
//            "<foreach collection=\"photos\" index=\"index\" item=\"item\" separator=\",\">",
//            "(#{item.id},#{item.userId},#{item.albumId},#{item.photoSize},#{item.photoWidth},#{item.photoHeight},#{item.photoOrientation})",
//            "</foreach>",
//            "ON DUPLICATE KEY UPDATE",
//            "photo_size = VALUES(photo_size),",
//            "photo_width = VALUES(photo_width),",
//            "photo_height = VALUES(photo_height),",
//            "photo_orientation = VALUES(photo_orientation)",
//            "</script>"})
//    public Integer batchUpdateInfo(@Param("photos") List photos);
//
//    @Select("select id,user_id,album_id,photo_path,photo_size,photo_height,photo_width,photo_orientation from activity_photo where photo_orientation is null order by id asc limit #{size}" )
//    @Results({
//            @Result(id = true,column = "id",property = "id"),
//            @Result( column = "user_id",property = "userId"),
//            @Result( column = "album_id",property = "albumId"),
//            @Result( column = "photo_path",property = "photoPath"),
//            @Result( column = "photo_status",property = "photoStatus"),
//            @Result( column = "photo_size",property = "photoSize"),
//            @Result( column = "photo_height",property = "photoHeight"),
//            @Result( column = "photo_width",property = "photoWidth"),
//            @Result( column = "photo_orientation",property = "photoOrientation"),
//            @Result( column = "c_time",property = "cTime"),
//            @Result( column = "like_num",property = "likeNum"),
//            @Result(column = "download_name",property = "downloadName"),
//            @Result(column = "photo_name",property = "photoName"),
//            @Result(column = "shooting_time",property = "shootingTime"),
//            @Result(column = "download_status",property = "downloadStatus"),
//            @Result(column = "replace_status",property = "replaceStatus")
//    })
//    List<ActivityPhoto> selectWithoutInfo(@Param("size") Integer size);

      @Select({"select p.id,p.user_id,p.album_id,p.photo_path,p.photo_status,p.photo_size,p.photo_height,p.photo_width,p.photo_orientation,p.c_time,p.u_time,p.like_num,p.download_name,p.photo_name,p.shooting_time,p.download_status,p.replace_sta,p.is_save_to_faceset",
              "from activity_photo as p",
              "left join activity_photo_token as apt on p.id=apt.photo_id",
              "where apt.face_token = #{faceToken}"

      })
      @Results({
              @Result(id = true,column = "id",property = "id"),
              @Result( column = "user_id",property = "userId"),
              @Result( column = "album_id",property = "albumId"),
              @Result( column = "photo_path",property = "photoPath"),
              @Result( column = "photo_status",property = "photoStatus"),
              @Result( column = "photo_size",property = "photoSize"),
              @Result( column = "photo_height",property = "photoHeight"),
              @Result( column = "photo_width",property = "photoWidth"),
              @Result( column = "photo_orientation",property = "photoOrientation"),
              @Result( column = "c_time",property = "cTime"),
              @Result( column = "u_time",property = "uTime"),
              @Result( column = "like_num",property = "likeNum"),
              @Result( column = "download_name",property = "downloadName"),
              @Result( column = "photo_name",property = "photoName"),
              @Result( column = "shooting_time",property = "shootingTime"),
              @Result( column = "download_status",property = "downloadStatus"),
              @Result( column = "replace_status",property = "replaceStatus"),
              @Result( column = "username",property = "username"),
              @Result( column = "nickname",property = "nickname"),
              @Result( column = "avatar",property = "avatarUrl"),
              @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
      })
      List<ActivityPhoto> getByFaceToken(@Param("faceToken")String faceToken);

    @Select("<script> " +
            " select p.id,p.user_id,p.album_id,p.photo_path,p.photo_status,p.photo_size,p.photo_height,p.photo_width,p.photo_orientation,p.c_time,p.u_time,p.like_num,p.download_name,p.photo_name,p.shooting_time,p.download_status,p.replace_status,p.is_save_to_faceset " +
            " from activity_photo p" +
            " left join activity_photo_token as apt on p.id=apt.photo_id " +
            " where apt.activity_id=#{activityId} and apt.face_token in "+
            "<foreach collection=\"faceTokenList\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "  #{item}"+
            "</foreach> " +
            "</script>")

    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result( column = "download_name",property = "downloadName"),
            @Result( column = "photo_name",property = "photoName"),
            @Result( column = "shooting_time",property = "shootingTime"),
            @Result( column = "download_status",property = "downloadStatus"),
            @Result( column = "replace_status",property = "replaceStatus"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet"),
    })
    List<ActivityPhoto> getByFaceTokenList(@Param("faceTokenList")List<String> faceTokenList,@Param("activityId") Integer activityId );

    @Select("<script> " +
            " select p.id,p.user_id,p.album_id,p.photo_path,p.photo_status,p.photo_size,p.photo_height,p.photo_width,p.photo_orientation,p.c_time,p.u_time,p.like_num,p.download_name,p.photo_name,p.shooting_time,p.download_status,p.replace_status,p.is_save_to_faceset " +
            " from activity_photo p " +
            " left join activity_album al on p.album_id = al.id " +
            " <where> and al.activity_id=#{activityId} " +
            "    and photo_status != 2 " +
            "    and is_save_to_faceset = #{isSaveToFaceSet}"+
            " </where>" +
            "</script>")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result( column = "download_name",property = "downloadName"),
            @Result( column = "photo_name",property = "photoName"),
            @Result( column = "shooting_time",property = "shootingTime"),
            @Result( column = "download_status",property = "downloadStatus"),
            @Result( column = "replace_status",property = "replaceStatus"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    //按照是否存入faceSet获取该活动下的照片
    List<ActivityPhoto> selectByActivityIdAndIsSaveToFaceSet(@Param("activityId") Integer activityId,@Param("isSaveToFaceSet") Integer isSaveToFaceSet);


    @Update("update activity_photo set is_save_to_faceset=#{isSaveToFaceSet} where id=#{id}")
    Integer updateIsSaveTOFaceSet(@Param("id") Integer id,@Param("isSaveToFaceSet") Integer isSaveToFaceSet);



    @Select("<script> " +
            " select id,user_id,album_id,photo_path,photo_status,photo_size,photo_height,photo_width,photo_orientation,c_time,u_time,like_num,download_name,photo_name,shooting_time,download_status,replace_status,is_save_to_faceset " +
            " from activity_photo " +
            " where id in "+
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "  #{item}"+
            "</foreach> " +
            "</script>")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "album_id",property = "albumId"),
            @Result( column = "photo_path",property = "photoPath"),
            @Result( column = "photo_status",property = "photoStatus"),
            @Result( column = "photo_size",property = "photoSize"),
            @Result( column = "photo_height",property = "photoHeight"),
            @Result( column = "photo_width",property = "photoWidth"),
            @Result( column = "photo_orientation",property = "photoOrientation"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "like_num",property = "likeNum"),
            @Result( column = "download_name",property = "downloadName"),
            @Result( column = "photo_name",property = "photoName"),
            @Result( column = "shooting_time",property = "shootingTime"),
            @Result( column = "download_status",property = "downloadStatus"),
            @Result( column = "replace_status",property = "replaceStatus"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatarUrl"),
            @Result( column = "is_save_to_faceset",property = "isSaveToFaceSet")
    })
    List<ActivityPhoto> getByIds(@Param("ids") List<Integer> ids);

}
