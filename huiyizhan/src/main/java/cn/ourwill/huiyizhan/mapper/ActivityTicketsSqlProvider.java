package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityTickets;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;

public class ActivityTicketsSqlProvider{

    public String insertSelective(ActivityTickets record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("activity_tickets");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getActivityId() != null) {
            sql.VALUES("activity_id", "#{activityId,jdbcType=INTEGER}");
        }
        
        if (StringUtils.isNotEmpty(record.getTicketName())) {
            sql.VALUES("ticket_name", "#{ticketName,jdbcType=VARCHAR}");
        }
        
        if (record.getTicketPrice() != null) {
            sql.VALUES("ticket_price", "#{ticketPrice,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getTicketExplain())) {
            sql.VALUES("ticket_explain", "#{ticketExplain,jdbcType=VARCHAR}");
        }
        
        if (record.getStartTime() != null) {
            sql.VALUES("start_time", "#{startTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCutTime() != null) {
            sql.VALUES("cut_time", "#{cutTime,jdbcType=TIMESTAMP}");
        }

        if (record.getIsFree() != null) {
            sql.VALUES("is_free", "#{isFree,jdbcType=INTEGER}");
        }

        if (record.getIsPublishSell() != null) {
            sql.VALUES("is_publish_sell", "#{isPublishSell,jdbcType=INTEGER}");
        }
        
        if (record.getIsCheck() != null) {
            sql.VALUES("is_check", "#{isCheck,jdbcType=INTEGER}");
        }
        
        if (record.getSellStatus() != null) {
            sql.VALUES("sell_status", "#{sellStatus,jdbcType=INTEGER}");
        }
        
        if (record.getSingleLimits() != null) {
            sql.VALUES("single_limits", "#{singleLimits,jdbcType=INTEGER}");
        }
        
        if (record.getTotalNumber() != null) {
            sql.VALUES("total_number", "#{totalNumber,jdbcType=INTEGER}");
        }

        if (record.getStockNumber() != null) {
            sql.VALUES("stock_number", "#{stockNumber,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.VALUES("c_time", "#{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUId() != null) {
            sql.VALUES("u_id", "#{uId,jdbcType=INTEGER}");
        }
        
        if (record.getUTime() != null) {
            sql.VALUES("u_time", "#{uTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ActivityTickets record) {
        SQL sql = new SQL();
        sql.UPDATE("activity_tickets");
        
        if (record.getActivityId() != null) {
            sql.SET("activity_id = #{activityId,jdbcType=INTEGER}");
        }
        
        if (StringUtils.isNotEmpty(record.getTicketName())) {
            sql.SET("ticket_name = #{ticketName,jdbcType=VARCHAR}");
        }
        
        if (record.getTicketPrice() != null) {
            sql.SET("ticket_price = #{ticketPrice,jdbcType=INTEGER}");
        }

        if (StringUtils.isNotEmpty(record.getTicketExplain())) {
            sql.SET("ticket_explain = #{ticketExplain,jdbcType=VARCHAR}");
        }

        if (record.getStartTime() != null) {
            sql.SET("start_time = #{startTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCutTime() != null) {
            sql.SET("cut_time = #{cutTime,jdbcType=TIMESTAMP}");
        }

        if (record.getIsFree() != null) {
            sql.SET("is_free = #{isFree,jdbcType=INTEGER}");
        }

        if (record.getIsPublishSell() != null) {
            sql.SET("is_publish_sell = #{isPublishSell,jdbcType=INTEGER}");
        }
        
        if (record.getIsCheck() != null) {
            sql.SET("is_check = #{isCheck,jdbcType=INTEGER}");
        }
        
        if (record.getSellStatus() != null) {
            sql.SET("sell_status = #{sellStatus,jdbcType=INTEGER}");
        }
        
        if (record.getSingleLimits() != null) {
            sql.SET("single_limits = #{singleLimits,jdbcType=INTEGER}");
        }
        
        if (record.getTotalNumber() != null) {
            sql.SET("total_number = #{totalNumber,jdbcType=INTEGER}");
        }

        if (record.getStockNumber() != null) {
            sql.SET("stock_number = #{stockNumber,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getCTime() != null) {
            sql.SET("c_time = #{cTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUId() != null) {
            sql.SET("u_id = #{uId,jdbcType=INTEGER}");
        }
        
        if (record.getUTime() != null) {
            sql.SET("u_time = #{uTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    public String batchInsertSelective(@Param("list") List<ActivityTickets> tickets) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into activity_tickets ");
        sql.append("(activity_id,ticket_name,ticket_price,ticket_explain,start_time,cut_time,is_free,is_publish_sell,is_check,sell_status,single_limits,total_number,stock_number,user_id,c_time,u_id,u_time) ");
        sql.append("values ");
        MessageFormat mf = new MessageFormat("(#'{'list[{0}].activityId},#'{'list[{0}].ticketName},#'{'list[{0}].ticketPrice},#'{'list[{0}].ticketExplain},#'{'list[{0}].startTime},#'{'list[{0}].cutTime},#'{'list[{0}].isFree},#'{'list[{0}].isPublishSell},#'{'list[{0}].isCheck},#'{'list[{0}].sellStatus},#'{'list[{0}].singleLimits},#'{'list[{0}].totalNumber},#'{'list[{0}].stockNumber},#'{'list[{0}].userId},#'{'list[{0}].cTime},#'{'list[{0}].uId},#'{'list[{0}].uTime})");
        for (int i = 0; i < tickets.size(); i++) {
            sql.append(mf.format(new Object[]{i}));
            if (i < tickets.size() - 1) {
                sql.append(",");
            }
        }
        return sql.toString();
    }
}