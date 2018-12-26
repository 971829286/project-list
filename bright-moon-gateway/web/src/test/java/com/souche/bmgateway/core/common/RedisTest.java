package com.souche.bmgateway.core.common;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis
 *
 * @author: chenwj
 * @since: 2018/7/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class RedisTest {

    @Resource
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            interMethod();
        }
    }

    private void interMethod() {
        String key = "sen";
        if (isConcurrentRequest(key)) {
            System.out.println("并发访问");
        } else {
            redisTemplate.expire(key, 10, TimeUnit.SECONDS);
            System.out.println("业务处理...");
        }
    }

    @Test
    public void incrementTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(redisTemplate.opsForValue().increment("sen", 1));
        }
        redisTemplate.expire("sen", 1, TimeUnit.SECONDS);
        Thread.sleep(3000);
        System.out.println(redisTemplate.opsForValue().get("sen"));
    }

    private boolean isConcurrentRequest(String key) {
        return redisTemplate.opsForValue().increment(key, 1) != 1;
    }

    @Test
    public void redisTest2(){
        for (int i = 0; i < 10; i++) {
            interFace("3627257438");
        }
    }

    public void interFace(String shopCode){
        if (StringUtils.equals(redisTemplate.opsForValue().get("merchantSettle"), shopCode)){
            System.out.println("并发访问");
        }else {
            System.out.println("业务处理...");
            redisTemplate.opsForValue().set("merchantSettle", shopCode, 10, TimeUnit.SECONDS);
        }
    }

}
