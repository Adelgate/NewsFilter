package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.domain.jpa.Source;
import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.dto.NewsResponse;
import com.newsfilter.demofilter.exception.BadRequestException;
import com.newsfilter.demofilter.exception.NotFoundException;
import com.newsfilter.demofilter.mapper.NewsMapper;
import com.newsfilter.demofilter.repository.jpa.SourceRepository;
import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsDocumentRepository newsDocumentRepository;
    private final SourceRepository sourceRepository;
    private final NewsMapper newsMapper;
    private final LLMService llmService;

    public List<NewsResponse> ingestNews(List<NewsRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            throw new BadRequestException("Request body must not be empty");
        }
        List<NewsDocument> documents = requests.stream()
                .filter(Objects::nonNull)
                .map(this::buildDocumentWithTopics)
                .toList();
        log.info("Ingesting {} news items", documents.size());
        List<NewsDocument> saved = newsDocumentRepository.saveAll(documents);
        return newsMapper.toResponseList(saved);
    }

    public List<NewsResponse> getLatest(Integer limit, String topics, Long sourceId) {
        int boundedLimit = limit == null ? 20 : Math.min(Math.max(limit, 1), 100);
        List<String> parsedTopics = parseTopics(topics);
        log.debug("Fetching latest news with limit {} topics {} sourceId {}", boundedLimit, parsedTopics, sourceId);
        List<NewsDocument> items = newsDocumentRepository.findLatest(sourceId, parsedTopics, boundedLimit);
        return newsMapper.toResponseList(items);
    }

    public NewsResponse getNewsById(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            NewsDocument newsDocument = newsDocumentRepository.findById(objectId)
                    .orElseThrow(() -> new NotFoundException("News item not found"));
            return newsMapper.toResponse(newsDocument);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid id format");
        }
    }

    private NewsDocument buildDocumentWithTopics(NewsRequest request) {
        // Map basic fields via mapper
        NewsDocument document = newsMapper.toDocument(request);

        // Set timestamp
        document.setFetchedAt(Instant.now());

        // Resolve and set sourceId from source name
        Source source = sourceRepository.findByName(request.source())
                .orElseGet(() -> createDefaultSource(request.source()));
        document.setSourceId(source.getId());

        // Extract topics if not provided
        if (CollectionUtils.isEmpty(request.topics())) {
            document.setTopics(llmService.extractTopics(document.getContent()));
        }

        return document;
    }

    private Source createDefaultSource(String sourceName) {
        log.info("Creating new source: {}", sourceName);
        Source source = Source.builder()
                .name(sourceName)
                .type(Source.SourceType.API)
                .url("https://example.com/" + sourceName)
                .active(true)
                .build();
        return sourceRepository.save(source);
    }

    private List<String> parseTopics(String topics) {
        if (!StringUtils.hasText(topics)) {
            return Collections.emptyList();
        }
        return List.of(topics.split(","))
                .stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }
}