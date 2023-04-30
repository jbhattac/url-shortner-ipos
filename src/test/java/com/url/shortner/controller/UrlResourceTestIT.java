package com.url.shortner.controller;

import com.url.shortner.UrlShortenerApplication;
import com.url.shortner.controller.dto.ShortUrlRequest;
import com.url.shortner.controller.dto.ShortUrlResponse;
import com.url.shortner.dao.TestRedisConfiguration;
import com.url.shortner.dao.UrlRepository;
import com.url.shortner.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestRedisConfiguration.class, UrlShortenerApplication.class})
class UrlResourceTestIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void shortenUrl_whenUrlIsValid() throws JsonProcessingException {
        //given
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().longUrl("https://group.mercedes-benz.com/careers/professionals/direct-entry/")
                .alias("https://mb4.me/").build();
        HttpEntity<String> entity = getStringHttpEntity(shortUrlRequest);
        String expectedShortValueUrl = "https://mb4.me/6592ac";
        //when
        ResponseEntity<ShortUrlResponse> response = restTemplate.postForEntity(
                "/api/v1/shortUrl", entity, ShortUrlResponse.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getShortUrl()).isEqualTo(expectedShortValueUrl);
    }

    @Test
    void shortenUrl_whenUrlIsNotValid() throws JsonProcessingException {
        //given
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().longUrl("benz").build();
        HttpEntity<String> entity = getStringHttpEntity(shortUrlRequest);
        //when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/shortUrl", entity, ErrorResponse.class);
        //then
        // verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody().getViolationErrors());
    }

    @Test
    void getLongUrl_whenThereExists_NoValidData() {
        //given
        String expectedErrorMessage = "Long url not found";
        //when
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                "/api/v1/shortUrl/url/noUrlKey", ErrorResponse.class);
        //then
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDebugMessage()).isEqualTo(expectedErrorMessage);
    }

    private HttpEntity<String> getStringHttpEntity(Object object) throws JsonProcessingException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonMeterData = mapper.writeValueAsString(object);
        return (HttpEntity<String>) new HttpEntity(jsonMeterData, headers);
    }
}