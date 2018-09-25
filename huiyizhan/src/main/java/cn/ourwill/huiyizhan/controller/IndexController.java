package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.*;
import cn.ourwill.huiyizhan.utils.CheckMobileUtils;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.JsonUtil;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import cn.ourwill.huiyizhan.weChat.Utils.HttpURlUtils;
import cn.ourwill.huiyizhan.weChat.pojo.TokenReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cn.ourwill.huiyizhan.entity.Config.systemDomain;

/**
 * 描述：
 *
 * @author uusao
 * @create 2018-03-20 10:38
 **/
@Controller
@Slf4j
public class IndexController {

    //微信开发者ID
    @Value("${weixin.appid}")
    private String appid;
    //授权后重定向的回调链接地址
    @Value("${weixin.redirect_uri}")
    private String redirect_uri;
    //应用授权作用域
    @Value("${weixin.scope}")
    private String scope;
    //开发者密码
    @Value("${weixin.appsecret}")
    private String secret;

    @Value("${system.index.isurl}")
    private String isUrl;

    //前端文件目录
    @Value("${system.index.path}")
    private String indexPath;

    @Autowired
    private IActivityService activityService;
    @Autowired
    private IBannerHomeService bannerHomeService;
    @Autowired
    private ITicketsRecordService ticketsRecordService;
    @Autowired
    private WillCenterConstants willCenter;
    @Autowired
    private IUserService userService;

    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private ISurveyQuestionService surveyQuestionService;

    @Autowired
    private ISurveyAnswerService surveyAnswerService;

    @Value("${system.domain}")
    private String systemDomain;

    @RequestMapping("/")
    public String index(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException {
        User loginUser = GlobalUtils.getLoginUser(request);
        if(loginUser!=null)
            loginUser.setUsername(loginUser.getUsername().replace(loginUser.getUsername().substring(1, loginUser.getUsername().length()-1), "***"));
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("loginUrl",willCenter.getLoginUrl());
        model.addAttribute("registerUrl",willCenter.getRegisterUrl());
        model.addAttribute("systemDomain",systemDomain);
        model.addAttribute("title","映目活动-您的一站式会议活动平台");
        String ticket = request.getParameter("ticket");
        if(StringUtils.isEmpty(ticket)&&loginUser==null) {
            String service = URLEncoder.encode(systemDomain, "UTF-8");
            model.addAttribute("checkLogin", willCenter.getCheckLoginUrl()+"?service=" + service);
        }
        //热门活动
        List<Activity> activitiesHot = activityService.getHotList();
        model.addAttribute("activityHotList",activitiesHot);
        List<Activity> activitiesRecent = activityService.getRecentList();
        model.addAttribute("activityRecentList",activitiesRecent);
        List<BannerHome> bannerHomes = bannerHomeService.findAll();
        model.addAttribute("bannerHomes",bannerHomes);
        if(loginUser!=null){
            Boolean flag = isWeChatLogin(request,loginUser.getUnionid());
            model.addAttribute("isWeChatLogin",flag);
        }
        if(CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())){
            return "mobile_index";
        } else {
            return "index";
        }
    }

    @RequestMapping("/meetingList")
    public String meetingList(HttpServletRequest request,Model model) {
        User loginUser = GlobalUtils.getLoginUser(request);
        if(loginUser!=null)
            loginUser.setUsername(loginUser.getUsername().replace(loginUser.getUsername().substring(1, loginUser.getUsername().length()-1), "***"));
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("loginUrl",willCenter.getLoginUrl());
        model.addAttribute("registerUrl",willCenter.getRegisterUrl());
        model.addAttribute("systemDomain",systemDomain);
        model.addAttribute("title","映目活动-发现活动");
        if(loginUser!=null){
            Boolean flag = isWeChatLogin(request,loginUser.getUnionid());
            model.addAttribute("isWeChatLogin",flag);
        }
        if(CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())){
            return "mobile_meetingList";
        } else {
            return "meetingList";
        }
    }

    @RequestMapping("/price")
    public String price(HttpServletRequest request,Model model) {
        User loginUser = GlobalUtils.getLoginUser(request);
        if(loginUser!=null)
            loginUser.setUsername(loginUser.getUsername().replace(loginUser.getUsername().substring(1, loginUser.getUsername().length()-1), "***"));
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("loginUrl",willCenter.getLoginUrl());
        model.addAttribute("registerUrl",willCenter.getRegisterUrl());
        model.addAttribute("systemDomain",systemDomain);
        model.addAttribute("title","映目活动-费用说明");
        if(loginUser!=null){
            Boolean flag = isWeChatLogin(request,loginUser.getUnionid());
            model.addAttribute("isWeChatLogin",flag);
        }
        if(CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())){
            return "mobile_price";
        } else {
            return "price";
        }
    }

    @RequestMapping("/service")
    public String service(HttpServletRequest request,Model model) {
        User loginUser = GlobalUtils.getLoginUser(request);
        if(loginUser!=null)
            loginUser.setUsername(loginUser.getUsername().replace(loginUser.getUsername().substring(1, loginUser.getUsername().length()-1), "***"));
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("loginUrl",willCenter.getLoginUrl());
        model.addAttribute("registerUrl",willCenter.getRegisterUrl());
        model.addAttribute("systemDomain",systemDomain);
        model.addAttribute("title","映目活动-服务介绍");
        if(loginUser!=null){
            Boolean flag = isWeChatLogin(request,loginUser.getUnionid());
            model.addAttribute("isWeChatLogin",flag);
        }
        if(CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())){
            return "mobile_service";
        } else {
            return "service";
        }
    }

    @RequestMapping("/web/**")
    @ResponseBody
    public ResponseEntity<byte[]> webFed(HttpServletRequest request) throws IOException {
        //判断是否为移动端访问
        String path = "";
        if(CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())){
            path = indexPath+"mobile.html";
        } else {
            path = indexPath+"index.html";
        }
        InputStream ins = null;
        if("false".equals(isUrl)) {
            ins = new FileInputStream(path);
        }else{
            URL url = new URL(path);
            ins =url.openStream();
        }
        byte[] fileContent = IOUtils.toByteArray(ins);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<byte[]>(fileContent, headers, HttpStatus.OK);
    }

    @RequestMapping("/manage/**")
    @ResponseBody
    @Access
    public ResponseEntity<byte[]> manage() throws IOException {
        InputStream ins = null;
        if("false".equals(isUrl)) {
            ins = new FileInputStream(indexPath+"manage.html");
        }else{
            URL url = new URL(indexPath+"manage.html");
            ins =url.openStream();
        }
        byte[] fileContent = IOUtils.toByteArray(ins);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<byte[]>(fileContent, headers, HttpStatus.OK);
    }

    @RequestMapping("/manage/pdf/**")
    @ResponseBody
    public ResponseEntity<byte[]> managepdf() throws IOException {
        InputStream ins = new FileInputStream(indexPath+"manage.html");
        byte[] fileContent = IOUtils.toByteArray(ins);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<byte[]>(fileContent, headers, HttpStatus.OK);
    }

    //用户退出(电脑端)
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //调用用户中心
            if(loginUser!=null) {
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(willCenter.getLogoutUrl());
                postMethod.addParameter("uuid", loginUser.getUUID());
                httpClient.executeMethod(postMethod);
                log.info("登出 session：" + request.getSession().getId());
                request.getSession().invalidate();
            }
            return "redirect:/";
        }catch (Exception e){
            log.error("UserController.logout",e);
            return null;
        }
    }

    @RequestMapping("/inspectTicket/{activityId}")
    public String inspectTicket(HttpServletRequest request, @PathVariable("activityId") Integer activityId,
                                @RequestParam("openId") String openId,Model model) {
        Activity activity = activityService.getById(activityId);
        if(activity!=null) {
            String address;
            if(1 == activity.getIsOnline()){
                address =  "线上活动";
            }else{
                address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
            }
//            String addStr = activity.getActivityAddress();
//            JSONObject jsonObject = JSONObject.fromObject(addStr);
//            addStr = jsonObject.getString("province") + " " + jsonObject.getString("city") + " " + jsonObject.getString("address");
            activity.setActivityAddress(address);
            List<TicketsRecord> ticketsRecordList = ticketsRecordService.selectTicketsByopenIdAndActivityId(openId, activityId);
            model.addAttribute("activity", activity);
            model.addAttribute("ticketsRecordList", ticketsRecordList);
        }
        return "inspect_ticket";

    }

    /**
     *进入授权页面
     * @param request
     * @param response
     */
    @RequestMapping("/toAutonrize")
    public void toAutonrizePage(HttpServletRequest request,HttpServletResponse response){
        try{
            if (CheckMobileUtils.check(request.getHeader( "USER-AGENT" ).toLowerCase())) {
                //手机微信授权登录
                String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" +
                        URLEncoder.encode(redirect_uri,"UTF-8") + "&response_type=code&scope=" + scope + "#wechat_redirect";
                log.info("loginUrl:"+ loginURL);
                response.sendRedirect(loginURL);
            }
        }catch (Exception e){
        }
    }

    /**
     * 获取unionId,登录
     * @param code
     * @return
     */
    @RequestMapping("/mobileWeChatLogin")
    public String mobileWeChatLogin(@RequestParam("code") String code,HttpServletRequest request,Model model){
        //通过code换取网页授权access_token
        //appid:应用唯一标识
        //secret:应用密钥AppSecret
        //code:填写第一步获取的code参数
        //grant_type:填写为authorization_code
        String getURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        ObjectMapper mapper = new ObjectMapper();
        try{
            TokenReturn tokenReturn = null;
            String returnStr = HttpURlUtils.getUrlResponse(getURL);
            if (returnStr.indexOf("errcode") <= 0) {
                //查看是否存在这个微信用户
                if (returnStr.indexOf("errcode") <= 0) {
                    //查看是否存在这个微信用户
                    tokenReturn = mapper.readValue(returnStr, TokenReturn.class);
                    UserBase user = userService.selectByUnionId(tokenReturn.getUnionid());
                    if(user != null){
                        User loginUser = new User();
                        HttpSession session = request.getSession();
                        //获取本地用户数据id,uuid,level,nickname,username,avatar,info"
                        loginUser.setId(user.getId());
                        loginUser.setLevel(user.getLevel());
                        loginUser.setUUID(user.getUUID());
                        loginUser.setNickname(user.getNickname());
                        loginUser.setUsername(user.getUsername());
                        loginUser.setAvatar(user.getAvatar());
                        loginUser.setInfo(user.getInfo());
                        session.setAttribute("loginUser", loginUser);
                    }
                }
//                tokenReturn = mapper.readValue(returnStr, TokenReturn.class);
//                HttpClient httpClient = new HttpClient();
//                GetMethod getMethod = new GetMethod(willCenter.getUserByUnionId() + "?unionId="+tokenReturn.getUnionid());
//                httpClient.executeMethod(getMethod);
//
//                String userString = getMethod.getResponseBodyAsString();
//                JSONObject jsonObject = JSONObject.fromObject(userString);
//                User user = (User) JSONObject.toBean(jsonObject, User.class);
//                if(user != null){
//                    User loginUser = user;
//                    HttpSession session = request.getSession();
//                    //获取本地用户数据
//                    User localUser = userService.selectByUuid(loginUser.getUUID());
//                    if (localUser == null) {
//                        user.setId(null);
//                        //如果本地没有该用户 则创建
//                        localUser = userService.SyncFormCenter(user);
//                    } else {
//                        localUser = userService.SyncFormCenter(user, localUser.getUUID());
//                    }
//                    loginUser.setId(localUser.getId());
//                    loginUser.setLevel(localUser.getLevel());
//                    session.setAttribute("loginUser", loginUser);
//                    //释放此HTTP方法正在使用的连接
//                    getMethod.releaseConnection();
//                }
            }
            return "redirect:"+systemDomain;
        }catch (Exception e){
            log.error("er",e);
            return "redirect:"+systemDomain;
        }
    }

    /**
     * 判断是否微信客户端登录
     * @param request
     * @param unionid
     * @return
     */
    public boolean isWeChatLogin(HttpServletRequest request,String unionid){
        if((null != unionid && !"".equals(unionid))&&CheckMobileUtils.checkWeChat(request.getHeader( "USER-AGENT" ))){
            return false;
        }
        return true;
    }

    @RequestMapping("/binding/success")
    public ModelAndView bindingSuccess(@RequestParam("key") String key){
        ModelAndView modelAndView = new ModelAndView("success");
        if(key.equalsIgnoreCase("bindingEmail")){
            modelAndView.addObject("message","邮箱绑定成功");
        }
        if(key.equalsIgnoreCase("unbindingEmail")){
            modelAndView.addObject("message","邮箱解绑成功");
        }
        return modelAndView;
    }

    @RequestMapping("/binding/defeat")
    public ModelAndView bindingDefeat(@RequestParam("key") String key){
        ModelAndView modelAndView = new ModelAndView("defeat");
        if(key.equalsIgnoreCase("bindingEmail")){
            modelAndView.addObject("message","邮箱绑定失败");
        }
        if(key.equalsIgnoreCase("unbindingEmail")){
            modelAndView.addObject("message","邮箱解绑失败");
        }
        return modelAndView;
    }

    @RequestMapping("/dic")
    public ModelAndView goDic(){
        return  new ModelAndView("/dictionary/dic");
    }

    @RequestMapping("/ticket")
    public ModelAndView goTicket(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/emailAndPdf/PDFTemplate");

        TicketsRecord ticketsRecord = ticketsRecordService.getById(619);
        Activity activity = activityService.getById(ticketsRecord.getActivityId());
        String address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();

//        Activity activity = (Activity) map.get("activity");
//        List<TicketsRecord> ticketsRecords = (List<TicketsRecord>)map.get("ticketsRecords");
//        String address = (String) map.get("address");
        List<TicketsRecord> ticketsRecords = new ArrayList<>();
        ticketsRecords.add(ticketsRecord);

        modelAndView.addObject("ticketsRecords",ticketsRecords);
        modelAndView.addObject("address",address);
        modelAndView.addObject("activity",activity);
        return modelAndView;
    }

    @RequestMapping("/export")
    public ModelAndView goExport(@RequestParam("surveyId") Integer surveyId, @RequestParam("ids") String ids) {
        ModelAndView modelAndView = new ModelAndView("/projectExports/projectExport");

        if(StringUtils.isEmpty(ids)){
            return null;
        }
        Map<String,String> typeMap = new HashMap();


//        饼状图 1 柱形图2 条形图3
        Survey survey = surveyService.getSurveyWithQuestionById(surveyId);
        if(survey == null) {
            return null;
        }
        Map map = surveyAnswerService.getAnswerStatisticsBySurveyId(surveyId);
        List<SurveyQuestion> answerStatistics = (List<SurveyQuestion>) map.get("answerStatistics");
//        answerStatistics.parallelStream()

        //        '类型索引：0-填空，1-单选，2-多选，3-打星题，5-图片选择题
        //筛选出单选题和多选题
        Predicate<SurveyQuestion> predicateType = surveyQuestion -> !Objects.equals(surveyQuestion.getType(), 1)
                && !Objects.equals(surveyQuestion.getType(), 2);

        if(!ids.contains("*")){
            String[] kv = ids.split(",");
            for(int i = 0; i < kv.length; i ++){
                String[] temp = kv[i].split("-");
                typeMap.put(temp[0],temp[1]);
            }
            Predicate<SurveyQuestion> predicateID = surveyQuestion->typeMap.containsKey(surveyQuestion.getId().toString()) == false;
            answerStatistics.removeIf(predicateType);
            answerStatistics.removeIf(predicateID);
            answerStatistics.stream().sorted((x,y)->{
                return x.getId().compareTo(y.getId());
            }).forEach(e->{
                if(typeMap.get(e.getId().toString()) != null) {
                    e.setChartType(typeMap.get(e.getId().toString()));
                }
            });

        }else{
            answerStatistics.removeIf(predicateType);
            String[] kv = ids.split("-");
            answerStatistics.stream().sorted((x,y)->{
                return x.getId()-y.getId();
            }).forEach(e->{
                e.setChartType(kv[1]);
            });
        }
        modelAndView.addObject("survey", survey);
        modelAndView.addObject("answerStatistics", answerStatistics);
        return modelAndView;
    }

}
