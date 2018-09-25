package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.service.IQiniuService;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/9 0009 9:37
 * @Description
 */
@Service
@Data
@Slf4j
public class QiniuServiceImpl implements IQiniuService {
    /**
     * 获取上传凭证
     */
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String bucketDomain;
    private String localPath;
    private String zone;
    private String basePrefix;

    @Value("${upload.accessKey}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    @Value("${upload.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    @Value("${upload.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    @Value("${upload.bucketDomain}")
    public void setBucketDomain(String bucketDomain) {
        this.bucketDomain = bucketDomain;
    }
    @Value("${upload.local.path}")
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    @Value("${upload.zone}")
    public void setZone(String zone) {
        this.zone = zone;
    }
    @Value("${upload.basePrefix}")
    public void setBasePrefix(String basePrefix) {
        this.basePrefix = basePrefix;
    }

    private static Configuration cfg;
    private static UploadManager uploadManager;
    private static CdnManager cdnManager;
    private Configuration getCfg(){
        if(cfg == null) {
            synchronized (Configuration.class) {
                if(cfg==null) {
                    if (zone.equals("0")) {
                        cfg = new Configuration(Zone.zone0());
                    } else if (zone.equals("1")) {
                        cfg = new Configuration(Zone.zone1());
                    } else if (zone.equals("2")) {
                        cfg = new Configuration(Zone.zone2());
                    } else {
                        cfg = new Configuration(Zone.zoneNa0());
                    }
                }
            }
        }
        return cfg;
    }
    private UploadManager getUploadManager(){
        if(uploadManager==null){
            synchronized (UploadManager.class){
                if(uploadManager==null){
                    uploadManager = new UploadManager(getCfg());
                }
            }
        }
        return uploadManager;
    }

    private CdnManager getCdnManager(){
        if(cdnManager==null){
            synchronized (CdnManager.class){
                if(cdnManager==null){
                    Auth auth = Auth.create(accessKey, secretKey);
                    cdnManager = new CdnManager(auth);
                }
            }
        }
        return cdnManager;
    }

    @Override
    public String getUpToken(){
        Auth auth = Auth.create(accessKey, secretKey);
//        StringMap putPolicy = new StringMap();
//        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket);
        log.info("token:"+upToken);
        return upToken;
    }

    @Override
    public String getUpTokenReplace(String key){
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket,key);
        log.info("token:"+upToken);
        return upToken;
    }

    @Override
    public String upload(InputStream is, String uploadDir, StringMap params, String mime){
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = getUploadManager();
        try {
            Response response = uploadManager.put(is, uploadDir, getUpToken(), params, mime);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        }catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }

    @Override
    public String replaceUpload(InputStream is, String uploadDir, StringMap params, String mime){
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = getUploadManager();
        CdnManager cdnManager = getCdnManager();
        try {
            Response response = uploadManager.put(is, uploadDir, getUpTokenReplace(uploadDir), params, mime);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            String[] urls = new String[7];
            urls[0] = bucketDomain+putRet.key;
            urls[1] = bucketDomain+putRet.key+"!low";
            urls[2] = bucketDomain+putRet.key+"!medium";
            urls[3] = bucketDomain+putRet.key+"!thumb";
            urls[4] = bucketDomain+putRet.key+"!thumb1";
            urls[5] = bucketDomain+putRet.key+"!thumbnail";
            urls[6] = bucketDomain+putRet.key+"!thumbnail2";
            CdnResult.RefreshResult result = cdnManager.refreshUrls(urls);
            System.out.println(result.code);
            return putRet.key;
        }catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }

    /**
     * 图片持久化
     * @param activityId
     * @param path
     * @return
     */
    @Override
    public String dataPersistence(Integer activityId,String path) throws QiniuException {
        //文件不以temp开头则跳过
        if(path==null||!path.contains("temp")){
            return path;
        }
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth,getCfg());
        StringBuffer replacePath = new StringBuffer(basePrefix);
        if(activityId!=null){
            replacePath.append("/"+activityId);
        }
        //将文件前缀temp/去掉就可以持久保存
        String newPath = path.replace("temp/"+basePrefix,replacePath.toString());
        try {
            bucketManager.move(bucket,path,bucket,newPath);
        } catch (QiniuException e) {
//            log.error("qiniuException",e);
            throw e;
        }
        //返回新的路径
        return newPath;
    }

    /**
     * 图片持久化(替换图片专用)
     * @param activityId
     * @param albumId
     * @param path
     * @return
     */
    @Override
    public String dataPersistenceForReplace(Integer activityId, Integer albumId, String path) {
        //文件不以temp开头则跳过
        if(path==null||!path.contains("temp")){
            return path;
        }
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth,getCfg());
        StringBuffer replacePath = new StringBuffer(basePrefix);
        if(activityId!=null){
            replacePath.append("/"+activityId);
        }
        if(albumId!=null){
            replacePath.append("/"+albumId);
        }
        //将文件前缀temp/去掉就可以持久保存
        String newPath = (path.substring(0,path.lastIndexOf("_"))+path.substring(path.lastIndexOf("."))).replace("temp/"+basePrefix,replacePath.toString());
//        path.replace("temp/"+basePrefix,replacePath.toString());
        try {
            bucketManager.move(bucket,path,bucket,newPath);
        } catch (QiniuException e) {
            log.error("qiniuException",e);
            return null;
        }
        //返回新的路径
        return newPath;
    }

    @Override
    public Boolean reName(String oldPath, String newPath) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth,getCfg());
        try {
            bucketManager.move(bucket,oldPath,bucket,newPath);
        } catch (QiniuException e) {
            log.error("qiniuException",e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean delete(String path) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth,getCfg());
        try {
            bucketManager.delete(bucket, path);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("qiniuException:code:"+ex.code());
            log.error("qiniuException:message:"+ex.response.toString());
            log.error("qiniuException",ex);
            return false;
        }
    }

    @Override
    public List<Boolean> delete(List<String> paths) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        List<Boolean> reList = new ArrayList<>();
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, (String[])paths.toArray(new String[paths.size()]));
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < paths.size(); i++) {
                BatchStatus status = batchStatusList[i];
//                String key = keyList[i];
//                System.out.print(key + "\t");
                if (status.code == 200) {
                    reList.add(true);
//                    System.out.println("delete success");
                } else {
                    reList.add(false);
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error("qiniuException",ex);
        }
        return reList;
    }
}
