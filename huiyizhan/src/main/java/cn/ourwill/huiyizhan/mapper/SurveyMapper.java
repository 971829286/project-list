package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Survey;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SurveyMapper extends IBaseMapper<Survey> {
    @Delete({
            "delete from survey",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into survey (id, title, ",
            "description, status, ",
            "dead_line, create_date, ",
            "uid, ",
            "type,bg_picture,visit_type,activity_id,submit_info)",
            "values (#{id,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, ",
            "#{description,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
            "#{deadLine,jdbcType=TIMESTAMP}, #{createDate,jdbcType=TIMESTAMP}, ",
            "#{answerNum,jdbcType=INTEGER}, #{uid,jdbcType=INTEGER}, ",
            "#{type,jdbcType=INTEGER}, #{bgPicture=VARCHAR}, #{visitType=INTEGER}," +
            "#{activityId=INTEGER},#{submitInfo=VARCHAR})"
    })
    int insert(Survey record);

    @InsertProvider(type = SurveySqlProvider.class, method = "insertSelective")
    int insertSelective(Survey record);

    @Select({
            "select",
            "id, title, description, status, dead_line, create_date, uid, type,bg_picture,visit_type,activity_id,send_way,submit_info",
            "from survey",
            "where id = #{id,jdbcType=INTEGER} "
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "dead_line", property = "deadLine", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "bg_picture", property = "bgPicture", jdbcType = JdbcType.VARCHAR),
            @Result(column = "visit_type", property = "visitType", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "submit_info", property = "submitInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "send_way", property = "sendWay", jdbcType = JdbcType.INTEGER),
    })
    Survey selectByPrimaryKey(Integer id);

    @UpdateProvider(type = SurveySqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Survey record);

    @Update({
            "update survey",
            "set title = #{title,jdbcType=VARCHAR},",
            "description = #{description,jdbcType=VARCHAR},",
            "status = #{status,jdbcType=INTEGER},",
            "dead_line = #{deadLine,jdbcType=TIMESTAMP},",
            "create_date = #{createDate,jdbcType=TIMESTAMP},",
            "uid = #{uid,jdbcType=INTEGER},",
            "type = #{type,jdbcType=INTEGER},",
            "bg_picture = #{bgPicture=VARCHAR},",
            "visit_type =  #{visitType=INTEGER},",
            "activity_id = #{activityId=INTEGER},",
            "submit_info = #{submitInfo=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER} AND delete_status = 0"
    })
    int updateByPrimaryKey(Survey record);


    @Select({
            "select",
            "id, title, description, status, dead_line, create_date, uid, type,bg_picture,visit_type,activity_id,send_way,submit_info",
            "from survey",
            "where id = #{id,jdbcType=INTEGER} AND delete_status = 0"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "dead_line", property = "deadLine", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "bg_picture", property = "bgPicture", jdbcType = JdbcType.VARCHAR),
            @Result(column = "visit_type", property = "visitType", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "submit_info", property = "submitInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "send_way", property = "sendWay", jdbcType = JdbcType.INTEGER),
            @Result(column = "id",property = "surveyQuestions",
                one = @One(select = "cn.ourwill.huiyizhan.mapper.SurveyQuestionMapper.getBySurveyId"
                    ,fetchType = FetchType.EAGER
                )
            )
    })
    Survey getSurveyWithQuestionById(Integer id);

    @Select({
            "select id",
            "from survey",
            "where create_date = #{createDate,jdbcType=TIMESTAMP} AND uid = #{uid,jdbcType=INTEGER} AND delete_status = 0"
    })
    Integer getIdByCreateDateAndUid(@Param("createDate") Date createDate,@Param("uid") Integer uid);

    @Update("update survey set delete_status = 1 where id = #{id,jdbcType=INTEGER}")
    Integer updateDeleteStatus(@Param("id") Integer id);

    @SelectProvider(type = SurveySqlProvider.class, method = "getByUid")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "dead_line", property = "deadLine", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "bg_picture", property = "bgPicture", jdbcType = JdbcType.VARCHAR),
            @Result(column = "visit_type", property = "visitType", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "submit_info", property = "submitInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "send_way", property = "sendWay", jdbcType = JdbcType.INTEGER),
            @Result(column = "id",property = "totalNum",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.SurveyMapper.getTotalNum", fetchType = FetchType.EAGER)),
            @Result(column = "id",property = "todayNum",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.SurveyMapper.getTodayNum", fetchType = FetchType.EAGER))

    })
    List<Survey> getByUid(@Param("uid") Integer uid,@Param("type")Integer type,@Param("searchText") String searchText);

    @Update("update survey set status = #{status,jdbcType=INTEGER} where id = #{id,jdbcType=INTEGER}")
    Integer updateStatus(@Param("id") Integer id,@Param("status") Integer status);

    @Select("select status from survey where id = #{id,jdbcType=INTEGER}")
    Integer getStatus(@Param("id") Integer id);

    @Select("select id from survey where uid = #{userId,jdbcType=INTEGER} AND id = #{surveyId,jdbcType=INTEGER} ")
    Integer checkPermission(@Param("userId")Integer userId, @Param("surveyId")Integer surveyId);

    @Select("select COUNT(1)num from (\n" +
            "SELECT source FROM survey_answer sa LEFT JOIN survey_question sq ON sq.id = sa.survey_question_id \n" +
            "LEFT JOIN survey s ON s.id = sq.survey_id WHERE s.id = #{surveyId} AND sa.delete_status = 0 group by source)tt")
    Integer getTotalNum( Integer surveyId);

    @Select("select COUNT(1)num from (\n" +
            "SELECT source FROM survey_answer sa LEFT JOIN survey_question sq ON sq.id = sa.survey_question_id \n" +
            "LEFT JOIN survey s ON s.id = sq.survey_id WHERE s.id = #{surveyId} AND sa.delete_status = 0 and TO_DAYS(sa.create_date) = TO_DAYS(NOW()) group by source)tt")
    Integer getTodayNum( Integer surveyId);
}