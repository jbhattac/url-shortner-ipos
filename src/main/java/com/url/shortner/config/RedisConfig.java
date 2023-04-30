package com.url.shortner.config;

import com.url.shortner.model.UrlDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                redisProperties.getRedisHost(),
                redisProperties.getRedisPort());
    }


    @Bean
    RedisTemplate<String, UrlDetails> redisTemplate() {
        final RedisTemplate<String, UrlDetails> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(UrlDetails.class);
        valueSerializer.setObjectMapper(objectMapper);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}
