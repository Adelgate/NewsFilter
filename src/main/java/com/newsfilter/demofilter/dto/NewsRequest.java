package com.newsfilter.demofilter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;
import java.util.List;

public record NewsRequest(
        @Schema(example = "telegram")
        @NotBlank(message = "Source is required")
        String source,

        @Schema(description = "Plain text of the news item")
        @NotBlank(message = "Text must not be empty")
        @Size(min = 3, max = 5000, message = "Text must be between 3 and 5000 characters")
        String text,

        @Schema(description = "Optional media URL attached to the news")
        @URL(message = "Media URL must be valid", protocol = "http", regexp = "(http|https)://.*", flags = {})
        @Size(max = 2048)
        String mediaUrl,

        @Schema(example = "2024-05-01T12:00:00Z")
        @NotNull(message = "postedAt is required")
        @PastOrPresent(message = "postedAt cannot be in the future")
        Instant postedAt,

        @Schema(example = "https://example.com/news/1")
        @NotBlank(message = "Link is required")
        @URL(message = "Link must be a valid URL", protocol = "http", regexp = "(http|https)://.*", flags = {})
        String link,

        @Schema(description = "Detected or assigned topics")
        List<@NotBlank(message = "Topic must not be blank") String> topics
) {
}
