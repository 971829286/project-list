package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityTickets;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityTicketsMapper extends IBaseMapper<ActivityTickets>{
    @Delete({
        "update activity_tickets set delete_status = 0",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into activity_tickets (id, activity_id, ",
        "ticket_name, ticket_price, ticket_explain, ",
        "start_time, cut_time, is_free,",
        "is_publish_sell, is_check, ",
        "sell_status, single_limits, ",
        "total_number,stock_number, user_id, ",
        "c_time, u_id, u_time)",
        "values (#{id,jdbcType=INTEGER}, #{activityId,jdbcType=INTEGER}, ",
        "#{ticketName,jdbcType=VARCHAR}, #{ticketPrice,jdbcType=INTEGER}, #{ticketPrice,jdbcType=VARCHAR}, ",
        "#{startTime,jdbcType=TIMESTAMP}, #{cutTime,jdbcType=TIMESTAMP}, #{isFree,jdbcType=INTEGER},",
        "#{isPublishSell,jdbcType=INTEGER}, #{isCheck,jdbcType=INTEGER}, ",
        "#{sellStatus,jdbcType=INTEGER}, #{singleLimits,jdbcType=INTEGER}, ",
        "#{totalNumber,jdbcType=INTEGER},#{stockNumber,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
        "#{cTime,jdbcType=TIMESTAMP}, #{uId,jdbcType=INTEGER}, #{uTime,jdbcType=TIMESTAMP})"
    })
    int insert(ActivityTickets record);

    @InsertProvider(type=ActivityTicketsSqlProvider.class, method="insertSelective")
    int insertSelective(ActivityTickets record);

    @Select({
        "select",
        "id, activity_id, ticket_name, ticket_price, ticket_explain, start_time, cut_time, is_free, is_publish_sell, ",
        "is_check, sell_status, single_limits, total_number, stock_number, user_id, c_time, u_id, u_time",
        "from activity_tickets",
        "where id = #{id,jdbcType=INTEGER} and delete_status = 1"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="activity_id", property="activityId", jdbcType=JdbcType.INTEGER),
        @Result(column="ticket_name", property="ticketName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ticket_price", property="ticketPrice", jdbcType=JdbcType.INTEGER),
        @Result(column="ticket_explain", property="ticketExplain", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="cut_time", property="cutTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_free", property="isFree", jdbcType=JdbcType.INTEGER),
        @Result(column="is_publish_sell", property="isPublishSell", jdbcType=JdbcType.INTEGER),
        @Result(column="is_check", property="isCheck", jdbcType=JdbcType.INTEGER),
        @Result(column="sell_status", property="sellStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="single_limits", property="singleLimits", jdbcType=JdbcType.INTEGER),
        @Result(column="total_number", property="totalNumber", jdbcType=JdbcType.INTEGER),
        @Result(column="stock_number", property="stockNumber", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="c_time", property="cTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="u_id", property="uId", jdbcType=JdbcType.INTEGER),
        @Result(column="u_time", property="uTime", jdbcType=JdbcType.TIMESTAMP)
    })
    ActivityTickets selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ActivityTicketsSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityTickets record);

    @Update({
        "update activity_tickets",
        "set activity_id = #{activityId,jdbcType=INTEGER},",
          "ticket_name = #{ticketName,jdbcType=VARCHAR},",
          "ticket_price = #{ticketPrice,jdbcType=INTEGER},",
          "ticket_explain = #{ticketExplain,jdbcType=VARCHAR},",
          "start_time = #{startTime,jdbcType=TIMESTAMP},",
          "cut_time = #{cutTime,jdbcType=TIMESTAMP},",
          "is_free = #{isFree,jdbcType=INTEGER},",
          "is_publish_sell = #{isPublishSell,jdbcType=INTEGER},",
          "is_check = #{isCheck,jdbcType=INTEGER},",
          "sell_status = #{sellStatus,jdbcType=INTEGER},",
          "single_limits = #{singleLimits,jdbcType=INTEGER},",
          "total_number = #{totalNumber,jdbcType=INTEGER},",
          "stock_number = #{stockNumber,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "c_time = #{cTime,jdbcType=TIMESTAMP},",
          "u_id = #{uId,jdbcType=INTEGER},",
          "u_time = #{uTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ActivityTickets record);

    @InsertProvider(type=ActivityTicketsSqlProvider.class, method="batchInsertSelective")
    int batchSave(@Param("list") List<ActivityTickets> tickets);

    @Select({
            "select",
            "COALESCE(sum(case when tr.ticket_status = 3 then 1 else 0 end),0) as no_check_number,",
            "COALESCE(sum(case when (tr.ticket_status = 1 or tr.ticket_status = 2) then 1 else 0 end),0) as sold_number,",
//            "COALESCE(sum(ats.stock_number),0) as total_stock_number,",
            "0 as no_pay_number,",
            "ats.id, ats.activity_id, ats.ticket_name, ats.ticket_price, ats.ticket_explain, ats.start_time, ats.cut_time, ats.is_free, ats.is_publish_sell, ",
            "ats.is_check, ats.sell_status, ats.single_limits, ats.total_number, ats.stock_number, ats.user_id, ats.c_time, ats.u_id, ats.u_time",
            "from activity_tickets ats",
            "left join tickets_record tr on tr.tickets_id = ats.id ",
            "where ats.activity_id = #{activityId,jdbcType=INTEGER} and ats.delete_status = 1 group by ats.id order by rank"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="activity_id", property="activityId", jdbcType=JdbcType.INTEGER),
            @Result(column="ticket_name", property="ticketName", jdbcType=JdbcType.VARCHAR),
            @Result(column="ticket_price", property="ticketPrice", jdbcType=JdbcType.INTEGER),
            @Result(column="ticket_explain", property="ticketExplain", jdbcType=JdbcType.VARCHAR),
            @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="cut_time", property="cutTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_free", property="isFree", jdbcType=JdbcType.INTEGER),
            @Result(column="is_publish_sell", property="isPublishSell", jdbcType=JdbcType.INTEGER),
            @Result(column="is_check", property="isCheck", jdbcType=JdbcType.INTEGER),
            @Result(column="sell_status", property="sellStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="single_limits", property="singleLimits", jdbcType=JdbcType.INTEGER),
            @Result(column="total_number", property="totalNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="stock_number", property="stockNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="no_check_number", property="noCheckNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="sold_number", property="soldNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="no_pay_number", property="noPayNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="c_time", property="cTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="u_id", property="uId", jdbcType=JdbcType.INTEGER),
            @Result(column="u_time", property="uTime", jdbcType=JdbcType.TIMESTAMP)
//            @Result(column="total_stock_number", property="totalStockNumber", jdbcType=JdbcType.INTEGER)

    })
    List<ActivityTickets> selectByActivityId(Integer activityId);

    @Select("select id from activity_tickets where activity_id = #{activityId} and delete_status = 1")
    List<Integer> getAllIdByActivityId(@Param("activityId") Integer activityId);

    @Update({"<script>",
             "update activity_tickets set delete_status = 0 where id in",
            "<foreach item=\"item\" collection=\"idList\" open=\"(\" separator=\",\" close=\")\">",
            "#{item}",
            "</foreach></script>"})
    int batchDelete(@Param("idList") List<Integer> deleteIds);

    @Insert({"<script> ",
            "insert into activity_tickets (id,activity_id,ticket_name,ticket_price,ticket_explain,start_time,cut_time,is_free,is_publish_sell,is_check,sell_status,single_limits,total_number,stock_number,user_id,c_time,u_id,u_time,rank)",
            "values ",
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> ",
            "(#{item.id},#{item.activityId},#{item.ticketName},#{item.ticketPrice},#{item.ticketExplain},#{item.startTime},#{item.cutTime},#{item.isFree},#{item.isPublishSell},",
            "#{item.isCheck},#{item.sellStatus},#{item.singleLimits},#{item.totalNumber},#{item.stockNumber},#{item.userId},#{item.cTime},#{item.uId},#{item.uTime},#{item.rank})",
            "</foreach> ",
            "ON DUPLICATE KEY UPDATE ",
            "activity_id = VALUES(activity_id), ticket_name = VALUES( ticket_name), ticket_price = VALUES( ticket_price), ticket_explain = VALUES( ticket_explain),",
            "start_time = VALUES( start_time), cut_time = VALUES( cut_time),is_free = VALUES(is_free),is_publish_sell = VALUES(is_publish_sell),is_check = VALUES(is_check),",
            "sell_status = VALUES(sell_status),single_limits = VALUES(single_limits),total_number = VALUES(total_number),stock_number = VALUES(stock_number),u_id = VALUES(u_id),u_time = VALUES(u_time),rank = VALUES(rank)",
            "</script>"})
    int batchUpdate(@Param("items") List<ActivityTickets> activityTickets);

    @Update("update activity_tickets set stock_number = stock_number - #{number} where id = #{id} and stock_number>=#{number} and delete_status = 1")
    int updateStockNumber(@Param("id") Integer id, @Param("number") Integer number);

    @Update("update activity_tickets set stock_number = case when stock_number + #{number} > total_number then total_number else stock_number + #{number} end where id = #{id}")
    int updateStockNumberAdd(@Param("id") Integer id, @Param("number") Integer number);

    @Select({"SELECT ",
            "at.id, at.activity_id, at.ticket_name, at.ticket_price, at.ticket_explain, at.start_time, at.cut_time, at.is_free, at.is_publish_sell, ",
            "at.is_check, at.sell_status, at.single_limits, at.total_number, at.stock_number, at.user_id, at.c_time, at.u_id, at.u_time",
            "FROM activity_tickets at ",
            "left join activity a on at.activity_id = a.id",
            "where at.delete_status = 1 and at.activity_id = #{activityId} and a.delete_status = 1 and a.issue_status = 1",
            "and (at.start_time <= NOW() or at.is_publish_sell = 1)",
            "and at.cut_time >= now()"})
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="activity_id", property="activityId", jdbcType=JdbcType.INTEGER),
            @Result(column="ticket_name", property="ticketName", jdbcType=JdbcType.VARCHAR),
            @Result(column="ticket_price", property="ticketPrice", jdbcType=JdbcType.INTEGER),
            @Result(column="ticket_explain", property="ticketExplain", jdbcType=JdbcType.VARCHAR),
            @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="cut_time", property="cutTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_free", property="isFree", jdbcType=JdbcType.INTEGER),
            @Result(column="is_publish_sell", property="isPublishSell", jdbcType=JdbcType.INTEGER),
            @Result(column="is_check", property="isCheck", jdbcType=JdbcType.INTEGER),
            @Result(column="sell_status", property="sellStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="single_limits", property="singleLimits", jdbcType=JdbcType.INTEGER),
            @Result(column="total_number", property="totalNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="stock_number", property="stockNumber", jdbcType=JdbcType.INTEGER),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="c_time", property="cTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="u_id", property="uId", jdbcType=JdbcType.INTEGER),
            @Result(column="u_time", property="uTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<ActivityTickets> getValidByActivityId(@Param("activityId") Integer activityId);
}