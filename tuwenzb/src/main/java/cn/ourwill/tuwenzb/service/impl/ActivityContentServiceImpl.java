package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityContent;
import cn.ourwill.tuwenzb.mapper.ActivityContentMapper;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.service.IActivityContentService;
import cn.ourwill.tuwenzb.weixin.Utils.Docx4jUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ActivityContentServiceImpl extends BaseServiceImpl<ActivityContent> implements IActivityContentService {

    @Autowired
    private ActivityContentMapper activityContentMapper;
    @Autowired
    private ActivityMapper activityMapper;

    private static final ObjectFactory factory = Context.getWmlObjectFactory();

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

    @Value("${upload.bucketDomain}")
    private String uploadDomain;
    @Override
    public List<ActivityContent> getRecentActivity(Integer activityId,String date,String timeOrder) {
        return activityContentMapper.getRecentActivity(activityId,date,timeOrder);
    }

    @Override
    public Integer getContentCount(Integer activityId) {
        return activityContentMapper.getContentCount(activityId);
    }

    @Override
    public List getAllImgByActivityId(Integer activityId) {
        List<String> imgsList = activityContentMapper.getAllImgByActivityId(activityId);
        Type type = new TypeToken<String[]>() {}.getType();
        Gson gson = new Gson();
        ArrayList<ArrayList<String>> reList = new ArrayList<>();
        imgsList.stream().forEach(
            imgs->{
                String[] imgsArray = gson.fromJson(imgs,type);
                ArrayList<String> picListPer = new ArrayList<>();
                Arrays.stream(imgsArray).forEach(img->{
                    if(StringUtils.isNotEmpty(img)&&img.indexOf("http")<0){
//                        if(activityId.equals(2382)) {
//                            img = uploadDomain + img + "?watermark/1/image/aHR0cDovL3MudHV3ZW56aGliby5jb20vaW1hZ2UvcG5nLzIwMTgwMjAzLzgzNDcyODM0MjQ1NjcucG5n/ws/0.3/imageslim";;
//                        }else{
                            img = uploadDomain + img;
//                        }
                    }
                    picListPer.add(img);
                });
                reList.add(picListPer);
        });
        return reList;
    }

    @Override
    public String exportWord(Integer activityId) {
        InputStream inStream = null;
        OutputStream out = null;
        try {
            Activity activity = activityMapper.getById(activityId);
            List<ActivityContent> list = activityContentMapper.getRecentActivityNoTags(activityId);
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            //添加页眉
            HeaderPart headerPart = Docx4jUtil.createHeader(wordMLPackage);
            P headerText = Docx4jUtil.newText("图文直播-专属您的图文直播平台");
            PPr ppr = factory.createPPr();
            Jc jc = factory.createJc();
            jc.setVal(JcEnumeration.RIGHT);
            ppr.setJc(jc);
            headerText.setPPr(ppr);
            headerPart.getContent().add(headerText);

            //添加页脚
            FooterPart footerPart = Docx4jUtil.createFooter(wordMLPackage);
            footerPart.getContent().add(Docx4jUtil.newText("http://www.tuwenzhibo.com"));

            //标题
            MainDocumentPart mainPart = wordMLPackage.getMainDocumentPart();
            mainPart.addStyledParagraphOfText("Title",activity.getTitle());
            mainPart.addStyledParagraphOfText("Subtitle",activity.getIntroduction());
            mainPart.addStyledParagraphOfText("Heading1","主办方："+activity.getOrganizer());
            if (activity.getEndTime()==null){
                mainPart.addStyledParagraphOfText("Heading1","时间："+sdfDay.format(activity.getStartTime()));
            }else {
                mainPart.addStyledParagraphOfText("Heading1","时间："+sdfDay.format(activity.getStartTime())+"-"+sdfDay.format(activity.getEndTime()));
            }
            mainPart.addStyledParagraphOfText("Heading1","地点："+activity.getSite());
            mainPart.addStyledParagraphOfText("Heading1","发布人："+activity.getPublisher());
            //内容
            for (ActivityContent content : list) {
                try {
                    mainPart.addParagraphOfText(sdfTime.format(content.getCTime()));
                    mainPart.addParagraphOfText(content.getContent());
                    mainPart.addStyledParagraphOfText("Normal",content.getContent());
                    mainPart.addParagraphOfText(content.getVideoUrl());
                    List<String> pics = content.getImgUrls();
                    for (String pic : pics) {
                        URL url = new URL(pic);
                        //打开链接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //设置请求方式为"GET"
                        conn.setRequestMethod("GET");
                        //超时响应时间为5秒
                        conn.setConnectTimeout(5 * 1000);
                        //通过输入流获取图片数据
                        inStream = conn.getInputStream();
                        byte[] bytes = IOUtils.toByteArray(inStream);
                        Docx4jUtil.addImageToPackage(wordMLPackage, bytes);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }

            //日期路径
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            String month = df.format(new Date());
            String uploadDir="docx/"+month;
            String toname = System.currentTimeMillis() + new Random(50000).nextInt() + ".docx";
            String key = uploadDir+"/"+toname;
            File file = new File(localPath+File.separator+toname);
            out = new FileOutputStream(file);
//            byte[] uploadBytes = null;
            wordMLPackage.save(out);
//            out.write(uploadBytes);
            if(uploadBytes(file,key)){
                activity.setDocx(key);
                activityMapper.update(activity);
//                file.delete();
                return bucketDomain+key;
            }else{
                return null;
            }
//            wordMLPackage.save(new java.io.File("C:\\xuanlinDoc\\twzbDoc\\wordTest\\create.docx"));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer getCountNum(Integer activityId) {
        Integer num = activityContentMapper.getCountNum(activityId);
        return num;
    }

    private boolean uploadBytes(File file,String key) {
        UploadManager uploadManager = new UploadManager(getCfg());
        String upToken = getUpToken();
        try {
            Response response = uploadManager.put(file, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return false;
    }

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

    @Override
    public Integer stickActivityContent(Integer id, Integer stickSign ){
        if(stickSign == 0){
            return activityContentMapper.stickActivityContent(id, stickSign);
        }else{
            Integer sign =  activityContentMapper.getMaxSticksign(id);
            return activityContentMapper.stickActivityContent(id, sign);
        }
    }

    @Override
    public Integer getStickNum(Integer id){
            return activityContentMapper.getStickNum(id);
    }

    @Override
    public Integer getNewContentNum (Integer activityId,Date date){
        Integer num  =  activityContentMapper.getNewContentNum(activityId,date);

        return num;
    }

    @Override
    public List<ActivityContent>  getRecentConcentByTime (Integer activityId, Date date, Integer timeOrder,Integer contentNum){
        return activityContentMapper.getRecentConcentByTime(activityId, date, timeOrder, contentNum);
    }

    @Override
    public String getMaxTime(Integer activityId){
        return activityContentMapper.getMaxTime(activityId);
    }

}
