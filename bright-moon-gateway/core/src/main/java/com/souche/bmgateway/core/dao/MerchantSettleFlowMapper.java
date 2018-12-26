package com.souche.bmgateway.core.dao;

import com.souche.bmgateway.core.domain.MerchantSettleFlow;

import java.util.List;

public interface MerchantSettleFlowMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MerchantSettleFlow record);

    int insertSelective(MerchantSettleFlow record);

    MerchantSettleFlow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantSettleFlow record);

    int updateByPrimaryKeyWithBLOBs(MerchantSettleFlow record);

    int updateByPrimaryKey(MerchantSettleFlow record);

    /**
     * 获取24小时内的处理中的商户入驻记录
     *
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectByDay();

    /**
     * 根据请求单号查询处理中的流水
     *
     * @param record 商户入驻申请总参数
     * @return MerchantSettleFlow
     */
    MerchantSettleFlow selectByOrderNo(MerchantSettleFlow record);

    /**
     * 根据会员号查询处理中的流水
     *
     * @param record 商户入驻申请总参数
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectByMemberId(MerchantSettleFlow record);

    /**
     * 根据会员号查询最新一条的流水
     *
     * @param record 商户入驻申请总参数
     * @return MerchantSettleFlow
     */
    MerchantSettleFlow selectByMemberIdLimit(MerchantSettleFlow record);

    /**
     * 查询入驻成功或者入驻中的流水
     *
     * @param record 商户入驻申请总参数
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectStatusNotFail(MerchantSettleFlow record);

}