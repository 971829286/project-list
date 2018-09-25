package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityGuest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityGuestMapper extends IBaseMapper<ActivityGuest>{

    public String columnNoId = "activity_id, guest_name, guest_identity, guest_avatar, priority, c_time,guest_intro";
    String column = "id,activity_id, guest_name, guest_identity, guest_avatar, priority, c_time,guest_intro";
    public String tableName = "activity_guest";

    @Delete({
        "delete from activity_guest",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into activity_guest (id, activity_id, ",
        "guest_name, guest_identity, ",
        "guest_avatar, priority, ",
        "c_time, guest_intro)",
        "values (#{id,jdbcType=INTEGER}, #{activityId,jdbcType=INTEGER}, ",
        "#{guestName,jdbcType=VARCHAR}, #{guestIdentity,jdbcType=VARCHAR}, ",
        "#{guestAvatar,jdbcType=VARCHAR}, #{priority,jdbcType=INTEGER}, ",
        "#{cTime,jdbcType=TIMESTAMP}, #{guestIntro,jdbcType=LONGVARCHAR})"
    })
    int insert(ActivityGuest record);

    @InsertProvider(type=ActivityGuestSqlProvider.class, method="insertSelective")
    int insertSelective(ActivityGuest record);

    @Select({
        "select",
        "id, activity_id, guest_name, guest_identity, guest_avatar, priority, c_time, ",
        "guest_intro",
        "from activity_guest",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="activity_id", property="activityId", jdbcType=JdbcType.INTEGER),
        @Result(column="guest_name", property="guestName", jdbcType=JdbcType.VARCHAR),
        @Result(column="guest_identity", property="guestIdentity", jdbcType=JdbcType.VARCHAR),
        @Result(column="guest_avatar", property="guestAvatar", jdbcType=JdbcType.VARCHAR),
        @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER),
        @Result(column="c_time", property="cTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="guest_intro", property="guestIntro", jdbcType=JdbcType.LONGVARCHAR)
    })
    ActivityGuest selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ActivityGuestSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityGuest record);

    @Update({
        "update activity_guest",
        "set activity_id = #{activityId,jdbcType=INTEGER},",
          "guest_name = #{guestName,jdbcType=VARCHAR},",
          "guest_identity = #{guestIdentity,jdbcType=VARCHAR},",
          "guest_avatar = #{guestAvatar,jdbcType=VARCHAR},",
          "priority = #{priority,jdbcType=INTEGER},",
          "c_time = #{cTime,jdbcType=TIMESTAMP},",
          "guest_intro = #{guestIntro,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(ActivityGuest record);

    @Update({
        "update activity_guest",
        "set activity_id = #{activityId,jdbcType=INTEGER},",
          "guest_name = #{guestName,jdbcType=VARCHAR},",
          "guest_identity = #{guestIdentity,jdbcType=VARCHAR},",
          "guest_avatar = #{guestAvatar,jdbcType=VARCHAR},",
          "priority = #{priority,jdbcType=INTEGER},",
          "c_time = #{cTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivityGuest record);

    @Select({
            "select",
            "id, activity_id, guest_name, guest_identity, guest_avatar, priority, c_time, ",
            "guest_intro",
            "from activity_guest"
    })
    List<ActivityGuest> findAll();

    @Select({
            "select",
            "id, activity_id, guest_name, guest_identity, guest_avatar, priority, c_time, ",
            "guest_intro",
            "from activity_guest",
            "where activity_id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="activity_id", property="activityId", jdbcType=JdbcType.INTEGER),
            @Result(column="guest_name", property="guestName", jdbcType=JdbcType.VARCHAR),
            @Result(column="guest_identity", property="guestIdentity", jdbcType=JdbcType.VARCHAR),
            @Result(column="guest_avatar", property="guestAvatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER),
            @Result(column="c_time", property="cTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="guest_intro", property="guestIntro", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<ActivityGuest> getByActivityId(Integer id);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> "
            +
            "(#{item.activityId},#{item.guestName},#{item.guestIdentity},#{item.guestAvatar},#{item.priority},#{item.cTime},#{item.guestIntro})"
            +
            "</foreach> " +
            "</script>")
    int batchSave(@Param("items") List<ActivityGuest> items);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column+ ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.activityId},#{item.guestName},#{item.guestIdentity},#{item.guestAvatar},#{item.priority},#{item.cTime},#{item.guestIntro})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),activity_id = VALUES(activity_id), guest_name = VALUES( guest_name), guest_identity = VALUES( guest_identity), guest_avatar = VALUES( guest_avatar), priority = VALUES( priority), c_time = VALUES( c_time),guest_intro = VALUES(guest_intro)" +
            "</script>")
    Integer batchUpload(@Param("items") List<ActivityGuest> items);

    @Select(" select id from activity_guest where activity_id = #{activityId,jdbcType=INTEGER}")
    List<Integer> findAllId(@Param("activityId") Integer activityId);

    @Select({"<script>" +
            "delete  from " + tableName +
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    void batchDelete(@Param("items") List<Integer> items);
}