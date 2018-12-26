package com.souche.bmgateway.core.service.merchant;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;
import com.souche.bmgateway.core.dto.request.UploadPicRequest;
import com.souche.bmgateway.model.request.MerchantSettledRequest;
import com.souche.optimus.core.web.Result;

/**
 * 商户服务
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface MerchantService {

    /**
     * 图片上传
     *
     * @param param 上传图片传参对象
     * @return Result
     */
    Result<String> uploadPic(UploadPicRequest param);

    /**
     * 商户入驻
     *
     * @param merchantSettleRequest 入驻总参数对象
     * @param flow                  流水
     */
    void merchantSettle(MerchantSettleRequest merchantSettleRequest, MerchantSettleFlow flow);

    /**
     * 查询商户入驻结果
     *
     * @param merchantResultQueryRequest 查询商户入驻结果传参对象
     * @return Result
     */
    Result<String> queryMerchantResult(MerchantResultQueryRequest merchantResultQueryRequest);

    /**
     * 商户信息修改-结算卡信息
     *
     * @param json 结算卡信息
     * @return Result
     */
    Result<String> modifyMerBankCardInfo(JSONObject json);
}
