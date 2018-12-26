package com.souche.bmgateway.builder;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.vo.BankCardVO;
import com.souche.bmgateway.vo.SiteInfoVO;
import com.souche.bmgateway.vo.UpdateMerInfoVO;
import com.souche.map.service.api.location.Area;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户信息修改入参构造器
 *
 * @author chenwj
 * @since 2018/10/12
 */
public class MerchantInfoBuilder {

    public static JSONObject cardInfoBuild(UpdateMerInfoVO req, String address, Area area) {
        // 结算卡
        BankCardVO bankCardParam = new BankCardVO();
        bankCardParam.setBankCardNo(req.getBankCardNo());
        bankCardParam.setBankCode(req.getBankCode());
        bankCardParam.setBankCertName(req.getBankCertName());
        bankCardParam.setAccountType(Constants.ACCOUNT_TYPE);
        bankCardParam.setBranchName(req.getBranchName());
        bankCardParam.setContactLine(req.getContactLine());
        bankCardParam.setBranchProvince(area.getProvinceCode().substring(0, 6));
        bankCardParam.setBranchCity(area.getCityCode().substring(0, 6));
        bankCardParam.setCertType("01");
        bankCardParam.setCertNo(req.getCertNo());
        bankCardParam.setCardHolderAddress(address);
        // 站点信息
        List<SiteInfoVO> siteUrlList = new ArrayList<>();
        siteUrlList.add(new SiteInfoVO(Constants.SITE_TYPE, Constants.SITE_URL));
        JSONObject businessJson = new JSONObject();
        businessJson.put("MerchantId", req.getMerchantId());
        businessJson.put("BankCardParam", bankCardParam.genJsonBase64());
        businessJson.put("ReqMsgId", CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OutTradeNo", "UMC" + CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OnlineMcc", Constants.ONLINE_MCC_NEW);
        businessJson.put("SiteInfo", SiteInfoVO.genJsonBase64(siteUrlList));
        businessJson.put("AlipaySource", OptimusConfig.getValue("souche.alipay.source"));
        return businessJson;
    }

}
