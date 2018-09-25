package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityContact;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityContactMapper extends IBaseMapper<ActivityContact> {

    public String tableName = "activity_contact";
    public String columnNoId = "activity_id, contact_name, contact_phone, contact_wechat, contact_email,contact_address, priority, c_time";
    public String column = "id,activity_id, contact_name, contact_phone, contact_wechat, contact_email,contact_address, priority, c_time";

    @Delete({
            "delete from activity_contact",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into activity_contact (id, activity_id, ",
            "contact_name, contact_phone, ",
            "contact_wechat, contact_email, ",
            "contact_address, priority, ",
            "c_time)",
            "values (#{id,jdbcType=INTEGER}, #{activityId,jdbcType=INTEGER}, ",
            "#{contactName,jdbcType=VARCHAR}, #{contactPhone,jdbcType=VARCHAR}, ",
            "#{contactWechat,jdbcType=VARCHAR}, #{contactEmail,jdbcType=VARCHAR}, ",
            "#{contactAddress,jdbcType=VARCHAR}, #{priority,jdbcType=INTEGER}, ",
            "#{cTime,jdbcType=TIMESTAMP})"
    })
    int insert(ActivityContact record);

    @InsertProvider(type = ActivityContactSqlProvider.class, method = "insertSelective")
    int insertSelective(ActivityContact record);

    @Select({
            "select",
            "id, activity_id, contact_name, contact_phone, contact_wechat, contact_email, ",
            "contact_address, priority, c_time",
            "from activity_contact",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_name", property = "contactName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_phone", property = "contactPhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_wechat", property = "contactWechat", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_email", property = "contactEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_address", property = "contactAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP)
    })
    ActivityContact selectByPrimaryKey(Integer id);


    @UpdateProvider(type = ActivityContactSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityContact record);

    @Update({
            "update activity_contact",
            "set activity_id = #{activityId,jdbcType=INTEGER},",
            "contact_name = #{contactName,jdbcType=VARCHAR},",
            "contact_phone = #{contactPhone,jdbcType=VARCHAR},",
            "contact_wechat = #{contactWechat,jdbcType=VARCHAR},",
            "contact_email = #{contactEmail,jdbcType=VARCHAR},",
            "contact_address = #{contactAddress,jdbcType=VARCHAR},",
            "priority = #{priority,jdbcType=INTEGER},",
            "c_time = #{cTime,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivityContact record);

    @Select({
            "select",
            "id, activity_id, contact_name, contact_phone, contact_wechat, contact_email, ",
            "contact_address, priority, c_time",
            "from activity_contact"
    })
    List<ActivityContact> findAll();


    @Select({
            "select",
            "id, activity_id, contact_name, contact_phone, contact_wechat, contact_email, ",
            "contact_address, priority, c_time",
            "from activity_contact",
            "where activity_id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_name", property = "contactName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_phone", property = "contactPhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_wechat", property = "contactWechat", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_email", property = "contactEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_address", property = "contactAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "priority", property = "priority", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ActivityContact> getByActivityId(Integer id);


    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.activityId},#{item.contactName},#{item.contactPhone},#{item.contactWechat},#{item.contactEmail},#{item.contactAddress},#{item.priority},#{item.cTime})"
            +
            "</foreach> " +
            "</script>")
    int batchSave(@Param("items") List<ActivityContact> items);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.activityId},#{item.contactName},#{item.contactPhone},#{item.contactWechat},#{item.contactEmail},#{item.contactAddress},#{item.priority},#{item.cTime})"
            +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),activity_id = VALUES(activity_id), contact_name = VALUES( contact_name), contact_phone = VALUES( contact_phone), contact_wechat = VALUES( contact_wechat), contact_email = VALUES( contact_email),contact_address = VALUES(contact_address), priority = VALUES( priority), c_time = VALUES( c_time)" +
            "</script>")
    Integer batchUpload(@Param("items") List<ActivityContact> items);

    @Select(" select id from activity_contact where activity_id = #{activityId,jdbcType=INTEGER}")
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