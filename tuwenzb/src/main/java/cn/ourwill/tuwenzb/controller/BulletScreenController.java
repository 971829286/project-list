package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.BulletScreen;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IBulletScreenService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/17 11:49
 * @Description  弹幕墙
 */

@Component
@Path("/bulletScreen")
public class BulletScreenController {

    /**
     * 获取上传凭证
     */
    @Value("${upload.accessKey}")
    private String accessKey;
    @Value("${upload.secretKey}")
    private String secretKey;
    @Value("${upload.bucket}")
    private String bucket;
    @Value("${upload.zone}")
    private String zone;

    @Autowired
    private IUserService userService;
    @Autowired
    private IBulletScreenService bulletScreenService;

    private static final Logger log = LogManager.getLogger(BulletScreenController.class);

    /**
     * 增加弹幕
     * @param request
     * @param roomId
     * @return
     */
    @POST
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, @PathParam("roomId") Integer roomId, BulletScreen bulletScreen){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            bulletScreen.setRoomId(roomId);
            bulletScreen.setUserId(userId);
            bulletScreen.setNickname(user.getNickname());
            bulletScreen.setAvatar(user.getAvatar());
            if (bulletScreenService.save(bulletScreen) > 0) {
                return ReturnResult.successResult("发布成功");
            }
            return ReturnResult.successResult("发布成功");
        }catch (Exception e){
            log.info("BulletScreenController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    private String getUpToken(){
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    private Configuration getCfg(){
        Configuration cfg = null;
        if(zone.equals("0")) {
            cfg = new Configuration(Zone.zone0());
        }else if(zone.equals("1")){
            cfg = new Configuration(Zone.zone1());
        }else if(zone.equals("2")){
            cfg = new Configuration(Zone.zone2());
        }else{
            cfg = new Configuration(Zone.zoneNa0());
        }
        return cfg;
    }

    @POST
    @Path("/singleUpload/{roomId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response doUpload(FormDataMultiPart form, @PathParam("roomId") Integer roomId) {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = new UploadManager(getCfg());

        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            FormDataBodyPart p = form.getField("file");
            String uploadDir = "bulletScreen/";
            InputStream is = p.getValueAs(InputStream.class);
            InputStream isLocal = p.getValueAs(InputStream.class);
            FormDataContentDisposition detail = p.getFormDataContentDisposition();
            MediaType mediaType = p.getMediaType();
            String subType = mediaType.getSubtype();
            String type = mediaType.getType();
            uploadDir = uploadDir + roomId+"/"+type + "/" + subType + "/" + month;
            String name = detail.getFileName();
            String last = name.substring(name.lastIndexOf(".") + 1);
            String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;
            uploadDir = uploadDir + "/" + toname;
            Response response = uploadManager.put(is, uploadDir, this.getUpToken(), null, "application/octet-stream");
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return javax.ws.rs.core.Response.ok(ReturnResult.successResult("data", putRet.key, "上传成功！")).build();

        }catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception ex) {
            log.error("UploadController.doUpload", ex);
            return javax.ws.rs.core.Response.ok(ReturnResult.errorResult(ReturnType.SERVER_ERROR)).build();
        }
        return javax.ws.rs.core.Response.ok(ReturnResult.errorResult("上传失败！")).build();
    }

    /**
     * 获取弹幕评论
     * @param request
     * @param roomId
     * @return
     */
    @GET
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findByRoom(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId,@QueryParam("time") Long time){
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if(time!=null)
                date = new Date(time);
            List<BulletScreen> bulletScreens = bulletScreenService.getByRoomId(roomId,date);
            return ReturnResult.successResult("data",bulletScreens,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("BulletScreenController.findByRoom",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
