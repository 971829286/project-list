package com.souche.bmgateway.builder;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.dto.request.MerchantSettleRequest;
import com.souche.bmgateway.core.enums.FeeTypeEnum;
import com.souche.bmgateway.core.enums.PayChannelEnum;
import com.souche.bmgateway.core.enums.PrincipalCertTypeEnum;
import com.souche.bmgateway.core.enums.TradeTypeEnum;
import com.souche.bmgateway.core.service.member.builder.BankCardInfoBO;
import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBO;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.vo.BankCardVO;
import com.souche.bmgateway.vo.FeeVO;
import com.souche.bmgateway.vo.MerchantDetailVO;
import com.souche.bmgateway.vo.SiteInfoVO;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户入驻入参构造器
 *
 * @author chenwj
 * @since 2018/7/23
 */
public class MerchantSettleBuilder {

    public static MerchantSettleRequest build(ShopInfoBO shopInfoBO, BankCardInfoBO bankCardInfo, JSONObject jsonPic, String uid) {
        // 商户详情
        MerchantDetailVO merchantDetail = new MerchantDetailVO();
        merchantDetail.setAlias(shopInfoBO.getAlias());
        merchantDetail.setContactMobile(shopInfoBO.getMobilePhone());
        merchantDetail.setContactName(shopInfoBO.getCorporationName());
        merchantDetail.setProvince(shopInfoBO.getProvinceCode());
        merchantDetail.setCity(shopInfoBO.getCityCode());
        merchantDetail.setDistrict(shopInfoBO.getRegionCode());
        merchantDetail.setAddress(shopInfoBO.getAddress());
        merchantDetail.setServicePhoneNo(shopInfoBO.getTelephone());
        merchantDetail.setLegalPerson(shopInfoBO.getLicCompanyName());
        merchantDetail.setPrincipalMobile(shopInfoBO.getMobilePhone());
        merchantDetail.setPrincipalCertType(PrincipalCertTypeEnum.IdentityCard);
        merchantDetail.setPrincipalCertNo(shopInfoBO.getIdentityCode());
        merchantDetail.setPrincipalPerson(shopInfoBO.getCorporationName());
        merchantDetail.setBussAuthNum(shopInfoBO.getCreditCode());
        merchantDetail.setCertPhotoA(jsonPic.getString("idCard"));
        merchantDetail.setCertPhotoB(jsonPic.getString("idCardBack"));
        merchantDetail.setLicensePhoto(jsonPic.getString("businessLic"));
        merchantDetail.setIndustryLicensePhoto(jsonPic.getString("acctOpenLic"));
        // 站点信息
        List<SiteInfoVO> siteUrlList = new ArrayList<>();
        siteUrlList.add(new SiteInfoVO(Constants.SITE_TYPE, Constants.SITE_URL));
        // 手续费列表
        List<FeeVO> feeParamList = new ArrayList<FeeVO>();
        feeParamList.add(new FeeVO(PayChannelEnum.Ali, FeeTypeEnum.T1, Constants.FEE_VALUE));
        feeParamList.add(new FeeVO(PayChannelEnum.WX, FeeTypeEnum.T1, Constants.FEE_VALUE));
        // 清算卡
        BankCardVO bankCardParam = new BankCardVO();
        bankCardParam.setBankCardNo(bankCardInfo.getBankCardNo());
        bankCardParam.setBankCertName(bankCardInfo.getBankCertName());
        bankCardParam.setContactLine(bankCardInfo.getContactLine());
        bankCardParam.setAccountType(Constants.ACCOUNT_TYPE);
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
        String outTradeNo = "MS" + CommonUtil.getToday() + UUIDUtil.getID();
        String reqMsgId = CommonUtil.getToday() + UUIDUtil.getID();
        MerchantSettleRequest merchantSettleRequest = new MerchantSettleRequest();
        merchantSettleRequest.setOutMerchantId(uid);
        merchantSettleRequest.setMerchantName(shopInfoBO.getLicCompanyName());
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
        merchantSettleRequest.setOutTradeNo(outTradeNo);
        merchantSettleRequest.setSupportStage(Constants.NOT_SUPPORT);
        merchantSettleRequest.setPartnerType(Constants.PARTNER_TYPE);
        merchantSettleRequest.setAlipaySource(OptimusConfig.getValue("souche.alipay.source"));
        merchantSettleRequest.setReqMsgId(reqMsgId);
        return merchantSettleRequest;
    }

}
