package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.TicketsRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class TicketsRecordSqlProvider {

    public String insertSelective(TicketsRecord record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("tickets_record");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getTicketsId() != null) {
            sql.VALUES("tickets_id", "#{ticketsId,jdbcType=INTEGER}");
        }

        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getTicketsName())) {
            sql.VALUES("tickets_name", "#{ticketsName,jdbcType=VARCHAR}");
        }

        if (record.getTicketsPrice() != null) {
            sql.VALUES("tickets_price", "#{ticketsPrice,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getConfereeName())) {
            sql.VALUES("conferee_name", "#{confereeName,jdbcType=VARCHAR}");
        }

        if (StringUtils.isNotEmpty(record.getConfereePhone())) {
            sql.VALUES("conferee_phone", "#{confereePhone,jdbcType=VARCHAR}");
        }

        if (StringUtils.isNotEmpty(record.getConfereeEmail())) {
            sql.VALUES("conferee_email", "#{confereeEmail,jdbcType=VARCHAR}");
        }

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getOrderId() != null) {
            sql.VALUES("order_id", "#{orderId,jdbcType=INTEGER}");
        }

        if (record.getTicketStatus() != null) {
            sql.VALUES("ticket_status", "#{ticketStatus,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getBackInfo())) {
            sql.VALUES("back_info", "#{backInfo,jdbcType=VARCHAR}");
        }

        if (record.getSignCode() != null) {
            sql.VALUES("sign_code", "#{signCode,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getAuthCode())) {
            sql.VALUES("auth_code", "#{authCode,jdbcType=VARCHAR}");
        }

        if (record.getSignTime() != null) {
            sql.VALUES("sign_time", "#{signTime,jdbcType=TIMESTAMP}");
        }

        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUTime() != null) {
            sql.VALUES("u_time", "#{uTime,jdbcType=TIMESTAMP}");
        }
        if (record.getTicketLink() != null) {
            sql.VALUES("ticket_link", "#{ticketLink,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TicketsRecord record) {
        SQL sql = new SQL();
        sql.UPDATE("tickets_record");

        if (record.getTicketsId() != null) {
            sql.SET("tickets_id = #{ticketsId,jdbcType=INTEGER}");
        }

        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getTicketsName())) {
            sql.SET("tickets_name=#{ticketsName,jdbcType=VARCHAR}");
        }

        if (record.getTicketsPrice() != null) {
            sql.SET("tickets_price=#{ticketsPrice,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getConfereeName())) {
            sql.SET("conferee_name = #{confereeName,jdbcType=VARCHAR}");
        }

        if (StringUtils.isNotEmpty(record.getConfereePhone())) {
            sql.SET("conferee_phone = #{confereePhone,jdbcType=VARCHAR}");
        }

        if (StringUtils.isNotEmpty(record.getConfereeEmail())) {
            sql.SET("conferee_email = #{confereeEmail,jdbcType=VARCHAR}");
        }

        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }

        if (record.getOrderId() != null) {
            sql.SET("order_id = #{orderId,jdbcType=INTEGER}");
        }

        if (record.getTicketStatus() != null) {
            sql.SET("ticket_status = #{ticketStatus,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getBackInfo())) {
            sql.SET("back_info = #{backInfo,jdbcType=VARCHAR}");
        }

        if (record.getSignCode() != null) {
            sql.SET("sign_code = #{signCode,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getAuthCode())) {
            sql.SET("auth_code = #{authCode,jdbcType=VARCHAR}");
        }

        if (record.getSignTime() != null) {
            sql.SET("sign_time = #{signTime,jdbcType=TIMESTAMP}");
        }

        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUTime() != null) {
            sql.SET("u_time = #{uTime,jdbcType=TIMESTAMP}");
        }
        if (record.getTicketLink() != null) {
            sql.VALUES("ticket_link", "#{ticketLink,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParams(final Map<String, Object> param) {
        return new SQL() {
            {
                SELECT("id, tickets_id, activity_id, tickets_name, tickets_price, conferee_name, conferee_phone, conferee_email, user_id, order_id, ticket_status, back_info, sign_code, auth_code, c_time, u_time,ticket_link");
                FROM("tickets_record");
                if (param.get("id") != null && param.get("id") != "") {
                    WHERE("id=#{id}");
                }
                if (param.get("ticketsId") != null && param.get("ticketsId") != "") {
                    WHERE("tickets_id=#{ticketsId}");
                }
                if (param.get("activityId") != null && param.get("activityId") != "") {
                    WHERE("activity_id=#{activityId}");
                }
                if (param.get("ticketsName") != null && param.get("ticketsName") != "") {
                    WHERE("tickets_name=#{ticketsName}");
                }
                if (param.get("ticketsPrice") != null && param.get("ticketsPrice") != "") {
                    WHERE("tickets_price=#{ticketsPrice}");
                }
                if (param.get("confereeName") != null && param.get("confereeName") != "") {
                    WHERE("conferee_name=#{confereeName}");
                }
                if (param.get("confereePhone") != null && param.get("confereePhone") != "") {
                    WHERE("conferee_phone=#{confereePhone}");
                }
                if (param.get("confereeEmail") != null && param.get("confereeEmail") != "") {
                    WHERE("conferee_email=#{confereeEmail}");
                }
                if (param.get("userId") != null && param.get("userId") != "") {
                    WHERE("user_id=#{userId}");
                }
                if (param.get("orderId") != null && param.get("orderId") != "") {
                    WHERE("order_id=#{orderId}");
                }
                if (param.get("ticketStatus") != null && param.get("ticketStatus") != "") {
                    WHERE("ticket_status=#{ticketStatus}");
                }
                if (param.get("searchKeys") != null && param.get("searchKeys") != "") {
                    WHERE("(conferee_name like concat(#{searchKeys},'%') or conferee_phone like concat(#{searchKeys},'%') or conferee_email like concat(#{searchKeys},'%'))");
                }
                ORDER_BY("id desc");
            }
        }.toString();
    }

    public String updateUserInfoByIdSelective(TicketsRecord record) {
        SQL sql = new SQL();
        sql.UPDATE("tickets_record");
        if (StringUtils.isNotEmpty(record.getConfereeName())) {
            sql.SET("conferee_name = #{confereeName,jdbcType=VARCHAR}");
        }
        if (StringUtils.isNotEmpty(record.getConfereePhone())) {
            sql.SET("conferee_phone = #{confereePhone,jdbcType=VARCHAR}");
        }
        if (StringUtils.isNotEmpty(record.getConfereeEmail())) {
            sql.SET("conferee_email = #{confereeEmail,jdbcType=VARCHAR}");
        }
        if (record.getTicketStatus() != null) {
            sql.SET("ticket_status = #{ticketStatus,jdbcType=INTEGER}");
        }
        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }

    public String getByActivityIdUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId, @Param("isValid") Boolean isValid) {
        return new SQL() {
            {
                SELECT("t.id, t.tickets_id, t.activity_id,t.tickets_name,t.tickets_price, t.conferee_name, t.conferee_phone, t.conferee_email, t.user_id, t.order_id",
                        "t.ticket_status, t.back_info, t.sign_code, t.auth_code,t.sign_time, t.c_time, t.u_time,t.ticket_link");
                FROM("tickets_record t");
                LEFT_OUTER_JOIN("activity a on t.activity_id = a.id");
                WHERE("t.activity_id = #{activityId} and t.user_id = #{userId}");
                if (isValid != null && isValid) {
                    WHERE("NOW() <= a.end_time and t.ticket_status in (1,2,3)");
                } else if (isValid != null) {
                    WHERE("(NOW() > a.end_time or t.ticket_status in (0,4,9))");
                }
            }
        }.toString();
    }

    public String statisticsMyTicket(@Param("userId") Integer userId,boolean isValid){
        return new SQL(){
            {
                SELECT("count(1) as count");
                FROM("tickets_record t");
                LEFT_OUTER_JOIN("activity a on t.activity_id = a.id");
                if(isValid){
                    WHERE("NOW() <= a.end_time and t.ticket_status in (1,2,3)");
                }else{
                    WHERE("(NOW() > a.end_time or t.ticket_status in (0,4,9))");
                }
                WHERE("t.user_id = #{userId}");
            }
        }.toString();
    }

    public String getParticipation(@Param("userId") Integer userId, @Param("status") Integer status) {
        final String value;
        if ( status == 1) { // 查询有效票
            value = "a.start_time >";
        } else {
            value = "a.end_time <";
        }
        return new SQL() {
            {
                SELECT(
                        // TicketRecord 属性
                "tr.id , tr.tickets_id, tr.activity_id, tr.tickets_name, tr.tickets_price, tr.conferee_name, tr.conferee_phone," +
                        "tr.conferee_email, tr.user_id user_id, tr.order_id,tr.ticket_status, tr.back_info, tr.sign_code, tr.auth_code, tr.c_time tr_c_time, tr.u_time,tr.ticket_link," +

                        // ActivityTicket属性
                        "ati.id ati_id, ati.activity_id ati_activity_id, ati.ticket_name, ati.ticket_price, ati.ticket_explain, ati.start_time ati_start_time, ati.cut_time, ati.is_free, ati.is_publish_sell," +
                        "ati.is_check, ati.sell_status, ati.single_limits, ati.total_number, ati.stock_number, ati.user_id ati_user_id, ati.c_time ati_c_time, ati.u_id ati_u_id, ati.u_time ati_u_time," +

                        // activity 属性
                        "a.id a_id, a.user_id a_user_id, a.activity_title, a.activity_type, a.start_time a_start_time, a.end_time a_end_time, a.activity_address," +
                        "a.activity_banner_mobile, a.activity_banner, a.is_open, a.is_online, a.is_hot, a.is_recent, a.schedule_status," +
                        "a.guest_status, a.partner_status, a.contact_status, a.banner_id,a.c_time a_c_time, a.u_time a_u_time, a.u_id a_u_id, a.activity_description ,a.issue_status,a.banner_type ,a.ticket_config, a.custom_url"
                 );
                FROM("tickets_record tr");
                LEFT_OUTER_JOIN("activity a on a.id = tr.activity_id");
                LEFT_OUTER_JOIN("activity_tickets ati on a.id = ati.activity_id");
                WHERE(" tr.user_id  = #{userId,jdbcType=INTEGER} AND " + value + "  NOW()" +
                        "AND a.issue_status = 1 AND a.delete_status = 1");
                GROUP_BY("tr.id asc");
            }

        }.toString();
    }
}