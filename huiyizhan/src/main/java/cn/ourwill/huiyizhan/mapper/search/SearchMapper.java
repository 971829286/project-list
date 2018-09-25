package cn.ourwill.huiyizhan.mapper.search;

import cn.ourwill.huiyizhan.entity.SearchBean;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:用于搜索数据库和ES搜索的同步
 * @author: XuJinniu
 * @create: 2018-05-05 20:30
 **/
@Repository
public interface SearchMapper {
    String columns = "activity.id, activity.user_id,activity.activity_title,activity.activity_address,activity.start_time,activity.end_time,activity.activity_description,activity.activity_banner,activity.activity_banner_mobile," +
            "u.avatar,u.company,u.nickname as nick_name,u.username as user_name,type.type_name";
    @Select(
          " select " +
                  columns +
          " from activity " +
          " left join user as u on activity.user_id=u.id" +
          " left join activity_type as type on type.id = activity.activity_type" +
          " where delete_status=1 and is_open = 1 and issue_status =1 and activity.id = #{activityId,jdbcType=INTEGER}"
    )

    @Results(value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id",property = "userId",jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title",property = "activityTitle",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_address",property = "activityAddress",jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_time",property = "startTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time",property = "endTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_description",property = "activityDescription",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner",property = "activityBanner",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile",property = "activityBannerMobile",jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar",property = "avatar",jdbcType = JdbcType.VARCHAR),
            @Result(column = "company",property = "company",jdbcType = JdbcType.VARCHAR),
            @Result(column = "nick_name",property = "nickName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name",property = "userName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "type_name",property = "typeName",jdbcType = JdbcType.VARCHAR),

    })
    SearchBean getSearchBean(Integer activityId);


    @Select(
            " select " +
                    columns +
                    " from activity " +
                    " left join user as u on activity.user_id=u.id" +
                    " left join activity_type as type on type.id = activity.activity_type" +
                    " where delete_status=1 and is_open = 1 and issue_status =1 and u.id = #{userId,jdbcType=INTEGER}"
    )

    @Results(value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id",property = "userId",jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title",property = "activityTitle",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_address",property = "activityAddress",jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_time",property = "startTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time",property = "endTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_description",property = "activityDescription",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner",property = "activityBanner",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile",property = "activityBannerMobile",jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar",property = "avatar",jdbcType = JdbcType.VARCHAR),
            @Result(column = "company",property = "company",jdbcType = JdbcType.VARCHAR),
            @Result(column = "nick_name",property = "nickName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name",property = "userName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "type_name",property = "typeName",jdbcType = JdbcType.VARCHAR),

    })
    List<SearchBean> getSearchBeanByUserId(Integer userId);



    @Select(
            " select " +
                    columns +
                    " from activity " +
                    " left join user as u on activity.user_id=u.id" +
                    " left join activity_type as type on type.id = activity.activity_type" +
                    " where delete_status=1 and is_open = 1 and issue_status =1"
    )
    @Results(value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id",property = "userId",jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title",property = "activityTitle",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_address",property = "activityAddress",jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_time",property = "startTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time",property = "endTime",jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_description",property = "activityDescription",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner",property = "activityBanner",jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile",property = "activityBannerMobile",jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar",property = "avatar",jdbcType = JdbcType.VARCHAR),
            @Result(column = "company",property = "company",jdbcType = JdbcType.VARCHAR),
            @Result(column = "nick_name",property = "nickName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name",property = "userName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "type_name",property = "typeName",jdbcType = JdbcType.VARCHAR),

    })
    List<SearchBean> getAllSearchBean();
}
