package com.url.shortner.dao;

import com.url.shortner.model.UrlDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The main purpose of this class is to deal with persistence related to url shortening.
 */
@Repository
public class RedisUrlRepository implements UrlRepository {

    @Autowired
    private RedisTemplate<String, UrlDetails> redisTemplate;


    @Override
    public UrlDetails addShortenUrl(UrlDetails urlDetails) {
        return fetchLongUrl(urlDetails.getKey()).orElse(insert(urlDetails));
    }

    @Override
    public Optional<UrlDetails> fetchLongUrl(String key) {
        UrlDetails urlDetails = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(urlDetails);
    }

    private UrlDetails insert(UrlDetails urlDetails) {
        redisTemplate.opsForValue().set(urlDetails.getKey(), urlDetails);
        return urlDetails;
    }

}
