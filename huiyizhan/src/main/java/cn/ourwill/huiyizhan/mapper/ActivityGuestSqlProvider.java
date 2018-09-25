package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityGuest;
import org.apache.ibatis.jdbc.SQL;

public class ActivityGuestSqlProvider {

    public String insertSelective(ActivityGuest record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_guest");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getGuestName() != null) {
            sql.VALUES("guest_name", "#{guestName,jdbcType=VARCHAR}");
        }
        
        if (record.getGuestIdentity() != null) {
            sql.VALUES("guest_identity", "#{guestIdentity,jdbcType=VARCHAR}");
        }
        
        if (record.getGuestAvatar() != null) {
            sql.VALUES("guest_avatar", "#{guestAvatar,jdbcType=VARCHAR}");
        }
        
        if (record.getPriority() != null) {
            sql.VALUES("priority", "#{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getGuestIntro() != null) {
            sql.VALUES("guest_intro", "#{guestIntro,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivityGuest record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_guest");
        
        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getGuestName() != null) {
            sql.SET("guest_name = #{guestName,jdbcType=VARCHAR}");
        }
        
        if (record.getGuestIdentity() != null) {
            sql.SET("guest_identity = #{guestIdentity,jdbcType=VARCHAR}");
        }
        
        if (record.getGuestAvatar() != null) {
            sql.SET("guest_avatar = #{guestAvatar,jdbcType=VARCHAR}");
        }
        
        if (record.getPriority() != null) {
            sql.SET("priority = #{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getGuestIntro() != null) {
            sql.SET("guest_intro = #{guestIntro,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}