package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.MerchantSettleFlowMapper;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.DaoException;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商户入驻流水仓库实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Repository
public class MerchantSettleFlowRepositoryImpl implements MerchantSettleFlowRepository {

    @Resource
    private MerchantSettleFlowMapper merchantSettleFlowMapper;

    @Override
    public List<MerchantSettleFlow> selectByDay() {
        return merchantSettleFlowMapper.selectByDay();
    }

    @Override
    public void insert(MerchantSettleFlow record) {
        merchantSettleFlowMapper.insert(record);
    }

    @Override
    public void update(MerchantSettleFlow record) {
        merchantSettleFlowMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public MerchantSettleFlow selectByOrderNo(MerchantSettleFlow record) {
        return merchantSettleFlowMapper.selectByOrderNo(record);
    }

    @Override
    public List<MerchantSettleFlow> selectByMemberId(MerchantSettleFlow record) {
        return merchantSettleFlowMapper.selectByMemberId(record);
    }

    @Override
    public MerchantSettleFlow selectByMemberIdLimit(MerchantSettleFlow record) {
        return merchantSettleFlowMapper.selectByMemberIdLimit(record);
    }

    @Override
    public List<MerchantSettleFlow> selectStatusNotFail(String shopCode) {
        MerchantSettleFlow record = new MerchantSettleFlow();
        record.setShopCode(shopCode);
        return merchantSettleFlowMapper.selectStatusNotFail(record);
    }

}
