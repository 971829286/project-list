package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.UserStatistics;
import org.apache.ibatis.jdbc.SQL;

public class UserStatisticsSqlProvider {

    public String insertSelective(UserStatistics record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user_statistics");

        if (record.getPopularity() != null) {
            sql.VALUES("popularity", "#{popularity,jdbcType=INTEGER}");
        }

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserStatistics record) {
        SQL sql = new SQL();
        sql.UPDATE("user_statistics");

        if (record.getPopularity() != null) {
            sql.SET("popularity = #{popularity,jdbcType=INTEGER}");
        }
        sql.WHERE("user_id = #{userId,jdbcType=INTEGER}");

        return sql.toString();
    }
}