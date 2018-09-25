package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivitySchedule;
import org.apache.ibatis.jdbc.SQL;

public class ActivityScheduleSqlProvider {

    public String insertSelective(ActivitySchedule record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_schedule");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getScheduleTitle() != null) {
            sql.VALUES("schedule_title", "#{scheduleTitle,jdbcType=VARCHAR}");
        }
        
        if (record.getSchedulePlace() != null) {
            sql.VALUES("schedule_place", "#{schedulePlace,jdbcType=VARCHAR}");
        }
        
        if (record.getStartDate() != null) {
            sql.VALUES("start_date", "#{startDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getPriority() != null) {
            sql.VALUES("priority", "#{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getScheduleDetail() != null) {
            sql.VALUES("schedule_detail", "#{scheduleDetail,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivitySchedule record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_schedule");
        
        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getScheduleTitle() != null) {
            sql.SET("schedule_title = #{scheduleTitle,jdbcType=VARCHAR}");
        }
        
        if (record.getSchedulePlace() != null) {
            sql.SET("schedule_place = #{schedulePlace,jdbcType=VARCHAR}");
        }
        
        if (record.getStartDate() != null) {
            sql.SET("start_date = #{startDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getPriority() != null) {
            sql.SET("priority = #{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getScheduleDetail() != null) {
            sql.SET("schedule_detail = #{scheduleDetail,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}