package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/6/30 0030 11:16
 * @Version1.0
 */
@Component
@Path("/upload")
public class UploadController {
    /**
     * 获取上传凭证
     */
    @Value("${upload.accessKey}")
    private String accessKey;
    @Value("${upload.secretKey}")
    private String secretKey;
    @Value("${upload.bucket}")
    private String bucket;
    @Value("${upload.bucketDomain}")
    private String bucketDomain;
    @Value("${upload.local.path}")
    private String localPath;
    @Value("${upload.zone}")
    private String zone;
    private static final Logger log = LogManager.getLogger(UploadController.class);

    public String getUpToken(){
//        Map reMap = new HashMap();
//        reMap.put("bucketDomain",bucketDomain);
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
//        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket);
//        reMap.put("upToken",upToken);
        return upToken;
    }

    @POST
    @Path("/single")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response doUpload(FormDataMultiPart form) {
        //构造一个带指定Zone对象的配置类
        log.info("++++++++++++++++上传开始");
        UploadManager uploadManager = new UploadManager(getCfg());

        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            FormDataBodyPart p = form.getField("file");
            String uploadDir = "";
            InputStream is = p.getValueAs(InputStream.class);
            InputStream isLocal = p.getValueAs(InputStream.class);
            FormDataContentDisposition detail = p.getFormDataContentDisposition();
            MediaType mediaType = p.getMediaType();
            String subType = mediaType.getSubtype();
            String type = mediaType.getType();
            uploadDir = type + "/" + subType + "/" + month;
            String name = detail.getFileName();
            String last = name.substring(name.lastIndexOf(".") + 1);
            String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;


            uploadDir = uploadDir + "/" + toname;
//                log.info("+++++++++++++++++++++上传地址："+uploadDir);
//                FileUtils.copyInputStreamToFile(isLocal,new File(localPath+File.separator+toname));
            log.info("+++++++++++++++++++++保存到本地成功：" + toname);
            Response response = uploadManager.put(is, uploadDir, this.getUpToken(), null, "application/octet-stream");
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                    System.out.println(putRet.key);
//                    System.out.println(putRet.hash);
            log.info("+++++++++++++++++++++上传成功：" + putRet.key);
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

    @POST
    @Path("/multiple")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response doUploads(FormDataMultiPart form) {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = new UploadManager(getCfg());
        List reList = new ArrayList();
        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            List<FormDataBodyPart> l= form.getFields("file");
            for (FormDataBodyPart p : l) {
                String uploadDir = "";
                InputStream is = p.getValueAs(InputStream.class);
                FormDataContentDisposition detail = p.getFormDataContentDisposition();
                MediaType mediaType = p.getMediaType();
                String subType = mediaType.getSubtype();
                String type = mediaType.getType();

                uploadDir=type+"/"+subType+"/"+month;
                String name = detail.getFileName();
                String last = name.substring(name.lastIndexOf(".") + 1);
                String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;
                if (name.length() > 0) {
                    uploadDir = uploadDir + "/"+ toname;
                    Response response = uploadManager.put(is,uploadDir,this.getUpToken(),null, "application/octet-stream");
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                    System.out.println(putRet.key);
//                    System.out.println(putRet.hash);
                    reList.add(putRet.key);
                }
            }

        }catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception ex) {
            log.error("UploadController.doUploads", ex);
            return javax.ws.rs.core.Response.ok(ReturnResult.errorResult("上传失败！")).build();
        }
        return javax.ws.rs.core.Response.ok(ReturnResult.successResult("data",reList, "上传成功！")).build();
    }

    @POST
    @Path("/multiple/base64")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response doUploadsWithBase64(List<String> files) {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = new UploadManager(getCfg());
        List reList = new ArrayList();
        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            for (String file : files) {
                String uploadDir = "";
                if(StringUtil.isEmpty(file)) continue;
                //需要解码的base64字符
                if(file.length()>100) log.info("base64:"+file.substring(0,100));
                else
                    log.info("base64:"+file);
                double size = file.length()/1024;
                log.info("base64Size:"+size);
                String base64 = file.substring(file.indexOf(";base64,")+8);
                //文件类型
                String mediaType= file.substring(5,file.indexOf(";base64,"));
                BASE64Decoder base64Decoder = new BASE64Decoder();
                byte[] bytes = base64Decoder.decodeBuffer(base64);
                InputStream is = new ByteArrayInputStream(bytes);
                InputStream isLocal = new ByteArrayInputStream(bytes);

                uploadDir=mediaType+"/"+month;
                String lastName = mediaType.substring(mediaType.indexOf("/")+1);
                String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "."+lastName;
//                if (name.length() > 0) {
                    uploadDir = uploadDir + "/"+ toname;
                    log.info("+++++++++++++++++++++上传地址："+uploadDir);
//                    FileUtils.copyInputStreamToFile(isLocal,new File(localPath+File.separator+toname));
//                    log.info("+++++++++++++++++++++保存到本地成功："+toname);
                    long start = System.currentTimeMillis();
                    Response response = uploadManager.put(is,uploadDir,this.getUpToken(),null, "application/octet-stream");
                    long end = System.currentTimeMillis();
                    long diffTime = end-start;
                    log.info("++++++++++Speed:"+size/(diffTime/1000));
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                    System.out.println(putRet.key);
//                    System.out.println(putRet.hash);
                    log.info("+++++++++++++++++++++上传成功："+ putRet.key);
                    reList.add(putRet.key);
//                }
            }
        }catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception ex) {
            log.error("UploadController.doUploadsWithBase64", ex);
            return javax.ws.rs.core.Response.ok(ReturnResult.errorResult("上传失败！")).build();
        }
        if(reList.size()>0) {
            return javax.ws.rs.core.Response.ok(ReturnResult.successResult("data", reList, "上传成功！")).build();
        }
        return javax.ws.rs.core.Response.ok(ReturnResult.errorResult("上传失败！")).build();
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

    @Path("/getToken")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getToken(){
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        log.info("token:"+upToken);
        return upToken;
    }
}
