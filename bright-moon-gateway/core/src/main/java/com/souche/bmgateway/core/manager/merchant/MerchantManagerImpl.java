package com.souche.bmgateway.core.manager.merchant;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.dto.request.UploadPhotoRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;
import com.souche.bmgateway.core.open.plat.OpenPlatformHttpClient;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商户服务相关
 *
 * @author chenwj
 * @since 2018/07/13
 */
@Service("merchantManager")
@Slf4j(topic = "manager")
public class MerchantManagerImpl implements MerchantManager {

    /**
     * 开放平台统一地址
     */
    private static final String OPEN_PLATFORM_URL = OptimusConfig.getValue("open.platform.url");

    /**
     * 开放平台图片上传地址
     */
    private static final String OPEN_PLATFORM_UPLOAD_URL = OptimusConfig.getValue("open.platform.upload.url");

    @Resource
    private OpenPlatformHttpClient openPlatformHttpClient;

    /**
     * 商户入驻结果查询
     *
     * @param req 商户入驻接口查询请求报文
     * @return HttpBaseResponse
     */
    @Override
    public HttpBaseResponse queryMerchantResult(MerchantResultQueryRequest req) throws ManagerException {
        req.setFunction(Constants.MYBANK_MERCHANT_RESULT);
        try {
            // 拼装开放平台要求格式
            JSONObject json = new JSONObject();
            json.put("api", req.getFunction());
            JSONObject businessJson = new JSONObject();
            businessJson.put("OrderNo", req.getOrderNo());
            businessJson.put("ReqMsgId", CommonUtil.getToday() + UUIDUtil.getID());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("body", businessJson);
            json.put("data", jsonBody);
            long beginTime = System.currentTimeMillis();
            // 调用开放平台
            HttpBaseResponse resp = openPlatformHttpClient.execute(OPEN_PLATFORM_URL, json.toJSONString());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("<商户入驻结果查询>调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
            if (!resp.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, resp.getMsg() + resp.getData());
            }
            return resp;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    /**
     * 图片上传
     *
     * @param req 图片上传请求报文
     * @return HttpBaseResponse
     */
    @Override
    public HttpBaseResponse uploadPhoto(UploadPhotoRequest req) throws ManagerException {
        req.setFunction(Constants.MYBANK_UPLOAD_PIC);
        try {
            // 拼装开放平台要求格式
            JSONObject json = new JSONObject();
            json.put("api", req.getFunction());
            JSONObject businessJson = new JSONObject();
            businessJson.put("PhotoType", req.getPhotoType());
            businessJson.put("OutTradeNo", req.getOutTradeNo());
            businessJson.put("Picture", req.getPicture());
            businessJson.put("FileName", req.getPictureName());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("body", businessJson);
            json.put("data", jsonBody);
            long beginTime = System.currentTimeMillis();
            // 调用开放平台
            HttpBaseResponse resp = openPlatformHttpClient.executeUpload(OPEN_PLATFORM_UPLOAD_URL, json.toJSONString());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("<图片上传>调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
            if (!resp.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, resp.getMsg() + resp.getData());
            }
            return resp;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    /**
     * 商户入驻申请
     *
     * @param req 商户入驻请求报文
     * @return HttpBaseResponse
     */
    @Override
    public HttpBaseResponse merchantSettle(MerchantSettleRequest req) throws ManagerException {
        req.setFunction(Constants.MYBANK_MERCHANT_REGISTER);
        try {
            // 拼装开放平台要求格式
            JSONObject json = new JSONObject();
            json.put("api", req.getFunction());
            JSONObject businessJson = new JSONObject();
            businessJson.put("OutMerchantId", req.getOutMerchantId());
            businessJson.put("MerchantName", req.getMerchantName());
            businessJson.put("MerchantType", req.getMerchantType());
            businessJson.put("DealType", req.getDealType());
            businessJson.put("SupportPrepayment", req.getSupportPrepayment());
            businessJson.put("SettleMode", req.getSettleMode());
            businessJson.put("Mcc", req.getMcc());
            businessJson.put("OnlineMcc", req.getOnlineMcc());
            businessJson.put("MerchantDetail", req.getMerchantDetail());
            businessJson.put("SiteInfo", req.getSiteInfo());
            businessJson.put("TradeTypeList", req.getTradeTypeList());
            businessJson.put("PayChannelList", req.getPayChannelList());
            businessJson.put("FeeParamList", req.getFeeParamList());
            businessJson.put("BankCardParam", req.getBankCardParam());
            businessJson.put("OutTradeNo", req.getOutTradeNo());
            businessJson.put("SupportStage", req.getSupportStage());
            businessJson.put("PartnerType", req.getPartnerType());
            businessJson.put("AlipaySource", req.getAlipaySource());
            businessJson.put("ReqMsgId", req.getReqMsgId());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("body", businessJson);
            json.put("data", jsonBody);
            long beginTime = System.currentTimeMillis();
            // 调用开放平台
            HttpBaseResponse resp = openPlatformHttpClient.execute(OPEN_PLATFORM_URL, json.toJSONString());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("<商户入驻申请>调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
            if (!resp.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, resp.getMsg() + resp.getData());
            }
            return resp;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

    /**
     * 商户信息修改-结算卡信息
     *
     * @param reqJson 结算卡信息业务报文
     * @return HttpBaseResponse
     */
    @Override
    public HttpBaseResponse modifyMerBankCardInfo(JSONObject reqJson) throws ManagerException {
        try {
            // 请求参数
            JSONObject json = new JSONObject();
            json.put("api", Constants.MYBANK_MODIFY_MERCHANT);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("body", reqJson);
            json.put("data", jsonBody);
            log.info("调用开放平台，请求参数JSON->{}", json.toJSONString());
            long beginTime = System.currentTimeMillis();
            // 调用开放平台
            HttpBaseResponse resp = openPlatformHttpClient.execute(OPEN_PLATFORM_URL, json.toJSONString());
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
            if (!resp.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, resp.getMsg() + resp.getData());
            }
            return resp;
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

}
