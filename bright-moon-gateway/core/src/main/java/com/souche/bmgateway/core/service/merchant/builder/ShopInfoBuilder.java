package com.souche.bmgateway.core.service.merchant.builder;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.enums.ShopInfoEnums;
import com.souche.map.service.api.location.Area;
import org.apache.commons.lang3.StringUtils;

/**
 * 商户相关构造
 *
 * @author chenwj
 * @since 2018/8/23
 */
public class ShopInfoBuilder {

    public static ShopInfoBO buildReqInfo(Area area, JSONObject json, JSONObject shopJson) {
        ShopInfoBO bo = new ShopInfoBO();
        bo.setIdCard(json.getString(ShopInfoEnums.ID_CARD.getValue()));
        bo.setIdCardBack(json.getString(ShopInfoEnums.ID_CARD_BACK.getValue()));
        bo.setBusinessLicPic(json.getString(ShopInfoEnums.BUSINESS_LIC.getValue()));
        bo.setMobilePhone(shopJson.getString(ShopInfoEnums.PHONE.getValue()));
        bo.setTelephone(shopJson.getString(ShopInfoEnums.ADDRESS_CALL.getValue()).replace("-", ""));
        bo.setLicCompanyName(json.getString(ShopInfoEnums.LIC_COMPANY_NAME.getValue()));
        bo.setCorporationName(json.getString(ShopInfoEnums.CORPORATION_NAME.getValue()));
        bo.setIdentityCode(json.getString(ShopInfoEnums.IDENTITY_CODE.getValue()));
        if (StringUtils.isNotBlank(json.getString(ShopInfoEnums.CREDIT_CODE.getValue()))) {
            bo.setCreditCode(json.getString(ShopInfoEnums.CREDIT_CODE.getValue()));
        } else {
            bo.setCreditCode(json.getString(ShopInfoEnums.BUSINESS_LIC_CODE.getValue()));
        }
        bo.setProvinceCode(area.getProvinceCode().substring(0, 6));
        bo.setCityCode(area.getCityCode().substring(0, 6));
        bo.setRegionCode(area.getCountyCode().substring(0, 6));
        bo.setAddress(shopJson.getString(ShopInfoEnums.PLACE.getValue()));
        bo.setAlias(shopJson.getString(ShopInfoEnums.ALIAS.getValue()));
        return bo;
    }

}
