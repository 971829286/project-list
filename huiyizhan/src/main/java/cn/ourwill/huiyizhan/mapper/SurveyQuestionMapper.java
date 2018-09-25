package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SurveyQuestionMapper extends IBaseMapper<SurveyQuestion>{

    String tableName = " survey_question ";
    String columnNoId = "survey_id, title, type, orderd, required, option_content,disabled ";
    String column = " id," + columnNoId;

    @Delete({
            "delete from survey_question",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into survey_question (id, survey_id, ",
            "title, type, orderd, ",
            "required, option_content)",
            "values (#{id,jdbcType=INTEGER}, #{surveyId,jdbcType=INTEGER}, ",
            "#{title,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER}, #{orderd,jdbcType=INTEGER}, ",
            "#{required,jdbcType=INTEGER}, #{optionContent,jdbcType=VARCHAR})"
    })
    int insert(SurveyQuestion record);

    @InsertProvider(type = SurveyQuestionSqlProvider.class, method = "insertSelective")
    int insertSelective(SurveyQuestion record);

    @Select({
            "select",
            "id, survey_id, title, type, orderd, required, disabled ,option_content",
            "from survey_question",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_id", property = "surveyId", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "required", property = "required", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR),
            @Result(column = "disabled", property = "disabled", jdbcType = JdbcType.INTEGER)
    })
    SurveyQuestion selectByPrimaryKey(Integer id);

    @UpdateProvider(type = SurveyQuestionSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SurveyQuestion record);

    @Update({
            "update survey_question",
            "set survey_id = #{surveyId,jdbcType=INTEGER},",
            "title = #{title,jdbcType=VARCHAR},",
            "type = #{type,jdbcType=INTEGER},",
            "orderd = #{orderd,jdbcType=INTEGER},",
            "required = #{required,jdbcType=INTEGER},",
            "option_content = #{optionContent,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SurveyQuestion record);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" type=\"type\" item=\"item\" separator=\",\"> " +
            "(#{item.surveyId},#{item.title},#{item.type},#{item.orderd},#{item.required},#{item.optionContent},#{item.disabled})" +
            "</foreach> " +
            "</script>")
    Integer batchSave(@Param("items") List<SurveyQuestion> items);


    @Select({
            "select",
            "id, survey_id, title, type, orderd, required, option_content",
            "from survey_question",
            "where survey_id = #{surveyId,jdbcType=INTEGER} AND  delete_status=0 AND type in (0,1,2) "
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_id", property = "surveyId", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "required", property = "required", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR),
    })
    List<SurveyQuestion> getBySurveyIdNoOther(@Param("surveyId") Integer surveyId);

    @Select({
            "select",
            "id, survey_id, title, type, orderd, required,disabled, option_content",
            "from survey_question",
            "where survey_id = #{surveyId,jdbcType=INTEGER} AND  delete_status=0 order by orderd"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_id", property = "surveyId", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "required", property = "required", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR),
            @Result(column = "disabled", property = "disabled", jdbcType = JdbcType.INTEGER)
    })
    List<SurveyQuestion> getBySurveyId(@Param("surveyId") Integer surveyId);

    @Select({"select sq.id ,sq.title,sq.type,sq.orderd,sq.option_content   \n" +
            "from survey_question sq \n" +
            "LEFT JOIN survey s ON sq.survey_id = s.id \n" +
            "WHERE s.id = #{surveyId,jdbcType=INTEGER} AND sq.type NOT IN (0,4)"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR)
    })
    List<SurveyQuestion> getBySurveyIdStatistics(@Param("surveyId") Integer surveyId);

    @Select("select id from survey_question where survey_id = #{surveyId,jdbcType=INTEGER}")
    List<Integer> getIdBySurveyId(@Param("surveyId") Integer surveyId);

    @Delete({"<script>" +
            "delete  from " + tableName +
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    Integer batchDelete(@Param("items") ArrayList<Integer> deleteIds);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.surveyId},#{item.title},#{item.type},#{item.orderd},#{item.required},#{item.optionContent},#{item.disabled})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id),survey_id = VALUES(survey_id), title = VALUES( title), type = VALUES( type), orderd = VALUES( orderd), required = VALUES( required), option_content = VALUES( option_content), disabled = VALUES( disabled)"+
            "</script>")
    Integer batchUpdate(@Param("items") List<SurveyQuestion> surveyQuestions);

    @Update("update survey_question set delete_status = 1 where id = #{id,jdbcType=INTEGER}")
    Integer updateDeleteStatus(Integer id);

    @Update({"<script>" +
            "update  " + tableName  +" set delete_status = 1"+
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    void batchUpdateDeleteStatus(@Param("items") ArrayList<Integer> deleteIds);

    @Select("select " +column + "from survey_question where title = #{title ,jdbcType=VARCHAR} and type = #{type,jdbcType = INTEGER} and survey_id = #{survey_id,jdbcType=INTEGER} and option_content = #{option_content,jdbcType=VARCHAR}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_id", property = "surveyId", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "required", property = "required", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR),
    })
    SurveyQuestion selectByTitleAndType(@Param("title") String title , @Param("type") Integer type,@Param("survey_id") Integer survey_id,@Param("option_content") String option_content);

    @Update("update survey_question set delete_status = 0 where id = #{id,jdbcType=INTEGER}")
    Integer updateAddStatus(Integer id);

    @Select({"select  sq.*, sa.* from  \n" +
            "(select id,survey_question_id,answer from survey_answer where source = #{source})sa LEFT JOIN  survey_question sq on sa.survey_question_id = sq.id"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_id", property = "surveyId", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "required", property = "required", jdbcType = JdbcType.INTEGER),
            @Result(column = "option_content", property = "optionContent", jdbcType = JdbcType.VARCHAR),
    })
    List<SurveyQuestion> getBySource(@Param("source") String source);

}