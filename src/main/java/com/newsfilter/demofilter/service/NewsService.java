package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.dto.NewsResponse;
import com.newsfilter.demofilter.exception.BadRequestException;
import com.newsfilter.demofilter.exception.NotFoundException;
import com.newsfilter.demofilter.mapper.NewsMapper;
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
        NewsDocument document = newsMapper.toDocument(request);
        document.setFetchedAt(Instant.now());
        if (CollectionUtils.isEmpty(document.getTopics())) {
            document.setTopics(llmService.extractTopics(document.getContent()));
        }
        return document;
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