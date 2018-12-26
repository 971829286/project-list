package com.souche.bmgateway.core.util;

import com.dingtalk.chatbot.message.Message;
import com.dingtalk.chatbot.message.TextMessage;
import com.google.common.util.concurrent.RateLimiter;
import com.netfinworks.common.util.DateUtil;
import com.souche.bmgateway.core.domain.BillSummary;
import com.souche.bmgateway.core.domain.DeductionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DingTalkClient {

    private static ResourceBundle dingResource = ResourceBundle.getBundle("dingtalk");

    private static final double MAX_MSG_NUM_PER_MINUTE = new Double(dingResource.getString("max.msg.num.per.minute"));

    private static final int MSG_MAX_BYTE_LENGTH = new Integer(dingResource.getString("msg.max.byte.length"));

    private static final int MSG_SAVE_LENGTH = new Integer(dingResource.getString("msg.save.length"));

    private static final String WEBHOOK_TOKEN = dingResource.getString("webhook.token");

    private static final String USER_AGENT = dingResource.getString("user.agent");

    private static double MSG_SEND_RATE;

    private static RateLimiter rateLimiter;

    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    private static HttpClient httpclient = null;

    public static void sendMsg(final String url, final String message, final List<String> atList) {
        Assert.hasText(url, "url不能为空");
        Assert.hasText(message, "message不能为空");
        singleThreadExecutor.execute((new Runnable() {
            @Override
            public void run() {
                String text = message;
                if (message.getBytes().length > MSG_MAX_BYTE_LENGTH) {
                    text = message.substring(MSG_SAVE_LENGTH) + "......\n-----------------消息超长!---------------";
                }
                TextMessage textMessage = new TextMessage(text);
                textMessage.setAtMobiles(atList);
                rateLimiter.acquire();
                postUrl(url, textMessage);
            }
        }));
    }

    public static void sendMsg(String message) {
        sendMsg(WEBHOOK_TOKEN, message, null);
    }

    public static void sendMsgAndAtMobiles(String message, List<String> atList) {
        sendMsg(WEBHOOK_TOKEN, message, atList);
    }

    private static void postUrl(String url, Message message) {
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);
        InputStream inputStream = null;
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            inputStream = response.getEntity().getContent();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes, 0, 1024);
            while (len > 0) {
                output.write(bytes, 0, len);
                len = inputStream.read(bytes, 0, 1024);
            }
            String result = new String(output.toByteArray(), "utf-8");
            log.info("发送叮叮消息完成,result:{}", result);
        } catch (IOException e) {
            log.error("发送叮叮消息失败,异常:{}", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void init() {
        MSG_SEND_RATE = MAX_MSG_NUM_PER_MINUTE / 60 * 0.9;

        if(rateLimiter == null ) {
            rateLimiter = RateLimiter.create(MSG_SEND_RATE);
        }

        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000)
                .setConnectionRequestTimeout(15000)
                .setMaxRedirects(10)
                .setSocketTimeout(15000)
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();

        builder.setMaxConnPerRoute(50);

        builder.setMaxConnTotal(500);
        builder.setUserAgent(USER_AGENT);
        builder.setDefaultRequestConfig(config);

        httpclient = builder.build();
    }

    public static String formatMessage(DeductionRecord record, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("手续费代发异常\nbatchID : " +  record.getRequestNo());
        sb.append("\norderNo : " + record.getOrderNo());
        sb.append("\n商户号 : " + record.getShopCode());
        sb.append("\n代发金额 : " + record.getTradeAmount());
        sb.append("元\n代发时间 : " + DateUtil.format(record.getGmtCreate(), "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n失败原因 : " + reason);
        return sb.toString();
    }

    public static String formatMessage(String batchID, BigDecimal totalAmount, Date time, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("手续费代发批量调用异常\nbatchID : " +  batchID);
        sb.append("\n代发金额 : " + totalAmount);
        sb.append("元\n代发时间 : " + DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n失败原因 : " + reason);
        return sb.toString();
    }

    public static String formatMessage(BillSummary summary, Date time, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("手续费代发异常");
        sb.append("\n商户号 : " + summary.getShopCode());
        sb.append("\n代发金额 : " + summary.getTotalAmount());
        sb.append("元\n代发时间 : " + DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n失败原因 : " + reason);
        return sb.toString();
    }

    public static String formatMessageForAccount(BillSummary summary, Date time, String reason , String account) {
        StringBuilder sb = new StringBuilder();
        sb.append("手续费代发异常");
        sb.append("\n商户号 : " + summary.getShopCode());
        sb.append("\n代发金额 : " + summary.getTotalAmount());
        sb.append("元\n代发时间 : " + DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n失败原因 : " + reason);
        sb.append("\n融易收账户 : " + account);
        return sb.toString();
    }

    static {
        init();
    }
}
