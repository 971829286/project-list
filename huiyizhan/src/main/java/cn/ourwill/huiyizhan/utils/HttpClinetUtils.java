package cn.ourwill.huiyizhan.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-18 14:30
 **/
@Slf4j
public class HttpClinetUtils {

    public static <T> T sendPost(String url, String content, Class<T> type) throws IOException {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        StringEntity s = new StringEntity(content, "UTF-8");
        s.setContentType("application/json");
        s.setContentEncoding("UTF-8");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setEntity(s);
        CloseableHttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = response.getEntity().getContent();
            String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if (StringUtils.isEmpty(res)) {
                return null;
            } else {
                T t = JsonUtil.fromJson(res, type);
                return t;
            }
        }
        return null;
    }


    public static String sendPostFile(String url, byte[] bytes) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
        post.setEntity(byteArrayEntity);
        CloseableHttpResponse response = client.execute(post);
        InputStream inStream = response.getEntity().getContent();
        String res = IOUtils.toString(inStream, StandardCharsets.UTF_8);
        return res;
    }

    public static <T> T sendPost(String url, Class<T> type, List<NameValuePair> list, HttpEntity entity) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        try {
            if (list != null) {
                post.setEntity(new UrlEncodedFormEntity(list));
            }
            if(entity != null){

                post.setEntity(entity);
            }
            CloseableHttpResponse response = client.execute(post);
            String res = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            T t = JsonUtil.fromJson(res, type);
            return t;
        } catch (IOException e) {
            log.info("HttpUtils.sendPost", e);
        }
        return null;
    }

    public static <T> T sendGet(String url, Class<T> type) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost(url);
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            String res = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            T t = JsonUtil.fromJson(res, type);
            return t;
        } catch (IOException e) {
            log.info("HttpUtils.sendPost", e);
        }
        return null;
    }


    public static byte[] getTicket(String url, String content) throws IOException {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        StringEntity s = new StringEntity(content, "UTF-8");
        s.setContentType("application/json");
        s.setContentEncoding("UTF-8");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setEntity(s);
        CloseableHttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = response.getEntity().getContent();
            return IOUtils.toByteArray(inputStream);
        }
        return null;
    }

}
