package com.souche.bmgateway.core.service.merchant.impl;

import com.alibaba.fastjson.JSONObject;
import com.netfinworks.common.lang.StringUtil;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.ShopInfoEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.shop.ShopManager;
import com.souche.bmgateway.core.service.merchant.ShopService;
import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBO;
import com.souche.bmgateway.core.service.merchant.builder.ShopInfoBuilder;
import com.souche.map.service.api.common.CodeTypeEnum;
import com.souche.map.service.api.location.Area;
import com.souche.map.service.api.location.service.AreaService;
import com.souche.optimus.common.util.Exceptions;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 企业（商户）服务实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Service("shopService")
@Slf4j(topic = "service")
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopManager shopManager;

    @Resource
    private AreaService areaService;

    /**
     * 查询企业信息
     *
     * @param shopCode 店铺code
     */
    @Override
    public Result<ShopInfoBO> queryShopInfo(String shopCode) {
        log.info("<查询商户信息>请求参数->{}", shopCode);
        ShopInfoBO shopInfo;
        try {
            HttpBaseResponse response = shopManager.queryShopInfo(shopCode);
            if (!response.isSuccess()) {
                return Result.fail(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), ErrorCodeEnums.INVOKE_HTTP_ERROR.getMessage());
            }
            // 接口响应判断
            if (response.getData() == null || response.getData().get(ShopInfoEnums.SHOP_AUTH.getValue()) == null) {
                return Result.fail(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), "<查询商户信息>接口响应为空");
            }
            JSONObject jsonShopResp = JSONObject.parseObject(response.getData().get("shopAuth").toString());
            if (!StringUtil.equals(jsonShopResp.getString(ShopInfoEnums.QUERY_INFO_SUCCESS.getCode()), ShopInfoEnums.QUERY_INFO_SUCCESS.getValue())) {
                return Result.fail(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), "<查询商户信息>接口调用失败");
            }
            JSONObject json = JSONObject.parseObject(jsonShopResp.getString("data"));
            JSONObject shopJson = json.getJSONObject("shop");
            // 返回必要字段校验
            Result<String> vailResult = validateRetParams(json, shopJson);
            if (!vailResult.isSuccess()) {
                return Result.fail(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), vailResult.getMsg());
            }
            // 根据regionCode查询省和市的码值
            Area area = areaService.loadAreaByCode(CodeTypeEnum.SOUCHE_CODE, shopJson.getString(ShopInfoEnums.REGION_CODE.getValue()));
            Result<String> vailAreaResult = validateAreaInfo(area);
            if (!vailAreaResult.isSuccess()) {
                return Result.fail(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), vailAreaResult.getMsg());
            }
            // 构建返回参数
            shopInfo = ShopInfoBuilder.buildReqInfo(area, json, shopJson);

        } catch (ManagerException e) {
            throw Exceptions.fault(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), ErrorCodeEnums.INVOKE_HTTP_ERROR.getMessage());
        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getCode(), ErrorCodeEnums.QUERY_SHOP_INFO_ERROR.getMessage());
        }
        log.info("<查询商户信息>响应->{}", shopInfo.toString());
        return Result.success(shopInfo);
    }

    /**
     * 查询商户地址
     *
     * @param shopCode 店铺code
     */
    @Override
    public Result<String> queryAddress(String shopCode) {
        String address;
        try {
            HttpBaseResponse response = shopManager.queryShopInfo(shopCode);
            if (!response.isSuccess()) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), ErrorCodeEnums.SYSTEM_ERROR.getMessage());
            }
            // 接口响应判断
            if (response.getData() == null || response.getData().get(ShopInfoEnums.SHOP_AUTH.getValue()) == null) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "接口响应为空");
            }
            JSONObject jsonShopResp = JSONObject.parseObject(response.getData().get("shopAuth").toString());
            if (!StringUtil.equals(jsonShopResp.getString(ShopInfoEnums.QUERY_INFO_SUCCESS.getCode()), ShopInfoEnums.QUERY_INFO_SUCCESS.getValue())) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "接口调用失败");
            }
            JSONObject shopJson = JSONObject.parseObject(jsonShopResp.getString("data")).getJSONObject("shop");
            if (StringUtils.isBlank(shopJson.getString(ShopInfoEnums.PLACE.getValue()))) {
                return Result.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "持卡人地址为空");
            }
            address = shopJson.getString(ShopInfoEnums.PLACE.getValue());

        } catch (ManagerException | OptimusExceptionBase e) {
            throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), ErrorCodeEnums.SYSTEM_ERROR.getMessage());
        }
        return Result.success(address);
    }

    /**
     * 校验接口返回必要字段
     *
     * @param json     店铺认证信息
     * @param shopJson 店铺基础信息
     * @return Result<String>
     */
    private Result<String> validateRetParams(JSONObject json, JSONObject shopJson) {
        // 校验字段：身份证正面
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.ID_CARD.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>身份证正面照为空");
        }
        // 校验字段：身份证背面
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.ID_CARD_BACK.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>身份证反面照为空");
        }
        // 校验字段：营业执照照片
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.BUSINESS_LIC.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>营业执照图片为空");
        }
        // 校验字段：手机号
        if (StringUtil.isBlank(shopJson.getString(ShopInfoEnums.PHONE.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>手机号为空");
        }
        // 校验字段：固定电话
        if (StringUtil.isBlank(shopJson.getString(ShopInfoEnums.ADDRESS_CALL.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>客服电话为空");
        }
        // 校验字段：企业名称
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.LIC_COMPANY_NAME.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>企业名为空");
        }
        // 校验字段：法人姓名
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.CORPORATION_NAME.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>法人姓名为空");
        }
        // 校验字段：证件号
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.IDENTITY_CODE.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>法人证件号为空");
        }
        // 校验字段：营业执照号
        // 先校验信用代码creditCode是否有值，有则取之；若无，校验营业执照编码businessLicenseCode是否有值，两者都无则报错
        if (StringUtil.isBlank(json.getString(ShopInfoEnums.CREDIT_CODE.getValue()))) {
            if (StringUtil.isBlank(json.getString(ShopInfoEnums.BUSINESS_LIC_CODE.getValue()))) {
                return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>营业执照号为空");
            }
        }
        // 校验字段：区域码
        if (StringUtil.isBlank(shopJson.getString(ShopInfoEnums.REGION_CODE.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>区码值为空");
        }
        // 校验字段：详细地址
        if (StringUtil.isBlank(shopJson.getString(ShopInfoEnums.PLACE.getValue()))) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>详细地址为空");
        }
        return Result.success("");
    }

    /**
     * 校验省市区码值接口响应信息
     *
     * @param area 省市区码值
     */
    private Result<String> validateAreaInfo(Area area) {
        if (area == null) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询字典服务>接口响应为空");
        }
        if (StringUtils.isBlank(area.getProvinceCode())) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询字典服务>省码值为空");
        }
        if (StringUtils.isBlank(area.getCityCode())) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询字典服务>市码值为空");
        }
        if (StringUtils.isBlank(area.getCountyCode())) {
            return Result.fail(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询字典服务>区码值为空");
        }
        return Result.success("");
    }

}
