package com.newsfilter.demofilter.controller;

import com.newsfilter.dto.NewsRequest;
import com.newsfilter.dto.NewsResponse;
import com.newsfilter.service.NewsService;
import com.newsfilter.service.SearchService;
import com.newsfilter.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "News", description = "Public APIs for news consumption")
public class NewsController {

    private final NewsService newsService;
    private final TopicService topicService;
    private final SearchService searchService;

    @Operation(summary = "Bulk ingest news", description = "Receives normalized news items from n8n")
    @PostMapping("/news/bulk")
    public ResponseEntity<List<NewsResponse>> ingest(@RequestBody @NotEmpty List<@Valid NewsRequest> requests) {
        log.info("Bulk ingest request with {} items", requests.size());
        List<NewsResponse> saved = newsService.ingestNews(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Latest news", description = "Get latest news filtered by topics or source")
    @GetMapping("/news/latest")
    public ResponseEntity<List<NewsResponse>> latest(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "topics", required = false) String topics,
            @RequestParam(name = "source", required = false) String source) {
        return ResponseEntity.ok(newsService.getLatest(limit, topics, source));
    }

    @Operation(summary = "Full-text search")
    @GetMapping("/news/search")
    public ResponseEntity<List<NewsResponse>> search(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return ResponseEntity.ok(searchService.search(query, limit));
    }

    @Operation(summary = "Topics", description = "List all known topics")
    @GetMapping("/topics")
    public ResponseEntity<List<String>> topics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @Operation(summary = "Get news by id")
    @GetMapping("/news/{id}")
    public ResponseEntity<NewsResponse> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }
}
