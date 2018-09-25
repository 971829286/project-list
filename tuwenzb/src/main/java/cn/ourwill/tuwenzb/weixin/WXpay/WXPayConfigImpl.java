package cn.ourwill.tuwenzb.weixin.WXpay;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/10 0010 17:01
 * @Version1.0
 */
@Component
public class WXPayConfigImpl implements WXPayConfig {

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;
    //微信开发者ID
    private static String appid;
    //开发者密码
    private static String secret;
    //商户id
    private static String mchId;
    //key
    private static String key;

    @Value("${weixin.appid}")
    public void setAppid(String appid) {
        this.appid = appid;
    }
    @Value("${weixin.appsecret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }
    @Value("${weixin.pay.mchId}")
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    @Value("${weixin.pay.key}")
    public void setKey(String key) {
        this.key = key;
    }

    private WXPayConfigImpl() throws Exception{
//        File file = new File(GlobalUtils.getConfig("weixin.pay.certPath"));
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    private WXPayConfigImpl(String certPath) throws Exception{
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl getInstance(String certPath) throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl(certPath);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getAppID() {
        return this.appid;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
