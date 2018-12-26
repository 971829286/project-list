package com.souche.bmgateway.core.manager.merchant;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.dto.request.UploadPhotoRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;

/**
 * 商户服务
 *
 * @author chenwj
 * @since 2018/7/23
 */
public interface MerchantManager {

    /**
     * 商户入驻结果查询
     *
     * @param merchantResultQueryRequest
     * @return
     * @throws ManagerException
     */
    HttpBaseResponse queryMerchantResult(MerchantResultQueryRequest merchantResultQueryRequest) throws ManagerException;

    /**
     * 图片上传
     *
     * @param uploadPhotoRequest
     * @return
     * @throws ManagerException
     */
    HttpBaseResponse uploadPhoto(UploadPhotoRequest uploadPhotoRequest) throws ManagerException;

    /**
     * 商户入驻申请
     *
     * @param merchantSettleRequest
     * @return
     * @throws ManagerException
     */
    HttpBaseResponse merchantSettle(MerchantSettleRequest merchantSettleRequest) throws ManagerException;

    /**
     * 商户信息修改-结算卡信息
     *
     * @param reqJson
     * @return
     * @throws ManagerException
     */
    HttpBaseResponse modifyMerBankCardInfo(JSONObject reqJson) throws ManagerException;

}
