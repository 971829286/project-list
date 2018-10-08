package com.souche.niu.json.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.souche.dictionary.api.bean.Area;
import com.souche.dictionary.api.service.AreaService;
import com.souche.niu.manager.shop.ShopManager;
import com.souche.niu.spi.ShopAuditSPI;
import com.souche.niu.spi.ShopSPI;
import com.souche.niu.util.ShopEnum;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.common.util.http.HttpClientGetUtil;
import com.souche.optimus.core.annotation.JsonEx;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.redis.RedisValueRepository;
import com.souche.shop.model.ShopAudit;
import com.souche.shop.model.ShopExtra;
import com.souche.sso.client2.AuthNHolder;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：店铺api
 * @remark: Created by wujingtao in 2018/9/21
 **/
@View
@Api(value = "shopApi", description = "店铺api")
public class ShopApi {

    private static final Logger logger = LoggerFactory.getLogger(ShopApi.class);

    @Autowired
    private ShopManager shopManager;
    @Resource
    private AreaService areaService;
    @Resource
    private RedisValueRepository redisValueRepository;
    @Autowired
    private ShopSPI shopSPI;
    @Autowired
    private ShopAuditSPI shopAuditSPI;

    @ApiOperation(value = "添加/更新车牛店铺信息")
    @JsonEx(serializerFeature = SerializerFeature.WriteMapNullValue)
    public Result<Object> addCheniuShop(
            @Param(value = "name", desc = "车行名称") String name,
            @Param(value = "marketId") Integer marketId,
            @Param(value = "marketName") String marketName,
            @Param(value = "area") String area,
            @Param(value = "introduce") String introduce,
            @Param(value = "place") String place,
            @Param(value = "booth") String booth,
            @Param(value = "storeType") Integer storeType,
            @Param(value = "majorLevel") Integer majorLevel) {
        Result<Object> ret = new Result<>();
        String shopCode = null;
        try {
            logger.info("添加/修改车牛店铺信息>>>entrance");
            //todo 参数获取&校验
            shopCode = AuthNHolder.shopCode();
            String userId = AuthNHolder.userId();
            if (StringUtil.isEmpty(userId)) {
                ret.setCode(ShopEnum.PARAM_ERROR.getCode());
                ret.setMsg(ShopEnum.PARAM_ERROR.getDesc());
                ret.setSuccess(false);
                logger.error("添加、更新车牛店铺信息失败 用户未登录");
                return ret;
            }

            if (StringUtil.isEmpty(shopCode)) {
                ret.setCode(ShopEnum.PARAM_ERROR.getCode());
                ret.setMsg(ShopEnum.PARAM_ERROR.getDesc());
                ret.setSuccess(false);
                logger.error("添加、更新车牛店铺信息失败 shopCode不允许为空");
                return ret;
            }

            //封装参数
            JSONObject jdata = getJsonParam(shopCode, name, marketId, marketName, area, introduce, place, booth, storeType, majorLevel);
            //转换实体
            ShopExtra shop = buildShop(jdata.toString());
            //更新店铺信息
            this.shopManager.addOrUpdateShop(shop);

            //发送一个包含shopCode拼装的请求
            HttpClientGetUtil.getUrl("http://" + shopCode + ".souche.com/json/clearShopInfo.json");

            ret.setCode(ShopEnum.SUCCESS.getCode());
            ret.setMsg(ShopEnum.SUCCESS.getDesc());
            ret.setSuccess(true);
        } catch (Exception e) {
            logger.error("添加/更新车牛店铺信息异常 {}", e);
            ret.setCode(ShopEnum.SYSTEM_ERROR.getCode());
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }

        Map<String, Object> reData = new HashMap<>();
        ShopExtra shop = shopSPI.loadShopExtraByCode(shopCode);

        ShopAudit shopAudit = shopAuditSPI.loadAuditByShopCode(shopCode);
        if (shopAudit != null) {
            shopAudit.setShop(null);
        }

        //todo 刷新Redis
        redisValueRepository.set("c_user_shop_data_status_" + shopCode, "1", 3600 * 24 * 100);
        logger.info("刷新缓存 [{}:{}]", "c_user_shop_data_status_" + shopCode, 1);

        reData.put("shopInfo", shop);
        reData.put("certifStatus", shopAudit);
        //为保持与node返回数据格式一致
        Map<String, Object> map = new HashMap<>();
        map.put("data", reData);
        ret.setData(map);
        logger.info("添加/修改车牛店铺信息>>>export");
        return ret;
    }

    /**
     * 根据参数封装为jsonObject对象
     *
     * @return
     */
    private JSONObject getJsonParam(String shopCode, String name,
                                    Integer marketId,
                                    String marketName,
                                    String area,
                                    String introduce,
                                    String place,
                                    String booth,
                                    Integer storeType,
                                    Integer majorLevel) {
        JSONObject jdata = new JSONObject();
        jdata.put("code", shopCode);
        if (StringUtil.isNotEmpty(name)) {
            jdata.put("name", name);
        }
        if (marketId != null) {
            jdata.put("marketId", marketId);
            jdata.put("marketName", marketName);
        } else {
            jdata.put("marketId", 0);
            jdata.put("marketName", null);
        }
        if (StringUtil.isNotEmpty(area)) {
            jdata.put("area", area);
        }
        if (StringUtil.isNotEmpty(introduce)) {
            jdata.put("introduce", introduce);
        }
        if (StringUtil.isNotEmpty(place)) {
            if (StringUtil.isEmpty(marketName)) {
                jdata.put("place", place + (StringUtil.isEmpty(booth) ? "" : booth));
                jdata.put("marketName", null);
                jdata.put("marketId", 0);
                jdata.put("booth", StringUtil.isEmpty(booth) ? "" : booth);
            } else if (StringUtil.isNotEmpty(marketName) && StringUtil.isEmpty(booth)) {
                jdata.put("booth", "");
                jdata.put("place", place + marketName);
            } else if (StringUtil.isNotEmpty(place) && StringUtil.isNotEmpty(booth)) {
                jdata.put("booth", booth);
                jdata.put("place", place + marketName + booth);
            }
        }
        if (storeType != null) {
            jdata.put("storeType", storeType);
        }
        if (majorLevel != null) {
            jdata.put("majorLevel", majorLevel);
        }
        return jdata;
    }

    /**
     * 生成店铺的更新信息
     *
     * @param data
     * @return
     */
    protected ShopExtra buildShop(String data) {
        ShopExtra shop = JsonUtils.fromJson(data, ShopExtra.class);
        if (StringUtil.isNotEmpty(shop.getArea()) && shop.getProvinceCode() == null) {
            Area area = areaService.matchArea(shop.getArea());
            if (area != null) {
                shop.setProvinceCode(area.getProvinceCode());
                shop.setCityCode(area.getCityCode());
            }
        }
        return shop;
    }
}
