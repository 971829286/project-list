package cn.ourwill.willcenter.controller;

import cn.ourwill.willcenter.entity.User;
import cn.ourwill.willcenter.service.IUserService;
import cn.ourwill.willcenter.utils.*;
import cn.ourwill.willcenter.weixin.Utils.HttpURlUtils;
import cn.ourwill.willcenter.weixin.Utils.PageAuthorizeUtils;
import cn.ourwill.willcenter.weixin.pojo.TokenReturn;
import cn.ourwill.willcenter.weixin.pojo.UserInfoReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-09 17:52
 **/
@Controller
@Component
@RequestMapping("/")
public class WeChatLoginController {

    @Value("${weixin.webappid}")
    private String appid;

    @Value("${weixin.webappsecret}")
    private String appsecret;

    @Value("${weixin.webapp.redirect_uri}")
    private String redirect_uri;

    @Value("${system.domain}")
    private String domain;

    private static final Logger log = LogManager.getLogger(WeChatLoginController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    RedisTemplate redisTemplate;


    //获取二维码
    @RequestMapping("login/getCode")
    public void loginByQRCode(@RequestParam("state") String state,@RequestParam("service") String service,
                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            //判断是否来自微信
            if (request.getHeader("user-agent").indexOf("MicroMessenger") > 0) {
                //手机微信授权登录
                response.sendRedirect(service+"/toAutonrize");

            }else{
                String codeURL = "https://open.weixin.qq.com/connect/qrconnect?appid=" + appid + "&redirect_uri=" + URLEncoder.encode(redirect_uri+"?service="+service, "UTF-8") + "&response_type=code&scope=snsapi_login&state=" + state + "#wechat_redirect";
                log.info("loginUrl:" + codeURL);
                response.sendRedirect(codeURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //扫描二维码登录
    @RequestMapping("login/byCode")
    public String getCode(@RequestParam("code") String code, @RequestParam("service") String service, @RequestParam("state") String state, HttpServletRequest request,
                          HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        //通过code换取网页授权access_token
        //appid:应用唯一标识
        //secret:应用密钥AppSecret
        //code:填写第一步获取的code参数
        //grant_type:填写为authorization_code
        String getURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
        ObjectMapper mapper = new ObjectMapper();
        TokenReturn tokenReturn = null;
        try {
            String returnStr = HttpURlUtils.getUrlResponse(getURL);
            System.out.printf("returnStr:" + returnStr);
            if (returnStr.indexOf("errcode") <= 0) {
                tokenReturn = mapper.readValue(returnStr, TokenReturn.class);
                UserInfoReturn userInfoReturn = PageAuthorizeUtils.getUserInfo(tokenReturn);
                //查看是否存在这个微信用户
                User user = userService.selectByUnionId(tokenReturn.getUnionid());
                if (user != null ) {
                    String username = user.getUsername();
                    //登录网站
                    //登录成功存入cookie和redis
                    String cookie = GlobalUtils.getMD5(username + System.currentTimeMillis());
                    CookieUtils.setCookie(response, "sso", cookie);
                    RedisUtils.set("cookie:" + cookie, user, 2, TimeUnit.HOURS);
                    //删除登出状态
                    redisTemplate.delete("logout:" + user.getUUID());
                    String ticket = GlobalUtils.generateTicket(user);
                    //重定向到登录页面
                    if (service != null || (!"".equals(service))){//验证service不为空
                        return "redirect:"+service+"?ticket="+ticket;
                    }
                    //return "login";
                } else {//不存在的话，返回微信用户信息,重定向到绑定页面
                    model.addAttribute("unionId",tokenReturn.getUnionid());
                    model.addAttribute("nickname",userInfoReturn.getNickname());
                    model.addAttribute("avatar",userInfoReturn.getHeadimgurl());
                    return "bind";
                }
            }
            return "bind";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //绑定二维码登录
    @RequestMapping("login/afterBind")
    @ResponseBody
    public Map loginAfterBind(@RequestBody Map<String,String> userMap,HttpServletRequest request, HttpServletResponse response){
        try{
            //通过手机号或者邮箱判断该用户是否已经注册
            String phoneOrEmail = userMap.get("phoneumOrMail");
            String password = userMap.get("password");
            String verifyCode = userMap.get("code");
            String unionId = userMap.get("unionId");
            if(StringUtils.isEmpty(phoneOrEmail)){
                return ReturnResult.errorResult("手机号为空！");
            }else{
                String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|16[6]|(17[0135678])|(18[0,0-9]|19[89]))\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phoneOrEmail);
                boolean isMatch = m.matches();
                if (!isMatch){
                    return ReturnResult.errorResult("请输入正确的手机号");
                }
            }
            if (StringUtils.isEmpty(password)) {
                // return ReturnResult.errorResult("密码为空!");
                return ReturnResult.errorResult("密码为空！");
            }
            String realSmsCode = (String) RedisUtils.get("smsCode:" + phoneOrEmail);
            if (verifyCode == null || realSmsCode == null || !verifyCode.equals(realSmsCode)) {
                //验证码为空
                return ReturnResult.errorResult("验证码输入有误！");
            }

            User user = userService.selectByEmailOrPhone(phoneOrEmail);

            if (user != null ){
                if ( user.getPassword().equals(GlobalUtils.getMD5(password + user.getSalt()))){//有用户且密码正确，update进行绑定，然后登录

                    user.setUnionid(unionId);
                    //user.setUserfromType(0);
                    user.setVersion(user.getVersion()+1);
                    userService.update(user);
                    //登录
                    String cookie = GlobalUtils.getMD5(user.getUsername() + System.currentTimeMillis());
                    CookieUtils.setCookie(response, "sso", cookie);
                    RedisUtils.set("cookie:" + cookie, user, 2, TimeUnit.HOURS);
                    //删除登出状态
                    redisTemplate.delete("logout:"+user.getUUID());
                    String ticket = GlobalUtils.generateTicket(user);
                    return ReturnResult.successResult("ticket", ticket, "登录成功！");
                }else{
                    return ReturnResult.errorResult("密码错误！");
                }
            }else{//没有该用户，insert该用户，然后登录
                Class<User> userBean = User.class;
                User userNew = userBean.newInstance();
                String timeStr=String.valueOf(System.currentTimeMillis()).substring(0,5);
                String username ="YM_"+getRandomString(5)+timeStr+GlobalUtils.getRandomString(1);
                boolean flagUser = false ;
                //校验username是否存在
                while(!flagUser){
                     User userL=  userService.selectByUsername(username);
                     if (userL == null){
                         flagUser = true;
                     }else {
                         username ="YM_"+getRandomString(5)+timeStr+GlobalUtils.getRandomString(1);
                         flagUser = false;
                     }
                }
                userNew.setUsername(username);
                String random = GlobalUtils.getRandomString(6);
                String newPassword = GlobalUtils.getMD5(password+random);
                userNew.setPassword(newPassword);
                userNew.setCTime(new Date());
                userNew.setSalt(random);
                userNew.setLevel(0);
                userNew.setUUID(GlobalUtils.generateUUID());
                userNew.setUnionid(unionId);
                userNew.setUserfromType(0);
                userNew.setAvatar(userMap.get("avatar"));
                userNew.setNickname(userMap.get("nickname"));
                if(phoneOrEmail.indexOf("@")<=0){
                    userNew.setMobPhone(phoneOrEmail);
                }else {
                    userNew.setEmail(phoneOrEmail);
                }
                Integer flag = userService.save(userNew);
                if(flag > 0){
                    //登录
                    String cookie = GlobalUtils.getMD5(username + System.currentTimeMillis());
                    CookieUtils.setCookie(response, "sso", cookie);
                    RedisUtils.set("cookie:" + cookie, userNew, 2, TimeUnit.HOURS);
                    String ticket = GlobalUtils.generateTicket(userNew);
                    return ReturnResult.successResult("ticket", ticket, "登录成功！");
                }
                return ReturnResult.successResult("注册失败!");

            }
        }catch(Exception e){
            log.error(e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //随机数字符串
    public String getRandomString(int length) {
        //随机字符串的随机字符库
        String KeyString = "0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }


}
