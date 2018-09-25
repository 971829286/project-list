package cn.ourwill.huiyizhan.utils;

import lombok.extern.slf4j.Slf4j;
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
 * Demo:hasee
 * CreatedDate:2017/7/4 11:36
 */
@Component
@Slf4j
public class RedisUtils {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static StringRedisTemplate strRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    private static RedisTemplate redisTemp;
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
        Integer onlineNum = (Integer) redisTemp.opsForHash().get(String.valueOf(activityId),"onlineNum");
        if(onlineNum>0) {
            return redisTemp.opsForHash().increment(String.valueOf(activityId), "onlineNum", -1);
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
        redisTemp.opsForHash().putAll(String.valueOf(activityId),map);
    }

    public static void deleteActivityHash(Integer activityId) {
        redisTemp.delete(String.valueOf(activityId));
    }

}
