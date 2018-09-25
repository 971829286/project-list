package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityPartner;
import org.apache.ibatis.jdbc.SQL;

public class ActivityPartnerSqlProvider {

    public String insertSelective(ActivityPartner record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_partner");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getPartnerType() != null) {
            sql.VALUES("partner_type", "#{partnerType,jdbcType=VARCHAR}");
        }
        
        if (record.getLogoPics() != null) {
            sql.VALUES("logo_pics", "#{logoPics,jdbcType=VARCHAR}");
        }
        
        if (record.getPriority() != null) {
            sql.VALUES("priority", "#{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivityPartner record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_partner");
        
        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }
        
        if (record.getPartnerType() != null) {
            sql.SET("partner_type = #{partnerType,jdbcType=VARCHAR}");
        }
        
        if (record.getLogoPics() != null) {
            sql.SET("logo_pics = #{logoPics,jdbcType=VARCHAR}");
        }
        
        if (record.getPriority() != null) {
            sql.SET("priority = #{priority,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}