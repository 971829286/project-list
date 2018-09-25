package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.mapper.ActivityStatisticsMapper;
import cn.ourwill.tuwenzb.mapper.CommentMapper;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.service.quartz.ActivityJob;
import cn.ourwill.tuwenzb.service.quartz.QuartzManager;
import jdk.nashorn.internal.runtime.options.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.net.dns.ResolverConfiguration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ActivityStatisticsServiceImpl extends BaseServiceImpl<ActivityStatistics> implements IActivityStatisticsService {

    @Autowired
    ActivityStatisticsMapper activityStatisticsMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    IActivityService activityService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CommentMapper commentMapper;

    @Value("${scheduled.switch}")
    private Boolean scheduledSwitch;

//    @Autowired
//    QuartzManager quartzManager;

    private static final Logger log = LogManager.getLogger(ActivityStatisticsServiceImpl.class);

    //直播点赞
    @Override
    public Integer addLikeNumber(Integer activityId) {
        return activityStatisticsMapper.addLikeNumber(activityId);
    }

    @Override
    public Integer addCommentNumber(Integer activityId) {
        return activityStatisticsMapper.addCommentNumber(activityId);
    }

    @Override
    public Integer minusCommentNumber(Integer activityId) {
        return activityStatisticsMapper.minusCommentNumber(activityId);
    }

    @Override
    public Integer addParticipantsNumber(Integer activityId) {
        return activityStatisticsMapper.addCommentNumber(activityId);
    }

    //根据AafdctivityId查找
    @Override
    public ActivityStatistics getByActivityId(Integer activityId) {
        return activityStatisticsMapper.findByActivityId(activityId);
    }

    @Override
    public Integer deleteByActivityId(Integer activityId) {
        HashMap param = new HashMap();
        param.put("activityId", activityId);
        return activityStatisticsMapper.deleteActivityId(param);
    }

    @Override
    public Long addLikeNumberRedis(Integer activityId, Integer number) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "likeNum", number);
    }

    @Override
    public Long addCommentNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "commentNum", 1);
    }

    @Override
    public Long minusCommentNumberRedis(Integer activityId) {
        Integer commentNum = (Integer) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "commentNum");
        if (commentNum > 0) {
            return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "commentNum", -1);
        }
        return 0L;
    }

    @Override
    public void refushCommentNumberRedis(Integer activityId) {
        Activity activity = activityMapper.getById(activityId);
        if (activity != null) {
            int count = commentMapper.selectCountByCheck(activityId, activity.getCheckType());
            redisTemplate.opsForHash().put("activity:" + String.valueOf(activityId), "commentNum", count);
        }
    }

    @Override
    public Long addParticipantsNumberRedis(Integer activityId, Integer number) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "participantsNum", number);
    }

    @Override
    public Long addOnlineNumberRedis(Integer activityId) {
        log.info("activity " + activityId + " onlineNum +1");
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "onlineNum", 1);
    }

    @Override
    public Long minusOnlineNumberRedis(Integer activityId) {
        log.info("activity " + activityId + " onlineNum-1");
        Integer onlineNum = (Integer) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "onlineNum");
        if (onlineNum != null && onlineNum > 0) {
            return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "onlineNum", -1);
        }
        return 0L;
    }

    @Override
    public void updateStatistics(Integer activityId, String type, Integer num) {
        redisTemplate.opsForHash().put("activity:" + String.valueOf(activityId), type, num);
    }


    @Scheduled(cron = "${activitystatistics.syncRedisToMySQL.scheduled}")
    public void syncRedisToMySQL() {
        if (scheduledSwitch) {
            log.info("===================================syncRedisToMySQL start=========================================");
            List<ActivityStatistics> list = activityStatisticsMapper.findAll();
            List<ActivityStatistics> reList = list.stream().map(entity -> {
                Map reMap = redisTemplate.opsForHash().entries("activity:" + String.valueOf(entity.getActivityId()));
                entity.setCommentNum((Integer) reMap.get("commentNum"));
                entity.setLikeNum((Integer) reMap.get("likeNum"));
                entity.setParticipantsNum((Integer) reMap.get("participantsNum"));
                entity.setShareNum((Integer) reMap.get("shareNum"));

                entity.setRealCommentNum(reMap.get("realCommentNum") == null ? 0 : (Integer) reMap.get("realCommentNum"));
                entity.setRealLikeNum(reMap.get("realLikeNum") == null ? 0 : (Integer) reMap.get("realLikeNum"));
                entity.setRealParticipantsNum(reMap.get("realParticipantsNum") == null ? 0 : (Integer) reMap.get("realParticipantsNum"));
                entity.setRealShareNum(reMap.get("realShareNum") == null ? 0 : (Integer) reMap.get("realShareNum"));
                return entity;
            }).collect(Collectors.toList());
            activityStatisticsMapper.batchUpdate(reList);
            log.info("====================================syncRedisToMySQL end==========================================");
        }
    }

    @Override
    public void initializeOnlineNum() {
        log.info("===================================initializeOnlineNum start======================================");
        List<Integer> ids = activityStatisticsMapper.findAllId();
        for (Integer id : ids) {
            redisTemplate.opsForHash().put("activity:" + String.valueOf(id), "onlineNum", 0);
        }
        log.info("===================================initializeOnlineNum end========================================");
    }

    @Override
    public void deleteRedis() {
        List<Integer> ids = activityStatisticsMapper.findAllId();
        for (Integer id : ids) {
            redisTemplate.delete(String.valueOf(id));
        }
    }

    @Override
    public Long addShareNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "shareNum", 1);
    }

    //    ====================真实数据=====================
    @Override
    public Long addRealShareNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "realShareNum", 1);
    }

    @Override
    public Long addRealLikeNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "realLikeNum", 1);
    }

    @Override
    public Long addRealCommentNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "realCommentNum", 1);
    }

    @Override
    public Long addRealParticipantsNumberRedis(Integer activityId) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "realParticipantsNum", 1);
    }

    @Override
    public Long minusRealCommentNumberRedis(Integer activityId) {
        Integer commentNum = (Integer) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "realCommentNum");
        if (commentNum > 0) {
            return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "realCommentNum", -1);
        }
        return 0L;
    }

    @Override
    public Long addTempLikeNumberRedis(Integer activityId, Integer number) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "tempLikeNum", number);
    }

    @Override
    public Long addTempParticipantsNumberRedis(Integer activityId, Integer number) {
        return redisTemplate.opsForHash().increment("activity:" + String.valueOf(activityId), "tempParticipantsNum", number);
    }

    @Override
    public Long getTempLikeNumberRedis(Integer activityId) {
        return (Long) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "tempLikeNum") == null
                ? 0L
                : (Long)redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "tempLikeNum");
    }

    @Override
    public Long getTempParticipantsNumberRedis(Integer activityId) {
        return (Long) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "tempParticipantsNum") == null
                ? 0L
                : (Long) redisTemplate.opsForHash().get("activity:" + String.valueOf(activityId), "tempParticipantsNum") ;
    }




//    @Scheduled(cron = "${add.likenum.scheduled}")
    public void timingIncrease() {
//       if(scheduledSwitch) {
        if (true) {
            Random rand = new Random();
            List<Activity> activities = activityMapper.getActivityIng(LocalDateTime.now());
            if (activities != null && activities.size() > 0) {
                for (int i = 0; i < activities.size(); i++) {
                    Activity activity = activities.get(i);
                    log.info(activity);
                    int likeNum = rand.nextInt(50);
                    int partNum = rand.nextInt(10);
                    log.info("=========:tempLikeNum "+getTempLikeNumberRedis(activity.getId()));
                    log.info("=========:tempParticipate "+getTempParticipantsNumberRedis(activity.getId()));
                    Map map = redisTemplate.opsForHash().entries("activity:" + String.valueOf(activity.getId()));
                    int curLikeNum = (int) map.get("likeNum");
                    int curPartNum = (int) map.get("participantsNum");
                    log.info("curLikeNum:" + curLikeNum);
                    log.info("curPartNum:" + curPartNum);
                    if(activity.getExpectParticipateNum() != null && activity.getExpectLikeNum() != null) {
                        if (activity.getExpectLikeNum() <= getTempLikeNumberRedis(activity.getId()) && activity.getExpectParticipateNum() <= getTempParticipantsNumberRedis(activity.getId())) {
                            continue;
                        }
                        if (activity.getExpectLikeNum() > getTempLikeNumberRedis(activity.getId())) {
                            addTempLikeNumberRedis(activity.getId(), likeNum);
                            addLikeNumberRedis(activity.getId(), likeNum);
                            int total = curLikeNum + likeNum;
                            log.info("活动id " + activity.getId() + ":" + "触发定时增加点赞任务,本次增加点赞数:" + likeNum + " 当前点赞数:" + total);
                        }
                        if (activity.getExpectParticipateNum() > getTempParticipantsNumberRedis(activity.getId())) {
                            addTempParticipantsNumberRedis(activity.getId(), partNum);
                            addParticipantsNumberRedis(activity.getId(), partNum);
                            int total = curPartNum + partNum;
                            log.info("活动id " + activity.getId() + ":" + "触发定时增加参与人数任务,本次增加参与数:" + partNum + " 当前参与数:" + total);
                        }
                    }
                }
            }
        }
    }


}
