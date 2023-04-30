package com.url.shortner.dao;

import com.url.shortner.model.UrlDetails;

import java.util.Optional;

public interface UrlRepository {
    UrlDetails addShortenUrl(UrlDetails urlDetails);

    Optional<UrlDetails> fetchLongUrl(String key);
}
