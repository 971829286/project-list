package cn.ourwill.tuwenzb.entity;

import lombok.Data;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-07-12 18:45
 **/
@Data
public class FundStatistics {

    /**
     * ID
     */
    private Integer id ;
    /**
     * 用户
     */
    private Integer userId;
    /**
     *总收益
     */
    private Integer inCome;
    /**
     *余额
     */
    private Integer surplus;

    /**
     *最大可提现金额
     */
    private Integer maxWithdraw;
    /**
     *已提现金额
     */
    private Integer withdraw;
    /**
     *今日收益
     */
    private Integer todayIncome;
}
