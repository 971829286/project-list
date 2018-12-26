package com.souche.bmgateway.core.dao;

import com.souche.bmgateway.core.domain.TradeSceneConfDO;
import org.apache.ibatis.annotations.Param;

public interface TradeSceneConfDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    int insert(TradeSceneConfDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    int insertSelective(TradeSceneConfDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    TradeSceneConfDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TradeSceneConfDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trade_scene_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TradeSceneConfDO record);

    /**
     * 根据业务产品编码查询配置信息
     *
     * @param bizProductCode
     * @return
     */
    TradeSceneConfDO queryByBizProductCode(@Param(value = "bizProductCode") String bizProductCode);
}