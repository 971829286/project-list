package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Survey;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;


public class SurveySqlProvider {

    public String insertSelective(Survey record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("survey");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getTitle() != null) {
            sql.VALUES("title", "#{title,jdbcType=VARCHAR}");
        }

        if (record.getDescription() != null) {
            sql.VALUES("description", "#{description,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }

        if (record.getDeadLine() != null) {
            sql.VALUES("dead_line", "#{deadLine,jdbcType=TIMESTAMP}");
        }

        if (record.getCreateDate() != null) {
            sql.VALUES("create_date", "#{createDate,jdbcType=TIMESTAMP}");
        }

        if (record.getUid() != null) {
            sql.VALUES("uid", "#{uid,jdbcType=INTEGER}");
        }

        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }

        if (record.getBgPicture() != null) {
            sql.VALUES("bg_picture", "#{bgPicture,jdbcType=VARCHAR}");
        }
        if (record.getVisitType() != null) {
            sql.VALUES("visit_type", "#{visitType,jdbcType=INTEGER}");
        }
        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }
        if (record.getSubmitInfo() != null) {
            sql.VALUES("submit_info", "#{submitInfo,jdbcType=VARCHAR}");
        }
        if (record.getSendWay() != null) {
            sql.VALUES("send_way", "#{sendWay,jdbcType=INTEGER}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Survey record) {
        SQL sql = new SQL();
        sql.UPDATE("survey");

        if (record.getTitle() != null) {
            sql.SET("title = #{title,jdbcType=VARCHAR}");
        }

        if (record.getDescription() != null) {
            sql.SET("description = #{description,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=INTEGER}");
        }

        if (record.getDeadLine() != null) {
            sql.SET("dead_line = #{deadLine,jdbcType=TIMESTAMP}");
        }

        if (record.getCreateDate() != null) {
            sql.SET("create_date = #{createDate,jdbcType=TIMESTAMP}");
        }

        if (record.getUid() != null) {
            sql.SET("uid = #{uid,jdbcType=INTEGER}");
        }

        if (record.getType() != null) {
            sql.SET("type = #{type,jdbcType=INTEGER}");
        }

        if (record.getBgPicture() != null) {
            sql.SET("bg_picture = #{bgPicture,jdbcType=VARCHAR}");
        }
        if (record.getVisitType() != null) {
            sql.SET("visit_type = #{visitType,jdbcType=INTEGER}");
        }
        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }
        if (record.getSubmitInfo() != null) {
            sql.SET("submit_info = #{submitInfo,jdbcType=VARCHAR}");
        }

        if (record.getSendWay() != null) {
            sql.SET("send_way = #{sendWay,jdbcType=INTEGER}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }

    public String getByUid(@Param("uid") Integer uid, @Param("type")Integer type,@Param("searchText") String searchText){
        return new SQL(){
            {
                SELECT(" id, title, description, status, dead_line, create_date, uid, type,bg_picture,visit_type,activity_id,submit_info,send_way  ");
                FROM("survey");
                WHERE("uid = #{uid,jdbcType=INTEGER} AND type = #{type,jdbcType=INTEGER}  AND delete_status = 0");
                if(StringUtils.isNotEmpty(searchText)){
                    WHERE("title like CONCAT('%',#{searchText},'%') ");
                }
            }
        }.toString();
    }
}