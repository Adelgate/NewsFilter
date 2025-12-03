package com.newsfilter.demofilter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record NewsResponse(
        @Schema(description = "MongoDB identifier")
        String id,
        String source,
        String text,
        String mediaUrl,
        Instant postedAt,
        String link,
        List<String> topics,
        Instant createdAt
) {
}