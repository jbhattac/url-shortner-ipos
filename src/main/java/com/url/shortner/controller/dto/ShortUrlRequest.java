package com.url.shortner.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortUrlRequest {
    @NotBlank(message = "Long url must not be empty!")
    @URL
    private String longUrl;

    @NotBlank(message = "Alias must not be empty!")
    @URL
    private String alias;
}
