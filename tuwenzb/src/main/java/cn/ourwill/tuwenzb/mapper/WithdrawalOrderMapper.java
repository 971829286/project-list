package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.WithdrawalOrder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

/**
 *@Author zhaoqing
 * @Time 2018/5/24 0010 18:03
 * @Version1.0
 */
@Repository
public interface WithdrawalOrderMapper extends IBaseMapper<WithdrawalOrder>{

    String columns = "id,partner_trade_no,user_id,openid,user_name,request_amount,practical_amount,create_ip,payment_no,payment_time,transfer_time,transfer_status,failed_reason,description";

    @InsertProvider(type = WithdrawalOrderMapperProvider.class ,method = "save")
    @Options(useGeneratedKeys = true , keyProperty = "id")
    Integer save(WithdrawalOrder withdrawalOrder);

    @UpdateProvider(type = WithdrawalOrderMapperProvider.class ,method = "update")
    Integer update(WithdrawalOrder withdrawalOrder);



}
