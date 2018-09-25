package cn.ourwill.willcenter.weixin.Utils;

import cn.ourwill.willcenter.weixin.pojo.Message;
import cn.ourwill.willcenter.weixin.pojo.MessageData;
import cn.ourwill.willcenter.weixin.pojo.MessageDataList;
import com.google.gson.Gson;
import com.qiniu.util.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * 微信推送信息
 *
 * @param
 * @author My
 * @CreateDate 2016-1-19
 */
public class WeixinPushMassage {

    private static final Logger log = LogManager.getLogger(WeixinPushMassage.class);

    //微信开发者ID
    @Value("${weixin.appid}")
    private static String appid;

    //开发者密码
    @Value("${weixin.appsecret}")
    private  static String secret;

    //购买成功通知
    private static final String template_id_successful_purchase = "SIxgJB7eBgWJOzBqc8eurh8JeDGxWgUn19XM9vP-ys0";

    //创建活动成功通知
    private static final String template_id_successful_create = "GN7CkI6NP5-1jFkmSHY9a-TT1dE7HCuHqIIdK9lqRNw";

    //关注活动成功通知
    private static final String template_id_successful_follow = "pSoyzdT0I4cfWWtV4lr4VxxzYMFWmS98ovyyOvjrxbA";

    //模板消息请求的URL
    private static final String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //购买成功通知
    public static String wxPurchaseSuccess(String name,String money,String buyTime,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_purchase);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("购买成功！"));
            messageData.setKeyword1(new MessageDataList(name));
            messageData.setKeyword2(new MessageDataList(money));
            messageData.setKeyword3(new MessageDataList(buyTime));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxPurchaseSuccess:" + js);
            return js;
        }
        return null;
    }

    //活动创建成功通知
    public static String wxCreateSuccess(String name,String place,String time,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_create);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("活动创建成功！"));
            messageData.setKeyword1(new MessageDataList(name));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setKeyword3(new MessageDataList(place));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxCreateSuccess:" + js);
            return js;
        }
        return null;
    }

    //活动关注成功
    public static String wxFollowSuccess(String nickName,String time,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_follow);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("关注活动成功！"));
            messageData.setKeyword1(new MessageDataList(nickName));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxFollowSuccess:" + js);
            return js;
        }
        return null;
    }

}
