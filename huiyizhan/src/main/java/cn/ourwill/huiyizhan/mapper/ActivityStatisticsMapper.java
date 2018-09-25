package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityStatistics;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityStatisticsMapper extends IBaseMapper<ActivityStatistics> {

    public String tableName = "activity_statistics";
    public String column = "activity_id, watch_count ";

    @Delete({
            "delete from activity_statistics",
            "where activity_id = #{activityId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Select({
            "select",
            "activity_id, watch_count",
            "from activity_statistics",
            "where activity_id = #{activityId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "watch_count", property = "watchCount", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER)
    })
    ActivityStatistics selectByPrimaryKey(Integer activityId);

    @Insert({
            "insert into activity_statistics (activity_id, watch_count)",
            "values (#{activityId,jdbcType=INTEGER}, ",
            "#{watchCount,jdbcType=INTEGER})"
    })
    int insert(ActivityStatistics record);

    @InsertProvider(type = ActivityStatisticsSqlProvider.class, method = "insertSelective")
    int insertSelective(ActivityStatistics record);

    @Select({
            "select",
            "activity_id,watch_count",
            "from activity_statistics"
    })
    @Results({
            @Result(column = "watch_count", property = "watchCount", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER)
    })
    List<ActivityStatistics> findAll();

    @UpdateProvider(type = ActivityStatisticsSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityStatistics record);

    @Update({
            "update activity_statistics",
            "set watch_count = #{watchCount,jdbcType=INTEGER},",
            "where activity_id = #{activityId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivityStatistics record);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.activityId},#{item.watchCount})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "activity_id = VALUES(activity_id)," +
            "watch_count = VALUES(watch_count) " +
            "</script>")
    Integer batchUpdate(@Param("items") List<ActivityStatistics> items);

    @Select("select id from activity_statistics")
    List<Integer> findAllId();
}