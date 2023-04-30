package com.url.shortner.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode
@Data
@Builder
@ToString
public class UrlDetails {
    @NotBlank
    private String key;

    @EqualsAndHashCode.Exclude
    @NotBlank
    private String longUrl;

    @EqualsAndHashCode.Exclude
    @NotBlank
    private String alias;


}
