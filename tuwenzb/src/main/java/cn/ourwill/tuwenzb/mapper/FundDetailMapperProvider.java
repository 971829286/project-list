package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.FundDetail;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-23 10:28
 **/
public class FundDetailMapperProvider {

    String columns = "id ,order_no,user_id,activity_id,amount,create_time,type,fund_status,finish_time,pay_user";

    public String save (final FundDetail fundDetail){
        return new SQL(){
            {
                INSERT_INTO("fund_detail");
                if (fundDetail.getOrderNo() != null){
                    VALUES("order_no","#{orderNo}");
                }
                if (fundDetail.getUserId() != null){
                    VALUES("user_id","#{userId}");
                }
                if (fundDetail.getActivityId() != null){
                    VALUES("activity_id","#{activityId}");
                }
                if (fundDetail.getAmount() != null){
                    VALUES("amount","#{amount}");
                }
                if(fundDetail.getCreateTime() != null){
                    VALUES("create_time","#{createTime}");
                }
                if(fundDetail.getFinishTime()!= null){
                    VALUES("finish_time","#{finishTime}");
                }
                if (fundDetail.getType() != null){
                    VALUES("type","#{type}");
                }
                if (fundDetail.getFund_status() != null){
                    VALUES("fund_status","#{fund_status}");
                }
                if(fundDetail.getPayUser() != null){
                    VALUES("pay_user","#{payUser}");
                }
                if(fundDetail.getPayWeChatNum() != null){
                    VALUES("pay_wechat_num","#{payWeChatNum}");
                }
                if(fundDetail.getLastBalance() != null){
                    VALUES("last_balance","#{lastBalance}");
                }
                if(fundDetail.getNowBalance() != null){
                    VALUES("now_balance","#{nowBalance}");
                }
            }
        }.toString();
    }

    public String update(final FundDetail fundDetail){
        return new SQL(){
            {
                UPDATE("fund_detail");
                if (fundDetail.getOrderNo() != null){
                    SET("order_no = #{orderNo}");
                }
                if (fundDetail.getUserId() != null){
                    SET("user_id=#{userId}");
                }
                if (fundDetail.getActivityId() != null){
                    SET("activity_id=#{activityId}");
                }
                if (fundDetail.getAmount() != null){
                    SET("amount=#{amount}");
                }
                if(fundDetail.getCreateTime() != null){
                    SET("create_time=#{createTime}");
                }
                if(fundDetail.getFinishTime()!= null){
                    SET("finish_time=#{finishTime}");
                }
                if (fundDetail.getType() != null){
                    SET("type=#{type}");
                }
                if (fundDetail.getFund_status() != null){
                    SET("fund_status=#{fund_status}");
                }
                if(fundDetail.getPayUser() != null){
                    SET("pay_user=#{payUser}");
                }
                if(fundDetail.getPayWeChatNum() != null){
                    SET("pay_wechat_num=#{payWeChatNum}");
                }
                if(fundDetail.getLastBalance() != null){
                    SET("last_balance = #{lastBalance}");
                }
                if(fundDetail.getNowBalance() != null){
                    SET("now_balance = #{nowBalance}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }

    public String getRewardList(Integer activityId){
        return new SQL(){
            {
                SELECT("tt.*,a.avatar,a.nickname, a.id user_id");
                FROM("(select max(pay_user) user_id,pay_wechat_num,SUM(amount)amount  from fund_detail where type = 0 and fund_status = 1 and activity_id = #{activityId} group by pay_wechat_num order by amount LIMIT 10 )tt");
                LEFT_OUTER_JOIN("user a on tt.user_id = a.id  order by tt.amount desc " );
            }
        }.toString();
    }

}
