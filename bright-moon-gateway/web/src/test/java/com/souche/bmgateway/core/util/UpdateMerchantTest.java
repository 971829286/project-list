package com.souche.bmgateway.core.util;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.open.plat.OpenPlatformHttpClient;
import com.souche.bmgateway.vo.BankCardVO;
import com.souche.bmgateway.vo.SiteInfoVO;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商户信息修改
 *
 * @author chenwj
 * @since 2018/8/21
 */
@Slf4j
public class UpdateMerchantTest extends BaseTest {

    /**
     * 开放平台统一地址
     */
    private static final String OPEN_PLATFORM_URL = OptimusConfig.getValue("open.platform.url");

    @Resource
    private OpenPlatformHttpClient openPlatformHttpClient;

    @Test
    public void updateBankCard() {
        // 结算卡
        BankCardVO bankCardParam = new BankCardVO();
        bankCardParam.setBankCardNo("50001040043052500213");
        bankCardParam.setBankCode("中国建设银行");
        bankCardParam.setBankCertName("重庆中汽西南本色汽车有限公司");
        bankCardParam.setAccountType(Constants.ACCOUNT_TYPE);
        bankCardParam.setBranchName("中国建设银行股份有限公司重庆两江汽博支行");
        bankCardParam.setContactLine("105653006148");
        bankCardParam.setBranchProvince("500000");
        bankCardParam.setBranchCity("500100");
        bankCardParam.setCertType("01");
        bankCardParam.setCertNo("510212197105193514");
        bankCardParam.setCardHolderAddress("重庆市两江新区金渝大道88号附5号");
        // 站点信息
        List<SiteInfoVO> siteUrlList = new ArrayList<>();
        siteUrlList.add(new SiteInfoVO(Constants.SITE_TYPE, Constants.SITE_URL));
        // 请求参数
        JSONObject json = new JSONObject();
        json.put("api", Constants.MYBANK_MODIFY_MERCHANT);
        JSONObject businessJson = new JSONObject();
        businessJson.put("MerchantId", "226801000006114808944");
        businessJson.put("BankCardParam", bankCardParam.genJsonBase64());
        businessJson.put("ReqMsgId", CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OutTradeNo", "UM" + CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OnlineMcc", Constants.ONLINE_MCC_NEW);
        businessJson.put("SiteInfo", SiteInfoVO.genJsonBase64(siteUrlList));
        businessJson.put("AlipaySource", OptimusConfig.getValue("souche.alipay.source"));
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("body", businessJson);
        json.put("data", jsonBody);
        log.info("调用开放平台，请求参数JSON->{}", json.toJSONString());
        long beginTime = System.currentTimeMillis();
        // 调用开放平台
        HttpBaseResponse resp = openPlatformHttpClient.execute(OPEN_PLATFORM_URL, json.toJSONString());
        long consumeTime = System.currentTimeMillis() - beginTime;
        log.info("调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
    }

    @Test
    public void updateBankCard2() {
        // 结算卡
        BankCardVO bankCardParam = new BankCardVO();
        bankCardParam.setBankCardNo("5440200001819100036008");
        bankCardParam.setBankCode("华夏银行");
        bankCardParam.setBankCertName("重庆丰誉汽车有限公司");
        bankCardParam.setAccountType(Constants.ACCOUNT_TYPE);
        bankCardParam.setBranchName("华夏银行股份有限公司重庆北部新区支行");
        bankCardParam.setContactLine("304653041922");
        bankCardParam.setBranchProvince("500000");
        bankCardParam.setBranchCity("500100");
        bankCardParam.setCertType("01");
        bankCardParam.setCertNo("210105196608211417");
        bankCardParam.setCardHolderAddress("重庆市北部新区金渝大道99号汽博中心C区丰誉三菱店");
        // 站点信息
        List<SiteInfoVO> siteUrlList = new ArrayList<>();
        siteUrlList.add(new SiteInfoVO(Constants.SITE_TYPE, Constants.SITE_URL));
        // 请求参数
        JSONObject json = new JSONObject();
        json.put("api", Constants.MYBANK_MODIFY_MERCHANT);
        JSONObject businessJson = new JSONObject();
        businessJson.put("MerchantId", "226801000006114371022");
        businessJson.put("BankCardParam", bankCardParam.genJsonBase64());
        businessJson.put("ReqMsgId", CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OutTradeNo", "UM" + CommonUtil.getToday() + UUIDUtil.getID());
        businessJson.put("OnlineMcc", Constants.ONLINE_MCC_NEW);
        businessJson.put("SiteInfo", SiteInfoVO.genJsonBase64(siteUrlList));
        businessJson.put("AlipaySource", OptimusConfig.getValue("souche.alipay.source"));
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("body", businessJson);
        json.put("data", jsonBody);
        log.info("调用开放平台，请求参数JSON->{}", json.toJSONString());
        long beginTime = System.currentTimeMillis();
        // 调用开放平台
        HttpBaseResponse resp = openPlatformHttpClient.execute(OPEN_PLATFORM_URL, json.toJSONString());
        long consumeTime = System.currentTimeMillis() - beginTime;
        log.info("调用开放平台，耗时:{} (ms); 响应结果:{} ", consumeTime, resp.toString());
    }

}
