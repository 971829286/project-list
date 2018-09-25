package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.service.quartz.ActivityJob;
import cn.ourwill.tuwenzb.service.quartz.QuartzManager;
import cn.ourwill.tuwenzb.utils.*;
import cn.ourwill.tuwenzb.weixin.Utils.PageAuthorizeUtils;
import cn.ourwill.tuwenzb.weixin.Utils.PastUtil;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/6/30 0030 16:07
 * @Version1.0
 */
@Component
@Path("/tests")
public class TestApi {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IActivityPhotoService activityPhotoService;

    @Autowired
    private IFacePlusServer facePlusServer;


    @Autowired
    private IActivityAlbumService activityAlbumService;
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Path("/redis")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object index() {
     /*   // 保存字符串
        stringRedisTemplate.opsForValue().set("aaa", "111");
        String string = stringRedisTemplate.opsForValue().get("aaa");
        System.out.println(string);
        return "Hello World";*/
        RedisUtils.set("aaa","11111");
        System.out.println(RedisUtils.get("aaa"));
        User user=new User();
        user.setAvatar("helloe");
        user.setRefreshToken("fdsfdsfd");
        user.setWechatNum("fdsfdsfd");
       /* RedisUtils.set("user",user);*/
        return RedisUtils.get("aaa");
    }

    @Path("/testIp")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIp(@Context HttpServletRequest request) {
        return Response.ok(GlobalUtils.getIp(request)).build();
    }

    @Path("/steam")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSteam(@Context HttpServletRequest request) {
        String img = "[[\"123\"],[\"234\"]]";
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<String> imgUrls= gson.fromJson(img,type);
        List<String> reList = imgUrls.stream().map(imgu -> imgu="123"+imgu).collect(Collectors.toList());
        return Response.ok(reList).build();
    }

    @Path("/redis2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void testRedis() {
        Map map = new HashMap();
        map.put("id",1);
        map.put("activityId",999);
        map.put("likeNum",3);
        map.put("commentNum",4);
        map.put("participantsNum",5);
        redisTemplate.opsForHash().putAll(map.get("activityId").toString(),map);
        System.out.println(redisTemplate.opsForHash().get("1","likeNum"));
    }

    @Path("/testWeixin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getweixin(@Context HttpServletRequest request) throws Exception {
//        String access_token = PageAuthorizeUtils.getAccess_token();
        String ticket = PageAuthorizeUtils.getJsApiTicket();
//        PageAuthorizeUtils.
//        System.out.println(access_token);
        System.out.println(ticket);
        Map remap = PastUtil.sign(ticket,"dev.tuwenzhibo.com/live/");
        return remap;
    }
    @Path("/testRedis")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object testRedis(@Context HttpServletRequest request) throws Exception {
       String access_token = "alsdjflasdkf";
       RedisUtils.set("overTime",access_token,20, TimeUnit.SECONDS);
//        AccessToken newAccessToken = new AccessToken();
//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.SECOND,+7100);
//        newAccessToken.setAccessToken(access_token);
//        newAccessToken.setDueDate(calendar.getTime());
//        RedisUtils.set("access_token",newAccessToken);
        return RedisUtils.get("overTime");
    }

    @Path("/testLoginInfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getLoginInfo(@Context HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        Integer userId = GlobalUtils.getUserId(request);
        Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
        map.put("userId",userId);
        map.put("userLevel",userLevel);
        return map;
    }
    @Path("/testWeixinLogin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map testWeixinLogin(@Context HttpServletRequest request, @QueryParam("openId") String openId) throws Exception {
        List<User> users = userService.selectByWechatNum(openId);
        User user = new User();
        if(users.size()>1){
            for (User entity : users) {
                if(entity.getUserfromType()==1) {
                    user = entity;
                    log.info("已有绑定微信用户：" + user.getWechatNum()+",存入session");
                }
            }

        }else if (users.size()==1){
            user = users.get(0);
            log.info("已有微信用户：" + user.getWechatNum()+",存入session");
        }

        GlobalUtils.setSessionValue(request,"userId",user.getId());
        GlobalUtils.setSessionValue(request, "userLevel", user.getLevel());
        return ReturnResult.successResult("登录成功!");
    }
    @Path("/redisZset")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public boolean redisZset(@Context HttpServletRequest request) {
        List<Integer> ids = activityMapper.getDeleteIds();
        for (Integer id : ids) {
            redisTemplate.delete(id.toString());
        }
        return true;
    }
    @Path("/wxerror")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map wxerror(@Context HttpServletRequest request,@QueryParam("error") String error) {
        try {
            System.out.println("++++++++微信回调错误error："+ URLDecoder.decode(error, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ReturnResult.successResult(ReturnType.GET_SUCCESS);
    }

    @Path("/getExif")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map exiftest(@Context HttpServletRequest request,List<String> paths) {
        try {
            qiniuService.redExif(paths.stream().map(path->{
                ActivityPhoto photo = new ActivityPhoto();
                photo.setPhotoPath(path);
                return photo;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.successResult(ReturnType.GET_SUCCESS);
    }

    @Path("/photoMerge")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void photoMerge(@Context HttpServletRequest request,@Context HttpServletResponse response,List<String> paths) {
        try {
            log.info("获取图片："+new Date());
////            String[] files = (String[])paths.toArray();
            InputStream[] inputStreams = ImgUtil.getImageByUrl(paths);
//            log.info(new Date());
//            ImgUtil.mergeImage(inputStreams,2,response.getOutputStream());
            log.info("开始拼图："+new Date());
            ImgUtil.addImgWatermark(inputStreams,response.getOutputStream());
            log.info("完毕："+new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Path("/getSandKey")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getSandKey() throws Exception {
        Map reqData = new HashMap();
        String nonceStr = WXPayUtil.generateNonceStr();
        reqData.put("mch_id","1487246152");
        reqData.put("nonce_str", nonceStr);
        reqData.put("sign", WXPayUtil.generateSignature(reqData,"4pgaWubsdNJwQp5EAfydH9QWRqbRzV7q"));
        String xml = WXPayUtil.mapToXml(reqData);

        String UTF8 = "UTF-8";
        String reqBody = WXPayUtil.mapToXml(reqData);
        URL httpUrl = new URL("https://apitest.mch.weixin.qq.com/sandboxnew/pay/getsignkey");
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));

        // if (httpURLConnection.getResponseCode()!= 200) {
        //     throw new Exception(String.format("HTTP response code is %d, not 200", httpURLConnection.getResponseCode()));
        // }

        //获取内容
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();
        if (stringBuffer!=null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream!=null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream!=null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String,String> reMap = WXPayUtil.xmlToMap(resp);
        return reMap;
    }

//    @GET
//    @Path("/testJob")
//    public void timingIncrease(){
//        System.out.println("开始执行");
//        List<Activity> activities = activityMapper.getActivityIng(LocalDateTime.now());
//        if(activities != null && activities.size() > 0){
//            for(Activity activity : activities){
//                System.out.println("===========");
//                System.out.println(quartzManager.isExist(activity));
//                System.out.println("===========");
//                if(!quartzManager.isExist(activity)){
//                    quartzManager.addJob(ActivityJob.class,activity);
//                }
//            }
//        }
//
//        quartzManager.getRunningJob();
//    }

}
