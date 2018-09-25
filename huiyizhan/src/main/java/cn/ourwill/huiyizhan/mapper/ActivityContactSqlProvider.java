package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityContact;
import org.apache.ibatis.jdbc.SQL;

public class ActivityContactSqlProvider {

    public String insertSelective(ActivityContact record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_contact");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }

        if (record.getContactName() != null) {
            sql.VALUES("contact_name", "#{contactName,jdbcType=VARCHAR}");
        }

        if (record.getContactPhone() != null) {
            sql.VALUES("contact_phone", "#{contactPhone,jdbcType=VARCHAR}");
        }

        if (record.getContactWechat() != null) {
            sql.VALUES("contact_wechat", "#{contactWechat,jdbcType=VARCHAR}");
        }

        if (record.getContactEmail() != null) {
            sql.VALUES("contact_email", "#{contactEmail,jdbcType=VARCHAR}");
        }

        if (record.getContactAddress() != null) {
            sql.VALUES("contact_address", "#{contactAddress,jdbcType=VARCHAR}");
        }

        if (record.getPriority() != null) {
            sql.VALUES("priority", "#{priority,jdbcType=INTEGER}");
        }

        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivityContact record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_contact");

        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }

        if (record.getContactName() != null) {
            sql.SET("contact_name = #{contactName,jdbcType=VARCHAR}");
        }

        if (record.getContactPhone() != null) {
            sql.SET("contact_phone = #{contactPhone,jdbcType=VARCHAR}");
        }

        if (record.getContactWechat() != null) {
            sql.SET("contact_wechat = #{contactWechat,jdbcType=VARCHAR}");
        }

        if (record.getContactEmail() != null) {
            sql.SET("contact_email = #{contactEmail,jdbcType=VARCHAR}");
        }

        if (record.getContactAddress() != null) {
            sql.SET("contact_address = #{contactAddress,jdbcType=VARCHAR}");
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