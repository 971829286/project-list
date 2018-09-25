package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.FundStatistics;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-07-12 18:51
 **/
public class FundStatisticsMapperProvider {

    String columns = "id,user_id,income,surplus,withdraw,today_income";

    public String save (final FundStatistics fundStatistics){
        return new SQL(){
            {
                INSERT_INTO("fund_statistics");
                if (fundStatistics.getUserId() != null){
                    VALUES("user_id","#{userId}");
                }
                if (fundStatistics.getInCome()!=null){
                    VALUES("income","#{inCome}");
                }
                if (fundStatistics.getSurplus()!=null){
                    VALUES("surplus","#{surplus}");
                }
                if (fundStatistics.getWithdraw()!=null){
                    VALUES("withdraw","#{withdraw}");
                }
                if (fundStatistics.getTodayIncome()!=null){
                    VALUES("today_income","#{todayIncome}");
                }

            }
        }.toString();
    }

    public String update(final FundStatistics fundStatistics){
        return new SQL(){
            {
                UPDATE("fund_statistics");
                if (fundStatistics.getUserId() != null){
                    SET("user_id = #{userId}");
                }
                if (fundStatistics.getInCome()!=null){
                    SET("income=#{inCome}");
                }
                if (fundStatistics.getSurplus()!=null){
                    SET("surplus = #{surplus}");
                }
                if (fundStatistics.getWithdraw()!=null){
                    SET("withdraw = #{withdraw}");
                }
                if (fundStatistics.getTodayIncome()!=null){
                    SET("today_income = #{todayIncome}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
