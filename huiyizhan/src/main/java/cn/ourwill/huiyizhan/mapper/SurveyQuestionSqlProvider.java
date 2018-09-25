package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import org.apache.ibatis.jdbc.SQL;

public class SurveyQuestionSqlProvider {

    public String insertSelective(SurveyQuestion record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("survey_question");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getSurveyId() != null) {
            sql.VALUES("survey_id", "#{surveyId,jdbcType=INTEGER}");
        }

        if (record.getTitle() != null) {
            sql.VALUES("title", "#{title,jdbcType=VARCHAR}");
        }

        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }

        if (record.getOrderd() != null) {
            sql.VALUES("orderd", "#{orderd,jdbcType=INTEGER}");
        }

        if (record.getRequired() != null) {
            sql.VALUES("required", "#{required,jdbcType=INTEGER}");
        }

        if (record.getOptionContent() != null) {
            sql.VALUES("option_content", "#{optionContent,jdbcType=VARCHAR}");
        }

        if (record.getDisabled() != null) {
            sql.VALUES("disabled", "#{disabled,jdbcType=INTEGER}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(SurveyQuestion record) {
        SQL sql = new SQL();
        sql.UPDATE("survey_question");

        if (record.getSurveyId() != null) {
            sql.SET("survey_id = #{surveyId,jdbcType=INTEGER}");
        }

        if (record.getTitle() != null) {
            sql.SET("title = #{title,jdbcType=VARCHAR}");
        }

        if (record.getType() != null) {
            sql.SET("type = #{type,jdbcType=INTEGER}");
        }

        if (record.getOrderd() != null) {
            sql.SET("orderd = #{orderd,jdbcType=INTEGER}");
        }

        if (record.getRequired() != null) {
            sql.SET("required = #{required,jdbcType=INTEGER}");
        }

        if (record.getOptionContent() != null) {
            sql.SET("option_content = #{optionContent,jdbcType=VARCHAR}");
        }
        if (record.getDisabled() != null) {
            sql.SET("disabled=#{disabled,jdbcType=INTEGER}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}