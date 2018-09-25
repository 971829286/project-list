package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityPartner;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityPartnerMapper extends IBaseMapper<ActivityPartner> {

    public String columnNoId = " activity_id, partner_type, logo_pics, priority, c_time";

    public String column = "id,activity_id, partner_type, logo_pics, priority, c_time";

    public String tableName = "activity_partner";

    @Delete({
            "delete from activity_partner",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into activity_partner (id, activity_id, ",
            "partner_type, logo_pics, ",
            "priority, c_time)",
            "values (#{id,jdbcType=INTEGER}, #{activityId,jdbcType=INTEGER}, ",
            "#{partnerType,jdbcType=VARCHAR}, #{logoPics,jdbcType=VARCHAR}, ",
            "#{priority,jdbcType=INTEGER}, #{cTime,jdbcType=TIMESTAMP})"
    })
    int insert(ActivityPartner record);

    @InsertProvider(type = ActivityPartnerSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(ActivityPartner record);

    @Select({
            "select",
            "id, activity_id, partner_type, logo_pics, priority, c_time",
            "from activity_partner",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_type", property = "partnerType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "logo_pics", property = "logoPics", jdbcType = JdbcType.VARCHAR),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP)
    })
    ActivityPartner selectByPrimaryKey(Integer id);

    @UpdateProvider(type = ActivityPartnerSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityPartner record);

    @Update({
            "update activity_partner",
            "set activity_id = #{activityId,jdbcType=INTEGER},",
            "partner_type = #{partnerType,jdbcType=VARCHAR},",
            "logo_pics = #{logoPics,jdbcType=VARCHAR},",
            "priority = #{priority,jdbcType=INTEGER},",
            "c_time = #{cTime,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivityPartner record);

    @Select({
            "select",
            "id, activity_id, partner_type, logo_pics, priority, c_time",
            "from activity_partner"
    })
    List<ActivityPartner> findAll();

    @Select({
            "select",
            "id, activity_id, partner_type, logo_pics, priority, c_time",
            "from activity_partner",
            "where activity_id = #{activityId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_type", property = "partnerType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "logo_pics", property = "logoPics", jdbcType = JdbcType.VARCHAR),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ActivityPartner> getByActivityId(@Param("activityId") Integer activityId);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.activityId},#{item.partnerType},#{item.logoPics},#{item.priority},#{item.cTime})"
            +
            "</foreach> " +
            "</script>")
    int batchSave(@Param("items") List<ActivityPartner> items);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.activityId},#{item.partnerType},#{item.logoPics},#{item.priority},#{item.cTime})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),activity_id = VALUES(activity_id), partner_type = VALUES( partner_type), logo_pics = VALUES( logo_pics), priority = VALUES( priority), c_time = VALUES( c_time)" +
            "</script>")
    Integer batchUpdate(@Param("items") List<ActivityPartner> items);

    @Select(" select id from activity_partner where activity_id = #{activityId,jdbcType=INTEGER}")
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