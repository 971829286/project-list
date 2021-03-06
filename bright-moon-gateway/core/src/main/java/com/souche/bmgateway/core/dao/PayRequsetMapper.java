package com.souche.bmgateway.core.dao;

import com.souche.bmgateway.core.domain.PayRequest;
import org.apache.ibatis.annotations.Param;

public interface PayRequsetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    int insert(PayRequest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    int insertSelective(PayRequest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    PayRequest selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PayRequest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pay_request
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PayRequest record);

    /**
     *
     * @param taskCode
     * @param businessDate
     * @return
     */
    PayRequest selectByTaskCode(@Param("taskCode") String taskCode, @Param("businessDate") String businessDate);

    /**
     * 更新版本号
     * @param record
     * @return
     */
    int updateVersionByID(PayRequest record);

    /**
     * 更新状态
     */
    int updateStatusBytaskCode(@Param("taskCode") String taskCode, @Param("businessDate") String businessDate,
                               @Param("status") Integer status);

    /**
     * 按taskcode+businessDateg更新数据
     *
     * @return
     */
    int updateByTaskcode(PayRequest record);

    /**
     * 上一批任务发送时间
     */
    String getLastJobBusinessDate(String businessDate);

}