package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ActivitySqlProvider {

    //表名
    private String tableName = "activity";

    //所有的列名
    private String columns = "id, user_id, activity_title, activity_type, start_time, end_time, activity_address," +
            "activity_banner_mobile, activity_banner, is_open, is_online, is_hot, is_recent, schedule_status, " +
            "guest_status, partner_status, contact_status, c_time, u_time, u_id, activity_description,issue_status,banner_type," +
            "banner_id,ticket_config,custom_url";

    //所有的列名
    private String columnsAlias = "a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address," +
            "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status, " +
            "a.guest_status, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description,a.issue_status,a.banner_type,a.banner_id,a.ticket_config,a.custom_url";

    public String insertSelective(Activity record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getActivityTitle() != null) {
            sql.VALUES("activity_title", "#{activityTitle,jdbcType=VARCHAR}");
        }

        if (record.getActivityType() != null) {
            sql.VALUES("activity_type", "#{activityType,jdbcType=INTEGER}");
        }

        if (record.getStartTime() != null) {
            sql.VALUES("start_time", "#{startTime,jdbcType=TIMESTAMP}");
        }

        if (record.getEndTime() != null) {
            sql.VALUES("end_time", "#{endTime,jdbcType=TIMESTAMP}");
        }

        if (record.getActivityAddress() != null) {
            sql.VALUES("activity_address", "#{activityAddress,jdbcType=VARCHAR}");
        }

        if (record.getActivityBannerMobile() != null) {
            sql.VALUES("activity_banner_mobile", "#{activityBannerMobile,jdbcType=VARCHAR}");
        }

        if (record.getActivityBanner() != null) {
            sql.VALUES("activity_banner", "#{activityBanner,jdbcType=VARCHAR}");
        }

        if (record.getIsOpen() != null) {
            sql.VALUES("is_open", "#{isOpen,jdbcType=INTEGER}");
        }

        if (record.getIsOnline() != null) {
            sql.VALUES("is_online", "#{isOnline,jdbcType=INTEGER}");
        }

        if (record.getScheduleStatus() != null) {
            sql.VALUES("schedule_status", "#{scheduleStatus,jdbcType=INTEGER}");
        }

        if (record.getGuestStatus() != null) {
            sql.VALUES("guest_status", "#{guestStatus,jdbcType=INTEGER}");
        }

        if (record.getPartnerStatus() != null) {
            sql.VALUES("partner_status", "#{partnerStatus,jdbcType=INTEGER}");
        }

        if (record.getContactStatus() != null) {
            sql.VALUES("contact_status", "#{contactStatus,jdbcType=INTEGER}");
        }

        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUTime() != null) {
            sql.VALUES("u_time", "#{uTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUId() != null) {
            sql.VALUES("u_id", "#{uId,jdbcType=INTEGER}");
        }

        if (record.getActivityDescription() != null) {
            sql.VALUES("activity_description", "#{activityDescription,jdbcType=LONGVARCHAR}");
        }

        if (record.getIssueStatus() != null) {
            sql.VALUES("issue_status", "#{issueStatus,jdbcType=INTEGER}");
        }
        if (record.getBannerType() != null) {
            sql.VALUES("banner_type", "#{bannerType,jdbcType=INTEGER}");
        }
        if (record.getBannerId() != null) {
            sql.VALUES("banner_id", "#{bannerId,jdbcType=INTEGER}");
        }
        if (record.getTicketConfig() != null) {
            sql.VALUES("ticket_config", "#{ticketConfig,jdbcType=INTEGER}");
        }
        if (record.getCustomUrl() != null) {
            sql.VALUES("custom_url", "#{customUrl,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Activity record) {
        SQL sql = new SQL();
        sql.UPDATE("activity");

        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }

        if (record.getActivityTitle() != null) {
            sql.SET("activity_title = #{activityTitle,jdbcType=VARCHAR}");
        }

        if (record.getActivityType() != null) {
            sql.SET("activity_type = #{activityType,jdbcType=INTEGER}");
        }

        if (record.getStartTime() != null) {
            sql.SET("start_time = #{startTime,jdbcType=TIMESTAMP}");
        }

        if (record.getEndTime() != null) {
            sql.SET("end_time = #{endTime,jdbcType=TIMESTAMP}");
        }

        if (record.getActivityAddress() != null) {
            sql.SET("activity_address = #{activityAddress,jdbcType=VARCHAR}");
        }

        if (record.getActivityBannerMobile() != null) {
            sql.SET("activity_banner_mobile = #{activityBannerMobile,jdbcType=VARCHAR}");
        }

        if (record.getActivityBanner() != null) {
            sql.SET("activity_banner = #{activityBanner,jdbcType=VARCHAR}");
        }

        if (record.getIsOpen() != null) {
            sql.SET("is_open = #{isOpen,jdbcType=INTEGER}");
        }

        if (record.getIsOnline() != null) {
            sql.SET("is_online = #{isOnline,jdbcType=INTEGER}");
        }

        if (record.getScheduleStatus() != null) {
            sql.SET("schedule_status = #{scheduleStatus,jdbcType=INTEGER}");
        }

        if (record.getGuestStatus() != null) {
            sql.SET("guest_status = #{guestStatus,jdbcType=INTEGER}");
        }

        if (record.getPartnerStatus() != null) {
            sql.SET("partner_status = #{partnerStatus,jdbcType=INTEGER}");
        }

        if (record.getContactStatus() != null) {
            sql.SET("contact_status = #{contactStatus,jdbcType=INTEGER}");
        }

        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUTime() != null) {
            sql.SET("u_time = #{uTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUId() != null) {
            sql.SET("u_id = #{uId,jdbcType=INTEGER}");
        }

        if (record.getActivityDescription() != null) {
            sql.SET("activity_description = #{activityDescription,jdbcType=LONGVARCHAR}");
        }
        if (record.getIssueStatus() != null) {
            sql.SET("issue_status = #{issueStatus,jdbcType=INTEGER}");
        }
        if (record.getBannerType() != null) {
            sql.SET("banner_type = #{bannerType,jdbcType=INTEGER}");
        }
        if (record.getTicketConfig() != null) {
            sql.SET("ticket_config = #{ticketConfig,jdbcType=INTEGER}");
        }
        if (record.getCustomUrl() != null) {
            sql.SET("custom_url = #{customUrl,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParam(final Map<String,Object> param){
        return new SQL(){
            {
//                SELECT("(case when a.start_time>NOW() then 1 when a.end_time<NOW() then 0 else 2 end) as statuss,a.id,a.user_id,a.priority,a.title,a.introduction," +
//                        "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status," +
//                        "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,a.water_complete,a.water_config,a.check_type,a.status," +
//                        "a.is_impower,a.qr_code,a.screen,docx,a.classical,a.model,a.c_time,a.u_time,a.type,a.show_start_time,a.show_end_time,t.type_name,a.icon_switch,a.is_auto_publish,a.switch_password,a.password,a.photo_live");
                SELECT(columnsAlias);
                FROM("activity a");
                LEFT_OUTER_JOIN("user u on a.user_id = u.id");
                if(param.get("id")!=null){
                    WHERE("a.id=#{id}");
                }
                if(param.get("userId")!=null){
                    WHERE("a.user_id=#{userId}");
                }
                if(param.get("activityTitle")!=null){
                    WHERE("a.activity_title=#{activityTitle}");
                }
                if(param.get("activityType")!=null){
                    WHERE("a.activity_type=#{activityType}");
                }
                if(param.get("isOpen")!=null){
                    WHERE("a.is_open=#{isOpen}");
                }
                if(param.get("isOnline")!=null){
                    WHERE("a.is_online=#{isOnline}");
                }
                if(param.get("scheduleStatus")!=null){
                    WHERE("a.end_time=#{scheduleStatus}");
                }
                if(param.get("guestStatus")!=null){
                    WHERE("a.guest_status=#{guestStatus}");
                }
                if(param.get("partnerStatus")!=null){
                    WHERE("a.partner_status=#{partnerStatus}");
                }
                if(param.get("contactStatus")!=null){
                    WHERE("a.contact_status=#{contactStatus}");
                }
                if(param.get("generalizeStatus")!=null){
                    WHERE("a.generalize_status=#{generalizeStatus}");
                }
                if(param.get("issueStatus")!=null){
                    WHERE("a.issue_status=#{issueStatus}");
                }
                if(param.get("bannerType")!=null){
                    WHERE("a.banner_type=#{bannerType}");
                }
                if (param.get("bannerId") != null) {
                    WHERE("a.banner_id=#{bannerId}");
                }
                if(param.get("status")!=null){
                    if(!param.get("status").equals(3)){
                        WHERE("a.issue_status = #{status}");// and a.end_time>NOW()
                    }else{
                        WHERE("a.end_time<NOW()");
                    }
                }
                WHERE("a.delete_status=1");
            }
        }.toString();
    }

    public String findJoins(@Param("userId") Integer userId,boolean isValid){
        return new SQL(){
            {
                SELECT("a.id, a.user_id, a.activity_title, a.activity_type, a.start_time, a.end_time, a.activity_address",
                        "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.schedule_status",
                        "a.guest_status, a.partner_status, a.contact_status, a.c_time, a.u_time, a.u_id, a.activity_description ,a.issue_status,a.banner_type,a.ticket_config,a.custom_url");
                FROM("tickets_record t");
                LEFT_OUTER_JOIN("trx_order trx on t.order_id = trx.id");
                LEFT_OUTER_JOIN("activity a on a.id = trx.activity_id");
                WHERE("trx.user_id = #{userId} and a.delete_status = 1");
                if(isValid){
                    WHERE("NOW() <= a.end_time and t.ticket_status in (1,2,3)");
                }else{
                    WHERE("(NOW() > a.end_time or t.ticket_status in (0,4,9))");
                }
                GROUP_BY("a.id");
                ORDER_BY("a.start_time desc");
            }
        }.toString();
    }

    public String findDetailById(@Param("id") Integer id) {
        return new SQL() {
            {
                SELECT("a.id,a.user_id,a.activity_title,a.activity_type,a.start_time,a.end_time,a.activity_address," +
                        " a.activity_banner_mobile,a.activity_banner,a.is_open,a.is_online,a.schedule_status,a.activity_description," +
                        " a.guest_status,a.partner_status,a.contact_status,a.c_time,a.u_time,a.u_id,u.username,u.nickname,issue_status,banner_type,personalized_signature,a.banner_id,a.ticket_config,a.custom_url");
                FROM("activity a  ");
                LEFT_OUTER_JOIN( "user u on a.user_id = u.id");
                WHERE("a.id = #{id}");
                WHERE("a.delete_status=1");
            }
        }.toString();
    }

    public String selectById(Integer id) {
        return new SQL() {
            {
                SELECT(columns);
                FROM("activity ");
                WHERE("id=#{id}");
                WHERE("delete_status=1");
            }
        }.toString();
    }



}