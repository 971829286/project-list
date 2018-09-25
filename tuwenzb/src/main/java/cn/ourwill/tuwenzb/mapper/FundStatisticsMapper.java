package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.FundStatistics;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
/**
 * @Description: 收益统计
 * @Author: zhaoqing
 * @Date: 2018/7/12 ❤❤ 19:01
 */
@Repository
public interface FundStatisticsMapper extends IBaseMapper<FundStatistics> {

    String columns = "id,user_id,income,surplus,withdraw,today_income";

    @InsertProvider(type = FundStatisticsMapperProvider.class ,method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(FundStatistics fundStatistics);

    @UpdateProvider(type = FundStatisticsMapperProvider.class ,method = "update")
    Integer update(FundStatistics fundStatistics);

    @Select("select "+columns+" from fund_statistics where user_id = #{userId}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "income",property = "inCome"),
            @Result( column = "surplus",property = "surplus"),
            @Result( column = "withdraw",property = "withdraw"),
            @Result( column = "today_income",property = "todayIncome")
    })
    FundStatistics selectByUserId(@Param("userId") Integer userId);
}
