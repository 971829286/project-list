package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityStatistics;
import org.apache.ibatis.jdbc.SQL;

public class ActivityStatisticsSqlProvider {

    public String insertSelective(ActivityStatistics record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_statistics");

        if (record.getWatchCount() != null) {
            sql.VALUES("watch_count", "#{watchCount,jdbcType=INTEGER}");
        }

        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivityStatistics record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_statistics");

        if (record.getWatchCount() != null) {
            sql.SET("watch_count = #{watchCount,jdbcType=INTEGER}");
        }
        sql.WHERE("activity_id = #{activityId,jdbcType=INTEGER}");

        return sql.toString();
    }
}