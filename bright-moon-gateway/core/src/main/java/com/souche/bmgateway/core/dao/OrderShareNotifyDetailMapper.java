package com.souche.bmgateway.core.dao;

import com.souche.bmgateway.core.domain.OrderShareNotifyDetail;

public interface OrderShareNotifyDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    int insert(OrderShareNotifyDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    int insertSelective(OrderShareNotifyDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    OrderShareNotifyDetail selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OrderShareNotifyDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order_share_notify_detail
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OrderShareNotifyDetail record);
}