package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.MerchantUploadFlowMapper;
import com.souche.bmgateway.core.domain.MerchantUploadFlow;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.DaoException;
import com.souche.bmgateway.core.repo.MerchantUploadFlowRepository;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;

/**
 * 商户入驻文件上传流水仓库实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Repository
public class MerchantUploadFlowRepositoryImpl implements MerchantUploadFlowRepository {

    @Resource
    private MerchantUploadFlowMapper merchantUploadFlowMapper;

    @Override
    public void insert(MerchantUploadFlow record) throws DaoException {
        int i = merchantUploadFlowMapper.insert(record);
        if (i == 0) {
            throw new DaoException(ErrorCodeEnums.DAO_OPERATION_ERROR, "插入文件上传流水失败");
        }
    }

    @Override
    public void updateByPrimaryKey(MerchantUploadFlow record) throws DaoException{
        int i = merchantUploadFlowMapper.updateByPrimaryKey(record);
        if (i == 0){
            throw new DaoException(ErrorCodeEnums.DAO_OPERATION_ERROR, "更新文件上传流水失败");
        }
    }

    @Override
    public MerchantUploadFlow selectByMemberId(MerchantUploadFlow record) {
        return merchantUploadFlowMapper.selectByMemberId(record);
    }

}
