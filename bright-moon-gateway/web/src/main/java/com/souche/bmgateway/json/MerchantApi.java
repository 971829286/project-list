package com.souche.bmgateway.json;

import com.alibaba.fastjson.JSONObject;
import com.netfinworks.common.lang.StringUtil;
import com.souche.bmgateway.builder.MerchantInfoBuilder;
import com.souche.bmgateway.builder.MerchantSettleBuilder;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.dto.request.UploadPicRequest;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.core.service.dto.MemberSimpleDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.member.builder.BankCardInfoBO;
import com.souche.bmgateway.core.service.merchant.FinanceService;
import com.souche.bmgateway.core.service.merchant.MerchantService;
import com.souche.bmgateway.core.service.merchant.ShopService;
import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBO;
import com.souche.bmgateway.core.util.ParamsValidate;
import com.souche.bmgateway.model.request.MerchantSettledRequest;
import com.souche.bmgateway.vo.MerchantVO;
import com.souche.bmgateway.vo.UpdateMerInfoVO;
import com.souche.map.service.api.location.Area;
import com.souche.map.service.api.location.service.AreaService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.Params;
import com.souche.optimus.core.annotation.Rest;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.web.OptimusRequestMethod;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 商户入驻相关HTTP接口
 *
 * @author chenwj
 * @since 2018/7/17
 */
@View(value = "merchantApi", desc = "商户入驻接口")
@Slf4j(topic = "restApi")
public class MerchantApi implements InitializingBean {

    @Resource
    private MerchantSettleFlowRepository merchantSettleFlowRepository;

    @Resource
    private WalletMemberService walletMemberService;

    @Resource
    private FinanceService financeService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private ShopService shopService;

    @Resource
    private AreaService areaService;

    @Resource
    private StringRedisTemplate redisTemplate;

    private final Integer keepAliveTime = OptimusConfig.getValue("keepAliveTime", Integer.class, 60);

    private final Integer corePoolSize = OptimusConfig.getValue("corePoolSize", Integer.class, 5);

    private Integer maximumPoolSize = OptimusConfig.getValue("maximumPoolSize", Integer.class, 10);

    private ExecutorService executorService;

    /**
     * 根据会员号查询商户号
     *
     * @param memberId 维金id（会员号）
     */
    @Rest(value = "/merchant/queryShopCode", method = OptimusRequestMethod.POST, desc = "根据会员号查询商户号")
    public Result<String> queryShopCode(@Param("memberId") String memberId) {
        log.info("<查询商户信息>根据memberId：{}，查询shopCode，开始", memberId);
        String outMerchantId;
        if (StringUtils.isBlank(memberId)) {
            log.info("<查询商户信息>请求参数memberId为空");
            return MerchantVO.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "<查询商户信息>请求参数memberId为空");
        }
        try {
            MerchantSettleFlow flow = new MerchantSettleFlow();
            flow.setMemberId(memberId);
            flow.setStatus("S");
            List<MerchantSettleFlow> resultList = merchantSettleFlowRepository.selectByMemberId(flow);
            if (resultList == null || resultList.size() == 0) {
                log.info("<查询商户信息>查询结果为空");
                return MerchantVO.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>查询结果为空");
            }
            outMerchantId = resultList.get(0).getOutMerchantId();
            if (StringUtils.isBlank(outMerchantId)) {
                log.info("<查询商户信息>网商商户号为空");
                return MerchantVO.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>网商商户号为空");
            }

        } catch (OptimusExceptionBase e) {
            log.error("<查询商户信息>异常，原因：{}", e.getMessage());
            return MerchantVO.fail(e.getErrCode(), e.getMessage());
        }
        log.info("<查询商户信息>响应->{}", outMerchantId);
        return MerchantVO.success("查询成功", outMerchantId);
    }

    /**
     * 商户入驻申请
     *
     * @param params 商户入驻传参
     */
    @Rest(value = "/merchant/settle", method = OptimusRequestMethod.POST, desc = "商户入驻申请")
    public Result<String> merchantSettle(@Params MerchantSettledRequest params) {
        log.info("<商户入驻申请>开始，请求参数->{}", params.toString());
        try {
            // 前置校验
            Result<String> preResult = preCheck(params);
            if (!preResult.isSuccess()) {
                if (StringUtils.equals(preResult.getCode(), Constants.SUCCESS_CODE)) {
                    return MerchantVO.success(preResult.getMsg(), "");
                }
                return MerchantVO.fail(preResult.getCode(), preResult.getMsg());
            }
            // 查询企业信息
            Result<ShopInfoBO> rsShop = shopService.queryShopInfo(params.getShopCode());
            if (StringUtil.equals(rsShop.getCode(), ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode())) {
                return MerchantVO.fail(rsShop.getCode(), rsShop.getMsg());
            }
            // 查询绑卡信息
            MemberSimpleDTO memberSimpleDTO = new MemberSimpleDTO(params.getMemberId());
            Result<BankCardInfoBO> rsBankInfo = walletMemberService.queryBankCardInfo(new MemberSimpleDTO(params.getMemberId()));
            if (StringUtil.equals(rsBankInfo.getCode(), ErrorCodeEnums.QUERY_BANKCARD_INFO_ERROR.getCode())) {
                return MerchantVO.fail(rsBankInfo.getCode(), rsBankInfo.getMsg());
            }
            // 查询开户许可证
            Result<String> rsAcctOpenLic = financeService.queryAcctOpenLicense(params.getMemberId());
            if (StringUtil.equals(rsAcctOpenLic.getCode(), ErrorCodeEnums.QUERY_ACCOUNT_OPEN_LIC_ERROR.getCode())) {
                return MerchantVO.fail(rsAcctOpenLic.getCode(), rsAcctOpenLic.getMsg());
            }
            // 文件上传+商户入驻（异步）
            settle(rsShop.getData(), rsBankInfo.getData(), rsAcctOpenLic.getData(), params);

        } catch (OptimusExceptionBase e) {
            log.error("<商户入驻申请>入驻异常，原因：{}", e.getMessage());
            return MerchantVO.fail(e.getErrCode(), e.getMessage());
        }
        log.info("<商户入驻申请>申请成功，处理中...");
        return MerchantVO.success("申请处理中", "");
    }

    /**
     * 修改商户信息-结算卡信息
     */
    @Rest(value = "/merchant/updateCardInfo", method = OptimusRequestMethod.POST, desc = "商户信息(结算卡)修改")
    public Result<String> updateMerBankCardInfo(@Params UpdateMerInfoVO req) {
        log.info("<修改商户结算卡信息>请求参数->{}", req);
        try {
            // 参数校验
            ParamsValidate.ParamsValidateResult<UpdateMerInfoVO> validateResult = ParamsValidate.validate(req);
            if (validateResult.hasError()) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), validateResult.getMsgError());
            }
            // 根据memberId查询是否存在已入驻成功
            MerchantSettleFlow flow = new MerchantSettleFlow();
            flow.setMemberId(req.getMerchantId());
            flow.setStatus("S");
            List<MerchantSettleFlow> list = merchantSettleFlowRepository.selectByMemberId(flow);
            if (list == null || list.size() == 0) {
                return Result.fail(ErrorCodeEnums.MERCHANT_NOT_SETTLE.getCode(), "暂无该商户入驻信息");
            }
            req.setMerchantId(list.get(0).getOutMerchantId());
            // 查询企业信息，获取持卡人地址
            Result<String> rsShop = shopService.queryAddress(req.getShopCode());
            if (StringUtil.equals(rsShop.getCode(), ErrorCodeEnums.SYSTEM_ERROR.getCode())) {
                return Result.fail(rsShop.getCode(), rsShop.getMsg());
            }
            // 匹配省市码值
            Area area = areaService.matchArea(req.getBranchProvince(), req.getBranchCity(), "");
            if (area == null || StringUtils.isBlank(area.getProvinceCode()) || StringUtils.isBlank(area.getCityCode())) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "省市码值映射为空");
            }
            // 调用修改接口
            Result<String> result = merchantService.modifyMerBankCardInfo(MerchantInfoBuilder.cardInfoBuild(req, rsShop.getData(), area));
            if (!result.isSuccess()) {
                return Result.fail(result.getCode(), result.getMsg());
            }
        } catch (OptimusExceptionBase e) {
            return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        return Result.success("商户信息(结算卡)修改成功");
    }

    /**
     * 异步处理文件上传和商户入驻
     */
    private void settle(ShopInfoBO shopInfo, BankCardInfoBO bankCardInfo, String acctOpenLicPic, MerchantSettledRequest params) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                log.info("------------------------< asynchronous method start >------------------------");
                MerchantSettleFlow flow = new MerchantSettleFlow();
                try {
                    // 数据落地
                    flow.setShopCode(params.getShopCode());
                    flow.setMemberId(params.getMemberId());
                    flow.setUid(params.getStoreId());
                    flow.setStatus("I");
                    flow.setGtmCreate(new Date());
                    flow.setGtmModified(new Date());
                    flow.setCallBackUrl(params.getCallbackUrl());
                    flow.setCallBackStatus("I");
                    flow.setReturnMsg("");
                    merchantSettleFlowRepository.insert(flow);
                    // 图片上传
                    UploadPicRequest picParam = new UploadPicRequest();
                    picParam.setMemberId(params.getMemberId());
                    picParam.setIdCardPic(shopInfo.getIdCard());
                    picParam.setIdCardBackPic(shopInfo.getIdCardBack());
                    picParam.setBusinessLicensePic(shopInfo.getBusinessLicPic());
                    picParam.setAcctOpenLicensePic(acctOpenLicPic);
                    Result<String> rsUploadPic = merchantService.uploadPic(picParam);
                    JSONObject jsonPic = JSONObject.parseObject(rsUploadPic.getData());
                    // 商户入驻
                    merchantService.merchantSettle(MerchantSettleBuilder.build(shopInfo, bankCardInfo, jsonPic, params.getStoreId()), flow);
                    log.info("------------------------< asynchronous method end >------------------------");
                } catch (Exception e) {
                    flow.setStatus("F");
                    flow.setReturnMsg("系统异常，请重试");
                    flow.setGtmModified(new Date());
                    merchantSettleFlowRepository.update(flow);
                    log.error("商户入驻异步处理异常,会员号:{}", params.getMemberId(), e);
                }
            }
        });
    }

    /**
     * 前置校验，限制重复申请，参数判空等
     *
     * @param params 商户入驻申请总参数
     */
    private Result<String> preCheck(MerchantSettledRequest params) {
        // 访问限制
        if (StringUtils.equals(redisTemplate.opsForValue().get(params.getShopCode()), params.getShopCode())) {
            log.error("<商户入驻申请>商户入驻中，请勿重复请求");
            return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "请勿重复请求");
        }
        redisTemplate.opsForValue().set(params.getShopCode(), params.getShopCode(), 5, TimeUnit.SECONDS);
        // 基础参数校验
        ParamsValidate.ParamsValidateResult<MerchantSettledRequest> validateResult = ParamsValidate.validate(params);
        if (validateResult.hasError()) {
            log.error("<商户入驻申请>入驻失败，原因->{}", validateResult.getMsgError());
            return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), validateResult.getMsgError());
        }
        // 数据库流水记录校验
        List<MerchantSettleFlow> list = merchantSettleFlowRepository.selectStatusNotFail(params.getShopCode());
        if (list.size() > 0) {
            log.error("<商户入驻申请>该商户已入驻成功或申请处理中，请勿重复申请");
            for (MerchantSettleFlow flow : list) {
                if (StringUtils.equals(flow.getStatus(), "S")) {
                    return Result.fail("200", "该商户已入驻成功，请勿重复申请");
                }
            }
            return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "申请处理中，请勿重复申请");
        }
        return Result.success("");
    }

    @Override
    public void afterPropertiesSet() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }

}
