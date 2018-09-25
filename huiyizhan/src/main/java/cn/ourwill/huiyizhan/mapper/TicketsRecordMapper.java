package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.TicketsRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketsRecordMapper extends IBaseMapper<TicketsRecord> {
    @Delete({
            "delete from tickets_record",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into tickets_record (tickets_id, activity_id, tickets_name, tickets_price,",
            "conferee_name, conferee_phone, ",
            "conferee_email, user_id, ",
            "order_id, ticket_status, ",
            "back_info, sign_code, auth_code, sign_time, ",
            "c_time, u_time,ticket_link)",
            "values (#{ticketsId,jdbcType=INTEGER},#{activityId,jdbcType=INTEGER},#{ticketsName,jdbcType=VARCHAR},#{ticketsPrice,jdbcType=INTEGER}, ",
            "#{confereeName,jdbcType=VARCHAR}, #{confereePhone,jdbcType=VARCHAR}, ",
            "#{confereeEmail,jdbcType=VARCHAR}, #{userId,jdbcType=INTEGER}, ",
            "#{orderId,jdbcType=INTEGER}, #{ticketStatus,jdbcType=INTEGER}, ",
            "#{backInfo,jdbcType=VARCHAR}, #{signCode,jdbcType=INTEGER}, #{authCode,jdbcType=VARCHAR}, #{signTime,jdbcType=TIMESTAMP}, ",
            "#{cTime,jdbcType=TIMESTAMP}, #{uTime,jdbcType=TIMESTAMP},#{ticketLink,jdbcType=VARCHAR})"
    })
    int insert(TicketsRecord record);

    @InsertProvider(type = TicketsRecordSqlProvider.class, method = "insertSelective")
    int insertSelective(TicketsRecord record);

    @Select({
            "select",
            "id, tickets_id, activity_id, tickets_name, tickets_price, conferee_name, conferee_phone, conferee_email, user_id, order_id, ",
            "ticket_status, back_info, sign_code, auth_code,sign_time, c_time, u_time,ticket_link",
            "from tickets_record",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    TicketsRecord selectByPrimaryKey(Integer id);

    @UpdateProvider(type = TicketsRecordSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TicketsRecord record);

    @Update({
            "update tickets_record",
            "set tickets_id = #{ticketsId,jdbcType=INTEGER},",
            "activity_id = #{activityId,jdbcType=INTEGER},",
            "tickets_name = #{ticketsName,jdbcType=VARCHAR},",
            "tickets_price = #{ticketsPrice,jdbcType=INTEGER},",
            "conferee_name = #{confereeName,jdbcType=VARCHAR},",
            "conferee_phone = #{confereePhone,jdbcType=VARCHAR},",
            "conferee_email = #{confereeEmail,jdbcType=VARCHAR},",
            "user_id = #{userId,jdbcType=INTEGER},",
            "order_id = #{orderId,jdbcType=INTEGER},",
            "ticket_status = #{ticketStatus,jdbcType=INTEGER},",
            "back_info = #{backInfo,jdbcType=VARCHAR},",
            "sign_code = #{signCode,jdbcType=INTEGER},",
            "auth_code = #{authCode,jdbcType=VARCHAR},",
            "sign_time = #{signTime,jdbcType=TIMESTAMP},",
            "c_time = #{cTime,jdbcType=TIMESTAMP},",
            "u_time = #{uTime,jdbcType=TIMESTAMP}",
            "ticket_link = #{ticket_link,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TicketsRecord record);

    @Select("select count(1) from tickets_record where tickets_id = #{ticketsId} and ticket_status in (1,2,3)")
    int getCountByTicketsId(Integer ticketsId);

    @Insert({"<script> ",
            "insert into tickets_record (tickets_id, activity_id, tickets_name, tickets_price, ",
            "conferee_name, conferee_phone, ",
            "conferee_email, user_id, ",
            "order_id, order_no,ticket_status, ",
            "back_info, sign_code, auth_code, ",
            "c_time, u_time) VALUES",
            "<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\"> ",
            "(#{item.ticketsId},#{item.activityId},#{item.ticketsName},#{item.ticketsPrice},#{item.confereeName},#{item.confereePhone},#{item.confereeEmail},#{item.userId},",
            "#{item.orderId},#{item.orderNo},#{item.ticketStatus},#{item.backInfo},#{item.signCode},#{item.authCode},#{item.cTime},#{item.uTime})",
            "</foreach> ",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int batchSave(List<TicketsRecord> list);

    @SelectProvider(type = TicketsRecordSqlProvider.class, method = "getByActivityIdUserId")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    List<TicketsRecord> getByActivityIdUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId, @Param("isValid") Boolean isValid);

    @UpdateProvider(type = TicketsRecordSqlProvider.class, method = "updateUserInfoByIdSelective")
    int updateUserInfoById(TicketsRecord ticketsRecord);

    @Select({
            "select",
            "t.id, t.tickets_id, t.activity_id,t.tickets_name,t.tickets_price, t.conferee_name, t.conferee_phone, t.conferee_email, t.user_id, t.order_id, ",
            "t.ticket_status, t.back_info, t.sign_code, t.auth_code,t.sign_time, t.c_time, t.u_time,t.ticket_link",
            "from tickets_record t",
            "where t.order_id = #{orderId}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    List<TicketsRecord> getByOrderId(@Param("orderId") Integer orderId);

    @SelectProvider(type = TicketsRecordSqlProvider.class, method = "selectByParams")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR),
            @Result(property = "trxOrder", column = "order_id",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.TrxOrderMapper.selectByPrimaryKey")
            )
    })
    List<TicketsRecord> selectByParamsWithOrder(Map params);

    @Update("update tickets_record set ticket_status = #{ticketStatus} where ticket_status = 3 and id = #{id} and activity_id = #{activityId}")
    int checkTicket(@Param("id") Integer id, @Param("activityId") Integer activityId, @Param("ticketStatus") int ticketStatus);

    @Update({"<script>update tickets_record set ticket_status = #{ticketStatus} where ticket_status = 3 and activity_id = #{activityId} and id in",
            "<foreach item=\"item\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">",
            "#{item}",
            "</foreach> ",
            "</script>"})
    int checkTicketBatch(@Param("ids") List<Integer> ids, @Param("activityId") Integer activityId, @Param("ticketStatus") int ticketStatus);

    @Update("update tickets_record set ticket_status = #{ticketStatus} where id = #{id} and activity_id = #{activityId}")
    int refundTicket(@Param("id") Integer id, @Param("activityId") Integer activityId, @Param("ticketStatus") int ticketStatus);

    @SelectProvider(type = TicketsRecordSqlProvider.class,method = "getParticipation")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR),

            /******************************************activityTickets字段***********************************/
            @Result(column = "ati_id", property = "activityTickets.id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "ati_activity_id", property = "activityTickets.activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_name", property = "activityTickets.ticketName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ticket_price", property = "activityTickets.ticketPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_explain", property = "activityTickets.ticketExplain", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ati_start_time", property = "activityTickets.startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "cut_time", property = "activityTickets.cutTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "is_free", property = "activityTickets.isFree", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_publish_sell", property = "activityTickets.isPublishSell", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_check", property = "activityTickets.isCheck", jdbcType = JdbcType.INTEGER),
            @Result(column = "sell_status", property = "activityTickets.sellStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "single_limits", property = "activityTickets.singleLimits", jdbcType = JdbcType.INTEGER),
            @Result(column = "total_number", property = "activityTickets.totalNumber", jdbcType = JdbcType.INTEGER),
            @Result(column = "stock_number", property = "activityTickets.stockNumber", jdbcType = JdbcType.INTEGER),
            @Result(column = "ati_user_id", property = "activityTickets.userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ati_c_time", property = "activityTickets.cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ati_u_id", property = "activityTickets.uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ati_u_time", property = "activityTickets.uTime", jdbcType = JdbcType.TIMESTAMP),


            /****************************************** activity***********************************/
            @Result(column = "a_id", property = "activity.id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "a_user_id", property = "activity.userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_title", property = "activity.activityTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_type", property = "activity.activityType", jdbcType = JdbcType.INTEGER),
            @Result(column = "a_start_time", property = "activity.startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "a_end_time", property = "activity.endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_address", property = "activity.activityAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner_mobile", property = "activity.activityBannerMobile", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_banner", property = "activity.activityBanner", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_open", property = "activity.isOpen", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_online", property = "activity.isOnline", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_hot", property = "activity.isHot", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_recent", property = "activity.isRecent", jdbcType = JdbcType.INTEGER),
            @Result(column = "schedule_status", property = "activity.scheduleStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "guest_status", property = "activity.guestStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "partner_status", property = "activity.partnerStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "contact_status", property = "activity.contactStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "a_c_time", property = "activity.cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "a_u_time", property = "activity.uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "a_u_id", property = "activity.uId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_description", property = "activity.activityDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "issue_status", property = "activity.issueStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_type", property = "activity.bannerType", jdbcType = JdbcType.INTEGER),
            @Result(column = "banner_id", property = "activity.bannerId", jdbcType = JdbcType.INTEGER),
            @Result(column = "avatar", property = "activity.avatar", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ticket_config", property = "activity.ticketConfig", jdbcType = JdbcType.INTEGER),
            @Result(column = "custom_url", property = "activity.customUrl", jdbcType = JdbcType.VARCHAR)

    })
    List<TicketsRecord> getParticipation(@Param("userId") Integer userId,@Param("status") Integer status);

    @Select("select count(1) from tickets_record where tickets_id = #{ticketsId} and ticket_status in (1,2,3)")
    int selectCountByTicketsId(@Param("ticketsId") Integer ticketsId);

    @Select({
            "select",
            "id, tickets_id, activity_id, tickets_name, tickets_price, conferee_name, conferee_phone, conferee_email, user_id, order_id, ",
            "ticket_status, back_info, sign_code, auth_code, sign_time, c_time, u_time,ticket_link",
            "from tickets_record",
            "where activity_id = #{activityId,jdbcType=INTEGER} and ticket_status = 2"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    List<TicketsRecord> selectSignedByActivityId(@Param("activityId") Integer activityId);

    @Select({
            "select",
            "id, tickets_id, activity_id, tickets_name, tickets_price, conferee_name, conferee_phone, conferee_email, user_id, order_id, ",
            "ticket_status, back_info, sign_code, auth_code, sign_time, c_time, u_time,ticket_link",
            "from tickets_record"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    List<TicketsRecord> findAll();

    @Update("update tickets_record set ticket_status = 2, sign_time = NOW() where id = #{id} and activity_id = #{activityId} and ticket_status=1")
    int doSign(@Param("id") Integer id, @Param("activityId") Integer activityId);

    @Update("update tickets_record set ticket_status = 1, sign_time = NOW() where id = #{id} and activity_id = #{activityId} and ticket_status=2")
    int cancelSign(@Param("id") Integer id, @Param("activityId") Integer activityId);

    @Select({
            "select",
            "t.id, t.tickets_id, t.activity_id, t.tickets_name, t.tickets_price, t.conferee_name, t.conferee_phone, t.conferee_email, t.user_id, t.order_id, ",
            "t.ticket_status, t.back_info, t.sign_time,t.c_time, t.u_time,t.ticket_link,a.activity_title,a.activity_address,a.start_time,a.end_time",
            "from tickets_record t",
            "left join activity a on a.id = t.activity_id",
            "where t.activity_id = #{activityId} and (t.auth_code = #{code} or t.sign_code = #{code})"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_title", property = "activity.activityTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_time", property = "activity.startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time", property = "activity.endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "activity_address", property = "activity.activityAddress", jdbcType = JdbcType.VARCHAR)
    })
    TicketsRecord selectByAuthCodeOrSignCode(@Param("activityId") Integer activityId, @Param("code") String code);

    //    @Select("select sum(tickets_price) as totalAmount,count(1) as ticketsCount from tickets_record where activity_id = #{activityId} and ticket_status in (1,2,3)")
    @Select({"select COALESCE(sum(tickets_price),0) as totalAmount,",
            "COALESCE(sum(case when (ticket_status = 1 or ticket_status = 2) then 1 else 0 end),0) as soldCount,",
            "COALESCE(sum(case when ticket_status = 3 then 1 else 0 end),0) as noCheckCount,",
            "0 as noPayCount",
            "from tickets_record",
            "where activity_id = #{activityId} and ticket_status in (1,2,3)"})
    Map selectCountByActivityId(@Param("activityId") Integer activityId);

    @Update("update tickets_record set ticket_link = #{ticketLink} where id = #{id}")
    int updateTicketLink(@Param("ticketLink") String ticketLink, @Param("id") Integer id);

    @Select({
            "select",
            "t.id, t.tickets_id, t.activity_id, k.ticket_name as tickets_name, t.tickets_price, t.conferee_name, t.conferee_phone, t.conferee_email, t.user_id, t.order_id, ",
            "t.ticket_status, t.back_info, t.sign_code, t.auth_code, t.sign_time, t.c_time, t.u_time,t.ticket_link",
            "from tickets_record t",
            "left join trx_order o on o.order_no = t.order_no",
            "left join activity_tickets k on k.id = t.tickets_id",
            "where o.activity_id = #{activityId} and o.open_id = #{openId} and t.ticket_status in (1,3)"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "tickets_id", property = "ticketsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "activity_id", property = "activityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "tickets_name", property = "ticketsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tickets_price", property = "ticketsPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "conferee_name", property = "confereeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_phone", property = "confereePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "conferee_email", property = "confereeEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "ticket_status", property = "ticketStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "back_info", property = "backInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_code", property = "signCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "auth_code", property = "authCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sign_time", property = "signTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "c_time", property = "cTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "u_time", property = "uTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ticket_link", property = "ticketLink", jdbcType = JdbcType.VARCHAR)
    })
    List<TicketsRecord> selectTicketsByopenIdAndActivityId(Map<String, Object> map);

    /**
     * 根据门票id集合 更新所属门票库存
     *
     * @param ticketRecordIds
     * @return
     */
    @Update({"<script> update activity_tickets at set at.stock_number = ",
            "(case when at.total_number > (select count(1) from tickets_record where tickets_id = at.id and ticket_status in (1,2,3)) ",
            "then  total_number - (select count(1) from tickets_record where tickets_id = at.id and ticket_status in (1,2,3)) ",
            "else 0 end)",
            "where at.id in (select DISTINCT(tickets_id) from tickets_record where id in ",
            "<foreach item=\"item\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">",
            "#{item}",
            "</foreach> ) ",
            "</script>"})
    int refreshStockNumByTicketIds(@Param("ids") List<Integer> ticketRecordIds);

    /**
     * 根据门票id 更新所属门票库存
     *
     * @param ticketId
     * @return
     */
    @Update({"<script> update activity_tickets at set at.stock_number = ",
            "(case when at.total_number > (select count(1) from tickets_record where tickets_id = at.id and ticket_status in (1,2,3)) ",
            "then  total_number - (select count(1) from tickets_record where tickets_id = at.id and ticket_status in (1,2,3)) ",
            "else 0 end)",
            "where at.id = (select tickets_id from tickets_record where id = #{ticketId}) </script>"})
    int refreshStockNumByTicketId(@Param("ticketId") Integer ticketId);

    @Select({"select COALESCE(sum(case when ticket_status = 1 then 1 else 0 end),0) as no_sign_count,",
            "COALESCE(sum(case when ticket_status = 3 then 1 else 0 end),0) as no_check_count",
            "from tickets_record where order_no = #{orderNo}"})
    HashMap<String, Integer> selectTicketStatusCountByOrderNo(@Param("orderNo") String orderNo);


    @SelectProvider(type = TicketsRecordSqlProvider.class, method = "statisticsMyTicket")
    Integer statisticsMyTicket(@Param("userId") int userId, boolean isValid);

    /**
     * 查询活动的有效门票数量
     *
     * @param id
     * @return
     */
    @Select("select count(1) from tickets_record where ticket_status = 1 and activity_id = #{id}")
    int countValid(@Param("id") int id);
}