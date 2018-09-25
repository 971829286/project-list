package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.WithdrawalOrder;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-24 18:01
 **/
public class WithdrawalOrderMapperProvider {

    String columns = "id,partner_trade_no,user_id,openid,user_name,request_amount,practical_amount,create_ip,payment_no,payment_time,transfer_time," +
            "transfer_status,failed_reason,description";

    public String save(final WithdrawalOrder withdrawalOrder){
        return new SQL(){
            {
                INSERT_INTO("withdrawal_order");
                if (withdrawalOrder.getPartnerTradeNo() != null){
                    VALUES("partner_trade_no","#{partnerTradeNo}");
                }
                if (withdrawalOrder.getOpenId() != null){
                    VALUES("openid","#{openId}");
                }
                if (withdrawalOrder.getUserId() != null){
                    VALUES("user_id","#{userId}");
                }
                if (withdrawalOrder.getUserName() != null){
                    VALUES("user_name","#{userName}");
                }
                if (withdrawalOrder.getRequestAmount() != null){
                    VALUES("request_amount","#{requestAmount}");
                }
                if (withdrawalOrder.getPracticalAmount() != null){
                    VALUES("practical_amount","#{practicalAmount}");
                }
                if (withdrawalOrder.getCreateIp() != null){
                    VALUES("create_ip","#{createIp}");
                }
                if (withdrawalOrder.getPaymentNo() != null){
                    VALUES("payment_no","#{paymentNo}");
                }
                if (withdrawalOrder.getPaymentTime()!= null){
                    VALUES("payment_time","#{paymentTime}");
                }
                if (withdrawalOrder.getTransferTime() != null){
                    VALUES("transfer_time","#{transferTime}");
                }
                if (withdrawalOrder.getTransferStatus() != null){
                    VALUES("transfer_status","#{transferStatus}");
                }
                if(withdrawalOrder.getFailedReason() != null){
                    VALUES("failed_reason","#{failedReason}");
                }
                if (withdrawalOrder.getDescription() != null){
                    VALUES("description","#{description}");
                }
            }
        }.toString();
    }

    public String update(final WithdrawalOrder withdrawalOrder){
        return new SQL(){
            {
                UPDATE("withdrawal_order");
                if (withdrawalOrder.getPartnerTradeNo() != null){
                    SET("partner_trade_no=#{partnerTradeNo}");
                }
                if (withdrawalOrder.getUserId() != null){
                    SET("user_id=#{userId}");
                }
                if (withdrawalOrder.getOpenId() != null){
                    SET("openid=#{openId}");
                }
                if (withdrawalOrder.getUserName() != null){
                    SET("user_name=#{userName}");
                }
                if (withdrawalOrder.getRequestAmount() != null){
                    SET("request_amount=#{requestAmount}");
                }
                if (withdrawalOrder.getPracticalAmount() != null){
                    SET("practical_amount=#{practicalAmount}");
                }
                if (withdrawalOrder.getCreateIp() != null){
                    SET("create_ip=#{createIp}");
                }
                if (withdrawalOrder.getPaymentNo() != null){
                    SET("payment_no=#{paymentNo}");
                }
                if (withdrawalOrder.getPaymentTime()!= null){
                    SET("payment_time=#{paymentTime}");
                }
                if (withdrawalOrder.getTransferTime() != null){
                    SET("transfer_time=#{transferTime}");
                }
                if (withdrawalOrder.getTransferStatus() != null){
                    SET("transfer_status=#{transferStatus}");
                }
                if(withdrawalOrder.getFailedReason() != null){
                    SET("failed_reason=#{failedReason}");
                }
                if (withdrawalOrder.getDescription() != null){
                    SET("description=#{description}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
