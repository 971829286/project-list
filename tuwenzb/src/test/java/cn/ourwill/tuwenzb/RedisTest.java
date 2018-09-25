package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.service.ITrxOrderService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/10 0010 14:30
 * @Version1.0
 */
@Component
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class RedisTest {
    @Autowired
    private ITrxOrderService trxOrderService;
    @Test
    public void testSet() throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse("2017-08-09 00:00:00");
        Date date2 = sdf.parse("2017-08-09 00:00:00");
        System.out.println(GlobalUtils.daysOfTwo(date1,date2));
        System.out.println(UUID.randomUUID().toString().replace("-",""));
//        SmsUtil.send("13146869491","您的验证码是：4568。请不要把验证码泄露给其他人。");
    }

    @Test
    public void wxpaytest() throws Exception {
        Map reData = new HashMap();
        String nonceStr = WXPayUtil.generateNonceStr();
        reData.put("mch_id","1487246152");
        reData.put("nonce_str", nonceStr);
        System.out.println(nonceStr);
        System.out.println(WXPayUtil.generateSignature(reData,"4pgaWubsdNJwQp5EAfydH9QWRqbRzV7q"));
    }

    @Test
    public void wxpaytest1() throws Exception {
        Map data = new HashMap();
        data.put("appid","wx2421b1c4370ec43b");
                data.put("attach","支付测试");
                data.put("bank_type","CFT");
                data.put("fee_type","CNY");
                data.put("is_subscribe","Y");
                data.put("mch_id","1487246152");
                data.put("nonce_str","5d2b6c2a8db53831f7eda20af46e531c");
                data.put("openid","oItvJjovCwKy9Sm2dPEQHIS2TIKE");
                data.put("out_trade_no","22437f35f109497094b5c2a00ae753a2");
                data.put("result_code","SUCCESS");
                data.put("return_code","SUCCESS");
//                data.put("sign","B552ED6B279343CB493C5DD0D78AB241");
                data.put("sub_mch_id","10000100");
                data.put("time_end","20170814134620");
                data.put("total_fee","101");
                data.put("trade_type","JSAPI");
                data.put("transaction_id","1004400740201409030005092168");
        System.out.println(WXPayUtil.generateSignature(data,"862d00b99ee104844fff82a0509c6c97"));
    }

    @Test
    public void testPic() throws IOException, ParseException {
        URL url = new URL("http://s.tuwenzhibo.com/image/jpeg/20170826/1505003726763.jpeg");
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        BufferedImage sourceImage = ImageIO.read(inStream);
        System.out.println(sourceImage.getWidth());
        System.out.println(sourceImage.getHeight());

        inStream.close();
    }
    @Test
    public void testsub () throws UnsupportedEncodingException {
        String str = "http://s.tupianzhibo.cn/test/2355/41/20180305/DSC_0002_1515665658904（2） (2)_1520245416086.JPG?imageInfo";
        System.out.println(IDN.toASCII(str));
    }
}
