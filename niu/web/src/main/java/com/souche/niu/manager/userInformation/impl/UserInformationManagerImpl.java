package com.souche.niu.manager.userInformation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.souche.adapter.search.common.SearchQuery;
import com.souche.adapter.search.common.SearchResponse;
import com.souche.niu.constant.CarShopConstants;
import com.souche.niu.constant.SubscribeConstants;
import com.souche.niu.dao.userInformation.UserInformationDao;
import com.souche.niu.manager.userInformation.UserInformationManager;
import com.souche.niu.model.userInformation.*;
import com.souche.niu.redis.RedisHashRepository;
import com.souche.niu.spi.UserInformationSPI;
import com.souche.niu.util.StringUtil;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.exception.OptimusExceptionBase;
import com.souche.optimus.redis.RedisCountRepository;
import com.souche.sso.client2.AuthNHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("UserInformationManager")
public class UserInformationManagerImpl implements UserInformationManager {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationManagerImpl.class);

    @Autowired
    private RedisCountRepository redisCountRepository;

    @Autowired
    private UserInformationDao userInformationDao;

    @Autowired
    private UserInformationSPI userInformationSPI;

    @Resource
    private RedisHashRepository redisHashRepository;
    /**
     * 查询订阅信息
     * @return
     */
    public SubscribeInformationDo querySubscribe(String userId) {

        try{
            logger.info("查询订阅信息开始。。。");
            SubscribeInformationDo subscribeInfo = new SubscribeInformationDo();
            //获取订阅信息
            List<SubscribeDo> list = userInformationDao.queryByUserID(userId);
            //是否有订阅的标志   0：没有订阅  1：有订阅
            int subscribe = 0;
            if(null != list && !list.isEmpty()){
                subscribe = 1;
                //只有一条数据
                SubscribeDo subscribeDo = list.get(0);

                //查询条件
                List<String> seriseList = new ArrayList();  //车系
                List<String> brandList = new ArrayList();  //品牌
                List<String> emissionList = new ArrayList();  //排放
                //List<String> locationList = new ArrayList();  //收车区域
                List<String> provinceCodeList = new ArrayList<String>(); //收车区域 省份code
                List<String> cityCodeList = new ArrayList<String>();  //收车区域  城市code
                //上牌年限
                int yearStart = subscribeDo.getYearStart();
                int yearEnd = subscribeDo.getYearEnd();
                //价格区间
                int priceLow = subscribeDo.getPriceLow();
                int priceHigh = subscribeDo.getPriceHigh();

                //品牌，车系
                String series = subscribeDo.getSeries();
                if (series!=null && !series.isEmpty()){
                    String[] strArr = series.split(";");
                    for (int i=0; i<strArr.length; i++){
                        String[] s = strArr[i].split("_");
                        brandList.add(s[0]);
                        if (s.length>1){
                            seriseList.add(s[1]);
                        }
                    }
                }

                //排放
                String emission = subscribeDo.getEmission();
                if (emission != null && !emission.isEmpty()){
                    String [] emissionArr = emission.split(";");
                    for (int i=0; i<emissionArr.length; i++){
                        emissionList.add(emissionArr[i]);
                    }
                }

                //省份，城市
                ArrayList locationMapList = subscribeDo.getLocationMap();
                for (int i=0; i<locationMapList.size(); i++){
                    JSONObject jsonObject = (JSONObject)locationMapList.get(i);
                    String code = jsonObject.getString("code");
                    if (!code.equals("00000")){
                        provinceCodeList.add(code);
                    }
                    JSONArray selectedRows = (JSONArray) jsonObject.get("selectedRows");

                    for (int j=0; j<selectedRows.size(); j++){
                        JSONObject obj = (JSONObject) selectedRows.get(j);
                        if (!obj.getString("code").equals(code)){
                            cityCodeList.add(obj.getString("code"));
                        }
                    }
                }

                //拼接查询条件
                StringBuilder stringBuilder = new StringBuilder();
                if (brandList!=null && brandList.size()>0){
                    stringBuilder.append("brand_code:").append(StringUtil.stringAppend(brandList));
                }
                if (seriseList!=null && seriseList.size()>0){
                    if (stringBuilder.length()>0){
                        stringBuilder.append(" AND ");
                    }
                    stringBuilder.append("series_code:").append(StringUtil.stringAppend(seriseList));
                }
                if (emissionList!=null && emissionList.size()>0){
                    if (stringBuilder.length()>0){
                        stringBuilder.append(" AND ");
                    }
                    stringBuilder.append("emission_standard:").append(StringUtil.stringAppend(emissionList));
                }
                if (provinceCodeList!=null && provinceCodeList.size()>0){
                    if (stringBuilder.length()>0){
                        stringBuilder.append(" AND ");
                    }
                    stringBuilder.append("province_code:").append(StringUtil.stringAppend(provinceCodeList));
                }
                if (cityCodeList!=null && cityCodeList.size()>0){
                    if (stringBuilder.length()>0){
                        stringBuilder.append(" AND ");
                    }
                    stringBuilder.append("city_code:").append(StringUtil.stringAppend(cityCodeList));
                }
                String s = stringBuilder.toString();

                //设置查询条件
                SearchQuery query = new SearchQuery();
                List<String> filter = new ArrayList<String>();
                filter.add("sale_price>="+priceLow);
                filter.add("sale_price<="+priceHigh);
                filter.add("register_year>=" + yearStart);
                filter.add("register_year<="+ yearEnd);
                query.setFilters(filter);
                query.setComboQuery(s);
                query.setSize(3);
                query.setSrc("cheniu");
                Map<String,String> sortMap = new HashMap<String, String>();
                sortMap.put("sort","date_update DESC");
                query.setFetchFields("id;brand_code;brand_name;series_code;series_name;model_name;sale_price;province_code;province_name;city_code;city_name;main_pic;register_year;first_license_plate_date;mileage;emission_standard");
                //调用搜索服务
                SearchResponse responseData = userInformationSPI.search(query, "car");

                List<CarInfoDo> carInfoDoList = new ArrayList();

                String jsonRespones = responseData.getJsonRespones();
                JSONObject jsonObject = JSON.parseObject(jsonRespones);
                JSONObject result = jsonObject.getJSONObject("result");
                Integer total = responseData.getTotal();  //总数量
                JSONArray jsonArray = (JSONArray) result.get("items");

                //将数据放到实体类中
                for (int m=0; m<jsonArray.size(); m++){
                    CarInfoDo carInfoDo = new CarInfoDo();
                    JSONObject object = (JSONObject) jsonArray.get(m);
                    String standard = object.getString("emission_standard");
                    if (standard.equals("g1")){
                        standard = "国一";
                    }else if (standard.equals("g2")){
                        standard = "国二";
                    }else if (standard.equals("g3")){
                        standard = "国三";
                    }else if (standard.equals("g4")){
                        standard = "国四";
                    }else if (standard.equals("g5")){
                        standard = "国五";
                    }else if (standard.equals("g6")){
                        standard = "国六";
                    }
                    carInfoDo.setCarId(object.getString("id"));
                    carInfoDo.setAndroid_protocol("cheniu://open/car/detail/" + object.getString("id"));
                    carInfoDo.setProtocol("cheniu://open/carDetail?carId="+ object.getString("id"));
                    //返回数据处理为"1.2万"的格式
                    double mileage = object.getInteger("mileage");
                    carInfoDo.setMileage(mileage/10000 + "万");
                    carInfoDo.setDate(object.getInteger("register_year"));
                    carInfoDo.setEmission(standard);
                    carInfoDo.setModeName(object.getString("model_name"));
                    //返回数据处理为"1.2万"的格式
                    double salePrice = object.getInteger("sale_price");
                    carInfoDo.setPrice(salePrice/10000 + "万");
                    carInfoDo.setMainPicture(object.getString("main_pic"));

                    carInfoDoList.add(carInfoDo);
                }

                //订阅的新增车辆,redis中去查询
                logger.info("redis中去查询订阅的新车数量。。。");
                int newSubscribeCar = 0;
                Map<String, Object> subscribeMap = getSubscribeByUserId(userId);
                logger.info("redis中去查询订阅的新车数量返回结果为subscribeMap={}",subscribeMap);
                int newDealerCars = (int)subscribeMap.get(SubscribeConstants.NEW_DEALER_CARS); //新增车商车源
                int newPersonalCars = (int)subscribeMap.get(SubscribeConstants.NEW_PERSONAL_CARS);//新增个人车源
                newSubscribeCar = newDealerCars + newPersonalCars;
                subscribeInfo.setTotleNum(newSubscribeCar);
                subscribeInfo.setSubscribe(subscribe);
                subscribeInfo.setCar_list(carInfoDoList);
                logger.info("订阅信息数据为subscribenInfo={}",subscribeInfo);
                return subscribeInfo;
            }else {
                subscribeInfo.setTotleNum(0);
                subscribeInfo.setSubscribe(subscribe);
                subscribeInfo.setCar_list(null);
                logger.info("订阅信息数据为subscribenInfo={}",subscribeInfo);
                return subscribeInfo;
            }
        }catch (Exception e){
            logger.error("查询订阅信息时异常 e=",e);
            return null;
        }
    }

    /**
     *图标工具
     * @return
     */
    public IconDo getIconList(){
        try{
            logger.info("获取图标工具");
            IconDo iconDo = new IconDo();

            List<IconUtilDo> iconDoList = new ArrayList<IconUtilDo>();

            String groupId = OptimusConfig.getValue("server.tool.groupId");
            //根据groupId查询信息
            List<TbKvValueDo> kvList = userInformationDao.getKvList(groupId);

            if (kvList == null || kvList.size()==0){
                return null;
            }

            //数据处理
            for (TbKvValueDo tbKvValueDo : kvList) {
                String value = tbKvValueDo.getValue();
                net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(value);
                IconUtilDo iconUtilDo = new IconUtilDo();
                boolean firstShow=true;//是否在首页显示
                for (int i=0; i<jsonArray.size(); i++) {
                    net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) jsonArray.get(i);
                    String keyStr = jsonObject.getString("key");
                    String valueStr = jsonObject.getString("value");
                    if (keyStr.equals("title")){
                        iconUtilDo.setTitle(valueStr);
                    }
                    if (keyStr.equals("protocol")){
                        iconUtilDo.setProtocol(valueStr);
                    }
                    if (keyStr.equals("image_2x")){
                        iconUtilDo.setImage_2x(valueStr);
                    }
                    if (keyStr.equals("image_3x")){
                        iconUtilDo.setImage_3x(valueStr);
                    }
                    if (keyStr.equals("eventKey")){
                        iconUtilDo.setEventKey(valueStr);
                    }
                    if (keyStr.equals("orderNum")){
                        iconUtilDo.setOrderNum(jsonObject.getInt("value"));
                    }
                    if (keyStr.equals("firstShow")){
                        iconUtilDo.setFirstShow(jsonObject.getBoolean("value"));
                        firstShow = jsonObject.getBoolean("value");
                    }
                }
                if (firstShow){
                    iconDoList.add(iconUtilDo);
                }
            }
            Collections.sort(iconDoList);//排序
            iconDo.setAllutilname("cheniu://open/webv?url=https://f2e-assets.souche.com/cheniu/tools/index");
            iconDo.setAllutilprotocol("所有工具");
            iconDo.setUtil(iconDoList);
            logger.info("查询图标工具信息为iconDo={}",iconDo);
            return iconDo;
        }catch (Exception e){
            logger.error("查询图标工具信息时异常 e=",e);
            return null;
        }
    }

    /**
     * 获取我的车店
     * @return MyCarShopDo
     */
    public MyCarShopDo getMyCarShop(String userId,String shopCode){

        MyCarShopDo myCarShop = new MyCarShopDo();
        //获取在售，已售，新访客数量
        CarShopDo carShopDo = myCarShop(userId,shopCode);

        //判断已售和在售车辆是否都为0，都为0则将标志为设置为0，否则为1
        if (carShopDo.getSaledNum()==0 && carShopDo.getOnSaleNum()==0){
            myCarShop.setHasCarShopData(0);
        }else {
            myCarShop.setHasCarShopData(1);
        }
        //新访客处理
        int newVistor = carShopDo.getNewVisitor();
        String newVistorStr= (newVistor>99) ? "99+" : String.valueOf(newVistor);
        //在售车辆处理
        int onsaleNum = carShopDo.getOnSaleNum();
        String onsaleNumStr = (onsaleNum>99) ? "99+" : String.valueOf(onsaleNum);
        //已售车辆处理
        int saledNum = carShopDo.getSaledNum();
        String saledNumStr = (saledNum > 99) ? "99+" : String.valueOf(saledNum);

        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("itemValue",String.valueOf(onsaleNumStr));
        map1.put("itemDesc","在售(辆)");
        map1.put("eventKey", CarShopConstants.ONSALING_EVENT_KEY);
        map1.put("protocol","http://www.baidu.com");

        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("itemValue",String.valueOf(saledNumStr));
        map2.put("itemDesc","已售(辆)");
        map2.put("eventKey", CarShopConstants.SALED_EVENT_KEY);
        map2.put("protocol","http://www.baidu.com");

        Map<String,Object> map3 = new HashMap<String,Object>();
        map3.put("itemValue",newVistorStr);
        map3.put("itemDesc","新访客");
        map3.put("eventKey",CarShopConstants.NEWVISITOR_EVENT_KEY);
        map3.put("protocol","http://www.baidu.com");

        list.add(map1);
        list.add(map2);
        list.add(map3);

        myCarShop.setCarShopData(list);
        logger.info("我的车店返回信息为myCarShop={}",myCarShop);
        return myCarShop;
    }

    private CarShopDo myCarShop(String userId,String shopCode) {

        try{
            logger.info("调用服务查询车店信息。。。");
            //调用搜索服务查询
            int onsaleNum = carSearch("5",shopCode); //在售
            int saledNum = carSearch("6",shopCode); //已售
            //访客数量
            int newVistor = userInformationSPI.getNewVistor(userId,shopCode);
            CarShopDo carShopDo = new CarShopDo();
            carShopDo.setOnSaleNum(onsaleNum);
            carShopDo.setSaledNum(saledNum);
            carShopDo.setNewVisitor(newVistor);
            logger.info("调用服务查询车店信息结果为carShop={}",carShopDo);
            return carShopDo;
        }catch (Exception e){
            logger.error("查询我的车店信息时出错 e=",e);
            return null;
        }
    }

    /**
     * 获取banner信息
     * @return
     */
    public List<BannerDo> getBannerlist() {

        try{
            logger.info("查询banner信息...");
            List<BannerDo> bannerDoList = new ArrayList<BannerDo>();

            String groupId = OptimusConfig.getValue("banner.groupId");
            //根据groupId查询banner信息
            List<TbKvValueDo> kvList = userInformationDao.getKvList(groupId);

            if (kvList == null || kvList.size()==0){
                return null;
            }

            //数据处理，将有用信息放到BannerDo中
            for (TbKvValueDo tbKvValueDo : kvList) {
                String value = tbKvValueDo.getValue();
                net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(value);
                BannerDo bannerDo = new BannerDo();
                for (int i=0; i<jsonArray.size(); i++) {
                    net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) jsonArray.get(i);
                    String keyStr = jsonObject.getString("key");
                    String valueStr = jsonObject.getString("value");
                    if (keyStr.equals("title")){
                        bannerDo.setDesc(valueStr);
                    }
                    if (keyStr.equals("image")){
                        bannerDo.setImage_url(valueStr);
                    }
                    if (keyStr.equals("address")){
                        bannerDo.setProtocol(valueStr);
                    }
                    if (keyStr.equals("orderNum")){
                        bannerDo.setOrderNum(jsonObject.getInt("value"));
                    }
                }
                bannerDoList.add(bannerDo);
            }
            Collections.sort(bannerDoList);
            logger.info("查询banner信息返回结果为bannerDoList={}",bannerDoList);
            return bannerDoList;
        }catch (Exception e){
            logger.error("查询banner信息异常e=",e);
            return null;
        }

    }

    /**
     * 获取活动浮窗
     * @return
     */
    public ActivityScreenDo getActivityScreen() {
        try{
            logger.info("查询活动浮窗信息。。。");
            List<ActivityScreenDo> list = new ArrayList<>();
            String groupId = OptimusConfig.getValue("activity.screen.group.id");
            //根据groupId查询活动浮窗信息
            List<TbKvValueDo> kvList = userInformationDao.getKvList(groupId);
            if (kvList == null || kvList.size()==0){
                return null;
            }
            //数据处理
            for (TbKvValueDo tbKvValueDo : kvList) {
                int id = tbKvValueDo.getId();
                String value = tbKvValueDo.getValue();
                net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(value);
                ActivityScreenDo activityScreenDo = new ActivityScreenDo();
                for (int i=0; i<jsonArray.size(); i++) {
                    net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) jsonArray.get(i);
                    String keyStr = jsonObject.getString("key");
                    String valueStr = jsonObject.getString("value");
                    if (keyStr.equals("name")){
                        activityScreenDo.setName(valueStr);
                    }
                    if (keyStr.equals("protocol")){
                        activityScreenDo.setProtocol(valueStr);
                    }
                    if (keyStr.equals("image")){
                        activityScreenDo.setImage(valueStr);
                    }
                    if (keyStr.equals("expiry_start")){
                        activityScreenDo.setExpiryStart(valueStr);
                    }
                    if (keyStr.equals("expiry_end")){
                        activityScreenDo.setExpiryEnd(valueStr);
                    }
                }
                activityScreenDo.setId(id);
                list.add(activityScreenDo);
            }
            Collections.sort(list);
            logger.info("查询活动浮窗信息为：{}",list.get(0));
            //返回最新一条数据
            return list.get(0);
        }catch (Exception e){
            logger.error("查询活动浮窗信息异常e=",e);
            return null;
        }
    }

    //search服务
    public int carSearch(String status,String shopCode) {
        logger.info("调用搜索服务。。。");
        SearchQuery query = new SearchQuery();
        Map<String, String> andQuery = new HashMap();
        andQuery.put("status",status); //5:在售  6:已售
        andQuery.put("store",shopCode);
        query.setAndQuerys(andQuery);
        //调用搜索服务
        SearchResponse result = userInformationSPI.search(query, "car");
        return result.getTotal();
    }

    /**
     * 根据user_id获取订阅信息
     * map包含三个键值对
     * has_subscribe：true/false
     * new_personal_cars:Integer
     * new_dealer_cars:Integer
     * @param userId
     * @return
     */
    private Map<String, Object> getSubscribeByUserId(String userId) {
        Map<String, Object> data = new HashMap<>();
        //初始化 个人车辆订阅数 零售商车辆订阅数
        int personCarsCount=0;
        int dealerCarsCount=0;
        data.put(SubscribeConstants.HAS_SUBSCRIBE, false);
        data.put(SubscribeConstants.NEW_PERSONAL_CARS, personCarsCount);
        data.put(SubscribeConstants.NEW_DEALER_CARS, dealerCarsCount);
        try {
            boolean hasSubscribe = countSubscribeByUserId(userId);

            logger.info("Redis取个人车辆订阅数 key:{},hashKey:{}",SubscribeConstants.SUBSCRIBE_PART_KEY + userId,SubscribeConstants.NEW_PERSONAL_CARS);
            Object personCarsObj = redisHashRepository.get(SubscribeConstants.SUBSCRIBE_PART_KEY + userId, SubscribeConstants.NEW_PERSONAL_CARS);
            if (personCarsObj != null && com.souche.optimus.common.util.StringUtil.isNotEmpty(personCarsObj.toString())) {
                personCarsCount = Integer.parseInt(personCarsObj.toString());
            }

            logger.info("Redis取零售商车辆订阅数 key:{},hashKey:{}",SubscribeConstants.SUBSCRIBE_PART_KEY + userId,SubscribeConstants.NEW_DEALER_CARS);
            Object dealerCarsObj = redisHashRepository.get(SubscribeConstants.SUBSCRIBE_PART_KEY + userId, SubscribeConstants.NEW_DEALER_CARS);
            if (dealerCarsObj != null && com.souche.optimus.common.util.StringUtil.isNotEmpty(dealerCarsObj.toString())) {
                dealerCarsCount = Integer.parseInt(dealerCarsObj.toString());
            }

            data.put(SubscribeConstants.HAS_SUBSCRIBE, hasSubscribe);
            data.put(SubscribeConstants.NEW_PERSONAL_CARS, personCarsCount);
            data.put(SubscribeConstants.NEW_DEALER_CARS, dealerCarsCount);
        } catch (OptimusExceptionBase e) {
            logger.error("获取订阅信息错误 {}",e.toString());
        }
        return data;
    }

    /**
     * 根据userid统计是否有订阅
     * @param userId
     * @return true:表示有订阅数 false：表示无订阅数
     */
    public Boolean countSubscribeByUserId(String userId) {
        if (com.souche.optimus.common.util.StringUtil.isEmpty(userId)) {
            logger.error("根据用户ID统计订阅数失败 userID为空");
            throw new OptimusExceptionBase("根据用户ID统计订阅数失败 userID为空");
        }
        try {
            int count = userInformationDao.countSubscribeByUserId(userId);
            if (count == 0) {
                logger.info("当前用户ID未统计到订阅信息 userID：{}",userId);
                return false;
            }
            logger.info("当前用户ID统计到订阅数 userID：{}，count:{}",userId,count);
            return true;
        } catch (OptimusExceptionBase e) {
            logger.error("根据用户ID统计订阅数失败 errMsg:{}",e.toString());
            throw e;
        }
    }

    /**
     * 获取开屏信息
     * @return
     */
    public OpenScreenDO getOpenScreen(String groupId) {
        try{
            logger.info("查询开屏信息。。。");
            List<OpenScreenDO> list = new ArrayList<>();
            //根据groupId查询开屏信息
            List<TbKvValueDo> kvList = userInformationDao.getKvList(groupId);
            if (kvList == null || kvList.size()==0){
                return null;
            }
            //数据处理
            for (TbKvValueDo tbKvValueDo : kvList) {
                int id = tbKvValueDo.getId();
                String value = tbKvValueDo.getValue();
                net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(value);
                OpenScreenDO openScreenDO = new OpenScreenDO();
                for (int i=0; i<jsonArray.size(); i++) {
                    net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) jsonArray.get(i);
                    String keyStr = jsonObject.getString("key");
                    String valueStr = jsonObject.getString("value");
                    if (keyStr.equals("protocol")){
                        openScreenDO.setProtocol(valueStr);
                    }
                    if (keyStr.equals("name")){
                        openScreenDO.setName(valueStr);
                    }
                    if (keyStr.equals("image")){
                        openScreenDO.setImage(valueStr);
                    }
                    if (keyStr.equals("expiry_start")){
                        openScreenDO.setExpiryStart(valueStr);
                    }
                    if (keyStr.equals("expiry_end")){
                        openScreenDO.setExpiryEnd(valueStr);
                    }
                }
                openScreenDO.setId(id);
                list.add(openScreenDO);
            }
            Collections.sort(list);
            logger.info("查询开屏信息为：{}",list.get(0));
            //返回最新一条数据
            return list.get(0);
        }catch (Exception e){
            logger.error("查询开屏信息异常e=",e);
            return null;
        }
    }

}
