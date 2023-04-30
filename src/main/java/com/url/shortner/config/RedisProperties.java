package com.url.shortner.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RedisProperties {
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    @Value("${spring.redis.host:redis}")
    private String redisHost;
}
