package com.souche.bmgateway.core.dao;


import com.souche.bmgateway.core.domain.MerchantUploadFlow;

public interface MerchantUploadFlowMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MerchantUploadFlow record);

    int insertSelective(MerchantUploadFlow record);

    MerchantUploadFlow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantUploadFlow record);

    int updateByPrimaryKeyWithBLOBs(MerchantUploadFlow record);

    int updateByPrimaryKey(MerchantUploadFlow record);

    /**
     * 根据memberId和photoType查询流水记录
     *
     * @param record 文件流水总参数
     * @return MerchantUploadFlow
     */
    MerchantUploadFlow selectByMemberId(MerchantUploadFlow record);
}