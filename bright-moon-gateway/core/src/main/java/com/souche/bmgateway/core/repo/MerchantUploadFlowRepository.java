package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.MerchantUploadFlow;
import com.souche.bmgateway.core.exception.DaoException;

/**
 * 商户入驻文件上传流水仓库
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface MerchantUploadFlowRepository {

    /**
     * 新增
     *
     * @param record
     * @return
     * @throws DaoException
     */
    void insert(MerchantUploadFlow record) throws DaoException;

    /**
     * 更新
     *
     * @param record
     * @return
     * @throws DaoException
     */
    void updateByPrimaryKey(MerchantUploadFlow record) throws DaoException;

    /**
     * 根据memberId和photoType查询流水记录
     *
     * @param record 文件流水总参数
     * @return MerchantUploadFlow
     */
    MerchantUploadFlow selectByMemberId(MerchantUploadFlow record);

}
