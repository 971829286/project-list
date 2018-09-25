package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.FundDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author zhaoqing
 * @Time 2017/8/10 0010 14:44
 * @Version1.0
 */
@Repository
public interface FundDetailMapper extends IBaseMapper<FundDetail> {

    String columns = "id ,order_no,user_id,activity_id,amount,create_time,type,fund_status,finish_time,pay_user,pay_wechat_num";

    @InsertProvider(type = FundDetailMapperProvider.class ,method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(FundDetail fundDetail);

    @UpdateProvider(type = FundDetailMapperProvider.class ,method = "update")
    Integer update(FundDetail fundDetail);

    @Select("select "+columns+" from fund_detail where order_no = #{orderNo} for update")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "type",property = "type"),
            @Result( column = "fund_status",property = "fund_status"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "pay_user",property = "payUser"),
            @Result(column = "pay_wechat_num",property = "payWeChatNum")
    })
    FundDetail getByOrderNo(String orderNo);


    @Select("select "+columns+" from fund_detail where user_id = #{userId} and fund_status = 1 order by create_time desc")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "type",property = "type"),
            @Result( column = "fund_status",property = "fund_status"),
            @Result( column = "create_time",property = "createTime"),
            @Result( column = "finish_time",property = "finishTime"),
            @Result( column = "pay_user",property = "payUser"),
            @Result(column = "pay_wechat_num",property = "payWeChatNum")
    })
    List<FundDetail> selectFundListById(Integer userId);

    @Select("SELECT IFNULL(SUM(amount),0) FROM fund_detail WHERE user_id = #{userId} and fund_status = 1 and type= 0")
    Integer selectInCome(Integer userId);

    @Select("SELECT IFNULL(SUM(amount),0) FROM fund_detail WHERE user_id = #{userId} and fund_status = 1 and type= 1")
    Integer selectWithdraw(Integer userId);

    @Select("SELECT IFNULL(SUM(amount),0) FROM fund_detail WHERE user_id = #{userId} and fund_status = 1")
    Integer selectSurplus(Integer userId);

    @Select("SELECT IFNULL(SUM(amount),0) FROM fund_detail WHERE user_id = #{userId} and fund_status = 1 and type= 0 and  TO_DAYS(create_time) = TO_DAYS(NOW())")
    Integer selectTodayIncome(Integer userId);

    @SelectProvider(type = FundDetailMapperProvider.class ,method = "getRewardList")
    @Results({
            @Result(column = "user_id",property = "payUser"),
            @Result(column = "pay_wechat_num",property = "payWeChatNum"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "nickname",property = "nickname"),
    })
    List<FundDetail>getRewardList(Integer activityId);

}
