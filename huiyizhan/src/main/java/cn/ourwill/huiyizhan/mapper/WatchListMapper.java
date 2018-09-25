package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityDynamic;
import cn.ourwill.huiyizhan.entity.WatchList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WatchListMapper extends IBaseMapper<WatchList> {
    @InsertProvider(type = WatchListSqlProvider.class, method = "save")
    public Integer save(WatchList watchList);

    @UpdateProvider(type = WatchListSqlProvider.class, method = "update")
    public Integer update(WatchList watchList);

    @SelectProvider(type = WatchListSqlProvider.class, method = "findAll")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "activity_id", property = "activity", one = @One(
                    select = "cn.ourwill.huiyizhan.mapper.ActivityMapper.getById",
                    fetchType = FetchType.EAGER
            ))
    })
    public List<WatchList> findAll();

    @SelectProvider(type = WatchListSqlProvider.class, method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "activity_id", property = "activity", one = @One(
                    select = "cn.ourwill.huiyizhan.mapper.ActivityMapper.getById",
                    fetchType = FetchType.EAGER
            ))
    })
    public WatchList getById(Integer id);

    @DeleteProvider(type = WatchListSqlProvider.class, method = "deleteById")
    public Integer delete(WatchList watchList);

    //根据用户id查找会议收藏列表
    @SelectProvider(type = WatchListSqlProvider.class, method = "getWatchList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "activity_id", property = "activity",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.ActivityMapper.getById",
                            fetchType = FetchType.EAGER)
            )
    })
    List<WatchList> getWatchList(@Param("userId") Integer userId);


    //根据用户id查找会议收藏列表
    String activityColumns = "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, " +
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.schedule_status, " +
            "a.guest_status, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type";

    @Select({
            "select " + activityColumns,
            "FROM watch_list w",
            "LEFT JOIN  activity a  on a.id = w.activity_id",
            "WHERE  w.user_id = #{userId} AND a.issue_status = #{issueStatus} AND a.end_time > #{nowDate,jdbcType=TIMESTAMP} and a.delete_status = 1"
    })
    @Results({
            //--------------------------------------------Actitivy----------------------------------------------
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title", property = "activityTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_type", property = "activityType", jdbcType = JdbcType.INTEGER),
            @Result(column = "start_time", property = "startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time", property = "endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_address", property = "activityAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile", property = "activityBannerMobile", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner", property = "activityBanner", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_open", property = "isOpen", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_online", property = "isOnline", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_status", property = "scheduleStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "guest_status", property = "guestStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_status", property = "partnerStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_status", property = "contactStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_id", property = "uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_description", property = "activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "issue_status", property = "issueStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_type", property = "bannerType", jdbcType = JdbcType.INTEGER),

            //--------------------------------------------User----------------------------------------------
            @Result(column = "user_id", property = "owner",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById",
                            fetchType = FetchType.EAGER)
            )

          /*  @Result(column = "level", property = "owner.level"),
            @Result(column = "uuid", property = "owner.UUID"),
            @Result(column = "nickname", property = "owner.nickname"),
            @Result(column = "username", property = "owner.username"),
            @Result(column = "avatar", property = "owner.avatar"),
            @Result(column = "info", property = "owner.info")*/

    })
    List<Activity> getWatchActivityListByIssue(@Param("userId") Integer userId, @Param("issueStatus") Integer issueStatus, @Param("nowDate") Date nowDate);


    @Select({
            "select " + activityColumns,
            "FROM watch_list w",
            "LEFT JOIN  activity a  on a.id = w.activity_id",
            "WHERE  w.user_id = #{userId,jdbcType=INTEGER}  AND a.end_time < #{nowDate,jdbcType=TIMESTAMP} and a.delete_status = 1"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title", property = "activityTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_type", property = "activityType", jdbcType = JdbcType.INTEGER),
            @Result(column = "start_time", property = "startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time", property = "endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_address", property = "activityAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile", property = "activityBannerMobile", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner", property = "activityBanner", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_open", property = "isOpen", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_online", property = "isOnline", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_status", property = "scheduleStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "guest_status", property = "guestStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_status", property = "partnerStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_status", property = "contactStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_id", property = "uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_description", property = "activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "issue_status", property = "issueStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_type", property = "bannerType", jdbcType = JdbcType.INTEGER),
            //--------------------------------------------User----------------------------------------------
            @Result(column = "user_id", property = "owner",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById",
                            fetchType = FetchType.EAGER)
            )
    })
    List<Activity> getWatchActivityOver(@Param("userId") int userId, @Param("nowDate") Date nowDate);


    //按用户id和活动id查找
    @Select("select id as id,activity_id as activityId,user_id as userId from watch_list where activity_id = #{activityId} and user_id = #{userId}")
    List<WatchList> selectByActivityAndUser(@Param("activityId") Integer activityId, @Param("userId") Integer userId);


    /**
     * <pre>
     *       根据ID 查询会议被多少人收藏了
     * </pre>
     */
    @Select("select count(0) from watch_list where activity_id = #{activityId}")
    Integer selectCountByActivity(@Param("activityId") Integer activityId);

    @SelectProvider(type = WatchListSqlProvider.class, method = "getActivityDynamic")
    @Results({
            @Result(column = "watch_date", property = "watchDate"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "activity_id", property = "activityId"),
            //--------------------------------------------会议的基本信息----------------------------------------------
            @Result(column = "activity_title", property = "activity.activityTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_type", property = "activity.activityType", jdbcType = JdbcType.INTEGER),
            @Result(column = "start_time", property = "activity.startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time", property = "activity.endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_address", property = "activity.activityAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile", property = "activity.activityBannerMobile", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner", property = "activity.activityBanner", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_open", property = "activity.isOpen", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_online", property = "activity.isOnline", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_status", property = "activity.scheduleStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "guest_status", property = "activity.guestStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_status", property = "activity.partnerStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_status", property = "activity.contactStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "activity.cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "activity.uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_id", property = "activity.uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_description", property = "activity.activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "issue_status", property = "activity.issueStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_type", property = "activity.bannerType", jdbcType = JdbcType.INTEGER),
            @Result(column = "avatar", property = "activity.avatar", jdbcType = JdbcType.VARCHAR),

            //--------------------------------------------人的基本信息----------------------------------------------
            @Result(column = "level", property = "user.level"),
            @Result(column = "nickname", property = "user.nickname"),
            @Result(column = "username", property = "user.username"),
            @Result(column = "avatar", property = "user.avatar"),
            @Result(column = "mob_phone", property = "user.mobPhone"),
            @Result(column = "tel_phone", property = "user.telPhone"),
            @Result(column = "email", property = "user.email"),
            @Result(column = "qq", property = "user.qq"),
            @Result(column = "company", property = "user.company"),
            @Result(column = "address", property = "user.address"),
            @Result(column = "version", property = "user.version"),
            @Result(column = "info", property = "user.info")

    })
    List<ActivityDynamic> getActivityDynamic(Integer userId);

    /**
     * <pre>
     *      同步会议被收藏的数目，从watch_list 表中获取数目，插入到activity_statistics
     * </pre>
     *
     * @param activityId
     */
    @Update({
            "update activity_statistics ",
            "set collect_count = (",
            "SELECT count(*) FROM watch_list WHERE activity_id = #{activityId,jdbcType=INTEGER}  )",
            "WHERE activity_id = #{activityId,jdbcType=INTEGER}"
    })
    void syncActivityCollectCountToStatistics(@Param("activityId") Integer activityId);

    /**
     * <pre>
     *     获取会议收藏的 会议ID列表（去重）
     * </pre>
     *
     * @return
     */
    @Select("select DISTINCT activity_id  FROM watch_list GROUP BY activity_id asc")
    List<Integer> getActivityIdsFromWatchList();

}
