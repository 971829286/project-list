package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivitySort;
import org.apache.ibatis.jdbc.SQL;

public class ActivitySortSqlProvider {

    public String insertSelective(ActivitySort record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_sort");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getHotActivityId() != null) {
            sql.VALUES("hot_activity_id", "#{hotActivityId,jdbcType=INTEGER}");
        }
        
        if (record.getRecentActivityId() != null) {
            sql.VALUES("recent_activity_id", "#{recentActivityId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivitySort record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_sort");
        
        if (record.getHotActivityId() != null) {
            sql.SET("hot_activity_id = #{hotActivityId,jdbcType=INTEGER}");
        }
        
        if (record.getRecentActivityId() != null) {
            sql.SET("recent_activity_id = #{recentActivityId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}