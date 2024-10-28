package com.group1.gosports_jojo.service;

public interface RedisService {
    void setAuthCode(String key, String authCode,long timeoutInSeconds);
    String getAuthCode(String key);
}
