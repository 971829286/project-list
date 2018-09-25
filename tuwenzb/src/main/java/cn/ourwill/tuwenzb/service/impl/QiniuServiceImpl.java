package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper;
import cn.ourwill.tuwenzb.service.IQiniuService;
import cn.ourwill.tuwenzb.weixin.Utils.HttpURlUtils;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import lombok.Data;
import org.apache.commons.httpclient.URI;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/9 0009 9:37
 * @Description
 */
@Service
@Data
public class QiniuServiceImpl implements IQiniuService {
    /**
     * 获取上传凭证
     */
    @Value("${upload.accessKey}")
    private String accessKey;

    @Value("${upload.secretKey}")
    private String secretKey;

    @Value("${upload.local.path}")
    private String localPath;


    @Value("${upload.zone}")
    private String zone;


//    @Value("${upload.packageStatusUrl}")
//    private String packageStatusUrl = "http://api.qiniu.com/status/get/prefop?id=";


    @Value("${upload.photo.bucket}")
    private String bucket;

    @Value("${upload.photo.bucketDomain}")
    private String bucketDomain;


    @Value("${upload.photo.basePrefix}")
    private String basePrefix;

    private String packageStatusUrl = "http://api.qiniu.com/status/get/prefop?id=";
    /**
     * 七牛回调URL
     */
    public static final String NOTIFY_URL = "*******";
    /**
     * 七牛间隔符
     */
    public static final String QN_SEPARATOR = "/";
    /**
     * txt换行符
     */
    public static final String QN_NEWLINE = "\n";
    /**
     * 索引文件名称
     */
    public static final String TXT_NAME = "index.txt";


    private static final Logger log = LogManager.getLogger(QiniuServiceImpl.class);

    private static Configuration cfg;
    private static UploadManager uploadManager;
    private static CdnManager cdnManager;
    private static OperationManager operationManager;

    private Configuration getCfg() {
        if (cfg == null) {
            synchronized (Configuration.class) {
                if (cfg == null) {
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

    private UploadManager getUploadManager() {
        if (uploadManager == null) {
            synchronized (UploadManager.class) {
                if (uploadManager == null) {
                    uploadManager = new UploadManager(getCfg());
                }
            }
        }
        return uploadManager;
    }

    private CdnManager getCdnManager() {
        if (cdnManager == null) {
            synchronized (CdnManager.class) {
                if (cdnManager == null) {
                    Auth auth = Auth.create(accessKey, secretKey);
                    cdnManager = new CdnManager(auth);
                }
            }
        }
        return cdnManager;
    }

    private OperationManager getOperationManager() {
        if (operationManager == null) {
            synchronized (OperationManager.class) {
                if (operationManager == null) {
                    Auth auth = Auth.create(accessKey, secretKey);
                    operationManager = new OperationManager(auth,getCfg());
                }
            }
        }
        return operationManager;
    }

    //    private BucketManager
    @Autowired
    private ActivityPhotoMapper activityPhotoMapper;

    @Override
    public String getUpToken() {
        Auth auth = Auth.create(accessKey, secretKey);
//        StringMap putPolicy = new StringMap();
//        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket);
        log.info("token:" + upToken);
        return upToken;
    }

    @Override
    public String getUpTokenReplace(String key) {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, key);
        log.info("token:" + upToken);
        return upToken;
    }

    @Override
    public String upload(InputStream is, String uploadDir, StringMap params, String mime) {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = getUploadManager();
        try {
            Response response = uploadManager.put(is, uploadDir, getUpToken(), params, mime);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
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
    public String replaceUpload(InputStream is, String uploadDir, StringMap params, String mime) {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = getUploadManager();
        CdnManager cdnManager = getCdnManager();
        try {
            Response response = uploadManager.put(is, uploadDir, getUpTokenReplace(uploadDir), params, mime);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            String[] urls = new String[7];
            urls[0] = bucketDomain + putRet.key;
            urls[1] = bucketDomain + putRet.key + "!low";
            urls[2] = bucketDomain + putRet.key + "!medium";
            urls[3] = bucketDomain + putRet.key + "!thumb";
            urls[4] = bucketDomain + putRet.key + "!thumb1";
            urls[5] = bucketDomain + putRet.key + "!thumbnail";
            urls[6] = bucketDomain + putRet.key + "!thumbnail2";
            CdnResult.RefreshResult result = cdnManager.refreshUrls(urls);
            System.out.println(result.code);
            return putRet.key;
        } catch (QiniuException ex) {
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
     *
     * @param activityId
     * @param albumId
     * @param path
     * @return
     */
    @Override
    public String dataPersistence(Integer activityId, Integer albumId, String path) {
        //文件不以temp开头则跳过
        if (path == null || !path.contains("temp")) {
            return path;
        }
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        StringBuffer replacePath = new StringBuffer(basePrefix);
        if (activityId != null) {
            replacePath.append("/" + activityId);
        }
        if (albumId != null) {
            replacePath.append("/" + albumId);
        }
        //将文件前缀temp/去掉就可以持久保存
        String newPath = path.replace("temp/" + basePrefix, replacePath.toString());
        try {
            bucketManager.move(bucket, path, bucket, newPath);
        } catch (QiniuException e) {
            log.error("qiniuException", e);
            return null;
        }
        //返回新的路径
        return newPath;
    }

    /**
     * 图片持久化(替换图片专用)
     *
     * @param activityId
     * @param albumId
     * @param path
     * @return
     */
    @Override
    public String dataPersistenceForReplace(Integer activityId, Integer albumId, String path) {
        //文件不以temp开头则跳过
        if (path == null || !path.contains("temp")) {
            return path;
        }
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        StringBuffer replacePath = new StringBuffer(basePrefix);
        if (activityId != null) {
            replacePath.append("/" + activityId);
        }
        if (albumId != null) {
            replacePath.append("/" + albumId);
        }
        //将文件前缀temp/去掉就可以持久保存
        String newPath = (path.substring(0, path.lastIndexOf("_")) + path.substring(path.lastIndexOf("."))).replace("temp/" + basePrefix, replacePath.toString());
//        path.replace("temp/"+basePrefix,replacePath.toString());
        try {
            bucketManager.move(bucket, path, bucket, newPath);
        } catch (QiniuException e) {
            log.error("qiniuException", e);
            return null;
        }
        //返回新的路径
        return newPath;
    }

    @Override
    public Boolean reName(String oldPath, String newPath) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        try {
            bucketManager.move(bucket, oldPath, bucket, newPath);
        } catch (QiniuException e) {
            log.error("qiniuException", e);
            return false;
        }
        return true;
    }

    @Override
    public String packagePics(List<String> pics, String downLoadFileName) {
        return this.packagePics(pics, basePrefix, downLoadFileName);
    }

    @Override
    public String packagePics(List<String> downloadPics, String prefix, String downLoadFileName) {
        if (downloadPics == null || downloadPics.size() <= 0) {
            return null;
        }
        //密钥配置
        Auth auth = Auth.create(accessKey, secretKey);
        //自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
//
//        Zone z = Zone.autoZone();
//        Configuration c = getCfg();

        //实例化一个BucketManager对象
//        BucketManager bucketManager = new BucketManager(auth, c);

        //创建上传对象
        UploadManager uploadManager = getUploadManager();

        try {
            //压缩索引文件内容
            StringBuilder content = new StringBuilder();
            for (String str : downloadPics) {
                String safeUrl = "/url/" + UrlSafeBase64.encodeToString(auth.privateDownloadUrl(str))
                        + "/alias/" + UrlSafeBase64.encodeToString(str.substring(str.lastIndexOf("/") + 1));
                content.append((StringUtils.isBlank(content) ? "" : "\n") + safeUrl);
            }
            //索引文件路径
            String txtKey = "temp/"+prefix +"/"+ downLoadFileName+".txt";
            log.info("txtKey"+txtKey);
            //生成索引文件token（覆盖上传）
//            String uptoken = auth.uploadToken(bucket, txtKey, 3600, new StringMap().put("insertOnly", 0));
//            String upToken = getUpToken();
            //上传索引文件
            Response res = uploadManager.put(content.toString().getBytes(), txtKey, getUpToken());

            //默认utf-8，但是中文显示乱码，修改为gbk
            String fops = "mkzip/4/encoding/"
                    + UrlSafeBase64.encodeToString("gbk")
                    + "|saveas/" + UrlSafeBase64.encodeToString(bucket + ":temp/" + prefix +"/"+ downLoadFileName + ".zip");

            StringMap params = new StringMap();
            OperationManager operationManager = getOperationManager();
            String id = operationManager.pfop(bucket, txtKey, fops, params);
            return id;
        } catch (QiniuException e) {
            Response res = e.response;
            System.out.println(res);
            try {
                System.out.println(res.bodyString());
            } catch (QiniuException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Map getPackageStatus(String packageId) throws Exception {
        String packageStatusStr = HttpURlUtils.getUrlResponse(packageStatusUrl + packageId);
        JSONObject packageStatusJson = new JSONObject(packageStatusStr);


        String code = packageStatusJson.get("code").toString();
        JSONArray jsonArray = packageStatusJson.getJSONArray("items");
        JSONObject jo = (JSONObject) jsonArray.get(0);
        String key = jo.getString("key");

        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("code", code);
        resultMap.put("downloadUrl", bucketDomain + key);

        return resultMap;
    }

    @Override
    public List<String> dataPersistence(Integer activityId, Integer albumId, List<ActivityPhoto> photos) {
        if (photos == null && photos.size() < 1)
            return null;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        List<String> newPaths = new ArrayList<>();
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            StringBuffer replacePath = new StringBuffer(basePrefix);
            if (activityId != null) {
                replacePath.append("/" + activityId);
            }
            if (albumId != null) {
                replacePath.append("/" + albumId);
            }
            for (ActivityPhoto photo : photos) {
                String newPath = photo.getPhotoPath().replace("temp/" + basePrefix, replacePath.toString());
                newPaths.add(newPath);
                batchOperations.addMoveOp(bucket, photo.getPhotoPath(), bucket, newPath);
            }
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < photos.size(); i++) {
                BatchStatus status = batchStatusList[i];
//                String key = keyList[i];
//                System.out.print(key + "\t");
                if (status.code == 200) {
//                    System.out.println("move success");
                } else {
                    newPaths.set(i, null);
                    log.error("deleteError：" + status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error("qiniuException", ex);
        }
        return newPaths;
    }

    @Override
    public List<String> dataPersistenceApp(Integer activityId, Integer albumId, List<ActivityPhoto> photos) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        List<String> newPaths = new ArrayList<>();
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            for (ActivityPhoto photo : photos) {
                String newPath = photo.getPhotoPath().replace("temp/" + basePrefix, basePrefix);
                newPaths.add(newPath);
                batchOperations.addMoveOp(bucket, photo.getPhotoPath(), bucket, newPath);
            }
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < photos.size(); i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code == 200) {
                } else {
                    newPaths.set(i, null);
                    log.error("deleteError：" + status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error("qiniuException", ex);
        }
        return newPaths;
    }

    @Override
    public Boolean delete(String path) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        try {
            bucketManager.delete(bucket, path);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("qiniuException:code:" + ex.code());
            log.error("qiniuException:message:" + ex.response.toString());
            log.error("qiniuException", ex);
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
            batchOperations.addDeleteOp(bucket, (String[]) paths.toArray(new String[paths.size()]));
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
            log.error("qiniuException", ex);
        }
        return reList;
    }

    @Override
    public void redExif(ActivityPhoto photo) {
        List<ActivityPhoto> list = new ArrayList<>();
        list.add(photo);
        this.redExif(list);
    }

    @Override
    public void redExif(List<ActivityPhoto> photos) {
        String url = "";
        String infoUrl = "";
        String infoReStr = "";
        String reStr = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String dateTime = null;
        Integer width = null;
        Integer height = null;
        Integer size = null;
        String orientation = null;
        for (ActivityPhoto photo : photos) {
            String photoPath = photo.getPhotoPath();
            if (StringUtil.isNotEmpty(photoPath)) {
                photo.setPhotoName(photoPath.substring(photoPath.lastIndexOf("/") + 1, photoPath.lastIndexOf("_")) + photoPath.substring(photoPath.lastIndexOf(".")));
                photo.setDownloadName(photoPath.substring(photoPath.lastIndexOf("/") + 1));
            }
            try {
                url = photo.getPhotoPathUrl() + "?exif";
                infoUrl = photo.getPhotoPathUrl() + "?imageInfo";
                URI uri = new URI(url, false, "UTF-8");
                URI infoUri = new URI(infoUrl, false, "UTF-8");
                url = uri.toString();
                infoUrl = infoUri.toString();
                log.info("url:" + url);
                log.info("infoUrl:" + infoUrl);
                try {
                    reStr = HttpURlUtils.getUrlResponse(url);
                    JSONObject jsonObject = new JSONObject(reStr);
                    log.info(reStr);
                    if (jsonObject.has("DateTime") && photo.getShootingTime() == null) {
                        dateTime = jsonObject.getJSONObject("DateTime").getString("val");
                        photo.setShootingTime(sdf.parse(dateTime));
                    }
                    if (jsonObject.has("Orientation")) {
                        orientation = jsonObject.getJSONObject("Orientation").getString("val");
                        photo.setPhotoOrientation(convertOrientation(orientation));
                    } else {
                        photo.setPhotoOrientation(1);
                    }
                } catch (IOException e) {
                    photo.setPhotoOrientation(1);
                }
                try {
                    infoReStr = HttpURlUtils.getUrlResponse(infoUrl);
                    JSONObject jsonObjectInfo = new JSONObject(infoReStr);
                    log.info(infoReStr);
                    if (jsonObjectInfo.has("width")) {
                        width = jsonObjectInfo.getInt("width");
                        photo.setPhotoWidth(width);
                    } else {
                        photo.setPhotoWidth(-1);
                    }
                    if (jsonObjectInfo.has("height")) {
                        height = jsonObjectInfo.getInt("height");
                        photo.setPhotoHeight(height);
                    } else {
                        photo.setPhotoHeight(-1);
                    }
                    if (jsonObjectInfo.has("size")) {
                        size = jsonObjectInfo.getInt("size");
                        photo.setPhotoSize(size);
                    } else {
                        photo.setPhotoSize(-1);
                    }
                } catch (IOException e) {
                    photo.setPhotoWidth(-1);
                    photo.setPhotoHeight(-1);
                    photo.setPhotoSize(-1);
                }
            } catch (Exception e) {
                log.error("QiniuServiceImpl.redExif", e);
                continue;
            }
        }
    }

    private Integer convertOrientation(String orientation) {
        Integer reType = 1;
        switch (orientation) {
            case "Top-left":
                reType = 1;
                break;
            case "Top-right":
                reType = 2;
                break;
            case "Bottom-right":
                reType = 3;
                break;
            case "Bottom-left":
                reType = 4;
                break;
            case "Left-top":
                reType = 5;
                break;
            case "Right-top":
                reType = 6;
                break;
            case "Right-bottom":
                reType = 7;
                break;
            case "Left-bottom":
                reType = 8;
                break;
            default:
                reType = 1;
        }
        return reType;
    }

    @Override
    public List<Integer> movePath(Integer activityId, Integer fAlbumId, Integer tAlbumId, List<ActivityPhoto> photos) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, getCfg());
        String oldPath = null;
        List<Integer> successList = new ArrayList<>();
        try {
            String oldPrefix = basePrefix + "/" + activityId + "/" + fAlbumId;
            String newPrefix = basePrefix + "/" + activityId + "/" + tAlbumId;
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            for (ActivityPhoto photo : photos) {
                batchOperations.addCopyOp(bucket, photo.getPhotoPath(), bucket, photo.getPhotoPath().replace(oldPrefix, newPrefix));
            }
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < photos.size(); i++) {
                BatchStatus status = batchStatusList[i];
                ActivityPhoto photo = photos.get(i);
                if (status.code == 200) {
                    oldPath = photo.getPhotoPath();
                    photo.setPhotoPath(oldPath.replace(oldPrefix, newPrefix));
                    photo.setAlbumId(tAlbumId);
                    if (activityPhotoMapper.update(photo) > 0) {
                        successList.add(photo.getId());
                        bucketManager.delete(bucket, oldPath);
                    }
                    System.out.println("move success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException e) {
            log.error("qiniuException", e);
            return successList;
        }
        //返回新的路径
        return successList;
    }


}
