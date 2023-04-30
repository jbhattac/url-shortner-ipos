package com.url.shortner.service;

import com.url.shortner.controller.dto.ShortUrlRequest;
import com.url.shortner.dao.UrlRepository;
import com.url.shortner.exception.LongUrlNotFoundException;
import com.url.shortner.model.UrlDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;

/**
 * Main purpose of this class is to have some business logic related to the
 * shortening of the url and vice versa.
 * But in accordance with SRS principle we are not dealing with persistence in here.
 */
@Slf4j
@Service
public class ShortUrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String shortenUrl(ShortUrlRequest shortUrlRequest) {
        String key = DigestUtils.md5DigestAsHex(shortUrlRequest.getLongUrl().getBytes(StandardCharsets.UTF_8)).substring(0,6);
        UrlDetails urlDetails = urlRepository.addShortenUrl(UrlDetails.builder()
                .longUrl(shortUrlRequest.getLongUrl())
                .key(key).alias(shortUrlRequest.getAlias()).build());
        log.info("The created urlDetails {}", urlDetails);
        return urlDetails.getAlias()+urlDetails.getKey();
    }

    public String getLongUrlByKey(String key) {
        UrlDetails urlDetails = urlRepository.fetchLongUrl(key).orElseThrow(LongUrlNotFoundException::new);
        log.info("Fetching" +
                " urlDetails {}", urlDetails);
        return urlDetails.getLongUrl();
    }


}
