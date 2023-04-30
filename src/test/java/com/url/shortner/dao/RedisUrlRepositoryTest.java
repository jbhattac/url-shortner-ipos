package com.url.shortner.dao;

import com.url.shortner.model.UrlDetails;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
class RedisUrlRepositoryTest {
    @Autowired
    private RedisUrlRepository redisUrlRepository;

    @Test
    public void shouldSaveAndFetchUrlDetails_toAndFromRedis() {
        // given
        UrlDetails urlDetails = UrlDetails.builder().longUrl("https://group.mercedes-benz.com/careers/professionals/direct-entry/").alias("https://mb4.me/").key("Short_url").build();
        // when
        redisUrlRepository.addShortenUrl(urlDetails);
        // then
        assertEquals(urlDetails.getLongUrl(), redisUrlRepository.fetchLongUrl(urlDetails.getKey()).get().getLongUrl());
    }
}