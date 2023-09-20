package com.url.shortner.controller;

import com.url.shortner.controller.dto.ShortUrlRequest;
import com.url.shortner.controller.dto.ShortUrlResponse;
import com.url.shortner.exception.LongUrlNotFoundException;
import com.url.shortner.service.ShortUrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/shortUrl")
public class UrlResource {
    @Autowired
    private ShortUrlService shortUrlService;

    static final String pattern = "(?i)http(s)?://.+/(\\w+)$";
    static final Pattern regex = Pattern.compile(pattern);

    @PostMapping()
    public ResponseEntity<?> shortenUrl(@RequestBody @Valid ShortUrlRequest shortUrlRequest) {
        log.info("Received request {}", shortUrlRequest);
        String shortUrl = shortUrlService.shortenUrl(shortUrlRequest);
        return new ResponseEntity<>(ShortUrlResponse.builder().shortUrl(shortUrl).build(),
                HttpStatus.CREATED);
    }


    @RequestMapping("/url/**")
    public ResponseEntity<?> findAddress(HttpServletRequest request) {
        String fullUrl = request.getRequestURL().toString();
        String url = fullUrl.split("/url/")[1];
        log.info("Received key {}", url);
        Matcher matcher = regex.matcher(url);
        if (matcher.find()) {
            String id = matcher.group(2);
            String foundUrl = shortUrlService.getLongUrlByKey(id);
            return ResponseEntity.ok(foundUrl);
        } else {
            throw new LongUrlNotFoundException("url not found "+ url);
        }
    }
}
