package com.newsfilter.demofilter.exception;

import java.time.Instant;

public record ErrorResponse(
        String error,
        String message,
        Instant timestamp,
        String path
) {
}