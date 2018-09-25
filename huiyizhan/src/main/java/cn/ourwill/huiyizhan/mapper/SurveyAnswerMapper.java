package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.AnswerStatistics;
import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface SurveyAnswerMapper {
    String tableName = "survey_answer";
    String columnNoId = " survey_question_id, answer, source, create_date ,type";
    String column = "id," + columnNoId;

    @Delete({
            "delete from survey_answer",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into survey_answer (id,  ",
            "survey_question_id, answer, ",
            "source, create_date)",
            "values (#{id,jdbcType=INTEGER},  ",
            "#{surveyQuestionId,jdbcType=INTEGER}, #{answer,jdbcType=VARCHAR}, ",
            "#{source,jdbcType=VARCHAR}, #{createDate,jdbcType=TIMESTAMP})"
    })
    int insert(SurveyAnswer record);

    @InsertProvider(type = SurveyAnswerSqlProvider.class, method = "insertSelective")
    int insertSelective(SurveyAnswer record);

    @Select({
            "SELECT sa.survey_question_id,sq.orderd,group_concat(sa.answer) answer, sa.create_date ,sa.source FROM survey_answer sa ",
            "LEFT JOIN survey_question sq  ON sq.id = sa.survey_question_id ",
            "LEFT JOIN survey s ON s.id = sq.survey_id ",
            "WHERE sq.type in (0,1,2) and s.id = #{surveyId,jdbcType=INTEGER} AND sa.delete_status = 0 group BY source ,survey_question_id ORDER BY source ,orderd",
    })
    @Results({
            @Result(column = "survey_question_id", property = "surveyQuestionId", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "source", property = "source", jdbcType = JdbcType.VARCHAR)

    })
    List<SurveyAnswer> getAnswerBySurveyId(Integer surveyId);

    @Select({
            "select",
            "id, survey_question_id, answer, source, create_date",
            "from survey_answer",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "survey_question_id", property = "surveyQuestionId", jdbcType = JdbcType.INTEGER),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
            @Result(column = "source", property = "source", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP)
    })
    SurveyAnswer selectByPrimaryKey(Integer id);

    @UpdateProvider(type = SurveyAnswerSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SurveyAnswer record);

    @Update({
            "update survey_answer",
            "survey_question_id = #{surveyQuestionId,jdbcType=INTEGER},",
            "answer = #{answer,jdbcType=VARCHAR},",
            "source = #{source,jdbcType=VARCHAR},",
            "create_date = #{createDate,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SurveyAnswer record);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + columnNoId + ") " +
            "values " +
            "<foreach collection=\"items\" type=\"type\" item=\"item\" separator=\",\"> " +
            "(#{item.surveyQuestionId},#{item.answer},#{item.source},#{item.createDate},#{item.type})" +
            "</foreach> " +
            "</script>")
    Integer batchSave(@Param("items") List<SurveyAnswer> surveyAnswers);

    @Insert("<script> " +
            "insert into " + tableName +
            "(" + "id, type ,answer" + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.id},#{item.type},#{item.answer})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "id = VALUES(id), answer = VALUES( answer)" +
            "</script>")
    Integer batchUpdate(@Param("items") List<SurveyAnswer> surveyAnswers);

    @Delete({"<script>" +
            "delete  from " + tableName +
            " where id in " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
            "#{item} \n" +
            "</foreach> " +
            "</script>"})
    Integer batchDelete(@Param("items") List<Integer> ids);


    @Select("SELECT title FROM survey_question WHERE survey_id = #{surveyId,jdbcType=INTEGER} AND delete_status = 0 ORDER BY orderd asc")
    List<String> getTitleBySurveyId(@Param("surveyId") Integer surveyId);

    @Update({"<script>" +
            "update  " + tableName + " set delete_status = 1",
            " where id in " +
                    "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
                    "#{item} \n" +
                    "</foreach> " +
                    "</script>"})
    Integer updateDeleteStatus(@Param("items") List<Integer> ids);

    @Select({
            "select sq.id,sq.title,sq.type,sq.orderd,sa.answer, count(*) as count " +
                    "from survey_answer sa " +
                    "LEFT JOIN survey_question sq ON " +
                    "sq.id = sa.survey_question_id " +
                    "LEFT JOIN survey s ON " +
                    "sq.survey_id = s.id " +
                    "WHERE s.id = #{surveyId,jdbcType=INTEGER} AND sa.type = 0 AND sa.delete_status = 0 AND sq.type NOT IN (0,4) " +
                    "GROUP BY survey_question_id,answer "
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
            @Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER),
            @Result(column = "type",property = "type",jdbcType = JdbcType.INTEGER)
    })
    List<AnswerStatistics> getAnswerStatisticsBySurveyId(@Param("surveyId") Integer surveyId);

    @Select({"\n" +
            "select sq.id, count(*) as count \n" +
            "                    from survey_answer sa \n" +
            "                    LEFT JOIN survey_question sq ON \n" +
            "                    sq.id = sa.survey_question_id \n" +
            "                    LEFT JOIN survey s ON \n" +
            "                    sq.survey_id = s.id \n" +
            "                    WHERE s.id = #{surveyId} AND sa.delete_status = 0 AND sq.type NOT IN (0,4) \n" +
            "                    GROUP BY survey_question_id "})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER),
    })
    List<HashMap<String,Integer>> getAnswerTotalBySurveyId(@Param("surveyId") Integer surveyId);

    @Update({"<script>" +
            "update  " + tableName + " set delete_status = 1",
            " where source in " +
                    "<foreach collection=\"items\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
                    "#{item} \n" +
                    "</foreach> " +
                    "</script>"})
    Integer updateDeleteStatusBySource(@Param("items") List<String> sourceList);

    @Update({"update  "+tableName+" set answer = #{answer} where source = #{source} and  survey_question_id = #{surveyQuestionId}"})
    Integer editSurveyAnswer(@Param("source")String source,@Param("answer")String answer,@Param("surveyQuestionId")Integer surveyQuestionId);


    @Select({
            "SELECT sq.orderd,sa.answer, sa.create_date ,sa.source FROM survey_answer sa ",
            "LEFT JOIN survey_question sq  ON sq.id = sa.survey_question_id ",
            "LEFT JOIN survey s ON s.id = sq.survey_id ",
            "WHERE s.id = #{surveyId,jdbcType=INTEGER} AND sa.answer= #{searchText,jdbcType=VARCHAR} AND sa.delete_status = 0 ORDER BY source ,orderd",
    })
    @Results({
            @Result(column = "orderd", property = "orderd", jdbcType = JdbcType.INTEGER),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "source", property = "source", jdbcType = JdbcType.VARCHAR)

    })
    List<SurveyAnswer> getAnswerBySearchText(Integer surveyId,String searchText);

    @Select({"select id,answer,type  from survey_answer where survey_question_id = #{surveyQuestionId} and source = #{source} "})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
    })
    List<SurveyAnswer>getAnswerByQuestionSourceAll(@Param("surveyQuestionId") Integer surveyQuestionId,@Param("source")String source);

    @Select({"select id,answer,type from survey_answer where survey_question_id = #{surveyQuestionId} and source = #{source} and delete_status = 0 "})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
    })
    List<SurveyAnswer> getAnswerByQuestionSource(@Param("surveyQuestionId") Integer surveyQuestionId, @Param("source")String source);

    @Update({"update survey_answer set delete_status = 1 where  survey_question_id = #{surveyQuestionId} and source = #{source} and answer = #{answer}"})
    Integer deleteAnswerBySource(@Param("surveyQuestionId") Integer surveyQuestionId,@Param("source")String source,@Param("answer")String answer);

    @Update({"update survey_answer set delete_status = 0 where  survey_question_id = #{surveyQuestionId} and source = #{source} and answer = #{answer}"})
    Integer addAnswerBySource(@Param("surveyQuestionId") Integer surveyQuestionId,@Param("source")String source,@Param("answer")String answer);

    @Select({"select answer, count(*) as count \n" +
            "from survey_answer \n" +
            "WHERE survey_question_id = #{surveyId} AND delete_status = 0 AND type = 0 GROUP BY answer "})
    @Results({
            @Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
            @Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER),
    })
    List<HashMap<String,Object>> getAnswerNumBySurveyQuestionId(@Param("surveyId") Integer surveyId);

    @Select({"select count(1) count from  survey_answer  where  survey_question_id = #{surveyQuestionId} and delete_status = 0"})
    Integer selectAnswerNumByQuestionId(@Param("surveyQuestionId") Integer surveyQuestionId);

    @Select({"select count(1) count from  survey_answer  where  survey_question_id = #{surveyQuestionId} AND type = 1 and delete_status = 0"})
    Integer selectOtherAnswerNumByQuestionId(@Param("surveyQuestionId") Integer surveyQuestionId);

}