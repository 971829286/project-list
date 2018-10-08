package com.souche.niu.util;

import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description：发送HTTP请求的工具类
 *
 * @remark: Created by wujingtao in 2018/9/21
 **/
public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static OkHttpClient client = new OkHttpClient();

    private static final String HEAD_CONTENT_TYPE = "content-type";
    private static final String RESPONSE_POST_HEAD = "application/json";
    private static final String RESPONSE_TEXT_HEAD = "text/plain";

    /**
     * todo 发起post请求
     * todo 先以"application/json"方式请求 如果没响应以"text/plain"发起二次响应
     * todo data可为空
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String postRequest(String url, String data) throws Exception {
        try {
            if (StringUtil.isEmpty(url)) {
                throw new OptimusExceptionBase("post请求失败 url为空");
            }
            Request.Builder builder=new Request.Builder().url(url);
            if (StringUtil.isNotEmpty(data)) {
                MediaType mediaType = MediaType.parse(RESPONSE_POST_HEAD);
                RequestBody body = RequestBody.create(mediaType, data);
                builder.post(body);
            }
            Request request=builder.addHeader(HEAD_CONTENT_TYPE, RESPONSE_POST_HEAD).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String header = response.header(HEAD_CONTENT_TYPE);
                if (StringUtils.isEmpty(header) || header.indexOf(RESPONSE_POST_HEAD) == -1) {
                    return postTextRequest(url, data);
                } else {
                    InputStream is=response.body().byteStream();
                    return readFromInputStream(is);
                }
            } else {
                logger.info("请求修改店铺接口失败，url={},response={}", url, response);
                throw new OptimusExceptionBase("请求修改店铺接口失败 url="+url);
            }
        } catch (Exception e) {
            logger.error("okhttp3 post请求出错 {}", e.toString());
            throw e;
        }
    }

    private static String postTextRequest(String url, String data) throws Exception {
        try {
            Request.Builder builder = new Request.Builder().url(url);
            if (StringUtil.isNotEmpty(data)) {
                MediaType mediaType = MediaType.parse(RESPONSE_TEXT_HEAD);
                RequestBody body = RequestBody.create(mediaType, data);
                builder.post(body);
            }
            Request request=builder.addHeader(HEAD_CONTENT_TYPE, RESPONSE_TEXT_HEAD).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is=response.body().byteStream();
                return readFromInputStream(is);
            } else {
                logger.error("请求修改店铺接口失败，url={},response={}", url, response);
                throw new OptimusExceptionBase("请求修改店铺接口失败 url="+url);
            }
        } catch (Exception e) {
            logger.error("okhttp3 post请求出错 {}", e.toString());
            throw e;
        }
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        byte[] b = new byte[1024];
        int i=-1;
        StringBuilder builder = new StringBuilder();
        while ((i = inputStream.read(b)) != -1) {
            builder.append(new String(b,0,i));
        }
        return builder.toString();
    }


    public static void main(String[] args) throws Exception {
        String url = "http://www.baidu.com";
        String data = null;
        String ss = postRequest(url, data);
        System.out.println(ss);
    }
}
