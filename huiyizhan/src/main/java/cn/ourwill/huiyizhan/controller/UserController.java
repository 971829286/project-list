package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.aop.UnauthenticatedException;
import cn.ourwill.huiyizhan.baseEnum.EmailType;
import cn.ourwill.huiyizhan.config.RabbitMqConfig;
import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.service.impl.GenerateTicketService;
import cn.ourwill.huiyizhan.service.search.IElasticSearchService;
import cn.ourwill.huiyizhan.service.search.ISearchService;
import cn.ourwill.huiyizhan.utils.*;
import cn.ourwill.huiyizhan.weChat.Utils.HttpURlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.ourwill.huiyizhan.entity.Config.systemDomain;


/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-03-30 18:05
 **/
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Value("${Amap.key}")
    private String AmpKey;

    @Value("${Amap.url}")
    private String AmpUrl;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserStatisticsService userStatisticsService;

//    @Autowired
//    private IUserBlackService userBlackService;

    @Autowired
    private WillCenterConstants willCenter;

    @Autowired
    IElasticSearchService elasticSearchService;

    @Autowired
    ISearchService searchService;

    @Autowired
    GenerateTicketService generateTicketService;


    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    Queue queue;

    @Autowired
    TopicExchange exchange;
    /**
     * <pre>
     * 根据指定用户id 获取用户基本信息
     *      不指定，默认查询当前用户
     * </pre>
     */
    @GetMapping
    public Map getUserInfo(Integer id, HttpServletRequest request) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            UserBasicInfo userInfo = null;
            if (id == null) {
                if (loginUser == null || loginUser.getId() == null) {  // id = null 且 未登录 ，不查
                    throw new UnauthenticatedException();
                } else {
                    // id = null 但登录了 ，查登录人的
                    userInfo = userService.getBasicInfoById(loginUser.getId());
                }
            } else {
                if (loginUser == null || loginUser.getId() == null) { //id !=null  但没登录 ，按id 查
                    userInfo = userService.getBasicInfoById(id);
                } else {
                    //id !=null  且登录 ，双向查询
                    userInfo = userService.getBasicInfoById(id, loginUser.getId());
                }
                if (loginUser == null || !id.equals(loginUser.getId()))
                    //发送redis消息, 当前浏览用户 人气 +1
                    userStatisticsService.addPopularity(id);
            }
            if (userInfo == null) {
//                String username = userInfo.getUsername();
//                if(StringUtils.isNotEmpty(username)&&username.length()>3)
//                    userInfo.setUsername(username.replace(username.substring(1, username.length()-1), "***"));
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            return ReturnResult.successResult("data", userInfo, ReturnType.GET_SUCCESS);
        } catch (UnauthenticatedException e) {
            throw e;
        } catch (Exception e) {
            log.info("UserController.getUserInfo", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     * 获取当前登录用户详细信息
     * </pre>
     */
    @GetMapping("/detail")
    @Access
    public Map getUserDetail(HttpServletRequest request) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
//            userInfo = userService.getBasicInfoById(id);
//            //发送redis消息, 当前浏览用户 人气 +1
//            userStatisticsService.addPopularity(id);
            if (loginUser == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            User reUser = userService.getById(loginUser.getId());
            String mobPhone = reUser.getMobPhone();
            if (StringUtils.isNotEmpty(mobPhone) && mobPhone.length() >= 11)
                reUser.setMobPhone(mobPhone.replace(mobPhone.substring(3, 7), "****"));
            return ReturnResult.successResult("data", reUser, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("UserController.getUserDetail", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *     查看他人时调用的接口
     * 按用户id获取用户详细信息
     * </pre>
     */
    @GetMapping("{userId}/detail")
    @Access(level = 1)
    public Map getUserDetail(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null || loginUser.getId() == null) {
                throw new UnPermissionException();
            }
            User userInfo = userService.getById(userId);
            if (userInfo == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            //发送redis消息, 当前浏览用户 人气 +1
//            userStatisticsService.addPopularity(userId);
//            String mobPhone = userInfo.getMobPhone();
//            if(StringUtils.isNotEmpty(mobPhone)&&mobPhone.length()>=11)
//                userInfo.setMobPhone(mobPhone.replace(mobPhone.substring(3, 7), "****"));
            return ReturnResult.successResult("data", userInfo, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("UserController.getUserDetail", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 返回用户列表或者按照手机号或者用户名查询
     *
     * @param param
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    @Access(level = 1)
    public Map listUsers(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestBody(required = false) Map<String, String> param) {
        try {
            if (param == null) param = new HashMap<>();
//            String mobPhone = param.get("mobPhone");
//            String userName = param.get("userName");
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<User> listUser = new PageInfo<>(userService.selectByParams(param));
            return ReturnResult.successResult("data", listUser, ReturnType.LIST_SUCCESS);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 判断当前登录用户的角色
     *
     * @param request
     * @return
     */
//    @PostMapping("/getUserLevel")
//    public Map isAccess(HttpServletRequest request) {
//        User user = GlobalUtils.getLoginUser(request);
//        if (user == null) {
//            return ReturnResult.errorResult("未登录");
//        }
//        Integer level = user.getLevel();
//        if (level != null && level == 1)
//            return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
//        else
//            return ReturnResult.errorResult("当前用户不是管理员");
//    }

    /**
     * 按照用户名或者手机号查询用户信息
     *
     * @param param
     * @return
     */
//    @PostMapping("/findUserByMobPhoneOrUserName")
//    public Map getUserByPhoneOrUsername(@RequestBody Map<String, String> param) {
//        String userName = param.get("userName");
//        String mobPhone = param.get("mobPhone");
//        User user = null;
//        if (StringUtils.isEmpty(userName) && StringUtils.isEmpty(mobPhone)) {
//            return ReturnResult.errorResult("参数信息不完整");
//        } else if (StringUtils.isNotEmpty(userName)) {
//            user = userService.selectUserByUsername(userName);
//            if (user != null) {
//                return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
//            }
//        } else if (StringUtils.isNotEmpty(mobPhone)) {
//            user = userService.selectUserByMobPhone(mobPhone);
//            if (user != null) return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
//            else return ReturnResult.errorResult("该用户不存在");
//        }
//        return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//
//    }


    /**
     * 管理员添加用户
     *
     * @param param
     * @return
     */
    @PostMapping("/addUser")
    @Access(level = 1)
    public Map addUserByAdmin(@RequestBody Map<String, String> param) {
        String userName = param.get("username");
        String mobPhone = param.get("mobPhone");
        String password = param.get("password");
        String confirmPassword = param.get("confirmPassword");
        String level = param.get("level");
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(mobPhone) || StringUtils.isEmpty(confirmPassword)) {
            return ReturnResult.errorResult("确认信息是否完整!");
        }
        if (!password.equals(confirmPassword)) {
            return ReturnResult.errorResult("两次密码不一致!");
        }
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(willCenter.getAddUserUrl());
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(param);
        ObjectMapper mapper = new ObjectMapper();
        try {
            StringEntity s = new StringEntity(jsonObject.toString(), "UTF-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            httpPost.setEntity(s);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String res = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
                Map resMap = mapper.readValue(res, Map.class);
                if (resMap.get("code").toString().equals("0")) {
                    //同步数据
                    JsonNode jsonNode = mapper.readTree(res);
                    String data = jsonNode.get("data").toString();
                    UserBase user = mapper.readValue(data, UserBase.class);
                    user.setLevel((level != null && level.equals("1")) ? 1 : 0);
                    userService.SyncFormCenter(user);
                }
                return resMap;
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (IOException e) {
            log.info("userController.addUserByAdmin", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 管理员修改用户信息
     *
     * @param param
     * @return
     */
    @PostMapping("/edit/{uuid}")
    @Access(level = 1)
    public Map editUserInfo(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> param, HttpServletRequest request) {
//        String uuid = param.get("uuid");
        String mobPhone = param.get("mobPhone");
        String nickName = param.get("nickName");
        String avatar = param.get("avatar");
        String info = param.get("info");
//        if (StringUtils.isEmpty(uuid)) {
//            User user = GlobalUtils.getLoginUser(request);
//            uuid = user.getUUID();
//        }
        JSONObject jspnParam = new JSONObject();
        jspnParam.put("uuid", uuid);
        if (StringUtils.isNotEmpty(mobPhone))
            jspnParam.put("mobPhone", mobPhone);
//        if(StringUtils.isNotEmpty(nickName))
        jspnParam.put("nickName", nickName);
        if (StringUtils.isNotEmpty(avatar))
            jspnParam.put("avatar", avatar);
//        if(StringUtils.isNotEmpty(info))
        jspnParam.put("info", info);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(willCenter.getEditUserInfoUrl());
        ObjectMapper mapper = new ObjectMapper();
        try {
            StringEntity s = new StringEntity(jspnParam.toString(), "UTF-8");
            s.setContentType("application/json");
            s.setContentEncoding("UTF-8");
            post.setEntity(s);
            CloseableHttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                Map resMap = mapper.readValue(res, Map.class);
                JsonNode jsonNode = mapper.readTree(res);
                String code = resMap.get("code").toString();
                if ("0".equals(code)) {
                    String userStr = jsonNode.get("data").toString();
                    UserBase user = mapper.readValue(userStr, UserBase.class);
                    User reUser = userService.SyncFormCenter(user, uuid);
                    //同步ES的数据
                    List<SearchBean> searchBeans = searchService.getSearchBeanByUserId(reUser.getId());
                    elasticSearchService.inserts(searchBeans);
                    return ReturnResult.successResult("data", reUser, ReturnType.UPDATE_SUCCESS);
                }
                return resMap;
            } else {
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
        } catch (IOException e) {
            log.info("UserController.editUserInfo", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改当前用户信息
     *
     * @param param
     * @return
     */
    @PostMapping("/edit")
    @Access
    public Map editUserInfo(@RequestBody Map<String, String> param, HttpServletRequest request) {
//        String uuid = param.get("uuid");
        User loginUser = GlobalUtils.getLoginUser(request);
        String uuid = loginUser.getUUID();
//        String mobPhone = param.get("mobPhone");
        String nickname = param.get("nickname");
        String avatar = param.get("avatar");
        String info = param.get("info");
//        if (StringUtils.isEmpty(uuid)) {
//            User user = GlobalUtils.getLoginUser(request);
//            uuid = user.getUUID();
//        }
        JSONObject jspnParam = new JSONObject();
        jspnParam.put("uuid", uuid);
//        if(StringUtils.isNotEmpty(mobPhone))
//            jspnParam.put("mobPhone", mobPhone);
//        if(StringUtils.isNotEmpty(nickname))
        jspnParam.put("nickname", nickname);
        if (StringUtils.isNotEmpty(avatar))
            jspnParam.put("avatar", avatar);
//        if(StringUtils.isNotEmpty(info))
        jspnParam.put("info", info);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(willCenter.getEditUserInfoUrl());
        ObjectMapper mapper = new ObjectMapper();
        try {
            StringEntity s = new StringEntity(jspnParam.toString(), "UTF-8");
            s.setContentType("application/json");
            s.setContentEncoding("UTF-8");
            post.setEntity(s);
            CloseableHttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                Map resMap = mapper.readValue(res, Map.class);
                JsonNode jsonNode = mapper.readTree(res);
                String code = resMap.get("code").toString();
                if ("0".equals(code)) {
                    String userStr = jsonNode.get("data").toString();
                    UserBase user = mapper.readValue(userStr, UserBase.class);
                    User newUser = userService.SyncFormCenter(user, uuid);
                    if (newUser != null) {
                        GlobalUtils.setSessionValue(request, "loginUser", newUser);
                    }
                    //同步ES的数据
                    List<SearchBean> searchBeans = searchService.getSearchBeanByUserId(loginUser.getId());
                    elasticSearchService.inserts(searchBeans);
                    return ReturnResult.successResult("data", newUser, ReturnType.UPDATE_SUCCESS);
                }
                return resMap;
            } else {
                log.info("请求"+willCenter.getEditUserInfoUrl()+"失败,状态码:"+response.getStatusLine().getStatusCode());
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
        } catch (IOException e) {
            log.info("UserController.editUserInfo", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改用户密码
     *
     * @param param
     * @return
     */
    @PostMapping("/resetPassword/{uuid}")
    @ResponseBody
    @Access(level = 1)
    public Map editUserPassword(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> param, HttpServletRequest request) {
        try {
            String password = param.get("password");
            String confirmPassword = param.get("confirmPassword");
            if (!password.equals(confirmPassword)) {
                return ReturnResult.errorResult("两次密码不一致!");
            }
            if (StringUtils.isNotEmpty(password)) {
                JSONObject jspnParam = new JSONObject();
                jspnParam.put("uuid", uuid);
                jspnParam.put("password", password);
                jspnParam.put("isVerify", false);//无需校验
                StringEntity s = new StringEntity(jspnParam.toString(), "UTF-8");
                s.setContentType("application/json");
                s.setContentEncoding("UTF-8");
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(willCenter.getChangePasswordUrl());
                ObjectMapper mapper = new ObjectMapper();
                post.setEntity(s);
                CloseableHttpResponse reponse = httpClient.execute(post);
                if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = reponse.getEntity();
                    InputStream inputStream = entity.getContent();
                    String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    Map resMap = mapper.readValue(res, Map.class);
                    JsonNode jsonNode = mapper.readTree(res);
                    String code = resMap.get("code").toString();
                    if ("0".equals(code)) {
                        String userStr = jsonNode.get("data").toString();
                        UserBase user = mapper.readValue(userStr, UserBase.class);
                        userService.SyncFormCenter(user, uuid);
                    }
                    return resMap;
                }
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            } else {
                return ReturnResult.errorResult("请将信息填写完整!");
            }
        } catch (Exception e) {
            log.info("UserController.editUserPassword.editUserInfo", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改当前用户密码
     *
     * @param param
     * @return
     */
    @PostMapping("/editPassword")
    @ResponseBody
    @Access
    public Map editUserPassword(@RequestBody Map<String, String> param, HttpServletRequest request) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            String uuid = loginUser.getUUID();
            String password = param.get("password");
            String oldPassword = param.get("oldPassword");
            String confirmPassword = param.get("confirmPassword");
            if (!password.equals(confirmPassword)) {
                return ReturnResult.errorResult("两次密码不一致!");
            }
            if (StringUtils.isNotEmpty(password)) {
                JSONObject jspnParam = new JSONObject();
                jspnParam.put("uuid", uuid);
                jspnParam.put("password", password);
                jspnParam.put("oldPassword", oldPassword);
                jspnParam.put("isVerify", true);//校验原密码
                StringEntity s = new StringEntity(jspnParam.toString(), "UTF-8");
                s.setContentType("application/json");
                s.setContentEncoding("UTF-8");
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(willCenter.getChangePasswordUrl());
                ObjectMapper mapper = new ObjectMapper();
                post.setEntity(s);
                CloseableHttpResponse reponse = httpClient.execute(post);
                if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = reponse.getEntity();
                    InputStream inputStream = entity.getContent();
                    String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    Map resMap = mapper.readValue(res, Map.class);
                    JsonNode jsonNode = mapper.readTree(res);
                    String code = resMap.get("code").toString();
                    if ("0".equals(code)) {
                        String userStr = jsonNode.get("data").toString();
                        UserBase user = mapper.readValue(userStr, UserBase.class);
                        userService.SyncFormCenter(user, uuid);
                        return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
                    } else {
                        return resMap;
                    }
                }
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            } else {
                return ReturnResult.errorResult("请将信息填写完整!");
            }
        } catch (Exception e) {
            log.info("UserController.editUserPassword", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 编辑用户信息-一般用户
     *
     * @param param
     * @return
     */
    /*@PostMapping("/editUserInfo")
    public Map edidUserInfo(@RequestBody Map<String, String> param, HttpServletRequest request) {
        User loginUser = GlobalUtils.getLoginUser(request);
        String uuid = "";
        if (loginUser != null) {
            uuid = loginUser.getUUID();
        } else {
            return ReturnResult.errorResult("用户未登录");
        }
        String nickName = GlobalUtils.paramHelperString(param.get("nickName"), "");
        String mobPhone = GlobalUtils.paramHelperString(param.get("mobPhone"), "");
        String avatar = GlobalUtils.paramHelperString(param.get("avatar"), "");
        String introduction = GlobalUtils.paramHelperString(param.get("introduction"), "");
        if (StringUtils.isEmpty(nickName) && StringUtils.isEmpty(mobPhone)
                && StringUtils.isEmpty(avatar) && StringUtils.isEmpty(introduction)) {
            return ReturnResult.errorResult("信息不足");
        }
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(WillCenterEditUserInfo);
        postMethod.addParameter("uuid", uuid);
        if (StringUtils.isNotEmpty(nickName)) {
            postMethod.addParameter("nickName", nickName);
        }
        if (StringUtils.isNotEmpty(mobPhone)) {
            postMethod.addParameter("mobPhone", mobPhone);
        }
        if (StringUtils.isNotEmpty(avatar)) {
            postMethod.addParameter("avatar", avatar);
        }
        if (StringUtils.isNotEmpty(introduction)) {
            postMethod.addParameter("introduction", introduction);
        }

        try {
            httpClient.executeMethod(postMethod);
            String res = postMethod.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map resMap = mapper.readValue(res, Map.class);
            JsonNode jsonNode = mapper.readTree(res);
            String userStr = jsonNode.get("data").toString();
            UserBase user = mapper.readValue(userStr, UserBase.class);
            String code = resMap.get("code").toString();
            if ("0".equals(code)) {
                SyncDate.SyncFormCenter(user, true);
                return ReturnResult.successResult("修改成功");
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
        return ReturnResult.errorResult("修改失败");
    }
    */

//    /**
//     * 修改用户密码-一般用户
//     *
//     * @param request
//     * @param password
//     * @return
//     */
    /*
    @PostMapping("/editUserPassword")
    public Map editUserPassword(HttpServletRequest request, @Param("password") String password) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) return ReturnResult.errorResult("用户未登录");
            String uuid = loginUser.getUUID();
            if (StringUtils.isNotEmpty(uuid) && StringUtils.isNotEmpty(password)) {
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(WillCenterChangePassword);
                postMethod.addParameter("uuid", uuid);
                postMethod.addParameter("password", password);
                httpClient.executeMethod(postMethod);
                String res = postMethod.getResponseBodyAsString();
                ObjectMapper mapper = new ObjectMapper();
                Map resMap = mapper.readValue(res, Map.class);
                String code = resMap.get("code").toString();
                if (code != null && "0".equals(code)) {
                    return ReturnResult.successResult("密码修改成功");
                } else {
                    return resMap;
                }
            } else {
                return ReturnResult.errorResult("请将信息填写完整");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }*/

    /**
     * 校验手机号
     *
     * @param request
     * @param param
     * @return
     */
    @PostMapping("/verifyOldPhone")
    @Access
    public Map verifyOldPhone(HttpServletRequest request, @RequestBody Map<String, String> param) {
        String mobPhone = param.get("mobPhone");
        User loginUser = GlobalUtils.getLoginUser(request);
        UserBase userBase = userService.getWillCenterUserByUUID(loginUser.getUUID());
        userService.SyncFormCenter(userBase, loginUser.getUUID());
        if (loginUser.getMobPhone().equals(mobPhone))
            return ReturnResult.successResult("验证通过！");
        return ReturnResult.errorResult("验证失败！");
    }

    /**
     * 发送手机验证码
     *
     * @param request
     * @return
     */
    @PostMapping("/sendMessage")
    public Map sendMessage(HttpServletRequest request) {
        try {
            String mobPhone = request.getParameter("mobPhone");
            if (StringUtils.isNotEmpty(mobPhone)) {
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(willCenter.getSendMessageUrl());
                postMethod.addParameter("mobPhone", mobPhone);
                httpClient.executeMethod(postMethod);
                String res = postMethod.getResponseBodyAsString();
                ObjectMapper mapper = new ObjectMapper();
                Map resMap = mapper.readValue(res, Map.class);
                return resMap;
            } else {
                return ReturnResult.errorResult("参数无效");
            }
        } catch (Exception e) {
            log.error("UserController.sendMessage", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改用户手机号码
     *
     * @param param
     * @return
     */
    @PostMapping("/editMobPhone")
    @Access
    public Map editUserMobPhone(HttpServletRequest request, @RequestBody Map<String, String> param) {
        try {
            String mobPhone = param.get("mobPhone");
            String smsCode = param.get("smsCode");
            User loginUser = GlobalUtils.getLoginUser(request);
            String uuid = loginUser.getUUID();
            if (StringUtils.isNotEmpty(uuid) && StringUtils.isNotEmpty(mobPhone)) {
                JSONObject jspnParam = new JSONObject();
                jspnParam.put("uuid", uuid);
                jspnParam.put("smsCode", smsCode);
                jspnParam.put("mobPhone", mobPhone);
                StringEntity s = new StringEntity(jspnParam.toString(), "UTF-8");
                s.setContentType("application/json");
                s.setContentEncoding("UTF-8");
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(willCenter.getChangeMobPhoneUrl());
                ObjectMapper mapper = new ObjectMapper();
                post.setEntity(s);
                CloseableHttpResponse reponse = httpClient.execute(post);
                if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = reponse.getEntity();
                    InputStream inputStream = entity.getContent();
                    String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    Map resMap = mapper.readValue(res, Map.class);
                    String code = resMap.get("code").toString();
                    if ("0".equals(code)) {
                        JsonNode jsonNode = mapper.readTree(res);
                        String userStr = jsonNode.get("data").toString();
                        UserBase user = mapper.readValue(userStr, UserBase.class);
                        userService.SyncFormCenter(user, uuid);
                        return ReturnResult.successResult("修改成功");
                    } else {
                        return resMap;
                    }
                }
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            } else {
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
        } catch (Exception e) {
            log.info("UserController.editPhone", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取用户的UUID
     *
     * @param param
     * @return
     */
    @RequestMapping("/getUserUUID")
    public Map getUserUUID(@RequestBody Map<String, String> param) {
        String userName = param.get("userName");
        if (StringUtils.isEmpty(userName)) {
            return ReturnResult.errorResult("请将信息填写正确");
        } else {
            String uuid = "";
            if (StringUtils.isNotEmpty(userName)) {
                User user = userService.selectUserByUsername(userName);
                if (user != null) {
                    uuid = user.getUUID();
                    return ReturnResult.successResult("data", uuid, ReturnType.GET_SUCCESS);
                } else {
                    return ReturnResult.errorResult("该用户不存在");
                }
            }
        }
        return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
    }

//    /**
//     * 添加黑名单
//     */
//    @PostMapping("/black/{userId}")
//    @Access
//    public Map addBlack(@PathVariable("userId") Integer userId, @RequestBody UserBlack userBlack) {
//        try {
//            Integer code = userBlackService.save(userBlack);
//            if (code <= 0) {
//                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
//            } else {
//                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("UserController.addBlack", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//

//    /**
//     * 编辑黑名单
//     */
//    @PutMapping("/black/{userId}")
//    @Access
//    public Map editBlack(@PathVariable("userId") Integer userId, @RequestBody UserBlack userBlack) {
//        try {
//            Integer code = userBlackService.editBlack(userBlack);
//            if (code <= 0) {
//                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
//            } else {
//                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("UserController.editBlack", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//
//    /**
//     * 批量删除黑名单
//     */
//    @DeleteMapping("/black")
//    @Access
//    public Map deleteBlack(@RequestBody List<Integer> ids) {
//        try {
//            Integer code = userBlackService.delete(ids);
//            if (code <= 0) {
//                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
//            } else {
//                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("UserController.deleteBlack", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/black")
//    @Access
//    public Map getBlack(String userName, @RequestParam(value = "page", defaultValue = "1") Integer page,
//                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//                        @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize,
//                        @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
//        try {
//            if (orderBy != null && orderBy == 0) {
//                PageHelper.orderBy(" id desc");
//            } else if (orderBy != null && orderBy == 1) {
//                PageHelper.orderBy(" id asc");
//            }
//            if (page != null && page == 1) {
//                PageHelper.startPage(pageNum, pageSize);
//                PageInfo<UserBlack> pages = new PageInfo<UserBlack>(userBlackService.getBlacks(userName));
//                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
//            }
//            List<UserBlack> userBlacks = userBlackService.getBlacks(userName);
//            return ReturnResult.successResult("data", userBlacks, ReturnType.GET_SUCCESS);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("UserController.getBlack", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }

//    @PutMapping("/{userId}")
//    @Access
//    public Map editUser(HttpServletRequest request, @RequestBody User user, @PathVariable("userId") Integer userId) {
//        try {
//            User loginUser = GlobalUtils.getLoginUser(request);
//            if (loginUser == null || loginUser.getId() == null) return ReturnResult.errorResult("用户未登录");
//            if (loginUser.getId() - userId != 0 || loginUser.getId() - user.getId() != 0) {
//                return ReturnResult.errorResult("非法操作");
//            }
//            user.setUTime(new Date());
//            Integer code = userService.update(user);
//            if (code < 0) {
//                return ReturnResult.errorResult("修改失败");
//            }
//            return ReturnResult.successResult("修改成功");
//        } catch (UnPermissionException e) {
//            throw e;
//        } catch (Exception e) {
//            log.info(e.getMessage());
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }

    /**
     * 验证邮箱绑定
     *
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/email")
    @Access
    public Map bindingEmail(HttpServletRequest request, @RequestBody Map<String, String> map) {
        User loginUser = GlobalUtils.getLoginUser(request);
        String email = map.get("email");
        String codeFromUser = map.get("code");
        String type = map.get("type");
        String codeFromSession = (String) GlobalUtils.getSessionValue(request, "emailCode");
        String key = "NULL";
        if (type.equalsIgnoreCase("binding")) {
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(codeFromUser)) {
                return ReturnResult.errorResult("error", "-1", "信息填写不完整");
            }
            if (loginUser == null) {
                return ReturnResult.errorResult("error", "-2", "用户不存在");
            }
            if (!codeFromUser.equalsIgnoreCase(codeFromSession)) {
                GlobalUtils.removeSessionValue(request, "emailCode");
                return ReturnResult.errorResult("error", "-3", "验证码填写错误");
            }
            Map param = new HashMap();
            param.put("email", email);
            List userList = userService.selectByParams(param);
            if ((userList == null) || (userList != null && userList.size() < 1)) {
                key = "bindingEmail";
            } else {
                return ReturnResult.errorResult("error", "-4", "该邮箱已经被绑定");
            }
        } else if (type.equalsIgnoreCase("unbinding")) {
            String userEmail1 = loginUser.getEmail();
            if (StringUtils.isNotEmpty(userEmail1)) {
                key = "unbindingEmail";
                email = userEmail1;
            }else{
                return ReturnResult.errorResult("尚未绑定邮箱");
            }
        }
        if (!key.equals("NULL")) {
            //验证消息存入redis
            String code = UUID.randomUUID().toString();
            RedisUtils.set(key + "-uuid:" + loginUser.getUUID(), loginUser.getUUID(), 48, TimeUnit.HOURS);
            RedisUtils.set(key + "-code:" + loginUser.getUUID(), code, 48, TimeUnit.HOURS);
            String emailTemp = email;
            if(type.equalsIgnoreCase("unbinding")) {
                emailTemp = "";
            }
            RedisUtils.set(key + "-email:" + loginUser.getUUID(), emailTemp, 48, TimeUnit.HOURS);
            //邮件
            // 封装数据
            String confirmUrl = systemDomain+"api/user/binding?code=" + code + "&email=" + emailTemp + "&uuid=" + loginUser.getUUID() + "&key="+key;
//            String confirmUrl = "http://localhost:5081" + "/api/user/binding?code=" + code + "&email=" + emailTemp + "&uuid=" + loginUser.getUUID() + "&key=" + key;
            String emailSubject;
            if(key.equalsIgnoreCase("bindingEmail")){
                emailSubject = "绑定邮箱验证";
            }else{
                emailSubject = "邮箱解绑验证";
            }

            EmailBean emailBean  = new EmailBean();
            HashMap hashMap = emailBean.getMap();
            hashMap.put("user", loginUser);
            hashMap.put("systemDomain", systemDomain);
            hashMap.put("confirmUrl", confirmUrl);
            //绑定type=1, 解绑type=0
            hashMap.put("type", key.equalsIgnoreCase("bindingEmail") ? 1 : 0);

            emailBean.setAttach(false);
            emailBean.setTicketsRecords(null);
            emailBean.setEmailTo(email);
            emailBean.setEmailSubject(emailSubject);
            emailBean.setMap(hashMap);
            emailBean.setEmailType(EmailType.BINDING);
            amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY, JsonUtil.toJson(emailBean));
            return ReturnResult.successResult("邮件已发送");
        } else {
            return ReturnResult.errorResult("error", "5", "未知错误");
        }
    }

    /**
     * 获取验证码图片
     *
     * @param request
     * @param response
     */
    @GetMapping("/verify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpg");//设置相应内容,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置相应头,告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);//过期时间
        VerifyCodeUtils verifyCodeUtils = new VerifyCodeUtils(180, 80, 4, 40);
        String verifyCode = verifyCodeUtils.getCodeText().toUpperCase();
        GlobalUtils.setSessionValue(request, "emailCode", verifyCode);
        try {
            VerifyCodeUtils.outputImage(100, 50, response.getOutputStream(), verifyCode);
        } catch (Exception e) {
            log.info("UserController.getVerify", e);
        }
    }

    /**
     * 验证操作邮箱
     *
     * @param email
     * @param code
     * @param uuid
     * @return
     */
    @GetMapping({"/binding", "unbinding"})
    public void bindingEmail(@RequestParam("email") String email,
                             @RequestParam("code") String code,
                             @RequestParam("uuid") String uuid,
                             @RequestParam("key") String key,
                             HttpServletResponse response,
                             HttpServletRequest request
    ) {
        String uuidRedis = (String) RedisUtils.get(key+"-uuid:" + uuid); //用户UUID
        String codeRedis = (String) RedisUtils.get(key+"-code:" + uuid);//验证code
        String emailRedis = (String) RedisUtils.get(key+"-email:" + uuid);//待绑定邮箱
        //redis中的验证数据全部存在
        if (StringUtils.isNotEmpty(uuidRedis) && StringUtils.isNotEmpty(codeRedis)) {
            if (uuidRedis.equals(uuid) && codeRedis.equals(code) && (emailRedis.equals(email) || key.equalsIgnoreCase("unbindingEmail"))) {
                //willCenter进行绑定操作
                String content = "{\"email\":\"" + email + "\",\"uuid\":\"" + uuid + "\"}";
                RedisUtils.deleteByKey(key+"-uuid:" + uuid);
                RedisUtils.deleteByKey(key+"-code:" + uuid);
                RedisUtils.deleteByKey(key+"-email:" + uuid);
                try {
                    User user = HttpClinetUtils.sendPost(willCenter.getEditEmail(), content, User.class);
//                    User user = HttpClinetUtils.sendPost("http://localhost:5080/api/user/editEmail", content, User.class);
                    if (user != null) {
                        //同步数据
                        userService.SyncFormCenter(user, uuid);
                        //在session中将email修改
                        User loginUser = GlobalUtils.getLoginUser(request);
                        if(loginUser != null){
                            loginUser.setEmail(email);
                            GlobalUtils.setSessionValue(request,"loginUser",loginUser);
                        }
                        response.sendRedirect("/binding/success?key="+key);
                    } else {
                        response.sendRedirect("/binding/defeat?key="+key);
                    }
                } catch (Exception e) {
                    log.info("UserController.bindingEmail", e);
                    try {
                        response.sendRedirect("/binding/defeat?key="+key);
                    } catch (IOException e1) {
                        log.info("UserController.bindingEmail", e1);
                    }
                }
            }
        }else{
            try {
                response.sendRedirect("/binding/defeat?key="+key);
            } catch (IOException e1) {
                log.info("UserController.bindingEmail", e1);
            }
        }
    }

    /**
     * 用户头像修改上传
     */
    @PostMapping("/uploadAvatar")
    @ResponseBody
    public Map upload(@RequestParam("file") MultipartFile file) {
        if(file == null){
            return ReturnResult.errorResult("上传的文件为空");
        }
        try{
            String url = this.willCenter.getSingleUpload()+"?fileName="+file.getOriginalFilename();
//            String local = "http://localhost:5080/api/upload/receive/single?fileName="+file.getOriginalFilename();
            String res = HttpClinetUtils.sendPostFile(url, file.getBytes());
            if(res.indexOf("error") != -1){
                return ReturnResult.errorResult("上传失败");
            }else{
                return ReturnResult.successResult("data",res,ReturnType.UPDATE_SUCCESS);
            }
        }catch (Exception e){
            log.error("UserController.upload",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取用户ip和对应城市
     * @param request
     * @return
     */
    @GetMapping("/ip")
    @ResponseBody
    public Map getIp(HttpServletRequest request){
        String ip = GlobalUtils.getIp(request);
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("ip", ip));
        list.add(new BasicNameValuePair("key", AmpKey));
        Map map = HttpClinetUtils.sendPost(AmpUrl, Map.class, list, null);
        map.put("ip",ip);
        return ReturnResult.successResult("data",map,ReturnType.GET_SUCCESS);
    }
}
