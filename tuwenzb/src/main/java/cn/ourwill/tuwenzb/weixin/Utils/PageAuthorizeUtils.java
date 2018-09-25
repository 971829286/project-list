package cn.ourwill.tuwenzb.weixin.Utils;

import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.weixin.pojo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

/**
 * 　ClassName:PageAuthorizeUtils
 * Description：
 * User:hasee
 * CreatedDate:2017/7/1 17:40
 */
//微信网页授权
//在微信客户端中访问第三方网页，公众号可以通过微信网页授权机制，来获取用户基本信息
@Component
public class PageAuthorizeUtils {
    //微信开发者ID
    private static String appid;

    //开发者密码
    private  static String secret;

    //ticket获取方式
    private static String mode;

    //ticket获取url
    private static String accessUrl;

    private static String ticketUrl;

    //网站应用ID
    @Value("${weixin.webappid}")
    private String webappid;

    //网站应用开发密码
    @Value("${weixin.webappsecret}")
    private String webappsecret;

    @Value("${weixin.appid}")
    public void setAppid(String appid) {
        PageAuthorizeUtils.appid = appid;
    }
    @Value("${weixin.appsecret}")
    public void setSecret(String secret) {
        PageAuthorizeUtils.secret = secret;
    }
    @Value("${weixin.accesstoken.mode}")
    public void setMode(String mode) {
        PageAuthorizeUtils.mode = mode;
    }
    @Value("${weixin.accesstoken.url}")
    public void setAccessUrl(String accessUrl) {
        PageAuthorizeUtils.accessUrl = accessUrl;
    }
    @Value("${weixin.accesstoken.ticket.url}")
    public void setTicketUrl(String ticketUrl){
        PageAuthorizeUtils.ticketUrl = ticketUrl;
    }
    private static final Logger log = LogManager.getLogger(PageAuthorizeUtils.class);

    //第一步：用户同意授权，获取code
    public void getCode(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String redirect_uri="https://www.baidu.com/";
        redirect_uri= URLEncoder.encode(redirect_uri,"UTF-8");
        //appid:公众号的唯一标识
        //redirect_uri:授权后重定向的回调链接地址
        //response_type:返回类型，请填写code
        //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        String scope="snsapi_userinfo";
        //state:重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
        //#wechat_redirect:无论直接打开还是做页面302重定向时候，必须带此参数
        String loginURL="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope="+scope+"&state=STATE#wechat_redirect";
        //如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
        //code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
        response.sendRedirect(loginURL);
    }
    //第二步：通过code换取网页授权access_token
    public static UserTokenReturn getUserAccessToken(@QueryParam("code") String code) throws Exception {
        //appid:公众号的唯一标识
        //secret:公众号的appsecret
        //code:填写第一步获取的code参数
        //grant_type:填写为authorization_code
        String getURL="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code ";
        String returnStr=HttpURlUtils.getUrlResponse(getURL);
        if(returnStr.indexOf("errcode")>0) {
            ObjectMapper mapper = new ObjectMapper();
            UserTokenReturn tokenReturn = mapper.readValue(returnStr, UserTokenReturn.class);
            return tokenReturn;
        }
        return null;
    }
    //第三步：刷新access_token（如果需要）
    //由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token有效期为30天，当refresh_token失效之后，需要用户重新授权。
    public static UserTokenReturn refreshUserToken(String refreshToken) throws Exception{
        //appid:公众号的唯一标识
        //grant_type:填写为refresh_token
        //refresh_token:填写通过access_token获取到的refresh_token参数
        String getURL="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+"&grant_type=refresh_token&refresh_token="+refreshToken;
        log.info("refreshUrl:"+getURL);
        String returnStr=HttpURlUtils.getUrlResponse(getURL);
        log.info("returnStr:"+returnStr);
        if(returnStr.indexOf("errcode")<0) {
            ObjectMapper mapper = new ObjectMapper();
            UserTokenReturn tokenReturn = mapper.readValue(returnStr, UserTokenReturn.class);
            return tokenReturn;
        }
        return null;
    }

    //第四步：拉取用户信息(需scope为 snsapi_userinfo)
    //如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
    public static UserInfoReturn getUserInfo(UserTokenReturn tokenReturn) throws Exception{
        //access_token:网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
        String access_token=tokenReturn.getAccess_token();
        //openid:用户的唯一标识
        String openid=tokenReturn.getOpenid();
        log.info("getUserInfo.openid"+openid);
        //lang:返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
        String getURL="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        String returnStr=HttpURlUtils.getUrlResponse(getURL);
        log.info("getUserInfo.returnStr"+returnStr);
        if(returnStr.indexOf("errcode")<0) {
            ObjectMapper mapper = new ObjectMapper();
            UserInfoReturn userInfo = mapper.readValue(returnStr, UserInfoReturn.class);
            return userInfo;
        }
        return null;
    }
    //检验授权凭证（access_token）是否有效
    public static  Boolean  checkAccessToken(UserTokenReturn tokenReturn) throws Exception{
        //access_token:网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
        String access_token=tokenReturn.getAccess_token();
        //openid:用户的唯一标识
        String openid=tokenReturn.getOpenid();
        String getURL="https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openid;

        ObjectMapper mapper=new ObjectMapper();
        WeixinErrorReturn errorReturn=mapper.readValue(HttpURlUtils.getUrlResponse(getURL),WeixinErrorReturn.class);
		/*正确的JSON返回结果：
		{ "errcode":0,"errmsg":"ok"}
		错误时的JSON返回示例：
		{ "errcode":40003,"errmsg":"invalid openid"}*/
        if(errorReturn.getErrcode().equals("0")||errorReturn.getErrmsg().equals("ok")){
            return true;
        }
        return false;
    }

    //根据openid 获取 用户信息
    public static UserInfoReturn getUserInfoByOpenId(String openId) throws Exception {
        //接口凭证 access_token 不是网页授权凭证
        String access_token=getAccess_token(false);
        String getURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openId+"&lang=zh_CN";
        //https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        String returnStr=HttpURlUtils.getUrlResponse(getURL);
        log.info("getUserInfoByOpenId.returnStr"+returnStr);
        if(returnStr.indexOf("errcode")<0) {
            ObjectMapper mapper = new ObjectMapper();
            UserInfoReturn userInfo = mapper.readValue(returnStr, UserInfoReturn.class);
            return userInfo;
        }
        return null;
    }

    /**
     * 获取接口访问凭证
     * isRdgain 是否强制刷新缓存
     * @return
     */
    public static String getAccess_token(boolean isRegain) throws Exception {
        if(mode.equals("1")){
            log.info("从其他服务获取accessToken："+accessUrl);
            String reObj = HttpURlUtils.getUrlResponse(accessUrl);
            JSONObject jsonObject = new JSONObject(reObj);
            if (null != jsonObject) {
                try {
                    if(jsonObject.getInt("code")==0) {
                        return jsonObject.getString("accessToken");
                    }else{
                        return null;
                    }
                } catch (JSONException e) {
                    // 获取token失败
                    log.error("获取token失败");
                    return null;
                }
            }
        }
        if(!isRegain) {
            AccessToken accessToken = (AccessToken) RedisUtils.get("access_token");
            if (accessToken != null && new Date().before(accessToken.getDueDate())) {
                log.info("从redis获取access_token：" + accessToken.getAccessToken());
                return accessToken.getAccessToken();
            }
        }
        //凭证获取(GET)
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", secret);
        // 发起GET请求获取凭证
//        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        String reStr = HttpURlUtils.getUrlResponse(requestUrl);
        JSONObject jsonObject = new JSONObject(reStr);
        String access_token = null;
        int expires_in = 0;
        if (null != jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
                expires_in = jsonObject.getInt("expires_in");

                log.info("重新获取access_token："+access_token);
                AccessToken newAccessToken = new AccessToken();
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTime(new Date());
                calendar.add(Calendar.SECOND,+(expires_in-3600));
                newAccessToken.setAccessToken(access_token);
                newAccessToken.setDueDate(calendar.getTime());
                RedisUtils.set("access_token",newAccessToken);
                log.info("access_token存入reids");
                return access_token;
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取token失败,errcode:"+jsonObject.getInt("errcode")+", errmsg:"+jsonObject.getString("errmsg"));
                return null;
            }
        }
        return null;
    }

    /**
     * 调用微信JS接口的临时票据
     * @return
     */
    public static String getJsApiTicket() throws Exception {
        if(mode.equals("1")){
            log.info("从其他服务获取ticket："+ticketUrl);
            String reObj = HttpURlUtils.getUrlResponse(ticketUrl);
            JSONObject jsonObject = new JSONObject(reObj);
            if (null != jsonObject) {
                try {
                    if(jsonObject.getInt("code")==0) {
                        return jsonObject.getString("ticket");
                    }else{
                        return null;
                    }
                } catch (JSONException e) {
                    // 获取token失败
                    log.error("获取token失败");
                    return null;
                }
            }
        }
        JsTicket jsTicket = (JsTicket) RedisUtils.get("js_ticket");
        if(jsTicket!=null&&new Date().before(jsTicket.getDueDate())){
            log.info("从redis获取ticket："+jsTicket.getJsTicket());
            return jsTicket.getJsTicket();
        }
        String access_token = getAccess_token(false);
        JsTicket ticket = getTicket(access_token);
        if(ticket==null){
            return null;
        }else if(ticket.getErrcode().intValue()==42001){
            access_token = getAccess_token(true);
            ticket = getTicket(access_token);
        }
        log.info("重新获取ticket："+ticket.getJsTicket());
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND,ticket.getExpireIn()-100);
        ticket.setDueDate(calendar.getTime());
        RedisUtils.set("js_ticket",ticket);
        log.info("ticket存入reids");
        return ticket.getJsTicket();
    }

    private static JsTicket getTicket(String access_token) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
//        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        String reStr = HttpURlUtils.getUrlResponse(requestUrl);
        JsTicket ticket = new JsTicket();
        JSONObject jsonObject = new JSONObject(reStr);
        if (null != jsonObject) {
            try {
                ticket.setErrcode(jsonObject.getInt("errcode"));
                if(jsonObject.getInt("errcode")==0) {
                    ticket.setJsTicket(jsonObject.getString("ticket"));
                    ticket.setErrmsg(jsonObject.getString("errmsg"));
                    ticket.setExpireIn(jsonObject.getInt("expires_in"));
                }
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取token失败,errcode:"+jsonObject.getInt("errcode")+", errmsg:"+jsonObject.getString("errmsg"));
                return null;
            }
        }
        return ticket;
    }

    /**
     * 获取appId
     */
    public static String getAppid(){
        return appid;
    }
}
