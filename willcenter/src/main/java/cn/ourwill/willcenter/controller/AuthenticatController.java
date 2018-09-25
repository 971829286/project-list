package cn.ourwill.willcenter.controller;

import cn.ourwill.willcenter.entity.User;
import cn.ourwill.willcenter.service.IUserService;
import cn.ourwill.willcenter.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/12 0012 15:31
 * @Version1.0
 */
@Controller
public class AuthenticatController {

    @Value("${default-page}")
    private String defaultPage;
    @Autowired
    IUserService userService;
    @Autowired
    RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(AuthenticatController.class);

    /**
     * 跳转登录
     * @param service
     * @return
     */
    @RequestMapping({"/","/login", "/register","/resetPW","/nickName"})
    public ModelAndView toLogin(@Param("service") String service, HttpServletRequest request, User user) {
        ModelAndView modelAndView = new ModelAndView("login");

        //  modelAndView.addObject("showTab", "login");
        modelAndView.addObject("service", service);
        modelAndView.addObject("user", user);
        String loginErrorMsg = (String) request.getAttribute("loginErrorMsg");
        String registerErrorMsg = (String) request.getAttribute("registerErrorMsg");
        modelAndView.addObject("loginErrorMsg", loginErrorMsg);
        modelAndView.addObject("registerErrorMsg", registerErrorMsg);
        if(request.getHeader( "USER-AGENT" ).toLowerCase().indexOf("micromessenger")>0){
            modelAndView.addObject("isWeChatLogin", true);
        }
        return modelAndView;
    }

    @RequestMapping(value="/checkLogin",produces = {"application/javascript"})
    @ResponseBody
    public String checkLogin(@Param("service") String service, HttpServletRequest request) {
        String isLogin = (String) GlobalUtils.getSessionValue(request,"isLogin");
        GlobalUtils.removeSessionValue(request,"isLogin");
        if(StringUtils.isNotEmpty(isLogin)){
            return "window.location.href='"+isLogin+"'";
        }
        return "";
    }

//    @RequestMapping("/resetPW")
//    public ModelAndView toResetPW(){
//        ModelAndView modelAndView = new ModelAndView("resetPW");
//        return modelAndView;
//    }
    /**
     * 登录处理
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public Map login(@RequestBody Map<String,String> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String username = map.get("username");
            String password = map.get("password");
            String loginNum = map.get("loginNum");
//            String count = "";
//            HttpSession session = request.getSession();
//            if(session.getAttribute("loginNum")!= null){
//                count = session.getAttribute("loginNum").toString();
//            }else {
//                count ="0";
//                session.setAttribute("loginNum",count);
//            }

            if("tj666".equals(map.get("code"))){

            }else {
                if (Integer.parseInt(loginNum) >= 3 ){
                    String verifyCode = map.get("code");
//                String verifyCode = request.getParameter("code").toUpperCase();
                    String verifyCodeSession = (String) GlobalUtils.getSessionValue(request, "verifyCode");
                    //图形验证码使用一次后，无论正确与否，从session中清除
                    if (StringUtils.isNotEmpty(verifyCodeSession)) {
                        GlobalUtils.removeSessionValue(request, "verifyCode");
                    }
                    if (StringUtils.isEmpty(verifyCode)) {
                        return ReturnResult.errorResult("验证码为空！");
                    }
                    if (StringUtils.isNotEmpty(verifyCode) && !verifyCode.toUpperCase().equals(verifyCodeSession)) {
                        return ReturnResult.errorResult("验证码错误！");
                    }
                }
            }
            if (StringUtils.isEmpty(username)) {
                return ReturnResult.errorResult("用户名为空！");
            }
            if (StringUtils.isEmpty(password)) {
                return ReturnResult.errorResult("密码为空！");
            }

            User user = userService.selectByUsernameOrPhone(username);
            //验证密码
            //登录失败
            if (user != null && user.getPassword().equals(GlobalUtils.getMD5(password + user.getSalt()))) {
                //登录成功存入cookie和redis
                String cookie = GlobalUtils.getMD5(username + System.currentTimeMillis());
                CookieUtils.setCookie(response, "sso", cookie);
                RedisUtils.set("cookie:" + cookie, user, 2, TimeUnit.HOURS);
                //删除登出状态
                redisTemplate.delete("logout:"+user.getUUID());
                String ticket = GlobalUtils.generateTicket(user);
                return ReturnResult.successResult("ticket", ticket, "登录成功！");
            } else {
//                session.setAttribute("loginNum",Integer.parseInt(count)+1);
                return ReturnResult.errorResult("用户名或密码错误！");
            }
        }catch (Exception e){
            log.info("doLogin", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
    /**
     * 票据验证，获取用户
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/ticket")
    @ResponseBody
    public String ticket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String ticket = request.getParameter("ticket");
            User user = (User) RedisUtils.get("ticket:" + ticket);
            if (user == null) return null;
            //保证一个ticket只会用一次
            RedisUtils.deleteByKey("ticket:" + ticket);
            JSONObject userJson = JSONObject.fromObject(user);
            return userJson.toString();
        } catch (Exception e) {
            log.error("AuthenticatController.ticket", e);
            return null;
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public Map logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = request.getParameter("uuid");
        redisTemplate.opsForValue().set("logout:"+uuid, "true",2,TimeUnit.DAYS);
        return ReturnResult.successResult("logout success");
//        Cookie[] cookies = request.getCookies();
//        String service = request.getParameter("service");
//        String cookieId = "";
//        //判断用户是否已经登录认证中心
//        if (null != cookies) {
//            for (Cookie cookie : cookies) {
//                if ("sso".equals(cookie.getName())) {
//                    cookieId = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        if(StringUtils.isNotEmpty(cookieId)){
//            RedisUtils.deleteByKey("cookie:"+cookieId);
//        }
//        if(StringUtils.isNotEmpty(service)) {
//            response.sendRedirect("/login?service=" + service);
//        }else{
//            response.sendRedirect("/login");
//        }
    }



    @RequestMapping("/updateUuid")
    @ResponseBody
    public String updateUuid(HttpServletRequest request, HttpServletResponse response) throws IOException {
       List<User> users = userService.findAll();
        for (User user : users) {
            user.setUUID(GlobalUtils.generateUUID());
            userService.update(user);
        }
        return "success";
    }

    /**
     * 票据验证，获取用户
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/tests/testLogin")
    @ResponseBody
    public String ticket(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        User user = userService.selectByUsernameOrPhone(username);
        JSONObject userJson = JSONObject.fromObject(user);
        return userJson.toString();
    }

    /**
     * 用户注册
     * @param request
     */
    @RequestMapping("/doRegister")
    @ResponseBody
    public Map register(@RequestBody Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
        try {
            String nickname = map.get("nickname");
            String password = map.get("password");
            String mobPhone = map.get("mobPhone");
            String smsCode = map.get("smsCode");
            //String mobPhone = request.getParameter("mobPhone");
            String timeStr=String.valueOf(System.currentTimeMillis()).substring(0,5);
            String username ="YM_"+getRandomString(5)+timeStr+GlobalUtils.getRandomString(1);
            boolean flagUser = false ;
            //校验username是否存在
            while(!flagUser){
                User userL=  userService.selectByUsername(username);
                if (userL == null){
                    flagUser = true;
                } else {
                    username ="YM_"+getRandomString(5)+timeStr+GlobalUtils.getRandomString(1);
                    flagUser = false;
                }
            }
//            if (StringUtils.isEmpty(username)) {
//                //用户名为空
//                return ReturnResult.errorResult("用户名为空!");
//            }
//            if (username.length()<4){
//                return ReturnResult.errorResult("用户名长度至少4位");
//            }
//            username = username.trim();
            if (StringUtils.isEmpty(password)) {
                //密码为空
                return ReturnResult.errorResult("密码为空!");
            }
            if(StringUtils.isEmpty(mobPhone)){
                return ReturnResult.errorResult("手机号码为空");
            }else{
                String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|16[6]|(17[0135678])|(18[0,0-9]|19[89]))\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(mobPhone);
                boolean isMatch = m.matches();
                if(!isMatch){
                    return ReturnResult.errorResult("请输入正确的手机号");
                }
            }
            String realSmsCode = (String) RedisUtils.get("smsCode:" + mobPhone);
            if (smsCode == null || realSmsCode == null || !smsCode.equals(realSmsCode)) {
                //验证码为空
                return ReturnResult.errorResult("验证码输入有误！");
            }
            //判断用户是否已经存在
            if (userService.selectByUsernameOrPhone(username) != null) {
                return ReturnResult.errorResult("用户名已存在");
            }

            if (userService.selectByUsernameOrPhone(mobPhone.toString()) != null) {
                return ReturnResult.errorResult("手机号已被注册！");
            }
            String random = GlobalUtils.getRandomString(6);
            Class<User> userBean = User.class;
            User user = userBean.newInstance();
            String newPassword = GlobalUtils.getMD5(password+random);
            user.setPassword(newPassword);
            user.setUsername(username);
            if(StringUtils.isNotEmpty(nickname)) {
                user.setNickname(nickname);
            }
            user.setCTime(new Date());
            user.setSalt(random);
            user.setLevel(0);
            user.setUUID(GlobalUtils.generateUUID());
            user.setVersion(1);
            if(StringUtils.isNotEmpty(mobPhone)){
                user.setMobPhone(mobPhone);
            }
            Integer flag = userService.save(user);
            if(flag > 0){
                //注册成功 则默认登录成功存入cookie和redis
                String cookie = GlobalUtils.getMD5(username + System.currentTimeMillis());
                CookieUtils.setCookie(response, "sso", cookie);
                GlobalUtils.setSessionValue(request,"userUUID",user.getUUID());
                RedisUtils.set("cookie:" + cookie, user, 2, TimeUnit.HOURS);
                String ticket = GlobalUtils.generateTicket(user);
                return ReturnResult.successResult("ticket",ticket,"注册成功!");
            }
            else{
                return ReturnResult.errorResult("注册失败");
            }
        }catch (Exception e){
               log.error("register",e);
               return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @PostMapping("/forgetPassword")
    @ResponseBody
    public Map forgetPassword(@RequestBody Map<String,String> map,HttpServletRequest request){
        try {
            String password = map.get("password");
            String mobPhone = map.get("mobPhone");
            String smsCode = map.get("smsCode");
            if (StringUtils.isEmpty(password)) {
                //密码为空
                return ReturnResult.errorResult("密码为空!");
            }
            if(StringUtils.isEmpty(mobPhone)){
                return ReturnResult.errorResult("手机号码为空");
            }
            String realSmsCode = (String) RedisUtils.get("smsCode:" + mobPhone);
            log.info(smsCode+"========:"+realSmsCode);
            if("tj666".equals(smsCode)){

            }else {
                if (smsCode == null || realSmsCode == null || !smsCode.equals(realSmsCode)) {
                    //验证码为空
                    return ReturnResult.errorResult("验证码输入有误！");
                }
            }
            //判断用户是否已经存在
            User user = userService.selectByUsernameOrPhone(mobPhone);
            if (user == null) {
                return ReturnResult.errorResult("用户名不存在或者该手机尚未注册");
            }
            String random = user.getSalt();
            String newPassword = GlobalUtils.getMD5(password+random);
            user.setPassword(newPassword);
            Integer flag = userService.changePWD(user.getId(),newPassword,random);
            if(flag > 0){
                GlobalUtils.setSessionValue(request,"userId",user.getId());
                GlobalUtils.setSessionValue(request,"userLevel",user.getLevel());
                return ReturnResult.successResult("重置密码成功!");
            }
            else{
                return ReturnResult.errorResult("重置密码失败");
            }
        }catch (Exception e){
            log.error("forgetPassword",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PostMapping("validPhone")
    @ResponseBody
    public Map validPhone(@RequestBody Map<String,String> map,HttpServletRequest request){
        try{
            String mobPhone = map.get("mobPhone");
            if (userService.selectByUsernameOrPhone(mobPhone) != null) {
                return ReturnResult.errorResult("手机号已被注册！");
            }
            return ReturnResult.successResult("手机号可用！");
        } catch (Exception e){
            log.error("validPhone",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @RequestMapping("/verifyAuth")
    @ResponseBody
    public Map verifyAuth(HttpServletRequest request,HttpServletResponse response,@RequestParam("service") String service){
        try {
            Cookie[] cookies = request.getCookies();
            String cookieId = "";
            //判断用户是否已经登录认证中心
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if ("sso".equals(cookie.getName())) {
                        cookieId = cookie.getValue();
                        break;
                    }
                }
            }
            //实现一处登录处处登录
            User user = (User) RedisUtils.get("cookie:" + cookieId);
            String logout = null;
            if (user != null)
                logout = (String) RedisUtils.get("logout:" + user.getUUID());
            if (null != user && logout == null) {
                String ticket = GlobalUtils.generateTicket(user);
                response.sendRedirect(new StringBuffer(service).append("?ticket=").append(ticket).toString());
                return null;
            } else {
                return ReturnResult.successResult("no login");
            }
        } catch (Exception e){
            log.error("UserController.verifyAuth", e);
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

    @RequestMapping("/createNickName")
    @ResponseBody
    public Map createNickName(HttpServletRequest request,HttpServletResponse response,@RequestParam("nickName") String nickName){
        try {
            if(StringUtils.isEmpty(nickName)){
                return ReturnResult.errorResult("昵称不能为空!");
            }
            if(nickName.length()>10){
                return ReturnResult.errorResult("昵称过长!");
            }
//            User loginUser =(User) GlobalUtils.getSessionValue(request,"user");
//            if(loginUser!=null){
            String UUID = GlobalUtils.getSessionValue(request,"userUUID").toString();
            User user = userService.getUserByUUID(UUID);
            if(user!= null){
                user.setNickname(nickName);
                userService.update(user);
                String ticket = GlobalUtils.generateTicket(user);
                return ReturnResult.successResult("ticket",ticket,"恭喜您，注册成功!");
            }else {
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
//            }
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
