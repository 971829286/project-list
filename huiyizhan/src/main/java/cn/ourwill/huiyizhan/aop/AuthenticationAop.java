package cn.ourwill.huiyizhan.aop;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBase;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IBlackListService;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.utils.CheckMobileUtils;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import cn.ourwill.huiyizhan.weChat.Utils.HttpURlUtils;
import cn.ourwill.huiyizhan.weChat.pojo.TokenReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLEncoder;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 15:48
 * @Version1.0
 */
@Component
@Aspect
@Slf4j
public class AuthenticationAop {
    @Pointcut("execution(* cn.ourwill.huiyizhan.controller..*.*(..))")
    public void executeService(){

    }

    //微信开发者ID
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.redirect_uri}")
    private String redirect_uri;
    //应用授权作用域
    @Value("${weixin.scope}")
    private String scope;
    //开发者密码
    @Value("${weixin.appsecret}")
    private String secret;

    @Autowired
    private WillCenterConstants willCenter;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBlackListService blackListService;

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request =  sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String ticket = request.getParameter("ticket");
        // 获取出方法上的Access注解
        Access access = method.getAnnotation(Access.class);

//            Integer userId = GlobalUtils.getUserId(request);
//            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
//            log.info("AOP+++++userid:"+userId);
//            log.info("AOP+++++userLevel:"+userLevel);
        HttpSession session = request.getSession();
        UserBase user = (UserBase) session.getAttribute("loginUser");
        if (user == null) {
            String flag = "";//用于区分是否是微信用户，不是微信用户不进入授权
            if (session.getAttribute("isWeChatUser") != null){
                 flag = session.getAttribute("isWeChatUser").toString();
            }
            String originUrl = request.getRequestURL().toString();
            //获取门票页面不做登陆处理
            if (!originUrl.endsWith("logout")&&!originUrl.contains("inspectTicket") && CheckMobileUtils.checkWeChat(request.getHeader( "USER-AGENT" )) && (!"false".equals(flag))) {
                if(StringUtils.isEmpty(request.getParameter("code"))){
                    //手机微信授权登录
                    String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" +
                            URLEncoder.encode(originUrl,"UTF-8") + "&response_type=code&scope=" + scope + "#wechat_redirect";
                    log.info("loginUrl:"+ loginURL);
                    response.sendRedirect(loginURL);
                    return;
                }else{
                    try{
                        String getURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + request.getParameter("code") + "&grant_type=authorization_code";
                        ObjectMapper mapper = new ObjectMapper();
                        TokenReturn tokenReturn = null;
                        String returnStr = HttpURlUtils.getUrlResponse(getURL);
                        if (returnStr.indexOf("errcode") <= 0) {
                            //查看是否存在这个微信用户
                            if (returnStr.indexOf("errcode") <= 0) {
                                //查看是否存在这个微信用户
                                tokenReturn = mapper.readValue(returnStr, TokenReturn.class);
                                UserBase userE = userService.selectByUnionId(tokenReturn.getUnionid());
                                if(userE != null){
                                    User loginUser = new User();
                                    //获取本地用户数据id,uuid,level,nickname,username,avatar,info"
                                    loginUser.setId(userE.getId());
                                    loginUser.setLevel(userE.getLevel());
                                    loginUser.setUUID(userE.getUUID());
                                    loginUser.setNickname(userE.getNickname());
                                    loginUser.setUsername(userE.getUsername());
                                    loginUser.setAvatar(userE.getAvatar());
                                    loginUser.setInfo(userE.getInfo());
                                    loginUser.setUnionid(userE.getUnionid());
                                    session.setAttribute("loginUser", loginUser);
                                    session.setAttribute("isWeChatUser",true);
                                }else{
                                    session.setAttribute("isWeChatUser",false);
                                }
                            }
                        }
                    }catch (Exception e){
                        return;
                    }
                }

            }
//            if(!originUrl.contains("/api/")) {
//                Boolean isDoLogin = (Boolean) session.getAttribute("isDoLogin");
//                if (isDoLogin == null) {
//                    session.setAttribute("isDoLogin", true);
//                    response.sendRedirect(willCenter.getCheckLoginUrl() + "?service=" + request.getRequestURL().toString());
//                    return;
//                }
//            }
            //1.判断用户是否有认证凭据--ticket（认证中心生成）
            if (null != ticket && !"".equals(ticket)) {
                try {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod(willCenter.getTicketUrl());
                    //给url添加新的参数
                    postMethod.addParameter("ticket", ticket);
                    //通过httpClient调用SSO Server中的TicektServlet，验证ticket是否有效
                    httpClient.executeMethod(postMethod);
                    //将HTTP方法的响应正文（如果有）返回为String
                    String userString = postMethod.getResponseBodyAsString();
                    if(userString==null) return;
                    JSONObject jsonObject= JSONObject.fromObject(userString);
                    user=(User)JSONObject.toBean(jsonObject, User.class);
                    //释放此HTTP方法正在使用的连接
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //2.判断认证凭据是否有效
                if (null != user) {
                    //session设置用户名，说明用户登录成功了
                    log.info("登录成功："+ user.getUUID());
                    User loginUser = (User) user;
                    //获取本地用户数据
                    User localUser = userService.selectByUuid(loginUser.getUUID());
                    if(localUser==null) {
                        user.setId(null);
                        //如果本地没有该用户 则创建
                        localUser = userService.SyncFormCenter(user);
                    }else if(!loginUser.getVersion().equals(localUser.getVersion())){
                        localUser = userService.SyncFormCenter(user, localUser.getUUID());
                    }
                    //替换主键
                    loginUser.setId(localUser.getId());
                    loginUser.setLevel(localUser.getLevel());
                    session.setAttribute("loginUser", loginUser);
//                    response.sendRedirect(request.getRequestURL().toString());
                    return;
                }
            }
        }
        if (access != null) {
            if(user == null) {
                String url = request.getRequestURL().toString();
                if (url.contains("/api/")) {
                    throw new UnauthenticatedException();
                } else {
                    throw new UnauthenticatedRedirectException(willCenter.getLoginUrl() + "?service=" + URLEncoder.encode(url, "UTF-8"));
                }
            }else if(access.level() == 1&&user.getLevel()!=1){
                throw new UnPermissionException();
            }else if(blackListService.isInBlackList(user.getId())){
                throw new BlacklistInException();
            }
        }
    }
}
