package cn.ourwill.willcenter.controller;


import cn.ourwill.willcenter.entity.FdfsImage;
import cn.ourwill.willcenter.service.IFdfsImageService;
import cn.ourwill.willcenter.service.IQiniuService;
import cn.ourwill.willcenter.utils.FastDFSClient;
import cn.ourwill.willcenter.utils.ReturnResult;
import cn.ourwill.willcenter.utils.ReturnType;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/22 16:51
 * @Description
 */
@Controller
@Slf4j
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private IQiniuService qiniuService;

//    @Value("${upload.basePrefix}")
//    private String basePrefix;

    @Value("${fastdfs.basePrefix}")
    private String basePrefix;

    @Autowired
    private IFdfsImageService fdfsImageService;

    private static final Logger log = LogManager.getLogger(UploadController.class);

//    /**
//     * 处理单文件上传
//     */
//    @PostMapping("/single")
//    @ResponseBody
//    public Map upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//        //日期路径
//        DateFormat df = new SimpleDateFormat("yyyyMMdd");
//        String month = df.format(new Date());
//        try {
//            String uploadDir = "";
//            InputStream is = file.getInputStream();
//            uploadDir = "temp/" + basePrefix + "/" + month;
////            String name = new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");
//            String name = file.getOriginalFilename();
//            String last = name.substring(name.lastIndexOf(".") + 1);
//            String toName = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;
////            String toname = name.replace("."+last,"_") + System.currentTimeMillis() + "." + last;
//            uploadDir = uploadDir + "/" + toName;
//            String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
////            log.info("+++++++++++++++++++++上传成功：" + key);
//            return ReturnResult.successResult("data", key, "上传成功！");
//        } catch (Exception ex) {
//            log.error("UploadController.upload", ex);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/receive/single")
//    @ResponseBody
//    public String receiveUpload(@RequestBody byte[] bytes, @RequestParam("fileName") String fileName){
//        //日期路径
//        DateFormat df = new SimpleDateFormat("yyyyMMdd");
//        String month = df.format(new Date());
//        try{
//            InputStream is = new ByteArrayInputStream(bytes);
//            String uploadDir = "";
//            uploadDir = "temp/" + basePrefix + "/" + month;
//            String last = fileName.substring(fileName.lastIndexOf(".") + 1);
//            String toName = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;
//            uploadDir = uploadDir + "/" + toName;
//            String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
//            return key;
//        }catch (Exception e){
//            log.info("UploadController.receiveUpload", e);
//            return null;
//        }
//    }
//
//
//    @PostMapping("/multiple")
//    @ResponseBody
//    public Map multipleUpload(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
//        //日期路径
//        DateFormat df = new SimpleDateFormat("yyyyMMdd");
//        String month = df.format(new Date());
//        List reList = new ArrayList();
//        String uploadDir = "";
//        try {
//            for (MultipartFile file : files) {
//                uploadDir = "temp/" + basePrefix + "/" + month;
//                InputStream is = file.getInputStream();
////            String name = new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");
//                String name = file.getOriginalFilename();
//                String last = name.substring(name.lastIndexOf(".") + 1);
//                String toName = System.currentTimeMillis() + new Random(50000).nextInt() + "." + last;
////            String toname = name.replace("."+last,"_") + System.currentTimeMillis() + "." + last;
//                uploadDir = uploadDir + "/" + toName;
//                String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
//                reList.add(key);
//            }
////            log.info("+++++++++++++++++++++上传成功：" + key);
//            return ReturnResult.successResult("data", reList, "上传成功！");
//        } catch (Exception ex) {
//            log.error("UploadController.multipleUpload", ex);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    /**
//     * @param files
//     * @return
//     */
//    @PostMapping("/multiple/base64")
//    @ResponseBody
//    public Map doUploadsWithBase64(@RequestBody List<String> files) {
//        List reList = new ArrayList();
//        //日期路径
//        DateFormat df = new SimpleDateFormat("yyyyMMdd");
//        String month = df.format(new Date());
//        try {
//            for (String file : files) {
//                String uploadDir = "";
//                if(StringUtil.isEmpty(file)) continue;
//                //需要解码的base64字符
//                if(file.length()>100) log.info("base64:"+file.substring(0,100));
//                else
//                    log.info("base64:"+file);
//                double size = file.length()/1024;
//                log.info("base64Size:"+size);
//                String base64 = file.substring(file.indexOf(";base64,")+8);
//                //文件类型
//                String mediaType= file.substring(5,file.indexOf(";base64,"));
//                BASE64Decoder base64Decoder = new BASE64Decoder();
//                byte[] bytes = base64Decoder.decodeBuffer(base64);
//                InputStream is = new ByteArrayInputStream(bytes);
//
//                uploadDir="temp/" + basePrefix + "/" +month;
//                String lastName = mediaType.substring(mediaType.indexOf("/")+1);
//                String toname = System.currentTimeMillis() + new Random(50000).nextInt() + "."+lastName;
//                uploadDir = uploadDir + "/"+ toname;
//                log.info("+++++++++++++++++++++上传地址："+uploadDir);
//                long start = System.currentTimeMillis();
//                String key = qiniuService.upload(is,uploadDir,null,"application/octet-stream");
//                long end = System.currentTimeMillis();
//                long diffTime = end-start;
//                log.info("++++++++++Speed:"+size/(diffTime/1000));
//                log.info("+++++++++++++++++++++上传成功："+ key);
//                reList.add(key);
//            }
//        } catch (Exception ex) {
//            log.error("PhotoUploadController.doUploadsWithBase64", ex);
//            return ReturnResult.errorResult("上传失败！");
//        }
//        if(reList.size()>0) {
//            return ReturnResult.successResult("data", reList, "上传成功！");
//        }
//        return ReturnResult.errorResult("上传失败！");
//    }

    @PostMapping("/single")
    @ResponseBody
    public Map uploadFDfs(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            if (file.isEmpty()){
                return ReturnResult.errorResult("文件不存在！");
            }
            Socket sock = new Socket();
            sock.setSoTimeout(3000);
            String originalFilename=file.getOriginalFilename();
            String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            FastDFSClient client=new FastDFSClient();
            String key=client.uploadFile(basePrefix,file.getBytes(),extName);
            if(!StringUtils.isEmpty(key)){
                FdfsImage fdfsImage = new FdfsImage();
                fdfsImage.setUrl(key);
                fdfsImage.setUploadTime(new Date());
                fdfsImage.setFlag(0);
                fdfsImageService.save(fdfsImage);
            }
            return ReturnResult.successResult("data", key, "上传成功！");
        } catch (Exception ex) {
            log.error("UploadController.uploadFDfs", ex);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PostMapping("/multiple")
    @ResponseBody
    public Map multipleUploadFDfs(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        List reList = new ArrayList();
        try {
            for (MultipartFile file : files) {
                Socket sock = new Socket();
                sock.setSoTimeout(3000);
                String originalFilename=file.getOriginalFilename();
                String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
                FastDFSClient client=new FastDFSClient();
                String key=client.uploadFile(basePrefix,file.getBytes(),extName);
                if(!StringUtils.isEmpty(key)){
                    FdfsImage fdfsImage = new FdfsImage();
                    fdfsImage.setUrl(key);
                    fdfsImage.setUploadTime(new Date());
                    fdfsImage.setFlag(0);
                    fdfsImageService.save(fdfsImage);
                }
                reList.add(key);
            }
            return ReturnResult.successResult("data", reList, "上传成功！");
        } catch (Exception ex) {
            log.error("UploadController.multipleUploadFDfs", ex);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
//
    @PostMapping("/receive/single")
    @ResponseBody
    public String receiveUpload(@RequestBody byte[] bytes, @RequestParam("fileName") String fileName){
        //日期路径
        try{
            Socket sock = new Socket();
            sock.setSoTimeout(3000);
            FastDFSClient client=new FastDFSClient();
            String key=client.uploadFile(basePrefix,bytes,fileName);
            if(!StringUtils.isEmpty(key)){
                FdfsImage fdfsImage = new FdfsImage();
                fdfsImage.setUrl(key);
                fdfsImage.setUploadTime(new Date());
                fdfsImage.setFlag(0);
                fdfsImageService.save(fdfsImage);
            }
            return key;
        }catch (Exception e){
            log.info("UploadController.receiveUpload", e);
            return null;
        }
    }
}
