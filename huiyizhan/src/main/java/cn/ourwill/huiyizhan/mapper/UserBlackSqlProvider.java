package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.UserBlack;
import org.apache.ibatis.jdbc.SQL;

public class UserBlackSqlProvider {

    public String insertSelective(UserBlack record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user_black");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getForceOutTime() != null) {
            sql.VALUES("force_out_time", "#{forceOutTime,jdbcType=INTEGER}");
        }
        
        if (record.getReason() != null) {
            sql.VALUES("reason", "#{reason,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserBlack record) {
        SQL sql = new SQL();
        sql.UPDATE("user_black");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getForceOutTime() != null) {
            sql.SET("force_out_time = #{forceOutTime,jdbcType=INTEGER}");
        }
        
        if (record.getReason() != null) {
            sql.SET("reason = #{reason,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}