package com.souche.niu.redis;

import com.google.common.collect.Maps;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.redis.RedisRepository;
import com.souche.optimus.remoting.trace.SoucheTraceUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 操作Redis的Hash数据结构
 *
 * key-hash
 *
 * @author wujingtao
 */
public class RedisHashRepository extends RedisRepository {

    /**
     * 将整个map存入缓存
     * @param key
     * @param map
     */
    public void putAll(String key, Map<String,Object> map) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "putAll");
        SoucheTraceUtil.genNextSubSeq();
        getRedisTemplate().opsForHash().putAll(key,map);
        super.logCacheRequest(params, null);
    }

    /**
     * 以hashKey value组成map，在以key为键存入缓存
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void put(String key, String hashKey, Object value) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (StringUtil.isEmpty(hashKey)) {
            return;
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "put");
        SoucheTraceUtil.genNextSubSeq();
        getRedisTemplate().opsForHash().put(key,hashKey,value);
        super.logCacheRequest(params,null);
    }

    /**
     * 查找以key为键的Map
     * @param key
     * @return
     */
    public Map<String, Object> entries(String key) {
        if (StringUtil.isEmpty(key)) {
            return new HashMap<>();
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "entries");
        Map<Object, Object> dataObj=getRedisTemplate().opsForHash().entries(key);
        if (CollectionUtils.isEmpty(dataObj)) {
            return new HashMap<>();
        }
        SoucheTraceUtil.genNextSubSeq();
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<Object, Object> entry : dataObj.entrySet()) {
            data.put(entry.getKey().toString(), entry.getValue());
        }
        super.logCacheRequest(params,null);
        return data;
    }

    /**
     * 先以key查找map，在以hashKey查找value
     * @param key
     * @param hashKey
     * @return
     */
    public Object get(String key, String hashKey) {
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        if (StringUtil.isEmpty(hashKey)) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "get");
        SoucheTraceUtil.genNextSubSeq();
        Object value=getRedisTemplate().opsForHash().get(key, hashKey);
        super.logCacheRequest(params,null);
        return value;
    }

    /**
     * 查找以key为键的Map全部键值
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        if (StringUtil.isEmpty(key)) {
            return new HashSet<>();
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "keys");
        Set<Object> keysObj = getRedisTemplate().opsForHash().keys(key);
        if (CollectionUtils.isEmpty(keysObj)) {
            return new HashSet<>();
        }
        SoucheTraceUtil.genNextSubSeq();
        Set<String> keys = new HashSet<>();
        for (Object o : keysObj) {
            keys.add(o.toString());
        }
        super.logCacheRequest(params,null);
        return keys;
    }

    /**
     * 以key为键查找map
     * 删除map中的以hashKeys为键的全部值
     * @param key
     * @param hashKeys 不定数参数
     */
    public void delete(String key,Object...hashKeys) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (hashKeys == null || hashKeys.length == 0) {
            return;
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "delete");
        SoucheTraceUtil.genNextSubSeq();
        getRedisTemplate().opsForHash().delete(key, hashKeys);
        super.logCacheRequest(params,null);
    }

    /**
     * 判断以key为键的Redis map结构中是否包含以hashKey为键的值
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hasKey(String key, String hashKey) {
        if (StringUtil.isEmpty(key)) {
            return false;
        }
        if (StringUtil.isEmpty(hashKey)) {
            return false;
        }
        Map<String, Object> params = Maps.newHashMap();
        String newKey = buildKey(key);
        params.put("key", newKey);
        params.put("method", "hasKey");
        SoucheTraceUtil.genNextSubSeq();
        boolean flag=getRedisTemplate().opsForHash().hasKey(key, hashKey);
        super.logCacheRequest(params,null);
        return flag;
    }

}
