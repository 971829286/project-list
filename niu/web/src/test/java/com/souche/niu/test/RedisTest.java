package com.souche.niu.test;

import com.souche.niu.redis.RedisHashRepository;
import com.wordnik.swagger.annotations.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作Redis中的map结构单元测试
 *
 * @author wujingtao
 * @since 2018-09-29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:**/applicationContext.xml"})
public class RedisTest {

    @Resource
    private RedisHashRepository redisHashRepository;

    private static final String key="testKey";


    @Test
    public void putAll() {
        Map<String, Object> data = new HashMap();
        data.put("s1", "1");
        data.put("s2", "hello");
        data.put("s3", "asdfasdf");
        redisHashRepository.putAll(key,data);
        System.out.print("测试结束");
    }

}
