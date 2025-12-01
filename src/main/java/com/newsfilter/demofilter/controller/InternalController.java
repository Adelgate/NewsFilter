package com.newsfilter.demofilter.controller;

import com.newsfilter.config.InternalAuthProperties;
import com.newsfilter.dto.NewsRequest;
import com.newsfilter.dto.NewsResponse;
import com.newsfilter.exception.UnauthorizedAccessException;
import com.newsfilter.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/n8n")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Internal", description = "Internal ingestion API for n8n")
public class InternalController {

    private static final String HEADER_NAME = "X-INTERNAL-KEY";

    private final NewsService newsService;
    private final InternalAuthProperties internalAuthProperties;

    @Operation(summary = "Ingest news from n8n", description = "Secured with shared secret header")
    @PostMapping("/news")
    public ResponseEntity<List<NewsResponse>> ingestFromN8N(
            @RequestHeader(value = HEADER_NAME) String headerValue,
            @RequestBody @NotEmpty List<@Valid NewsRequest> requests) {
        validateInternalKey(headerValue);
        List<NewsResponse> saved = newsService.ingestNews(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private void validateInternalKey(String provided) {
        if (!internalAuthProperties.getKey().equals(provided)) {
            log.warn("Unauthorized internal request with header: {}", provided);
            throw new UnauthorizedAccessException("Invalid internal key");
        }
    }
}
