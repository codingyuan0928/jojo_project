package com.group1.gosports_jojo.service.impl;


import com.group1.gosports_jojo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setAuthCode(String key, String authCode,long timeoutInSeconds){
        redisTemplate.opsForValue().set(key,authCode,180, TimeUnit.SECONDS);
    }

    public String getAuthCode(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }
}
