package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.exception.DaoException;

import java.util.List;

/**
 * 商户入驻流水仓库
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface MerchantSettleFlowRepository {

    /**
     * 获取24小时内的处理中的商户入驻记录
     *
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectByDay();

    /**
     * 新增
     *
     * @param record 商户入驻申请总参数
     */
    void insert(MerchantSettleFlow record);

    /**
     * 更新
     *
     * @param record 商户入驻申请总参数
     * @throws DaoException 数据库操作异常
     */
    void update(MerchantSettleFlow record);

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
     * @param merchantSettleFlow 商户入驻申请总参数
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectByMemberId(MerchantSettleFlow merchantSettleFlow);

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
     * @param shopCode 内部商户号
     * @return List<MerchantSettleFlow>
     */
    List<MerchantSettleFlow> selectStatusNotFail(String shopCode);

}
