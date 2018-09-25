package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.UserStatistics;
import cn.ourwill.huiyizhan.mapper.UserMapper;
import cn.ourwill.huiyizhan.mapper.UserStatisticsMapper;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListPeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-03 18:53
 **/
@Service
@Slf4j
public class UserStatisticsServiceImpl extends BaseServiceImpl<UserStatistics> implements IUserStatisticsService {

    @Autowired
    private UserStatisticsMapper userStatisticsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IWatchListPeopleService watchListPeopleService;

    @Autowired
    private IActivityService activityService;

    @Value("${scheduled.switch}")
    private Boolean scheduledSwitch;

    @Override
    public Long addPopularity(Integer userId) {
        log.info("user " + userId + " popularity + 1");
        return redisTemplate.opsForHash().increment("user:" +
                String.valueOf(userId), "popularity", 1);
    }

    @Override
    public Long addFansCount(Integer userId) {
        log.info("user " + userId + " fansCount + 1");
        return redisTemplate.opsForHash().increment("user:" +
                String.valueOf(userId), "fansCount", 1);
    }

    @Override
    public Long reduceFansCount(Integer userId) {
        log.info("user " + userId + " fansCount - 1");
        // 取消关注，确保不为 负数
        UserStatistics userStatistics = this.getUserStatisticsFromRedis(userId);
        if (userStatistics == null || userStatistics.getFansCount() == 0)
            return redisTemplate.opsForHash().increment("user:" +
                    String.valueOf(userId), "fansCount", 0);
        else
            return redisTemplate.opsForHash().increment("user:" +
                    String.valueOf(userId), "fansCount", -1);
    }

    @Override
    @Scheduled(cron = "${activitystatistics.syncRedisToMySQL.scheduled}")
    public void syncRedisToMySQL() {
        if (scheduledSwitch) {
            log.info("===================================syncRedisToMySQL start=========================================");
            List<Integer> userIds = userMapper.findAllId();
            List<UserStatistics> reList = userIds.stream().map(userId -> {
                Map reMap = redisTemplate.opsForHash().entries("user:" + String.valueOf(userId));
                UserStatistics userStatistics = new UserStatistics();
                userStatistics.setPopularity((Integer) reMap.get("popularity") == null ? 0 : (Integer) reMap.get("popularity"));
                return userStatistics;
            }).collect(Collectors.toList());

            userStatisticsMapper.batchUpdate(reList);
            log.info("====================================syncRedisToMySQL end==========================================");
        }
    }


    @Override
//    @Scheduled(cron = "${activitystatistics.syncRedisToMySQL.scheduled}")
    public void deleteAllFromRedis() {
        List<Integer> ids = userMapper.findAllId();
        for (Integer id : ids) {
            redisTemplate.delete("user:" + String.valueOf(id));
        }
    }

    @Override
    public void deleteByUserIdFromRedis(Integer id) {
        redisTemplate.delete("user:" + String.valueOf(id));
    }

    @Override
    public UserStatistics getByUserId(int userId) {
        return userStatisticsMapper.getByUserId(userId);
    }

    @Override
    public UserStatistics getUserStatisticsFromRedis(int userId) {
        Map reMap = redisTemplate.opsForHash().entries("user:" + String.valueOf(userId));
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUserId(userId);
        userStatistics.setPopularity((Integer) reMap.get("popularity") == null ? 0 : (Integer) reMap.get("popularity"));
        userStatistics.setFansCount((Integer) reMap.get("fansCount") == null ? 0 : (Integer) reMap.get("fansCount"));
        userStatistics.setActivityCount((Integer) reMap.get("activityCount") == null ? 0 : (Integer) reMap.get("activityCount"));
        userStatistics.setTotalActivityCount((Integer) reMap.get("totalActivityCount") == null ? 0 :(Integer) reMap.get("totalActivityCount") );
        return userStatistics;
    }

    @Override
    public void syncActivityCount(Integer userId) {
        log.info("user:" + userId + " :activityCount--------------数据同步到redis");
        Integer activityCount = activityService.getIssueActivityCount(userId);
        Integer totalActivityCount = activityService.getByUserId(userId).size();
        redisTemplate.opsForHash().put("user:" +
                String.valueOf(userId), "activityCount", activityCount);
        redisTemplate.opsForHash().put("user:" +
                String.valueOf(userId),"totalActivityCount",totalActivityCount);
    }

    @Override
    public void syncFansCount(Integer userId) {
        log.info("user:" + userId + " :fansCount--------------数据同步到redis");
        Integer fansCount = watchListPeopleService.getFansCount(userId);
        redisTemplate.opsForHash().put("user:" +
                String.valueOf(userId), "fansCount", fansCount);
    }

    @Scheduled(cron = "0 0 3 * * ?")//凌晨3点触发
    public void syncUserStatisticsToRedis(){
        List<Integer> ids = userMapper.findAllId();
        for (Integer id : ids) {
            Integer activityCount = activityService.getIssueActivityCount(id);
            Integer fansCount = watchListPeopleService.getFansCount(id);
            UserStatistics userStatistics = userStatisticsMapper.getByUserId(id);
            Integer totalActivityCount = activityService.getAllIdByUserId(id).size();
            redisTemplate.opsForHash().put("user:" +
                    String.valueOf(id), "activityCount", activityCount);
            redisTemplate.opsForHash().put("user:" +
                    String.valueOf(id), "fansCount", fansCount);
            redisTemplate.opsForHash().put("user:" +
                    String.valueOf(id),"totalActivityCount",totalActivityCount);
            if(userStatistics!=null) {
                redisTemplate.opsForHash().put("user:" +
                        String.valueOf(id), "popularity", userStatistics.getPopularity());
            }

        }
    }
}
