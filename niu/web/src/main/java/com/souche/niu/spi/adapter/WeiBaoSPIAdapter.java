package com.souche.niu.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.souche.niu.model.maintenance.request.QueryMaintenanceRequest;
import com.souche.niu.spi.WeiBaoSPI;
import com.souche.niu.util.ObjectToMapUtil;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.http.HttpClientPostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 维保接口
 * Created by sid on 2018/9/3.
 */
@Component
public class WeiBaoSPIAdapter implements WeiBaoSPI {

    private static final Logger logger = LoggerFactory.getLogger(OrderCenterSPIAdapter.class);


    private String requestUrl = OptimusConfig.getValue("wb.query.url");

    @Override
    public Map<String, Object> thirdServiceQuery(QueryMaintenanceRequest queryMaintenanceRequest) {
        Map<String, Object> resMap = Maps.newHashMap();
        try {
            logger.info("请求维保查询 请求参数 queryMaintenanceRequest={}", JsonUtils.toJson(queryMaintenanceRequest));
            Map<String, String> params = ObjectToMapUtil.objectToMapString(queryMaintenanceRequest);
            String res = HttpClientPostUtil.postUrl(requestUrl, params);
            logger.info("请求维保查询 返回的结果为:{}", res);
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject == null) {
                resMap.put("success", false);
                resMap.put("msg", "系统错误");
                return resMap;
            }
            boolean isSuccess = jsonObject.getBoolean("success");
            String msg = jsonObject.getString("msg");
            resMap.put("success", isSuccess);
            resMap.put("msg", msg);
            return resMap;
        } catch (Exception e) {
            logger.error("请求维保查询 异常:{}", e);
            resMap.put("success", false);
            resMap.put("msg", e.getMessage());
            return resMap;
        }
    }
}
