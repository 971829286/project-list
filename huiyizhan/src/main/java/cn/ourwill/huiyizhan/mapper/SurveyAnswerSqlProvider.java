package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import org.apache.ibatis.jdbc.SQL;

public class SurveyAnswerSqlProvider {

    public String insertSelective(SurveyAnswer record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("survey_answer");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getSurveyQuestionId() != null) {
            sql.VALUES("survey_question_id", "#{surveyQuestionId,jdbcType=INTEGER}");
        }
        
        if (record.getAnswer() != null) {
            sql.VALUES("answer", "#{answer,jdbcType=VARCHAR}");
        }
        
        if (record.getSource() != null) {
            sql.VALUES("source", "#{source,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateDate() != null) {
            sql.VALUES("create_date", "#{createDate,jdbcType=TIMESTAMP}");
        }
        if (record.getType()!= null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(SurveyAnswer record) {
        SQL sql = new SQL();
        sql.UPDATE("survey_answer");
        

        if (record.getSurveyQuestionId() != null) {
            sql.SET("survey_question_id = #{surveyQuestionId,jdbcType=INTEGER}");
        }
        
        if (record.getAnswer() != null) {
            sql.SET("answer = #{answer,jdbcType=VARCHAR}");
        }
        
        if (record.getSource() != null) {
            sql.SET("source = #{source,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateDate() != null) {
            sql.SET("create_date = #{createDate,jdbcType=TIMESTAMP}");
        }

        if (record.getType()!= null) {
            sql.SET("type = #{type,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}