package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.mapper.ActivityMapper;
import cn.ourwill.huiyizhan.mapper.ActivitySortMapper;
import cn.ourwill.huiyizhan.mapper.ActivityTicketsMapper;
import cn.ourwill.huiyizhan.mapper.TicketsRecordMapper;
import cn.ourwill.huiyizhan.service.*;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：
 *
 * @author uusao
 * @create 2018-03-21 10:38
 **/
@Service
public class ActivityServiceImpl extends BaseServiceImpl<Activity> implements IActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private IQiniuService qiniuService;
    @Autowired
    private ActivityTicketsMapper activityTicketsMapper;
    @Autowired
    private IActivityStatisticsService activityStatisticsService;
    @Autowired
    private TicketsRecordMapper ticketsRecordMapper;


    @Autowired
    private ActivitySortMapper activitySortMapper;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(Activity activity) throws QiniuException {
        activity.setCTime(new Date());
        Integer reCount = activityMapper.insertSelective(activity);
        //图片持久
//        if (StringUtils.isNotEmpty(activity.getActivityBanner())) {
//            activity.setActivityBanner(qiniuService.dataPersistence(activity.getId(), activity.getActivityBanner()));
//            if (activity.getActivityBanner() != null && activity.getActivityBanner().startsWith("temp/"))
//                return 0;
//        }
//        if (StringUtils.isNotEmpty(activity.getActivityBannerMobile())) {
//            activity.setActivityBannerMobile(qiniuService.dataPersistence(activity.getId(), activity.getActivityBannerMobile()));
//            if (activity.getActivityBannerMobile() != null && activity.getActivityBannerMobile().startsWith("temp/"))
//                return 0;
//        }
        if (StringUtils.isNotEmpty(activity.getActivityBanner())) {
            Integer count = fdfsImageService.dataPersistence(activity.getActivityBanner());
            if (count<=0){
                return 0;
            }
        }
        if (StringUtils.isNotEmpty(activity.getActivityBannerMobile())) {
            Integer count = fdfsImageService.dataPersistence(activity.getActivityBannerMobile());
            if (count<=0){
                return 0;
            }
        }
        activityMapper.updateByPrimaryKeySelective(activity);
        if (activity.getActivityTickets() != null && activity.getActivityTickets().size() > 0) {
            List<ActivityTickets> tickets = activity.getActivityTickets();
            tickets.stream().forEach(entity -> {
                entity.setTicketPrice(entity.getTicketPrice() == null ? 0 : entity.getTicketPrice());
                entity.setIsPublishSell(entity.getIsPublishSell() == null ? 0 : entity.getIsPublishSell());
                entity.setIsFree(entity.getIsFree() == null ? 1 : entity.getIsFree());
                entity.setIsCheck(entity.getIsCheck() == null ? 0 : entity.getIsCheck());
                entity.setSellStatus(entity.getSellStatus() == null ? 0 : entity.getSellStatus());
                entity.setSingleLimits(entity.getSingleLimits() == null ? 0 : entity.getSingleLimits());
                entity.setTotalNumber(entity.getTotalNumber() == null ? 0 : entity.getTotalNumber());
                entity.setStockNumber(entity.getTotalNumber() == null ? 0 : entity.getTotalNumber());
                entity.setActivityId(activity.getId());
                entity.setUserId(activity.getUserId());
                entity.setCTime(new Date());
            });
            activityTicketsMapper.batchSave(activity.getActivityTickets());
        }
        return reCount;
    }

    @Override
    public Integer update(Activity activity) throws QiniuException {
        //图片持久
//        if (StringUtils.isNotEmpty(activity.getActivityBanner())) {
//            activity.setActivityBanner(qiniuService.dataPersistence(activity.getId(), activity.getActivityBanner()));
//            if (activity.getActivityBanner() != null && activity.getActivityBanner().startsWith("temp/"))
//                return 0;
//        }
//        if (StringUtils.isNotEmpty(activity.getActivityBannerMobile())) {
//            activity.setActivityBannerMobile(qiniuService.dataPersistence(activity.getId(), activity.getActivityBannerMobile()));
//            if (activity.getActivityBannerMobile() != null && activity.getActivityBannerMobile().startsWith("temp/"))
//                return 0;
//        }
        if (StringUtils.isNotEmpty(activity.getActivityBanner())) {
            Integer count = fdfsImageService.dataPersistence(activity.getActivityBanner());
            if (count<=0){
                return 0;
            }
        }
        if (StringUtils.isNotEmpty(activity.getActivityBannerMobile())) {
            Integer count = fdfsImageService.dataPersistence(activity.getActivityBannerMobile());
            if (count<=0){
                return 0;
            }
        }
        int count = activityMapper.updateByPrimaryKeySelective(activity);
        return count;
    }

    @Override
    public boolean checkOwnerOrAdmin(Integer activityId, User user) {
        int count = activityMapper.checkOwners(activityId, user.getId());
        if (count > 0 || user.getLevel().equals(1))
            return true;
        return false;
    }

    @Override
    public List<Activity> getActivityList() {
        List<Activity> activities = activityMapper.findAll();
        return activities;
    }

    @Autowired
    private IWatchListService watchListService;

    @Autowired
    private IWatchListPeopleService watchListPeopleService;

    @Override
    public Activity getDetailById(Integer activityId, User loginUser) {
        Activity activity = activityMapper.findDetailById(activityId);
        if (loginUser != null && activity != null) {  // 已登录，查询当前会议是否被收藏
            Boolean isCollect = watchListService.checkWatchStatus(activityId, loginUser.getId());
            if (isCollect) {
                activity.setIsCollect(1);
            } else {
                activity.setIsCollect(0);
            }
        }
        //此人是否 已经被关注
        if (loginUser != null) {
            Boolean isWatch = watchListPeopleService.checkWatchStatus(activity.getUserId(), loginUser.getId());
            if (isWatch)
                activity.setIsWatch(1);
            else
                activity.setIsWatch(0);
        }
        return activity;
    }

    @Override
    public List<Activity> getByUserId(Integer id) {
        List<Activity> activitys = activityMapper.findByUserId(id);
        return activitys;
    }

    @Override
    public List<Activity> getActivityJoin(User user, boolean isValid) {
        List<Activity> activitys = activityMapper.findJoins(user.getId(), isValid);
        return activitys;
    }

    @Override
    public List<Integer> getAllIdByUserId(Integer id) {
        return activityMapper.getAllIdByUserId(id);
    }

    @Override
    public void setIsHot(int id, int isHot) {
        activityMapper.setIsHot(isHot == 0 ? 1 : 0, id);
    }

    @Override
    public void setIsRecent(int id, int isRecent) {
        activityMapper.setIsRecent(isRecent == 0 ? 1 : 0, id);
    }


    @Override
    public List<Activity> getHotList() {
        List<Activity> activities = activityMapper.getHotList();
        if (activities == null || activities.size() <= 0) {
            activities = activityMapper.getHotListBase();
            if (activities != null && activities.size() > 0) {
                //第一次 进行 插入 ,自定义排序
                List<ActivitySort> sorts = new ArrayList<>();
                int[] i = {1};
                activities.stream().forEach(entity -> {
                            ActivitySort sort = new ActivitySort();
                            sort.setHotActivityId(entity.getId());
                            sort.setId(i[0]++);
                            sorts.add(sort);
                        }
                );
                activitySortMapper.batchUpdateHot(sorts);
            }
        } else {
            activities.stream().forEach(entity -> entity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(entity.getId())));
        }
        return activities;
    }

    @Override
    public Integer updateHotSort(List<Integer> activityIds) {
        if (activityIds == null || activityIds.size() <= 0) {
            return -1;
        }
        List<ActivitySort> sorts = new ArrayList<>();
        int[] i = {1};
        activityIds.stream().forEach(entity -> {
                    ActivitySort sort = new ActivitySort();
                    sort.setHotActivityId(entity);
                    sort.setId(i[0]++);
                    sorts.add(sort);
                }

        );
        return activitySortMapper.batchUpdateHot(sorts);
    }


    @Override
    public List<Activity> getRecentList() {
        List<Activity> activities = activityMapper.getRecentList();
        if (activities == null || activities.size() <= 0) {
            activities = activityMapper.getRecentListBase();
            if (activities != null && activities.size() > 0) {
                //第一次 进行 插入 ,自定义排序
                List<ActivitySort> sorts = new ArrayList<>();
                int[] i = {1};
                activities.stream().forEach(entity -> {
                            ActivitySort sort = new ActivitySort();
                            sort.setRecentActivityId(entity.getId());
                            sort.setId(i[0]++);
                            sorts.add(sort);
                            entity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(entity.getId()));
                        }

                );
                activitySortMapper.batchUpdateRecent(sorts);
            }
        } else {
            activities.stream().forEach(entity -> entity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(entity.getId())));
        }
        return activities;
    }

    @Override
    public Integer updateRecentSort(List<Integer> activityIds) {
        if (activityIds == null || activityIds.size() <= 0) {
            return -1;
        }
        List<ActivitySort> sorts = new ArrayList<>();
        int[] i = {1};
        activityIds.stream().forEach(entity -> {
                    ActivitySort sort = new ActivitySort();
                    sort.setRecentActivityId(entity);
                    sort.setId(i[0]++);
                    sorts.add(sort);
                }
        );
        return activitySortMapper.batchUpdateRecent(sorts);
    }

    @Autowired
    private IUserStatisticsService userStatisticsService;

    @Override
    public Integer updateIssueStatus(int id, int userId) {
        Integer issueStatus = activityMapper.getIssueStatus(id);
        if (issueStatus == 1) {
            //取消发布必须没有有效门票
            if (ticketsRecordMapper.countValid(id) > 0) {
                return -2;
            }
        }
        return activityMapper.updateIssueStatus(issueStatus == 0 ? 1 : 0, id);
    }

    @Override
    public List<Activity> getINGActivity(int userId) {
        List<Activity> ingList = activityMapper.getINGActivity(userId);
        ingList.stream().forEach(e -> e.setIsCancelIssue(ticketsRecordMapper.countValid(e.getId()) > 0 ? 0 : 1));
        return ingList;
    }

    @Override
    public List<Activity> getActivityByStatus(int userId, int status) {
        List<Activity> reList = activityMapper.getActivityByStatus(userId, status);
        if (status == 1) {
            reList.stream().forEach(e -> e.setIsCancelIssue(ticketsRecordMapper.countValid(e.getId()) > 0 ? 0 : 1));
        }
        return reList;
    }

    @Override
    public List<Activity> getActivityOver(int userId) {
        return activityMapper.getActivityOver(userId, new Date());
    }

    @Override
    public List<Activity> getAllActivityNotOver(int userId) {
        return activityMapper.getActivityNotOver(userId,new Date());
    }

    @Override
    public List<Activity> selectByParam(HashMap param) {
        return activityMapper.selectByParam(param);
    }

    @Override
    public List<ActivityType> getActivityTypeList() {
        return activityMapper.getAllActivityType();
    }

    @Override
    public List<Activity> getAllActivityByStatus(int issueStatus) {
        List<Activity> reList = activityMapper.getAllActivityByStatus(issueStatus);
        if (issueStatus == 1) {
            reList.stream().forEach(e -> e.setIsCancelIssue(ticketsRecordMapper.countValid(e.getId()) > 0 ? 0 : 1));
        }
        return reList;
    }

    @Override
    public List<Activity> getAllActivityOver() {
        return activityMapper.getAllActivityOver(new Date());
    }

    @Override
    public Integer updateBannerId(Integer activityId, Integer bannerId) {
        return activityMapper.updateBannerId(activityId, bannerId);
    }

    @Override
    public Integer getIssueActivityCount(int userId) {
        return activityMapper.selectIssueActivityCountByUserId(userId);
    }

    @Override
    public Integer getActivityCountWithOutDelete(Integer userId) {
        return activityMapper.getActivityCountWithOutDelete(userId);
    }

    @Override
    public Activity getByCustomUrl(String customUrl) {
        return activityMapper.getByCustomUrl(customUrl);
    }

    @Override
    public boolean checkCustomUrl(String customUrl, Integer activityId) {
        if (activityMapper.checkCustomUrl(customUrl, activityId) > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Activity getById(Integer id) {
        return this.activityMapper.getById(id);
    }
}


