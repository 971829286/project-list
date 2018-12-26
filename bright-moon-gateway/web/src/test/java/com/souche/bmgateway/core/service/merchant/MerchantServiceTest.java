package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.dto.request.MerchantResultQueryRequest;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.FeeTypeEnum;
import com.souche.bmgateway.core.enums.PayChannelEnum;
import com.souche.bmgateway.core.enums.PrincipalCertTypeEnum;
import com.souche.bmgateway.core.enums.TradeTypeEnum;
import com.souche.bmgateway.core.manager.merchant.MerchantManager;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.vo.BankCardVO;
import com.souche.bmgateway.vo.FeeVO;
import com.souche.bmgateway.vo.MerchantDetailVO;
import com.souche.bmgateway.vo.SiteInfoVO;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.core.web.Result;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商户服务
 *
 * @author chenwj
 * @since 2018/7/26
 */
public class MerchantServiceTest extends BaseTest {

    @Resource
    private MerchantManager merchantManager;

    @Resource
    private MerchantSettleFlowRepository repository;

    @Resource
    private MerchantService merchantService;

    @Test
    public void uploadTest() throws Exception {
        // 商户详情
        MerchantDetailVO merchantDetail = new MerchantDetailVO();
        merchantDetail.setAlias("商户别名");
        merchantDetail.setContactMobile("18609320193");
        merchantDetail.setContactName("陈一");
        merchantDetail.setProvince("330000");
        merchantDetail.setCity("330100");
        merchantDetail.setDistrict("330106");
        merchantDetail.setAddress("余杭区五常大道175号");
        merchantDetail.setServicePhoneNo("01040304230");
        merchantDetail.setLegalPerson("陈一");
        merchantDetail.setPrincipalMobile("18609320193");
        merchantDetail.setPrincipalCertType(PrincipalCertTypeEnum.IdentityCard);
        merchantDetail.setPrincipalCertNo("330327199311301710");
        merchantDetail.setPrincipalPerson("陈一");
        merchantDetail.setBussAuthNum("330327199311301710");
        merchantDetail.setCertPhotoA("c4b7d98d-49d0-4617-a526-5bffbba13718");
        merchantDetail.setCertPhotoB("20417170-4bcc-47e8-9748-a9ea564a4032");
        merchantDetail.setLicensePhoto("aebfe095-5f81-4476-92d2-f32f7b04a040");
        merchantDetail.setIndustryLicensePhoto("d51cf71f-1330-4d76-ab8c-d571f6df6a60");

        // 站点信息
        List<SiteInfoVO> siteUrlList = new ArrayList<>();
        siteUrlList.add(new SiteInfoVO(Constants.SITE_TYPE, Constants.SITE_URL));

        // 手续费列表
        List<FeeVO> feeParamList = new ArrayList<FeeVO>();
        feeParamList.add(new FeeVO(PayChannelEnum.Ali, FeeTypeEnum.T1, Constants.FEE_VALUE));
        feeParamList.add(new FeeVO(PayChannelEnum.WX, FeeTypeEnum.T1, Constants.FEE_VALUE));
        //feeParamList.add(new FeeParam(PayChannelEnum.AliOnline, FeeTypeEnum.T1, Constants.FEE_VALUE));

        // 清算卡
        BankCardVO bankCardParam = new BankCardVO();
        bankCardParam.setBankCardNo("6228480328468304279");
        bankCardParam.setBankCertName("陈一");
        bankCardParam.setAccountType(Constants.ACCOUNT_TYPE);
        bankCardParam.setContactLine("103100000026");

        // 支持交易类型列表
        List<String> tradeTypeEnumList = new ArrayList<>();
        tradeTypeEnumList.add(TradeTypeEnum.Forward.getTradeCode());
        tradeTypeEnumList.add(TradeTypeEnum.Backward.getTradeCode());
        tradeTypeEnumList.add(TradeTypeEnum.Refund.getTradeCode());
        tradeTypeEnumList.add(TradeTypeEnum.SCAN.getTradeCode());

        // 支持支付渠道列表
        List<String> payChannelEnumList = new ArrayList<>();
        payChannelEnumList.add(PayChannelEnum.Ali.getChnCode());
        payChannelEnumList.add(PayChannelEnum.WX.getChnCode());

        // 总参数
        MerchantSettleRequest merchantSettleRequest = new MerchantSettleRequest();
        merchantSettleRequest.setOutMerchantId("20180719187236136");
        merchantSettleRequest.setMerchantName("陈一");
        merchantSettleRequest.setMerchantType(Constants.MERCHANT_TYPE);
        merchantSettleRequest.setDealType(Constants.DEAL_TYPE);
        merchantSettleRequest.setSupportPrepayment(Constants.NOT_SUPPORT);
        merchantSettleRequest.setSettleMode(Constants.SETTLE_MODE);
        merchantSettleRequest.setMcc(Constants.MCC);
        merchantSettleRequest.setOnlineMcc(Constants.ONLINE_MCC);
        merchantSettleRequest.setMerchantDetail(merchantDetail.genJsonBase64());
        merchantSettleRequest.setSiteInfo(SiteInfoVO.genJsonBase64(siteUrlList));
        merchantSettleRequest.setTradeTypeList(CommonUtil.listToString(tradeTypeEnumList, ','));
        merchantSettleRequest.setPayChannelList(CommonUtil.listToString(payChannelEnumList, ','));
        merchantSettleRequest.setFeeParamList(FeeVO.genJsonBase64(feeParamList));
        merchantSettleRequest.setBankCardParam(bankCardParam.genJsonBase64());
        merchantSettleRequest.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        merchantSettleRequest.setSupportStage(Constants.NOT_SUPPORT);
        merchantSettleRequest.setPartnerType(Constants.PARTNER_TYPE);
        merchantSettleRequest.setAlipaySource("");
        merchantSettleRequest.setReqMsgId(CommonUtil.getToday() + UUIDUtil.getID());
        // 落地
        MerchantSettleFlow flow = new MerchantSettleFlow();
        flow.setOutTradeNo(merchantSettleRequest.getOutTradeNo());
        flow.setShopCode(merchantSettleRequest.getOutTradeNo());
        flow.setMemberId(merchantSettleRequest.getOutTradeNo());
        flow.setUid(merchantSettleRequest.getOutTradeNo());
        flow.setReqInfo(merchantSettleRequest.toString());
        flow.setMerchantName(merchantSettleRequest.getMerchantName());
        flow.setGtmCreate(new Date());
        flow.setGtmModified(new Date());
        flow.setStatus("F");
        flow.setReqMsgId(merchantSettleRequest.getReqMsgId());
        repository.insert(flow);
        // 调用开放平台
        HttpBaseResponse responseDTO = merchantManager.merchantSettle(merchantSettleRequest);
        Assert.assertTrue(responseDTO.isSuccess());
    }

    /**
     * 商户入驻结果查询
     */
    @Test
    public void queryMerchantResultTest() {
        MerchantResultQueryRequest request = new MerchantResultQueryRequest();
        request.setOrderNo("2018072311150710010000000000000000142585");
        Result<String> rs = merchantService.queryMerchantResult(request);
        Assert.assertTrue(rs.isSuccess());
    }

}
