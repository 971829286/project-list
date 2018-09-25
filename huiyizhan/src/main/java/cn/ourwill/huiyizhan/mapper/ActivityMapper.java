package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface ActivityMapper extends IBaseMapper<Activity> {

    @Delete({
            "update activity set delete_status = 0",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @InsertProvider(type = ActivitySqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(Activity record);

    @Select({
            "select",
            "id, user_id, activity_title, activity_type, start_time, end_time, activity_address, ",
            "activity_banner_mobile, activity_banner, is_open, is_online, is_hot, is_recent, schedule_status, ",
            "guest_status, partner_status, contact_status, c_time, u_time, u_id, activity_description,issue_status,banner_type," +
                    "banner_id, ticket_config, custom_url ",
            "from activity",
            "where id = #{id,jdbcType=INTEGER} and delete_status = 1"
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    Activity selectByPrimaryKey(Integer id);

    @UpdateProvider(type = ActivitySqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Activity record);

    @Select({"select",
            "id, user_id, activity_title, activity_type, start_time, end_time, activity_address, ",
            "activity_banner_mobile, activity_banner, is_open, is_online, is_hot, is_recent, schedule_status, ",
            "guest_status, partner_status, contact_status, c_time, u_time, u_id, activity_description,issue_status,banner_type," +
                    "banner_id, ticket_config, custom_url ",
            "from activity  where delete_status = 1"})
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> findAll();

    @Select("select count(1) from activity where id = #{id} and user_id = #{userId} and delete_status = 1")
    int checkOwners(@Param("id") Integer id, @Param("userId") Integer userId);


    @SelectProvider(type = ActivitySqlProvider.class, method = "selectById")
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    public Activity getById(Integer id);

    //根据用户id查找详情
    @SelectProvider(type = ActivitySqlProvider.class, method = "findDetailById")
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_status", property = "scheduleStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "guest_status", property = "guestStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_status", property = "partnerStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_status", property = "contactStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_id", property = "uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_description", property = "activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_description", property = "activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "issue_status", property = "issueStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_type", property = "bannerType", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR),

            @Result(property = "activityContacts", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.ActivityContactMapper.getByActivityId")
            ),
            @Result(property = "activityGuests", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.ActivityGuestMapper.getByActivityId")
            ),
            @Result(property = "activityPartners", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.ActivityPartnerMapper.getByActivityId")
            ),
            @Result(property = "activitySchedules", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.ActivityScheduleMapper.getByActivityId")
            ),
            @Result(property = "activityTickets", column = "id",
                    many = @Many(select = "cn.ourwill.huiyizhan.mapper.ActivityTicketsMapper.selectByActivityId")
            ),
            @Result(property = "owner", column = "user_id",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById")
            )
    })
    public Activity findDetailById(@Param("id") Integer id);


    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status, a.partner_status, a.contact_status, a.banner_id,a.c_time, a.u_time, a.u_id, a.activity_description ,a.issue_status,a.banner_type ,u.avatar,a.ticket_config, a.custom_url ",
            "from activity a",
            "left join  user u on u.id = a.user_id",
            "where a.user_id = #{id,jdbcType=INTEGER} and a.delete_status = 1"
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "avatar", property = "avatar", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> findByUserId(Integer id);


    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description ,a.issue_status,a.banner_type ,a.banner_id,u.avatar,a.ticket_config, a.custom_url ",
            "from activity a",
            "left join  user u on u.id = a.user_id",
            "where a.user_id = #{id,jdbcType=INTEGER} and a.delete_status = 1 and a.issue_status = 1 order by id desc limit 0,3"
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "avatar", property = "avatar", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> findByUserIdThree(Integer id);


    @Select("select id from activity where delete_status = 1")
    List<Integer> findAllId();


    @Select("select id from activity where user_id = #{id,jdbcType=INTEGER} and delete_status = 1")
    List<Integer> getAllIdByUserId(Integer id);

    @SelectProvider(type = ActivitySqlProvider.class, method = "findJoins")
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> findJoins(@Param("userId") Integer userId,boolean isValid);

    @Select("select is_hot from activity where id = #{id,jdbcType = INTEGER}")
    int getIsHot(@Param("id") int id);

    @Select("select is_recent from activity where id = #{id,jdbcType = INTEGER}")
    int getIsRecent(@Param("id") int id);

    @Update("update  activity set is_hot = #{isHot,jdbcType=INTEGER} where id = #{id,jdbcType=INTEGER}")
    int setIsHot(@Param("isHot") int isHot, @Param("id") int id);

    @Update("update  activity set is_recent = #{isRecent,jdbcType=INTEGER} where id = #{id,jdbcType=INTEGER}")
    int setIsRecent(@Param("isRecent") int isRecent, @Param("id") int id);


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url ",
            "from activity a",
            "where a.is_hot = 1 and a.delete_status = 1 limit 0,8"
    })
    List<Activity> getHotListBase();

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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,issue_status,banner_type,a.ticket_config,a.custom_url",
            "from activity a",
            "where a.is_recent = 1 and a.delete_status = 1 limit 0,8"
    })
    List<Activity> getRecentListBase();


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR),
            @Result(property = "owner", column = "user_id",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById")
            )
    })
    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,issue_status,banner_type,a.ticket_config,a.custom_url",
            "from activity a",
            "LEFT JOIN activity_sort h ON a.id = h.hot_activity_id",
            "where a.is_open = 1 and a.is_hot = 1  and a.delete_status = 1 and a.issue_status = 1 ",
            "order by h.id asc limit 0,8"
    })
    List<Activity> getHotList();


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(property = "owner", column = "user_id",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById")
            )
    })
    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,issue_status,banner_type,a.ticket_config,a.custom_url",
            "from activity a",
            "LEFT JOIN activity_sort h ON a.id = h.recent_activity_id",
            "where a.is_open = 1 and a.is_recent = 1  and a.delete_status = 1 and a.issue_status = 1",
            "order by h.id asc limit 0,8 "
    })
    List<Activity> getRecentList();

    @Select("select issue_status from activity where id = #{id,jdbcType = INTEGER} and delete_status = 1")
    Integer getIssueStatus(@Param("id") int id);

    @Update("update activity set issue_status = #{issueStatus,jdbcType = INTEGER} where id = #{id,jdbcType = INTEGER}")
    Integer updateIssueStatus(@Param("issueStatus") int issueStatus, @Param("id") int id);


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where a.user_id = #{userId,jdbcType=INTEGER}  and a.delete_status = 1 AND a.start_time < NOW() AND a.end_time > NOW()",
//            "group by a.start_time",
    })
    List<Activity> getINGActivity(@Param("userId") int userId);


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select ",
            "(case when a.banner_id > 1 then 1 else 0 end) as  banner_id,",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where a.user_id = #{userId,jdbcType=INTEGER}  and a.delete_status = 1 AND a.issue_status = #{issueStatus,jdbcType=INTEGER}",
    })
    List<Activity> getActivityByStatus(@Param("userId") int userId, @Param("issueStatus") int issueStatus);


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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select ",
            "(case when a.banner_id > 1 then 1 else 0 end) as  banner_id,",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where a.user_id = #{userId,jdbcType=INTEGER} and a.delete_status = 1 AND a.end_time < #{nowDate,jdbcType=TIMESTAMP}",
    })
    List<Activity> getActivityOver(@Param("userId") int userId, @Param("nowDate") Date nowDate);

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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    @Select({
            "select ",
            "(case when a.banner_id > 1 then 1 else 0 end) as  banner_id,",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where a.user_id = #{userId,jdbcType=INTEGER} and a.delete_status = 1 AND a.end_time > #{nowDate,jdbcType=TIMESTAMP}",
    })
    List<Activity> getActivityNotOver(@Param("userId") int userId, @Param("nowDate") Date nowDate);

    @SelectProvider(type = ActivitySqlProvider.class, method = "selectByParam")
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR),
            @Result(property = "owner", column = "user_id",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.UserMapper.getBasicInfoById")
            ),
    })
    List<Activity> selectByParam(HashMap param);

    @Select("select id,type,type_name from activity_type")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER),
            @Result(column = "type_name", property = "typeName", jdbcType = JdbcType.VARCHAR),
    })
    List<ActivityType> getAllActivityType();


    @Select({
            "select ",
            "(case when a.banner_id > 1 then 1 else 0 end) as  banner_id,",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status, a.banner_id,a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where  a.delete_status = 1 AND a.issue_status = #{issueStatus,jdbcType=INTEGER}",
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> getAllActivityByStatus(@Param("issueStatus") int issueStatus);


    @Select({
            "select ",
            "(case when a.banner_id > 1 then 1 else 0 end) as  banner_id,",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where  a.delete_status = 1 AND a.end_time < #{nowDate,jdbcType=TIMESTAMP}"
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    List<Activity> getAllActivityOver(Date nowDate);

    @Update("update activity set banner_id = #{bannerId} where id = #{activityId}")
    Integer updateBannerId(@Param("activityId") Integer activityId, @Param("bannerId") Integer bannerId);

    /**
     * 获取用户发布的会议数目
     */
    @Select("select count(1) from activity where user_id = #{userId,jdbcType=INTEGER} AND issue_status = 1 and delete_status = 1")
    Integer selectIssueActivityCountByUserId(@Param("userId") Integer userId);

    @Select("select count(1) from activity where user_id = #{userId,jdbcType=INTEGER} AND  delete_status = 1")
    Integer getActivityCountWithOutDelete(Integer userId);

    @Select({ "select",
            "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address, ",
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, ",
            "a.guest_status,a.banner_id, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.ticket_config,a.custom_url",
            "from activity a ",
            "where a.custom_url = #{customUrl}" })
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
            @Result(column = "is_hot", property = "isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "isRecent", jdbcType = JdbcType.INTEGER),
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
            @Result(column = "banner_id", property = "bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_config", property = "ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "customUrl", jdbcType = JdbcType.VARCHAR)
    })
    Activity getByCustomUrl(@Param("customUrl") String customUrl);

    @Select("select count(1) from activity where custom_url = #{customUrl} and id <> #{activityId}")
    int checkCustomUrl(@Param("customUrl") String customUrl, @Param("activityId") Integer activityId);
}