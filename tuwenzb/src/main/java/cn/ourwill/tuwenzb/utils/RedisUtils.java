package cn.ourwill.tuwenzb.utils;

import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 　ClassName:RedisUtils
 * Description：
 * User:hasee
 * CreatedDate:2017/7/4 11:36
 */
@Component
public class RedisUtils {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static StringRedisTemplate strRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    private static RedisTemplate redisTemp;
    private static final Logger log = LogManager.getLogger(RedisUtils.class);
    @PostConstruct
    public void init() {
        this.strRedisTemplate = stringRedisTemplate;
        this.redisTemp=redisTemplate;
    }

    public  static void set(String key,Object value){
        redisTemp.opsForValue().set(key,value);
    }

    public  static void set(String key, Object value, long overtime, TimeUnit timeUnit){
        redisTemp.opsForValue().set(key,value,overtime,timeUnit);
    }

    public  static Object get(String key){
        return redisTemp.opsForValue().get(key);
    }

    public  static Object get(String key,Object o){
        return redisTemp.opsForHash().get(key,o);
    }

    public static void deleteByKey(String key){
        if(redisTemp.hasKey(key))
            redisTemp.delete(key);
    }

    public static Long minusOnlineNumberRedis(Integer activityId) {
        log.info("activity "+activityId+" onlineNum-1");
        Integer onlineNum = (Integer) redisTemp.opsForHash().get("activity:"+String.valueOf(activityId),"onlineNum");
        if(onlineNum>0) {
            return redisTemp.opsForHash().increment("activity:"+String.valueOf(activityId), "onlineNum", -1);
        }
        return 0L;
    }

    public static void addActivityHash(Integer activityId, Integer id, Integer participantsNum,Integer likeNum) {
        Map map = new HashMap();
        map.put("id",id);
        map.put("activityId",activityId);
        map.put("likeNum",likeNum);
        map.put("commentNum",0);
        map.put("participantsNum",participantsNum);
        map.put("onlineNum",0);
        map.put("shareNum",0);
        //真实数据
        map.put("realLikeNum",0);
        map.put("realCommentNum",0);
        map.put("realShareNum",0);
        map.put("realParticipantsNum",0);
        redisTemp.opsForHash().putAll("activity:"+String.valueOf(activityId),map);
    }

    public static void deleteActivityHash(Integer activityId) {
        redisTemp.delete(String.valueOf(activityId));
    }

    public static void updateActivityHash(ActivityStatistics statistics) {
        Map map = new HashMap();
        map.put("id",statistics.getId());
        map.put("activityId",statistics.getActivityId());
        map.put("likeNum",statistics.getLikeNum());
        map.put("commentNum",statistics.getCommentNum());
        map.put("participantsNum",statistics.getParticipantsNum());
        map.put("shareNum",statistics.getShareNum());


        map.put("realLikeNum",statistics.getRealLikeNum());
        map.put("realCommentNum",statistics.getRealCommentNum());
        map.put("realShareNum",statistics.getRealShareNum());
        map.put("realParticipantsNum",statistics.getRealParticipantsNum());
        redisTemp.opsForHash().putAll("activity:"+String.valueOf(statistics.getActivityId()),map);
    }

    public static ActivityStatistics getByActivityId(Integer activityId) {
        ActivityStatistics statistics = new ActivityStatistics();
        String id = "activity:"+String.valueOf(activityId);
        Map reMap = redisTemp.opsForHash().entries(id);
        statistics.setActivityId((Integer) reMap.get("activityId"));
        statistics.setId((Integer) reMap.get("id"));
        statistics.setCommentNum((Integer) reMap.get("commentNum"));
        statistics.setLikeNum((Integer) reMap.get("likeNum"));
        statistics.setParticipantsNum((Integer) reMap.get("participantsNum"));
        statistics.setOnlineNum((Integer) reMap.get("onlineNum"));
        statistics.setShareNum((Integer) reMap.get("shareNum"));

        statistics.setRealShareNum((Integer) reMap.get("realShareNum"));
        statistics.setRealParticipantsNum((Integer) reMap.get("realParticipantsNum"));
        statistics.setRealLikeNum((Integer) reMap.get("realLikeNum"));
        statistics.setRealCommentNum((Integer) reMap.get("realCommentNum"));
        return statistics;
    }
}
