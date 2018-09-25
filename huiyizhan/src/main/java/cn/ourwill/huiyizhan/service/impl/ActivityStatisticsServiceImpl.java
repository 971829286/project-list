package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityStatistics;
import cn.ourwill.huiyizhan.mapper.ActivityMapper;
import cn.ourwill.huiyizhan.mapper.ActivityStatisticsMapper;
import cn.ourwill.huiyizhan.service.IActivityStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-03 11:19
 **/


@Service
@Slf4j
public class ActivityStatisticsServiceImpl extends BaseServiceImpl<ActivityStatistics> implements IActivityStatisticsService {

    @Autowired
    private ActivityStatisticsMapper activityStatisticsMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${scheduled.switch}")
    private Boolean scheduledSwitch;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private IWatchListService watchListService;

    @Override
    public Long addCollectNumber(Integer activityId) {
        log.info("activity " + activityId + " collectCount +1");
        return redisTemplate.opsForHash().increment("activity:" +
                String.valueOf(activityId), "collectCount", 1);
    }

    @Override
    public Long reduceCollectNumber(Integer activityId) {
        log.info("activity " + activityId + " collectCount -1");
        ActivityStatistics activityStatistics = this.getActivityStatisticsFromRedis(activityId);
        if (activityStatistics == null || activityStatistics.getCollectCount() == 0)
            return redisTemplate.opsForHash().increment("activity:" +
                    String.valueOf(activityId), "collectCount", 0);
        else
            return redisTemplate.opsForHash().increment("activity:" +
                    String.valueOf(activityId), "collectCount", -1);
    }

    @Override
    public Long addWatch(Integer activityId) {
        log.info("activity " + activityId + " watchCount +1");
        return redisTemplate.opsForHash().increment("activity:" +
                String.valueOf(activityId), "watchCount", 1);
    }

    @Override
    public Long addLeaveMessage(Integer activityId) {
        return null;
    }


    @Override
    @Scheduled(cron = "${activitystatistics.syncRedisToMySQL.scheduled}")
    public void syncRedisToMySQL() {
        if (scheduledSwitch) {
            log.info("===================================syncRedisToMySQL start=========================================");
            List<Integer> activityIds = activityMapper.findAllId();
            List<ActivityStatistics> reList = activityIds.stream().map(activityId -> {
                Map reMap = redisTemplate.opsForHash().entries("activity:" + String.valueOf(activityId));
                ActivityStatistics activityStatistics = new ActivityStatistics();
                activityStatistics.setWatchCount((Integer) reMap.get("watchCount") == null ? 0 : (Integer) reMap.get("watchCount"));
                return activityStatistics;
            }).collect(Collectors.toList());
            activityStatisticsMapper.batchUpdate(reList);
            log.info("====================================syncRedisToMySQL end==========================================");
        }
    }

    @Override
    public void deleteAllFromRedis() {
        List<Integer> ids = activityStatisticsMapper.findAllId();
        for (Integer id : ids) {
            redisTemplate.delete("activity:" + String.valueOf(id));
        }
    }

    @Override
    public void deleteByActivityIdFromRedis(Integer id) {
        redisTemplate.delete("activity:" + id);
    }


    @Override
    public ActivityStatistics getActivityStatisticsFromRedis(Integer activityId) {

        Map reMap = redisTemplate.opsForHash().entries("activity:" + String.valueOf(activityId));
        ActivityStatistics activityStatistics = new ActivityStatistics();
        activityStatistics.setId(activityId);
        activityStatistics.setActivityId(activityId);
        activityStatistics.setWatchCount((Integer) reMap.get("watchCount") == null ? 0 : (Integer) reMap.get("watchCount"));
        activityStatistics.setCollectCount((Integer) reMap.get("collectCount") == null ? 0 : (Integer) reMap.get("collectCount"));
        return activityStatistics;
    }

    @Override
    public void updateActivityHash(ActivityStatistics statistics) {
        Map map = new HashMap();
        map.put("id", statistics.getId());
        map.put("activityId", statistics.getActivityId());
        map.put("collectCount", statistics.getCollectCount());
        map.put("watchCount", statistics.getWatchCount());
        redisTemplate.opsForHash().putAll("activity:" + String.valueOf(statistics.getActivityId()), map);
    }

    @Override
    public void syncCollectCount(Integer id) {
        log.info("activity:" + id + ":sync  collectCount to  redis");
        Integer collectCount = watchListService.getCollectCountByActivityId(id);
        redisTemplate.opsForHash().put("activity:" +
                String.valueOf(id), "collectCount", collectCount);
    }


}
