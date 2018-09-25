package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.service.*;
import cn.ourwill.huiyizhan.service.impl.GenerateTicketService;
import cn.ourwill.huiyizhan.service.search.IElasticSearchService;
import cn.ourwill.huiyizhan.service.search.ISearchService;
import cn.ourwill.huiyizhan.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.ourwill.huiyizhan.entity.Config.bucketDomain;
import static cn.ourwill.huiyizhan.entity.Config.willcenterBucketDomain;
import static cn.ourwill.huiyizhan.utils.EmailUtil.SMTP_QQ;
/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/21 16:38
 * @Description
 */
@RestController
@Slf4j
public class TestApi {

    @Autowired
    private WillCenterConstants willCenter;

    @Autowired
    private TransportClient client;

    @Autowired
    private IUserService userService;


    @Autowired
    private IActivityService activityService;

    @Autowired
    private ITrxOrderService trxOrderService;

    @Autowired
    private ITicketsRecordService ticketsRecordService;

    @Autowired
    IActivityTypeService activityTypeService;

    @Autowired
    GenerateTicketService generateTicketService;

    @Autowired
    ObjectMapper          objectMapper;
    @Autowired
    ISearchService        searchService;
    @Autowired
    IElasticSearchService elasticSearchService;

    @Autowired
    IUserStatisticsService userStatisticsService;

    @Autowired
    IImageTmpService imageTmpService;

    @Autowired
    IFdfsImageService fdfsImageService;

    @Value("${fastdfs.basePrefix}")
    private String basePrefix;


//    @Autowired
//    IImgService;
//    @Autowired
//    SyncToESUpdateService syncToESUpdateService;


    @RequestMapping("/tests/testLogin")
    @ResponseBody
    public Map testLogin(HttpServletRequest request, String username) {
        try {
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(willCenter.getTestLoginUrl() + "?username=" + username);
            httpClient.executeMethod(getMethod);
            String userString = getMethod.getResponseBodyAsString();
            JSONObject jsonObject = JSONObject.fromObject(userString);
            User user = (User) JSONObject.toBean(jsonObject, User.class);
            User loginUser = user;
            HttpSession session = request.getSession();
            //获取本地用户数据
            User localUser = userService.selectByUuid(loginUser.getUUID());
            if (localUser == null) {
                user.setId(null);
                //如果本地没有该用户 则创建
                localUser = userService.SyncFormCenter(user);
            } else {
                localUser = userService.SyncFormCenter(user, localUser.getUUID());
            }
            loginUser.setId(localUser.getId());
            loginUser.setLevel(localUser.getLevel());
            session.setAttribute("loginUser", loginUser);
            //释放此HTTP方法正在使用的连接
            getMethod.releaseConnection();
        } catch (Exception e) {
            log.info("TestApi.testLogin", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
        return ReturnResult.successResult("登录成功！");
    }

    @PostMapping("/user/login")
    @ResponseBody
    public Map test(HttpServletRequest request, Map param) {
        return ReturnResult.successResult("data", param, ReturnType.GET_SUCCESS);
    }

    @RequestMapping("/test/send")
    @ResponseBody
    public Map testSend() {
        try {
            EmailUtil.config(SMTP_QQ(false), "515891584@qq.com", "vnhkyepmsxiubiec");
            BufferedReader reader = new BufferedReader(new FileReader(new File("H:\\result.html")));
            String line;
            String html = "";
            while ((line = reader.readLine()) != null) {
                html += line;
            }

            EmailUtil.subject("测试HTML")
                    .from("蜗牛快跑")
                    .to("862990693@qq.com")
                    .html(html)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
        return ReturnResult.successResult("成功");
    }


    @GetMapping("/testES")
    @ResponseBody
    public ResponseEntity get() {
        GetResponse res = this.client.prepareGet("people", "man", "1").get();
        client.prepareBulk();
        return new ResponseEntity(res.getSource(), HttpStatus.OK);
    }

    @GetMapping("/testImport")
    @ResponseBody
    public void testImport() throws Exception {
        List<Activity> list = activityService.findAll();
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Activity activity : list) {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", activity.getId())
                    .field("userId", activity.getUserId())
                    .field("activityTitle", activity.getActivityTitle())
                    .field("activityAddress", activity.getActivityAddress())
                    .field("startTime", dateFm.format(activity.getStartTime()))
                    .field("endTime", dateFm.format(activity.getEndTime()))
                    .field("activityDescription", activity.getActivityDescription())
                    .field("activityBanner", activity.getActivityBanner())
                    .field("activityBannerMobile", activity.getActivityBannerMobile())
                    .endObject();

            client.prepareIndex("huiyizhan", "activity")
                    .setSource(xContentBuilder)
                    .get();
        }

    }

    @GetMapping("/testUpdate")
    @ResponseBody
    public void testUpdate() throws IOException {
        List<Activity> list = activityService.findAll();
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BulkRequestBuilder bulkRequestBuilder = this.client.prepareBulk();
        for (Activity activity : list) {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", activity.getId())
                    .field("userId", activity.getUserId())
                    .field("activityTitle", activity.getActivityTitle())
                    .field("activityType", activity.getActivityType())
                    .field("startTime", dateFm.format(activity.getStartTime()))
                    .field("endTime", dateFm.format(activity.getEndTime()))
                    .field("activityAddress", activity.getActivityAddress())
                    .field("activityDescription", activity.getActivityDescription())
                    .field("isOpen", activity.getIsOpen())
                    .field("isOnline", activity.getIsOnline())
                    .field("scheduleStatus", activity.getScheduleStatus())
                    .field("guestStatus", activity.getGuestStatus())
                    .field("partnerStatus", activity.getPartnerStatus())
                    .field("activityBanner", activity.getActivityBanner())
                    .field("activityBannerMobile", activity.getActivityBannerMobile())
                    .endObject();
            IndexRequestBuilder indexRequestBuilder = client
                    .prepareIndex("huiyizhan", "activity")
                    .setSource(xContentBuilder);
            bulkRequestBuilder.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        System.out.println(bulkResponse.status());
    }


//    @GetMapping("/testDelete")
//    @ResponseBody
//    public void testDelete()throws  Exception{
//        DeleteResponse res = this.client.prepareDelete("huiyizhan","activity");
//    }


    @GetMapping("/testAuto")
    @ResponseBody
    public void testAuto() throws Exception {
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

            }
        }).setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB))//总体积达到1M提交
                .setFlushInterval(TimeValue.timeValueSeconds(5))//五秒提交一次
                .setConcurrentRequests(1)//加1后为可并行的提交请求数，即设为0代表只可1个请求并行，设为1为2个并行
                .build();
    }

    @RequestMapping("/testSearchByDate")
    @ResponseBody
    public Map testSearchByDate(int amount, @RequestParam("filed") String filed, @RequestParam("value") String value) {
        try {
            String date = TimeUtils.calDate(amount);
            String index = "huiyizhan";
            String type = "activity";
            SearchResponse response = this.client.prepareSearch()
                    .setIndices(index)
                    .setTypes(type)
                    .setQuery(QueryBuilders.rangeQuery("start_time").format("yyyy-MM-dd").gt(date))
                    //.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery(filed,value)))
                    .setQuery(QueryBuilders.matchQuery(filed, value))
                    .get();

            //.setQuery(QueryBuilders.rangeQuery("start_time").gt(today)).get();
            List<Map<String, Object>> res = new ArrayList<>();
            for (SearchHit hit : response.getHits()) {
                System.out.println(hit.getSource().get("activity_banner"));
                System.out.println(hit.getSource().get("activity_banner_mobile"));
                System.out.println(hit.getSource().get("avatar"));

                String activityBannerTemp = (String) hit.getSource().get("activity_banner");
                if (StringUtils.isNotEmpty(activityBannerTemp) && activityBannerTemp.indexOf("http") < 0) {
                    String activityBanner = bucketDomain + activityBannerTemp;
                    hit.getSource().replace("activity_banner", activityBanner);
                }
                String activityBannerMobileTemp = (String) hit.getSource().get("activity_banner_mobile");
                if (StringUtils.isNotEmpty(activityBannerMobileTemp) && activityBannerMobileTemp.indexOf("http") < 0) {
                    String activityBannerMobile = bucketDomain + activityBannerMobileTemp;
                    hit.getSource().replace("activity_banner_mobile", activityBannerMobile);
                }
                String avatarTemp = (String) hit.getSource().get("avatar");
                if (StringUtils.isNotEmpty(activityBannerTemp) && activityBannerTemp.indexOf("http") < 0) {
                    String avatar = willcenterBucketDomain + avatarTemp;
                    hit.getSource().replace("activity_banner", avatar);
                }
                Map map = CamelCaseUtil.toCamlCase(hit.getSource());
                res.add(map);
            }
            return ReturnResult.successResult("data", res, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    @PutMapping("/test/{id}")
//    @ResponseBody
//    public Map uploadActivity(@PathVariable("id") Integer id) {
//        try {
//            Activity activity1 = new Activity();
//            activity1.setUTime(new Date());
//            activity1.setActivityTitle("今夜阳光明媚");
//            activity1.setStartTime(new Date());
//            activity1.setId(id);
//            Integer resultCode = activityService.update(activity1);
////            ActivityType activityTypeById = this.activityTypeService.getActivityTypeById(12);
//            activity1.setActivityType(15);
//            syncToESUpdateService.init(activity1);
//            Thread t = new Thread(syncToESUpdateService);
//            t.start();
//            if (resultCode > 0) {
//                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
//            } else {
//                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
//            }
//        } catch (UnPermissionException e) {
//            throw e;
//        } catch (Exception e) {
//            log.info("ActivityController.uploadActivity", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }

    @GetMapping("/test/activityType/{id}")
    @ResponseBody
    public Map testActivityType(@PathVariable("id") Integer id) {
        ActivityType activityTypeById = this.activityTypeService.getActivityTypeById(id);
        return ReturnResult.successResult("data", activityTypeById, ReturnType.GET_SUCCESS);
    }

    @GetMapping("/test/insertAll")
    @ResponseBody
    public Map testInsertAll() throws Exception {
        List<SearchBean> allSearchBean = this.searchService.getAllSearchBean();
//        for(SearchBean searchBean : allSearchBean){
//              this.client.prepareIndex(SearchIndex.INDEX, SearchIndex.TYPE)
//                    .setSource(this.objectMapper.writeValueAsBytes(searchBean), XContentType.JSON).get();
//        }
//        BulkRequestBuilder bulkRequestBuilder = this.client.prepareBulk();
//        for(SearchBean searchBean : allSearchBean){
//            bulkRequestBuilder.add(this.client.prepareIndex(SearchIndex.INDEX,SearchIndex.TYPE)
//                    .setId(searchBean.getId().toString())
//                    .setSource(this.objectMapper.writeValueAsBytes(searchBean),XContentType.JSON));
//        }
//        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().get();
//        System.out.println(bulkItemResponses.status());//OK
//        return ReturnResult.successResult("data",bulkItemResponses,ReturnType.GET_SUCCESS);
        this.elasticSearchService.importAll();
        return ReturnResult.successResult("OK");
    }

    @DeleteMapping("/test/delete/{id}")
    @ResponseBody
    public Map testDelete(@PathVariable("id") Integer id) {
        DeleteByQueryRequestBuilder source = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(this.client)
                .filter(QueryBuilders.termQuery(SearchIndex.ID, id))
                .source(SearchIndex.INDEX);
        BulkByScrollResponse response = source.get();
        BulkByScrollTask.Status status = response.getStatus();
        System.out.println(response.getStatus() + "   ====");
        return ReturnResult.successResult("data", response.toString(), ReturnType.GET_SUCCESS);
    }

    @GetMapping("/test/insert/{id}")
    @ResponseBody
    public Map testImportAll(@PathVariable("id") Integer id) throws Exception {
//        SearchBean searchBean = this.searchService.getSearchBean(id);
//        IndexResponse response = this.client.prepareIndex(SearchIndex.INDEX, SearchIndex.TYPE,id.toString())
//                .setSource(this.objectMapper.writeValueAsBytes(searchBean), XContentType.JSON).get();
//        return ReturnResult.successResult("data",response.status(),ReturnType.GET_SUCCESS);
        SearchBean searchBean = searchService.getSearchBean(id);
        this.elasticSearchService.insert(searchBean);

        return ReturnResult.successResult("OK");

    }

    @DeleteMapping("/test/deleteAll")
    @ResponseBody
    public Map testDeleteAll() {
//        DeleteByQueryRequestBuilder source = DeleteByQueryAction.INSTANCE
//                .newRequestBuilder(this.client)
//                .filter(QueryBuilders.matchAllQuery())
//                .source(SearchIndex.INDEX);
//        BulkByScrollResponse response= source.get();
//        System.out.println(response.getStatus()+"   ====");
//        return ReturnResult.successResult("data",response.toString(),ReturnType.GET_SUCCESS);
        this.elasticSearchService.deleteAll();
        return ReturnResult.successResult("OK");

    }

    @PostMapping("/test/img")
    @ResponseBody
    public Map testCreateIndex(@RequestParam("id") Integer id) throws Exception {
        HashMap map = new HashMap();
        if (id == null) {
            for (int i = 0; i <= 163; i++) {
                TicketsRecord record = ticketsRecordService.getById(i);
                if (record == null) {
                    continue;
                } else {
                    String s = generateTicketService.generateTicketImg(record);
                    map.put(record.getId(), s);
                }
            }
        } else {
            TicketsRecord record = ticketsRecordService.getById(Integer.valueOf(id));
            String s = generateTicketService.generateTicketImg(record);
            map.put(record.getId(), s);
        }
        return ReturnResult.successResult("data", map, ReturnType.GET_SUCCESS);
    }


    @PostMapping("/test/singlePDF")
    @ResponseBody
    public Map testPDF(@RequestParam("id") Integer id) throws Exception {
        List<String> list = new ArrayList<>();
        if(id <= 0){
            for (int i = 0; i <= 163; i++) {
                TicketsRecord record = ticketsRecordService.getById(i);
                if (record == null) {
                    continue;
                } else {
                    List<TicketsRecord> list1 = new ArrayList();
                    list1.add(record);
                    String s = generateTicketService.getTicketPDF(list1, false);
                    list.add(s);
                }
            }
        }else{
            TicketsRecord record = ticketsRecordService.getById(id);
            List<TicketsRecord> list1 = new ArrayList();
            list1.add(record);
            String s = generateTicketService.getTicketPDF(list1,false);
            return ReturnResult.successResult("data", s, ReturnType.GET_SUCCESS);
        }
        return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
    }

    @PostMapping("/test/email")
    @ResponseBody
    public Map testEmail(@RequestParam("id") Integer id) {
        HashMap map = new HashMap();
        HashMap res = new HashMap();
        TrxOrder trxOrder = trxOrderService.selectById(id);
        User user = userService.getById(trxOrder.getUserId());
        Activity activity = activityService.getById(trxOrder.getActivityId());
        HashMap tempMap = new HashMap();
        tempMap.put("orderId", trxOrder.getId());
        ticketsRecordService.selectByParamsWithOrder(tempMap);
        List<TicketsRecord> ticketsRecords = trxOrder.getTicketsRecords();
        int checkNot = 0;//待审核票的数量
        int check = 0;//已审核票的数量
        //遍历获取票的相关情况
//        for (TicketsRecord record : ticketsRecords) {
//            if (record.getTicketStatus() == 3) {
//                checkNot++;
//            } else {
//                check++;
//            }
//
//            String authCode = record.getAuthCode();
//            record.setAuthCode(ImgUtil.getQRCodeBase64(authCode));
//        }
        //将josn类型的地址解析成字符串
        String address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
        //将数据封装
        map.put("checkNot", checkNot);
        map.put("check", check);
        map.put("ticketsRecords", ticketsRecords);
        map.put("user", user);
        map.put("activity", activity);
        map.put("address", address);
        map.put("count", ticketsRecords.size());
        map.put("QRCode", trxOrder.getQRCodeTicketUrl());
        String path = generateTicketService.generateEmailContent(map, "email.ftl", "test");
        res.put(trxOrder.getId(), path);
        return ReturnResult.successResult("data", res, ReturnType.GET_SUCCESS);
    }

    @PostMapping("/tests/userStatisticsSync")
    @ResponseBody
    public Map userStatisticsSync(@RequestParam("password") String id) {
        if(id.equals("ourwill")){
            userStatisticsService.syncUserStatisticsToRedis();
        }
        return null;
    }

    //@Value("${IMAGE_SERVER_BASE_URL}")
    private String IMAGE_SERVER_BASE_URL;

    /**
     * 测试单图片上传
     * @param picFile
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/test/fastDFS")
    @ResponseBody
    public String uploadPic(MultipartFile picFile, HttpServletRequest request, HttpServletResponse response){
        try{
            if (picFile.isEmpty()){
                return null;
            }
            Socket sock = new Socket();
            sock.setSoTimeout(3000);
            //get the file's extension
            String originalFilename=picFile.getOriginalFilename();
            String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            FastDFSClient client=new FastDFSClient();
//            String url=client.uploadFile(picFile.getBytes(),extName);
            String url=client.uploadFile(basePrefix,picFile.getBytes(),extName);
            //stitching the URL
            //url=IMAGE_SERVER_BASE_URL+url;
            if(!StringUtils.isEmpty(url)){
                FdfsImage fdfsImage = new FdfsImage();
                fdfsImage.setUrl(url);
                fdfsImage.setUploadTime(new Date());
                fdfsImage.setFlag(0);
                fdfsImageService.save(fdfsImage);
            }
            return url;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除图片
     * @param url
     * @return
     */
    @RequestMapping("/test/deleteFastDFS")
    @ResponseBody
    public Map deletePic (@RequestParam("url") String url){
        try {
            FastDFSClient client=new FastDFSClient();
            Integer num = client.deleteFile(url);
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }

    }

    /**
     * 测试网站图片上传
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/test/downPic")
    @ResponseBody
    public String downPic(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String picPath = "http://s.tuwenzhibo.com/"+"temp/willcenter/20180615/1530325739192.blob";
        try{
            File file = new File(picPath);
            String filename = file.getName();
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            URL url = new URL(picPath);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
             con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();

            FastDFSClient client2=new FastDFSClient();
            byte[] buffer =readInputStream(is);
            String re2 = client2.uploadFile("group2",buffer, ext);
            if(!StringUtils.isEmpty(re2)){
                FdfsImage fdfsImage = new FdfsImage();
                fdfsImage.setUrl(re2);
                fdfsImage.setUploadTime(new Date());
                fdfsImage.setFlag(0);
                fdfsImageService.save(fdfsImage);
            }
            return re2;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 读取InputStream数据，转为byte[]数据类型
     * @param is InputStream数据
     * @return 返回byte[]数据
     */
    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 图片批量转移
     * @param tableName 表名
     * @param fieldName 字段名
     * @return
     */
    @RequestMapping("/test/transferImage")
    @ResponseBody
    public Map transferImage(@RequestParam("tableName") String tableName,@RequestParam("fieldName") String fieldName){
//        String tableName = "activity";
//        String fieldName = "activity_banner";
        List<ImageTmp> list = imageTmpService.selectImageTmp(tableName,fieldName);
        Integer id = null;
        try {
            //List<ImageTmp> list = imageTmpService.selectImageTmp(tableName,fieldName);
            String urlOld;
            for(int i= 0;i<list.size();i++){
                ImageTmp imageTmp = list.get(i);
                id = imageTmp.getId();
                if(!StringUtils.isEmpty(imageTmp.getUrlOld())){
//                    if("user".equals(tableName)&&imageTmp.getUrlOld().indexOf("thirdwx")<0){
//                        urlOld = "http://s.tuwenzhibo.com"+imageTmp.getUrlOld();
//                    }else {
                        if(imageTmp.getUrlOld().indexOf("thirdwx")>0||imageTmp.getUrlOld().indexOf("p5zhgy42k")>0 || imageTmp.getUrlOld().indexOf("qiantucdn")>0){
                            urlOld = imageTmp.getUrlOld();
                        }else if (imageTmp.getUrlOld().indexOf("tuwenzhibo")>0){
                            urlOld = null;
                        }else {
                            urlOld = "http://s.tuwenzhibo.com/"+imageTmp.getUrlOld();
                        }
//                    }
                    String url = downPic(urlOld);
                    imageTmp.setUrlNew(url);
                    imageTmpService.update(imageTmp);
                }
            }
            return ReturnResult.successResult(ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(id.toString());
        }
    }

    public String downPic(String picPath)throws Exception {
        try{
            if (StringUtils.isEmpty(picPath)){
                return null;
            }
            File file = new File(picPath);
            String filename = file.getName();
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            URL url = new URL(picPath);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();

            FastDFSClient client=new FastDFSClient();
            byte[] buffer =readInputStream(is);
            String re = client.uploadFile("group2",buffer,  ext);
            if(!StringUtils.isEmpty(re)) {
                FdfsImage fdfsImage = new FdfsImage();
                fdfsImage.setUrl(re);
                fdfsImage.setUploadTime(new Date());
                fdfsImage.setFlag(1);
                fdfsImageService.save(fdfsImage);
            }
            return re;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
