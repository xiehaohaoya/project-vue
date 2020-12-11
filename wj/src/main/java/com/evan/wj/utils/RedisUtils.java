package com.evan.wj.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis工具类
 */
public class RedisUtils {

    //  redis
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * redis存值
     * @throws Exception
     */
    public void redisSetVal() throws Exception {
        redisTemplate.opsForValue().set("user", "xieHaoHaoYa");
    }

    /**
     * redis取值
     * @throws Exception
     */
    public void redisGetVal() throws Exception {
        System.out.println(redisTemplate.opsForValue().get("user"));
    }
}
