package cn.ourwill.tuwenzb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 14:45
 **/
@Slf4j
public class HttpUtils {
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
}
