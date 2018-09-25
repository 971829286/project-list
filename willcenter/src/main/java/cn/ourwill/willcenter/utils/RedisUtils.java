package cn.ourwill.willcenter.utils;

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
}
