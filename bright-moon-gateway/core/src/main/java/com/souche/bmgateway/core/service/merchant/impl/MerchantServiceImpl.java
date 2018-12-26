package com.souche.bmgateway.core.service.merchant.impl;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.domain.MerchantUploadFlow;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.dto.request.UploadPhotoRequest;
import com.souche.bmgateway.core.dto.request.UploadPicRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.MerchantEnums;
import com.souche.bmgateway.core.enums.PicInfoEnums;
import com.souche.bmgateway.core.exception.DaoException;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.merchant.MerchantManager;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.core.repo.MerchantUploadFlowRepository;
import com.souche.bmgateway.core.service.merchant.MerchantService;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.util.UploadUtil;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.Exceptions;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 商户服务实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Service("merchantService")
@Slf4j(topic = "service")
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantUploadFlowRepository merchantUploadFlowRepository;

    @Resource
    private MerchantManager merchantManager;

    @Resource
    private MerchantSettleFlowRepository merchantSettleFlowRepository;

    /**
     * 图片下载后保存的路径
     */
    private static final String DOWNLOAD_FILE_PATH = OptimusConfig.getValue("file.download.path");

    /**
     * 图片上传
     *
     * @param param 上传图片传参对象
     * @return Result
     */
    @Override
    public Result<String> uploadPic(UploadPicRequest param) {
        log.info("图片上传开始,请求参数->{}", param.toString());
        JSONObject jsonObject = new JSONObject();
        try {
            // 1.身份证正面
            Result<String> rsIdCard = upload(param.getMemberId(), param.getIdCardPic(), PicInfoEnums.ID_CARD);
            // 2.身份证反面
            Result<String> rsIdCardBack = upload(param.getMemberId(), param.getIdCardBackPic(), PicInfoEnums.ID_CARD_BACK);
            // 3.营业执照
            Result<String> rsBusLic = upload(param.getMemberId(), param.getBusinessLicensePic(), PicInfoEnums.BUSINESS_LICENSE);
            // 4.开户许可证
            Result<String> rsAcctOpenLic = upload(param.getMemberId(), param.getAcctOpenLicensePic(), PicInfoEnums.ACCT_OPEN_LICENSE);
            // 拼接返回字段
            jsonObject.put("idCard", rsIdCard.getData());
            jsonObject.put("idCardBack", rsIdCardBack.getData());
            jsonObject.put("businessLic", rsBusLic.getData());
            jsonObject.put("acctOpenLic", rsAcctOpenLic.getData());

        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(e.getErrCode(), e.getMessage());
        }
        log.info("图片上传完成,响应->{}", jsonObject.toJSONString());
        return Result.success(jsonObject.toJSONString());
    }

    /**
     * 商户入驻
     *
     * @param req  入驻总参数对象
     * @param flow 流水
     */
    @Override
    public void merchantSettle(MerchantSettleRequest req, MerchantSettleFlow flow) {
        log.info("商户入驻开始,请求参数->{}", req.toString());
        try {
            // 数据落地
            flow.setOutTradeNo(req.getOutTradeNo());
            flow.setReqInfo(req.toString());
            flow.setMerchantName(req.getMerchantName());
            flow.setReqMsgId(req.getReqMsgId());
            merchantSettleFlowRepository.update(flow);
            // 调用开放平台
            HttpBaseResponse response = merchantManager.merchantSettle(req);
            JSONObject jsonResp = JSONObject.parseObject(response.getData().get("RespInfo").toString());
            if (!StringUtils.equals(jsonResp.getString(Constants.RESULT_STATUS), Constants.MYBANK_SUCCESS_FLAG)) {
                // 更新返回错误信息
                flow.setReturnMsg(jsonResp.getString("ResultMsg"));
                flow.setGtmModified(new Date());
                flow.setStatus("F");
                merchantSettleFlowRepository.update(flow);
                return;
            }
            String orderNo = response.getData().get("OrderNo").toString();
            // 成功，记录请求单号
            flow.setStatus("P");
            flow.setMybankOrderNo(orderNo);
            flow.setGtmModified(new Date());
            merchantSettleFlowRepository.update(flow);
            log.info("入驻申请成功,请求单号->{}", orderNo);

        } catch (ManagerException e) {
            throw Exceptions.fault(e.getCode(), e.getMessage());
        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(e.getErrCode(), e.getMessage());
        }
    }

    /**
     * 查询商户入驻结果
     *
     * @param request 查询商户入驻结果传参对象
     * @return Result
     */
    @Override
    public Result<String> queryMerchantResult(MerchantResultQueryRequest request) {
        log.info("<查询商户入驻结果>开始，请求参数 ->{}", request.toString());
        HttpBaseResponse resp;
        try {
            // 调用开放平台
            resp = merchantManager.queryMerchantResult(request);
            JSONObject jsonResp = JSONObject.parseObject(resp.getData().get("RespInfo").toString());
            // 查询失败
            if (!StringUtils.equals(jsonResp.getString(MerchantEnums.RESULT_STATUS.getMCode()),
                    MerchantEnums.RESULT_STATUS_SUCCESS.getMCode())) {
                log.error("<查询商户入驻结果>查询失败，" + jsonResp.getString("ResultMsg"));
                return Result.fail(ErrorCodeEnums.QUERY_MER_RESULT_ERROR.getCode(),
                        "<查询商户入驻结果>查询失败，" + jsonResp.getString("ResultMsg"));
            }

        } catch (ManagerException e) {
            throw Exceptions.fault(ErrorCodeEnums.INVOKE_OPEN_PLATFORM_ERROR.getCode(), "<查询商户入驻结果>调用开放平台，失败");
        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(e.getErrCode(), e.getMessage());
        }
        log.info("<查询商户入驻结果>响应 ->{}", resp.getData().toString());
        return Result.success(resp.getData().toString());
    }

    /**
     * 商户信息修改-结算卡信息
     *
     * @param json 结算卡信息
     * @return Result
     */
    @Override
    public Result<String> modifyMerBankCardInfo(JSONObject json) {
        log.info("修改商户结算卡信息,请求参数->{}", json);
        HttpBaseResponse resp;
        try {
            resp = merchantManager.modifyMerBankCardInfo(json);
            JSONObject jsonResp = JSONObject.parseObject(resp.getData().get("RespInfo").toString());
            if (!StringUtils.equals(jsonResp.getString(Constants.RESULT_STATUS), Constants.MYBANK_SUCCESS_FLAG)) {
                return Result.fail(ErrorCodeEnums.MERCHANT_SETTLE_ERROR.getCode(),
                        "失败原因：" + jsonResp.getString("ResultMsg"));
            }
        } catch (ManagerException e) {
            throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), ErrorCodeEnums.SYSTEM_ERROR.getMessage());
        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(e.getErrCode(), e.getMessage());
        }
        log.info("修改商户结算卡信息->SUCCESS!");
        return Result.success("");
    }

    /**
     * 上传图片公用方法
     *
     * @param memberId 会员号
     * @param pic      图片下载链接
     * @param enums    枚举
     * @return Result
     */
    private Result<String> upload(String memberId, String pic, PicInfoEnums enums) {
        log.info("上传图片，开始...");
        String savePath = DOWNLOAD_FILE_PATH + memberId + "/";
        try {
            // 0.先校验数据库中是否存在该商户的某个照片类型的流水，若存在直接返回photo_url
            MerchantUploadFlow flow = new MerchantUploadFlow();
            flow.setMerchantId(memberId);
            flow.setPhotoType(enums.getPhotoType());
            MerchantUploadFlow result = merchantUploadFlowRepository.selectByMemberId(flow);
            if (result == null) {
                // 1.下载图片二进制流，转成base64字符串
                UploadUtil.download(pic, enums.getFileName(), savePath);
                UploadUtil.compressPicture(savePath, enums.getFileName());
                String photo = UploadUtil.getImgStr(savePath + enums.getFileName());
                String outTradeNo = "UP" + CommonUtil.getToday() + UUIDUtil.getID();
                // 2、数据落地
                flow.setOutTradeNo(outTradeNo);
                flow.setStatus("F");
                flow.setGtmCreate(new Date());
                flow.setGtmModified(new Date());
                merchantUploadFlowRepository.insert(flow);
                // 3、调用开放平台
                UploadPhotoRequest req = new UploadPhotoRequest();
                req.setOutTradeNo(outTradeNo);
                req.setPhotoType(enums.getPhotoType());
                req.setPictureName(enums.getFileName());
                req.setPicture(photo);
                HttpBaseResponse httpBaseResponse = merchantManager.uploadPhoto(req);
                // 4、上传成功，数据更新
                flow.setStatus("S");
                flow.setRespInfo(httpBaseResponse.getData().toString());
                flow.setGtmModified(new Date());
                flow.setPhotoUrl(httpBaseResponse.getData().get("PhotoUrl").toString());
                merchantUploadFlowRepository.updateByPrimaryKey(flow);
                log.info("上传成功...更新流水状态->{}", flow.toString());
                return Result.success(httpBaseResponse.getData().get("PhotoUrl").toString());
            } else {
                log.info("无需上传，数据库取数据，memberId->{}，photoType->{}，photoUrl->{}", memberId, enums.getPhotoType(), result.getPhotoUrl());
                return Result.success(result.getPhotoUrl());
            }

        } catch (ManagerException e) {
            throw Exceptions.fault(ErrorCodeEnums.INVOKE_OPEN_PLATFORM_ERROR.getCode(), ErrorCodeEnums.INVOKE_OPEN_PLATFORM_ERROR.getMessage());
        } catch (DaoException e) {
            throw Exceptions.fault(ErrorCodeEnums.DAO_OPERATION_ERROR.getCode(), ErrorCodeEnums.DAO_OPERATION_ERROR.getMessage());
        } catch (Exception e) {
            throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), ErrorCodeEnums.SYSTEM_ERROR.getMessage());
        }
    }

}
