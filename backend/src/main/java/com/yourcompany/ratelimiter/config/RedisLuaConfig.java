package com.yourcompany.ratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisLuaConfig {

    @Bean
    public RedisScript<Long[]> slidingWindowScript() {
        return RedisScript.of(
                new org.springframework.core.io.ClassPathResource(
                        "redis/sliding_window.lua"
                ),
                Long[].class
        );
    }
}