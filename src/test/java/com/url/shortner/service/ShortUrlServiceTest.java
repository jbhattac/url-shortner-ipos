package com.url.shortner.service;

import com.url.shortner.controller.dto.ShortUrlRequest;
import com.url.shortner.dao.UrlRepository;
import com.url.shortner.model.UrlDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Captor
    ArgumentCaptor<UrlDetails> urlDetailsCaptor;
    @Captor
    ArgumentCaptor<String> shortUrlCaptor;
    @InjectMocks
    private ShortUrlService shortUrlService;
    @Mock
    private UrlRepository urlRepository;

    @Test
    void shorten_validLongUrl() {
        //given
        UrlDetails expectedShortUrl = UrlDetails.builder().key("6592ac").alias("https://mb4.me/").build();
        when(urlRepository.addShortenUrl(any(UrlDetails.class))).thenReturn(expectedShortUrl);
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().longUrl("https://group.mercedes-benz.com/careers/professionals/direct-entry/").alias("https://mb4.me/").build();
        //when
        String shortUrl = shortUrlService.shortenUrl(shortUrlRequest);
        //then
        // verify arguments to the redis dao
        verify(urlRepository, atLeast(1)).addShortenUrl(urlDetailsCaptor.capture());
        assertThat(urlDetailsCaptor.getValue().getLongUrl()).isEqualTo("https://group.mercedes-benz.com/careers/professionals/direct-entry/");
        assertThat(urlDetailsCaptor.getValue()).isEqualTo(expectedShortUrl);

        assertThat(shortUrl).isEqualTo(expectedShortUrl.getAlias()+expectedShortUrl.getKey());
    }

    @Test
    void getLongUrl_ByValidKey() {
        //given
        Optional<UrlDetails> expectedUrlDetails = Optional.ofNullable(UrlDetails.builder().key("6592ac").alias("https://mb4.me/").longUrl("http://www.log.url").build());
        when(urlRepository.fetchLongUrl(any(String.class))).thenReturn(expectedUrlDetails);
        //when
        Optional<UrlDetails> actualUrlDetails = urlRepository.fetchLongUrl("6592ac");
        //then
        // verify arguments to the redis dao
        verify(urlRepository, atLeast(1)).fetchLongUrl(shortUrlCaptor.capture());
        assertThat(shortUrlCaptor.getValue()).isEqualTo("6592ac");
        // verify the actual long url
        assertThat(actualUrlDetails.get().getLongUrl()).isEqualTo(expectedUrlDetails.get().getLongUrl());
    }
}