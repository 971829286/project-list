package cn.ourwill.tuwenzb.utils;

/**
 * Created by liuqinggang on 15/7/28.
 */

import cn.ourwill.tuwenzb.controller.ActivityController;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.LogManager;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

public class SmsUtil {


    private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

    private static String account = "cf_bjwill";//http://sms.ihuyi.com/login.html

//    private static String password = "2014bjwill";

    private static String APIKEY = "77d77c5750487f9e0717fb126837177e";

    private String mobile;
    private String content;


    private static final org.apache.log4j.Logger log = LogManager.getLogger(SmsUtil.class);

    public static boolean send(String mobile,String content) {

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);
        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//        Date now = new Date();
//        String orignPassword = account+APIKEY+mobile+content+now.getTime();
//        String md5Password = GlobalUtils.getMD5(account+APIKEY+mobile+content+now.getTime());
//        log.info(orignPassword);
//        log.info(md5Password);
        NameValuePair[] data = {//提交短信
                new NameValuePair("account",account),
                new NameValuePair("password", APIKEY), //密码可以使用明文密码或使用32位MD5加密
                //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
//                new NameValuePair("time",String.valueOf(now.getTime())),
                new NameValuePair("mobile", mobile),
                new NameValuePair("content", content),
        };

        method.setRequestBody(data);

        try {
            client.executeMethod(method);
            String SubmitResult =method.getResponseBodyAsString();
            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();
            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");
            log.info("+++sendsms result code:"+code+",msg:"+msg+",smsid:"+smsid);
            if("2".equals(code)){
                log.info("+++sendsms mobile:" + mobile + ",content:" + content);
                return true;
            }else {
                log.info("+++sendsms failure:" + mobile+ ",content:" + content);
                return false;
            }
        } catch (Exception e) {
           log.error("+sendsms",e);
           return false;
        }
    }

}
