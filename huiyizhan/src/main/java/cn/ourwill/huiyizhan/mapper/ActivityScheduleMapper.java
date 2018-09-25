package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivitySchedule;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityScheduleMapper extends IBaseMapper<ActivitySchedule> {
    public String columnNoId = " activity_id, schedule_title, schedule_place, start_date, priority, c_time,schedule_detail";

    public String tableName = "activity_schedule";

    String column = "id, activity_id, schedule_title, schedule_place, start_date, priority, c_time,schedule_detail";

    @Delete({
            "delete from activity_schedule",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into activity_schedule (id, activity_id, ",
            "schedule_title, schedule_place, ",
            "start_date, priority, ",
            "c_time, schedule_detail)",
            "values (#{id,jdbcType=INTEGER}, #{activityId,jdbcType=INTEGER}, ",
            "#{scheduleTitle,jdbcType=VARCHAR}, #{schedulePlace,jdbcType=VARCHAR}, ",
            "#{startDate,jdbcType=TIMESTAMP}, #{priority,jdbcType=INTEGER}, ",
            "#{cTime,jdbcType=TIMESTAMP}, #{scheduleDetail,jdbcType=LONGVARCHAR})"
    })
    int insert(ActivitySchedule record);

    @InsertProvider(type = ActivityScheduleSqlProvider.class, method = "insertSelective")
    int insertSelective(ActivitySchedule record);

    @Select({
            "select",
            "id, activity_id, schedule_title, schedule_place, start_date, priority, c_time, ",
            "schedule_detail",
            "from activity_schedule",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_title", property = "scheduleTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "schedule_place", property = "schedulePlace", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_date", property = "startDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "schedule_detail", property = "scheduleDetail", jdbcType = JdbcType.LONGVARCHAR)
    })
    ActivitySchedule selectByPrimaryKey(Integer id);

    @UpdateProvider(type = ActivityScheduleSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivitySchedule record);

    @Update({
            "update activity_schedule",
            "set activity_id = #{activityId,jdbcType=INTEGER},",
            "schedule_title = #{scheduleTitle,jdbcType=VARCHAR},",
            "schedule_place = #{schedulePlace,jdbcType=VARCHAR},",
            "start_date = #{startDate,jdbcType=TIMESTAMP},",
            "priority = #{priority,jdbcType=INTEGER},",
            "c_time = #{cTime,jdbcType=TIMESTAMP},",
            "schedule_detail = #{scheduleDetail,jdbcType=LONGVARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(ActivitySchedule record);

    @Update({
            "update activity_schedule",
            "set activity_id = #{activityId,jdbcType=INTEGER},",
            "schedule_title = #{scheduleTitle,jdbcType=VARCHAR},",
            "schedule_place = #{schedulePlace,jdbcType=VARCHAR},",
            "start_date = #{startDate,jdbcType=TIMESTAMP},",
            "priority = #{priority,jdbcType=INTEGER},",
            "c_time = #{cTime,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivitySchedule record);

    @Select({
            "select",
            "id, activity_id, partner_type, logo_pics, priority, c_time",
            "from activity_partner"
    })
    List<ActivitySchedule> findAll();

    @Select({
            "select",
            "id, activity_id, schedule_title, schedule_place, start_date, priority, c_time, ",
            "schedule_detail",
            "from activity_schedule",
            "where activity_id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_title", property = "scheduleTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "schedule_place", property = "schedulePlace", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_date", property = "startDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "schedule_detail", property = "scheduleDetail", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<ActivitySchedule> getByActivityId(Integer id);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> "
            +
            "(#{item.activityId},#{item.scheduleTitle},#{item.schedulePlace},#{item.startDate},#{item.priority},#{item.cTime},#{item.scheduleDetail})"
            +
            "</foreach> " +
            "</script>")
    int batchSave(@Param("items") List<ActivitySchedule> items);

    @Insert("<script> " +
            "insert into " + tableName +
            "(id, activity_id, schedule_title, schedule_place, start_date, priority,schedule_detail) " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.activityId},#{item.scheduleTitle},#{item.schedulePlace},#{item.startDate},#{item.priority},#{item.scheduleDetail})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id), activity_id = VALUES( activity_id), schedule_title = VALUES( schedule_title), schedule_place = VALUES( schedule_place), start_date = VALUES( start_date), priority = VALUES( priority),schedule_detail = VALUES(schedule_detail)" +
            "</script>")
    Integer batchUpDate(@Param("items") List<ActivitySchedule> items);

    @Select(" select id from activity_schedule where activity_id = #{activityId,jdbcType=INTEGER}")
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