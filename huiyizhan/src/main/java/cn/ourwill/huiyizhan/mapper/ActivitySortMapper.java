package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivitySort;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitySortMapper {
    @Delete({
        "delete from activity_sort",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into activity_sort (id, hot_activity_id, ",
        "recent_activity_id)",
        "values (#{id,jdbcType=INTEGER}, #{hotActivityId,jdbcType=INTEGER}, ",
        "#{recentActivityId,jdbcType=INTEGER})"
    })
    int insert(ActivitySort record);

    @InsertProvider(type=ActivitySortSqlProvider.class, method="insertSelective")
    int insertSelective(ActivitySort record);

    @Select({
        "select",
        "id, hot_activity_id, recent_activity_id",
        "from activity_sort",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="hot_activity_id", property="hotActivityId", jdbcType=JdbcType.INTEGER),
        @Result(column="recent_activity_id", property="recentActivityId", jdbcType=JdbcType.INTEGER)
    })
    ActivitySort selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ActivitySortSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivitySort record);

    @Update({
        "update activity_sort",
        "set hot_activity_id = #{hotActivityId,jdbcType=INTEGER},",
          "recent_activity_id = #{recentActivityId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivitySort record);


    @Insert("<script> " +
            "insert into activity_sort"  +
            "(id,hot_activity_id) " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.hotActivityId})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),hot_activity_id = VALUES( hot_activity_id)" +
            "</script>")
    Integer batchUpdateHot(@Param("items") List<ActivitySort> items);

    @Insert("<script> " +
            "insert into activity_sort"  +
            "(id,recent_activity_id) " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.recentActivityId})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),recent_activity_id = VALUES( recent_activity_id)" +
            "</script>")
    Integer batchUpdateRecent(@Param("items") List<ActivitySort> items);
}