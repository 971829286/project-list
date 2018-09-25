package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IActivityPhotoService;
import cn.ourwill.tuwenzb.service.IQiniuService;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/9 0009 10:13
 * @Description 照片直播上传类
 */
@Component
@Path("/photoUpload")
public class PhotoUploadController {
    @Autowired
    private IQiniuService qiniuService;
    @Autowired
    private IActivityPhotoService activityPhotoService;

    @Value("${upload.photo.basePrefix}")
    private String basePrefix;
    @Value("${upload.photo.bucketDomain}")
    private String photoBucketDomain;
    private static final Logger log = LogManager.getLogger(UploadController.class);

    /**
     *
     * @param request
     * @param form
     * @return
     */
    @POST
    @Path("/single")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map doUpload(@Context HttpServletRequest request,FormDataMultiPart form) {
        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            FormDataBodyPart p = form.getField("file");
            String uploadDir = "";
            InputStream is = p.getValueAs(InputStream.class);
//            InputStream isLocal = p.getValueAs(InputStream.class);
            FormDataContentDisposition detail = p.getFormDataContentDisposition();
            uploadDir = "temp/" + basePrefix + "/" + month;
            String name = new String(detail.getFileName().getBytes("ISO-8859-1"),"UTF-8");
            String last = name.substring(name.lastIndexOf(".") + 1);
            String toname = name.replace("."+last,"_") + System.currentTimeMillis() + "." + last;
            uploadDir = uploadDir + "/" + toname;
//            log.info("+++++++++++++++++++++保存到本地成功：" + toname);
            String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
            log.info("+++++++++++++++++++++上传成功：" + key);
            return ReturnResult.successResult("data", key, "上传成功！");
        } catch (Exception ex) {
            log.error("PhotoUploadController.doUpload", ex);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 水印上传
     * @param request
     * @param form
     * @return
     */
    @POST
    @Path("/single/waterMark")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map doUploadWater(@Context HttpServletRequest request,FormDataMultiPart form) {
        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        try {
            FormDataBodyPart p = form.getField("file");
            String uploadDir = "";
            InputStream is = p.getValueAs(InputStream.class);
//            InputStream isLocal = p.getValueAs(InputStream.class);
            FormDataContentDisposition detail = p.getFormDataContentDisposition();
            uploadDir = basePrefix + "/waterMark/" + month;
            String name = new String(detail.getFileName().getBytes("ISO-8859-1"),"UTF-8");
            String last = name.substring(name.lastIndexOf(".") + 1);
            String toname = name.replace("."+last,"_") + System.currentTimeMillis() + "." + last;
            uploadDir = uploadDir + "/" + toname;
//            log.info("+++++++++++++++++++++保存到本地成功：" + toname);
            String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
            log.info("+++++++++++++++++++++上传成功：" + key);
            HashMap reMap = new HashMap();
            reMap.put("path", key);
            reMap.put("url", photoBucketDomain+key);
            return ReturnResult.successResult("data", reMap, "上传成功！");
        } catch (Exception ex) {
            log.error("PhotoUploadController.doUploadWater", ex);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @POST
    @Path("/multiple")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map doUploads(@Context HttpServletRequest request,FormDataMultiPart form) {
        //日期路径
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String month = df.format(new Date());
        List reList = new ArrayList();
        try {
            List<FormDataBodyPart> l= form.getFields("file");
            for (FormDataBodyPart p : l) {
                String uploadDir = "";
                InputStream is = p.getValueAs(InputStream.class);
                FormDataContentDisposition detail = p.getFormDataContentDisposition();
                uploadDir="temp/" + basePrefix + "/" +month;
                String name = new String(detail.getFileName().getBytes("ISO-8859-1"),"UTF-8");
                String last = name.substring(name.lastIndexOf(".") + 1);
                String toname = name.replace("."+last,"_") + System.currentTimeMillis() + "." + last;
                if (name.length() > 0) {
                    uploadDir = uploadDir + "/"+ toname;
                    String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
                    reList.add(key);
                }
            }
        } catch (Exception ex) {
            log.error("PhotoUploadController.doUploads", ex);
            return ReturnResult.errorResult("上传失败！");
        }
        return ReturnResult.successResult("data",reList, "上传成功！");
    }

    /**
     * @param files
     * @return
     */
    @POST
    @Path("/multiple/base64")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map doUploadsWithBase64(List<String> files) {
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

                uploadDir="temp/" + basePrefix + "/" +month;
                String lastName = mediaType.substring(mediaType.indexOf("/")+1);
                String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "."+lastName;
                uploadDir = uploadDir + "/"+ toname;
                log.info("+++++++++++++++++++++上传地址："+uploadDir);
                long start = System.currentTimeMillis();
                String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
                long end = System.currentTimeMillis();
                long diffTime = end-start;
                log.info("++++++++++Speed:"+size/(diffTime/1000));
                log.info("+++++++++++++++++++++上传成功："+ key);
                reList.add(key);
            }
        } catch (Exception ex) {
            log.error("PhotoUploadController.doUploadsWithBase64", ex);
            return ReturnResult.errorResult("上传失败！");
        }
        if(reList.size()>0) {
            return ReturnResult.successResult("data", reList, "上传成功！");
        }
        return ReturnResult.errorResult("上传失败！");
    }

    @Path("/getToken")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getToken(){
        return qiniuService.getUpToken();
    }

    @Path("/testsave")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map testsave(List<String> paths){
        String restr = null;
        for (String path : paths) {
           restr = qiniuService.dataPersistence(123,12,path);
        }
        return ReturnResult.successResult("data",restr,ReturnType.GET_SUCCESS);
    }

}
