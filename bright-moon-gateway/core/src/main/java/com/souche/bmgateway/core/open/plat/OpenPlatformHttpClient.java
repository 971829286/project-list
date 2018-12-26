package com.souche.bmgateway.core.open.plat;

import com.souche.bmgateway.core.dto.response.HttpBaseResponse;

/**
 * 开放平台公用接口
 *
 * @author chenwj
 */
public interface OpenPlatformHttpClient {

    /**
     * 发起请求
     *
     * @param url  开放平台请求地址
     * @param json 请求报文
     * @return HttpBaseResponse
     */
    HttpBaseResponse execute(String url, String json);

    /**
     * 发起请求-文件上传
     *
     * @param url  开放平台请求地址
     * @param json 请求报文
     * @return HttpBaseResponse
     */
    HttpBaseResponse executeUpload(String url, String json);

}
