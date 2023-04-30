package com.url.shortner.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortUrlResponse {
    @NotBlank
    private String shortUrl;
}
