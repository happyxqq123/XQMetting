package com.xqmetting.server.cache;

import com.google.gson.Gson;
import com.xqmetting.entity.SessionCache;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SessionCacheSupportImpl implements SessionCacheSupport{

    public static final String REDIS_PREFIX = "SessionCache:id:";

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${session.expireTime}")
    private long expireTime;


    @Override
    public void save(SessionCache sessionCache) {
        String key = REDIS_PREFIX +  sessionCache.getUserId();
        String value = new Gson().toJson(sessionCache);
        redisTemplate.opsForValue().set(key,value,expireTime);
    }

    @Override
    public SessionCache get(String userId) {
        String key = REDIS_PREFIX+userId;
        String value = (String) redisTemplate.opsForValue().get(key);
        if(!StringUtil.isNullOrEmpty(value)){
            return new Gson().fromJson(value,SessionCache.class);
        }
        return null;
    }

    @Override
    public void remove(String userId) {
        String value = REDIS_PREFIX + userId;
        redisTemplate.delete(value);
    }
}
