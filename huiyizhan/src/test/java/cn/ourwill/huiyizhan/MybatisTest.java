package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.mapper.*;
import cn.ourwill.huiyizhan.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-03-22 22:01
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    @Autowired
    private BannerHomeMapper bannerHomeMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityContactMapper activityContactMapper;


    @Autowired
    private ActivityGuestMapper activityGuestMapper;

    @Autowired
    private ActivityPartnerMapper activityPartnerMapper;

    @Autowired
    private ActivityScheduleMapper activityScheduleMapper;

    @Autowired
    private ActivityTicketsMapper activityTicketsMapper;

    @Test
    public void addTest() {
        BannerHome bh = new BannerHome();
        bh.setId(6);
        bh.setTitle("测试");
        bannerHomeMapper.updateByPrimaryKeySelective(bh);
    }

    @Test
    public void getTest() {
        // BannerHome bh = bannerHomeMapper.selectByPrimaryKey(1);
        //   BannerHome bh = bannerHomeMapper.selectByPrimaryKey(92);
        //  System.out.println(bh.toString());

        //ActivityPartner activityPartner = activityPartnerMapper.selectByPrimaryKey(6);
        //System.out.println(activityPartner.toString());

//        List<Activity> activityList = activityService.getActivityList();
//        System.out.println(activityList.toString());


        //联系人
//        List<ActivityContact> contacts = activityContactMapper.getByActivityId(14);
//        System.out.println(contacts.toString());

//         List<ActivityGuest> activityGuests = activityGuestMapper.getByActivityId(14);
//         System.out.println(activityGuests.toString());

        // List<ActivityPartner> activityPartners = activityPartnerMapper.getByActivityId(14);
        //System.out.println(activityPartners.toString());

        // List<ActivitySchedule> activitySchedules = activityScheduleMapper.getByActivityId(14);
        //System.out.println(activitySchedules.toString());

        //  List<ActivityTickets> activityTickets = activityTicketsMapper.getByActivityId(14);
        //System.out.println(activityTickets.toString());


        Activity activity = activityMapper.findDetailById(14);
        System.out.println(activity.toString());
    }


    @Autowired
    private IWatchListPeopleService watchListPeopleService;


    @Test
    public void testWatchListPeople() {
        // List<WatchListPeople> watchListPeople = watchListPeopleMapper.selectByWatchUserAndUser(2, 3);
        // System.out.println(watchListPeople);
        //watchListPeopleMapper.selectByWatchUserAndUser()

//        List<User> users = watchListPeopleMapper.getFansInfo(1);
//        System.out.println(users);

//        List<User> users = watchListPeopleMapper.getWatchPeopleInfo(1);
//        System.out.println(users);

//        User user = watchListPeopleMapper.getWatchAll(1);
//        System.out.println(user.toString());

        boolean mutualfans = watchListPeopleService.isMutualfans(3, 2);
        System.out.println(mutualfans ? "互粉" : "不互粉");

    }


    @Test
    public void testContactSave() {
        ArrayList<ActivityContact> activityContacts = new ArrayList<>();
        ActivityContact activityContact = new ActivityContact();
        activityContact.setActivityId(new Random().nextInt(60));
        activityContact.setContactName("胡歌");


        ActivityContact activityContact1 = new ActivityContact();
        activityContact1.setActivityId(new Random().nextInt(60));
        activityContact1.setContactName("杨幂");


        activityContacts.add(activityContact);
        activityContacts.add(activityContact1);

        activityContactMapper.batchSave(activityContacts);
    }


    @Test
    public void testBatch1Save() {
        ArrayList<ActivityGuest> activityGuests = new ArrayList<>();
        ActivityGuest activityGuest = new ActivityGuest();
        activityGuest.setActivityId(new Random().nextInt(60));


        ActivityGuest activityGuest1 = new ActivityGuest();
        activityGuest1.setActivityId(new Random().nextInt(60));


        activityGuests.add(activityGuest);
        activityGuests.add(activityGuest1);

        activityGuestMapper.batchSave(activityGuests);
    }

    @Test
    public void testPartnerBatchSave() {
        ArrayList<ActivityPartner> activityPartners = new ArrayList<>();
        ActivityPartner activityPartner = new ActivityPartner();
        activityPartner.setActivityId(new Random().nextInt(60));


        ActivityPartner activityPartner1 = new ActivityPartner();
        activityPartner1.setActivityId(new Random().nextInt(60));


        activityPartners.add(activityPartner);
        activityPartners.add(activityPartner1);

        activityPartnerMapper.batchSave(activityPartners);
    }

    @Test
    public void testScheduleBatchSave() {
        ArrayList<ActivitySchedule> activitySchedules = new ArrayList<>();
        ActivitySchedule activitySchedule = new ActivitySchedule();
        activitySchedule.setActivityId(new Random().nextInt(60));

        ActivitySchedule activitySchedule1 = new ActivitySchedule();
        activitySchedule1.setActivityId(new Random().nextInt(60));


        activitySchedules.add(activitySchedule);
        activitySchedules.add(activitySchedule1);

        activityScheduleMapper.batchSave(activitySchedules);
    }


    @Autowired
    private IActivityContactService activityContactService;


    @Autowired
    private WatchListMapper watchListMapper;


    @Autowired
    private ActivityStatisticsMapper activityStatisticsMapper;

    @Test
    public void TestContact() {
       /* List<Activity> activities = activityService.getByUserId(1);
        System.out.println(activities.toString());*/
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //simpleDateFormat.

        // activityMapper.getByUserIdAndCDate(1,"");

       /* for (int i = 0; i < 10; i++) {
            WatchList watchList = new WatchList();
            watchList.setActivityId(i);
            watchList.setUserId(2);
            watchListMapper.save(watchList);
        }*/

        // Integer count = watchListMapper.selectCountByActivity(2);
        // System.out.println(count);
        List<Integer> allId = activityMapper.findAllId();
        System.out.println(allId.toString());


        ArrayList<ActivityStatistics> activityStatisticsList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ActivityStatistics activityStatistics = new ActivityStatistics();
            activityStatistics.setId(i);
            activityStatistics.setActivityId(i);
            activityStatistics.setWatchCount(i);
            activityStatistics.setCollectCount(i);

            activityStatisticsList.add(activityStatistics);

        }


        activityStatisticsMapper.batchUpdate(activityStatisticsList);
    }


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserStatisticsMapper userStatisticsMapper;

    @Test
    public void syncToRedis() {
        List<Integer> userIds = userMapper.findAllId();
        List<UserStatistics> reList = userIds.stream().map(userId -> {
            Map reMap = redisTemplate.opsForHash().entries("user:" + String.valueOf(userId));
            UserStatistics userStatistics = new UserStatistics();

            userStatistics.setFansCount((Integer) reMap.get("fansCount") == null ? 0 : (Integer) reMap.get("fansCount"));
            userStatistics.setActivityCount((Integer) reMap.get("activityCount") == null ? 0 : (Integer) reMap.get("activityCount"));
            userStatistics.setPopularity((Integer) reMap.get("popularity") == null ? 0 : (Integer) reMap.get("popularity"));
            userStatistics.setUserId(userId);

            return userStatistics;
        }).collect(Collectors.toList());
        userStatisticsMapper.batchUpdate(reList);
    }


    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @Autowired
    private IUserStatisticsService userStatisticsService;

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            userStatisticsService.addPopularity(i);
            /*userStatisticsService.addFansCount(i);
            userStatisticsService.addActivityCount(i);*/
        }
    }


    @Test
    public void batchDelete() {
       /* ArrayList<Integer> ids = new ArrayList<>();
        ids.add(53);
        ids.add(52);
        activityContactMapper.batchDelete(ids);*/

        List<Activity> hotList = activityMapper.getHotList();
        System.out.println(hotList.toString());

       /* List<HotSort> hotSorts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HotSort hotSort = new HotSort();
            hotSort.setPosition(i);
            hotSort.setUserId(1);
            hotSort.setActivityId(6);

            hotSorts.add(hotSort);
        }
        hotSortMapper.batchSave(hotSorts);*/

    }

    @Test
    public void hostSortBatchSave() {
        List<Activity> hotList = activityService.getHotList();
        System.out.println(hotList.size());

    }


    @Autowired
    private UserBlackMapper userBlackMapper;

    @Test
    public void userBlack() {

        // 插入
        /*List<UserBlack> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserBlack userBlack = new UserBlack();
            userBlack.setUserId(i);
            userBlack.setForceOutTime(new Random().nextInt(3));
            userBlack.setReason("欠钱" + i + "万元");


            userBlackMapper.insert(userBlack);
        }*/

        //更新
        /*UserBlack userBlack = new UserBlack();
        userBlack.setUserId(1);
        userBlack.setForceOutTime(new Random().nextInt(3));
        userBlack.setReason("欠钱" + 999 + "万元");
        userBlackMapper.updateByUserId(userBlack);*/


        //批量删除

       /* List<Integer> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            items.add(i);
        }
        userBlackMapper.deleteByUserIds(items);*/

        // 根据username 查找

      /*  List<UserBlack> byUserId = userBlackMapper.findByUserId("huge");
        System.out.println();*/


    }


    @Autowired
    private WatchListPeopleMapper watchListPeopleMapper;


    @Autowired
    private TicketsRecordMapper ticketsRecordMapper;

    @Test
    public void testINGActivity() {
        //List<Activity> ingActivity = activityMapper.getINGActivity(1, new Date());
        // List<Activity> watchActivityListByIssue = watchListMapper.getWatchActivityListByIssue(1, 1,new Date());
        //  List<Activity> activities = watchListMapper.getWatchActivityOver(1,new Date());

        // List<UserBasicInfo> watchPeopleInfo = watchListPeopleMapper.getFansInfo(1);

        //List<UserBasicInfo> fansInfo = watchListPeopleMapper.getFansInfo(1);

        List<TicketsRecord> ticketsRecords = ticketsRecordMapper.getParticipation(1, 0);

        System.out.println();
    }


    @Test
    public void testPeopleDynamic() {
        //List<ActivityDynamic> peopleDynamic = watchListMapper.getActivityDynamic(1);

        // List<PeopleDynamic> peopleDynamic = watchListPeopleMapper.getPeopleDynamic(1);
        System.out.println();

       /* ActivityStatistics activityStatisticsFromRedis = activityStatisticsService.getActivityStatisticsFromRedis(14);
        System.out.println();*/

        //UserStatistics userStatisticsFromRedis = userStatisticsService.getUserStatisticsFromRedis(5);
        System.out.println("");


    }

    @Test
    public void testGetCheckAttention() {
        boolean checkAttention = getCheckAttention(1, 18);
        System.out.println(checkAttention ? "关注" : "未关注");

    }

    public boolean getCheckAttention(int watchedUserId, int userId) {
        WatchListPeople watchListPeoples = watchListPeopleMapper.selectByWatchUserAndUser(watchedUserId, userId);
        if (watchListPeoples != null) {
            return true;//已关注
        }
        return false;//未关注
    }


    @Autowired
    private IUserService userService;

    @Test
    public void getUserByIdTest() {
        // UserBasicInfo basicInfoById = userService.getBasicInfoById(218, 1);
        int s = 1;
    }

    /******************************************  同步数据 (mysql to redis) ***********************************/

    /**
     * 需求：将mysql 中数据库的数据同步到redis 中，确保redis数据与mysql 保持一致
     * <p>
     * 1.  同步会议被收藏的数目，从watch_list 表中获取数目，插入到activity_statistics
     * 2.  同步activity_statistics 中的会议收藏数目到 redis 中
     */
    @Test
    public void syncActivityStatisticFromMysqlToRedis() {
        // 1.  同步会议被收藏的数目，从watch_list 表中获取数目，插入到activity_statistics
       /* List<Integer> activityIds = watchListMapper.getActivityIdsFromWatchList();
        for (Integer activityId : activityIds) {
            ActivityStatistics activityStatistics = activityStatisticsMapper.selectByPrimaryKey(activityId);
            if (activityStatistics == null) {  // 未统计该会议数据，设置默认值
                ActivityStatistics as = new ActivityStatistics();
                as.setActivityId(activityId);
                as.setId(activityId);
                as.setCollectCount(0);
                as.setWatchCount(0);
                activityStatisticsMapper.insert(as);
            }
            watchListMapper.syncActivityCollectCountToStatistics(activityId);
        }*/

        //  2.  同步activity_statistics 中的会议收藏数目到 redis 中
        List<ActivityStatistics> activityStatisticsRedis = activityStatisticsMapper.findAll();
        for (ActivityStatistics as : activityStatisticsRedis) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("collectCount", watchListService.getCollectCountByActivityId(as.getActivityId()));
            map.put("watchCount", as.getWatchCount());

            redisTemplate.opsForHash().putAll("activity:" + as.getActivityId(), map);
        }
    }

    @Autowired
    private IWatchListService watchListService;


    /**
     * 介绍同上一方法：only edit to user
     */
    @Test
    public void syncUserStatisticFromMysqlToRedis() {
        // 1.  同步会议被收藏的数目，从watch_list 表中获取数目，插入到activity_statistics
       /* List<Integer> userIds = watchListPeopleMapper.getWatchUserIdFromWatchList();
        for (Integer userId : userIds) {
            UserStatistics userStatistics = userStatisticsMapper.selectByPrimaryKey(userId);

            if (userStatistics == null) {  // 未统计该会议数据，设置默认值
                UserStatistics as = new UserStatistics();
                as.setUserId(userId);
                as.setActivityCount(0);
                as.setFansCount(0);
                as.setPopularity(0);
                userStatisticsMapper.insert(as);
            }
            userStatisticsMapper.syncActivityCountToStatistics(userId);
            userStatisticsMapper.syncFansCountToStatistics(userId);
        }*/


        //  2.  同步activity_statistics 中的会议收藏数目到 redis 中
        List<UserStatistics> userStatisticsRedis = userStatisticsMapper.findAll();
        for (UserStatistics us : userStatisticsRedis) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("activityCount", activityService.getIssueActivityCount(us.getUserId()));
            map.put("fansCount", watchListPeopleService.getFansCount(us.getUserId()));
            map.put("popularity", us.getPopularity());

            redisTemplate.opsForHash().putAll("user:" + us.getUserId(), map);
        }
    }


    @Test
    public void deleteRedisStatistics() {
        // 1.  同步会议被收藏的数目，从watch_list 表中获取数目，插入到activity_statistics
        for (int i = 0; i < 300; i++) {
            //activityStatisticsService.deleteByActivityIdFromRedis(i);
            userStatisticsService.deleteByUserIdFromRedis(i);
        }
    }


    @Test
    public void insertUpdateRedis() {
        List<ActivityStatistics> activityStatistics = activityStatisticsMapper.findAll();
        //redisTemplate.opsForHash().increment("user:" +
        //                String.valueOf(userId), "activityCount", 1);


        //redisTemplate.opsForHash().put("activity:1", "collectCount", 3);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("activityId", 1);
        map.put("collectCount", 6);
        map.put("id", 1);
        map.put("watchCount", 6);

        redisTemplate.opsForHash().putAll("activity:1", map);
    }


   /* @Autowired
    private ISurveyDetailDao surveyDetailDao;

    @Test
    public void testJson() throws JSONException {
        List<String> optionContents = new ArrayList<String>();
        optionContents.add("香蕉");
        optionContents.add("苹果");

        QuCheckbox quCheckbox = new QuCheckbox();
        quCheckbox.setOrder(1);
        quCheckbox.setTitle("你喜欢啥水果？");
        quCheckbox.setRequired(1);
        quCheckbox.setOptionContents(optionContents);

        QuFillBlank quFillBlank = new QuFillBlank();
        quFillBlank.setOrder(2);
        quFillBlank.setTitle("你最喜欢的姑娘是？");
        quFillBlank.setRequired(1);


        QuMark quMark = new QuMark();
        quMark.setOrder(3);
        quMark.setTitle("给如下水果打分");
        quMark.setOptionContents(optionContents);

        List<BaseQuestion> questions = new ArrayList<>();
        questions.add(quCheckbox);
        questions.add(quFillBlank);
        questions.add(quMark);

        Question question = new Question();
        question.setId(1);
        question.setQuestions(questions);


        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(question.toSave());

        surveyDetailDao.save(question);
        //Object jsonObject = surveyDetailDao.findBySurveyId(1);
        //System.out.println(jsonObject.toString());

    }

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testInsertMongoDb() {
        for (int i = 0; i < 10; i++) {
            Data data = new Data();
            data.setId(i);
            data.setQuestion_0("问题1：" + i);
            data.setQuestion_1("问题2：" + i);
            mongoTemplate.insert(data, "test");
        }


    }

    @Test
    public void testQueryMongoDb() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(1));

//        Data data = mongoTemplate.findOne(query, Data.class, "test");
//        System.out.println(data.toString());

        DBObject dbObject = mongoTemplate.findOne(query, DBObject.class, "survey_detail");
        //System.out.println(dbObject.toString());
        //System.out.println(dbObject.get("question_0"));

        Map map = dbObject.toMap();
        System.out.println();
    }


    @Test
    public void testQueryMongoDb2() {
        DBCollection dbCollection = mongoTemplate.getCollection("test");

        //DBObject one = dbCollection.findOne();
        //System.out.println("------------------------");
        //System.out.println(one);

        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put("_id", 2);

        DBObject obj = dbCollection.findOne(queryObject);

        System.out.println("------------------------");
        System.out.println(obj.toString());


    }*/

    class Data implements Serializable {

        private Integer id;
        private String question_0;
        private String question_1;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getQuestion_0() {
            return question_0;
        }

        public void setQuestion_0(String question_0) {
            this.question_0 = question_0;
        }

        public String getQuestion_1() {
            return question_1;
        }

        public void setQuestion_1(String question_1) {
            this.question_1 = question_1;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", question_0='" + question_0 + '\'' +
                    ", question_1='" + question_1 + '\'' +
                    '}';
        }

    }

    /****************************************** 问卷调查 dao 测试***********************************/
    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Autowired
    private SurveyMapper surveyMapper;


    @Autowired
    private ISurveyAnswerService surveyAnswerService;

    @Test
    public void testQueryAnswer() {
       /* Map map = surveyAnswerService.getAnswersBySurveyId(7);
        JSONObject.fromObject(map);
        System.out.println();*/

        Map map = surveyAnswerService.getAnswerStatisticsBySurveyId(7);
        System.out.println();
    }

    @Test
    public void testSyncRedis() {
        userStatisticsService.syncUserStatisticsToRedis();
    }

    @Test
    public void testGetIdByCreateDateAndUid() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2018-06-04 17:09:11");
        Survey survey = new Survey();
        survey.setCreateDate(date);

        Integer id = surveyMapper.getIdByCreateDateAndUid(survey.getCreateDate(), 1);
        System.out.println("----------------------------------------------");
        System.out.println("id = " + id);
    }
}


