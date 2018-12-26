package com.souche.bmgateway.core.open.plat.impl;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.open.plat.OpenPlatformHttpClient;
import com.souche.bmgateway.core.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author zs
 */
@Service
public class OpenPlatformHttpClientImpl implements OpenPlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenPlatformHttpClientImpl.class);

    @Override
    public HttpBaseResponse execute(String url, String json) {
        logger.info("请求开放平台：url=>{}，json=>{}", url, json);
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String response = httpClientUtil.doPost(url, json);
        logger.info("开放平台返回请求结果:response=>{}", response);
        return convertResult(response);
    }

    @Override
    public HttpBaseResponse executeUpload(String url, String json) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String response = httpClientUtil.doPost(url, json);
        logger.info("开放平台返回请求结果:response=>{}", response);
        return convertResult(response);
    }

    /**
     * 解析返回参数
     *
     * @param response 开放平台响应结果
     * @return HttpBaseResponse
     */
    private HttpBaseResponse convertResult(String response) {
        HttpBaseResponse httpBaseResponseDTO = new HttpBaseResponse();
        try {
            Map<String, Object> respMap = (Map<String, Object>) JSONObject.parse(response);
            if (respMap != null && (boolean) respMap.get("success")) {
                httpBaseResponseDTO.setSuccess(true);
                httpBaseResponseDTO.setData((Map<String, Object>) respMap.get("data"));
                httpBaseResponseDTO.setMsg("接口调用成功，业务处理成功！");
            } else {
                httpBaseResponseDTO.setSuccess(false);
                httpBaseResponseDTO.setCode(respMap.get("code").toString());
                httpBaseResponseDTO.setMsg("接口调用成功,业务处理失败！" + respMap.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("response=>{}解析开放平台返回参数失败,异常=>{}", response, e);
        }
        return httpBaseResponseDTO;
    }

}
