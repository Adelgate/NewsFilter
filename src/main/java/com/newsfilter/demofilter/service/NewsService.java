package com.newsfilter.demofilter.service;

import com.newsfilter.dto.NewsRequest;
import com.newsfilter.dto.NewsResponse;
import com.newsfilter.entity.News;
import com.newsfilter.exception.BadRequestException;
import com.newsfilter.exception.NotFoundException;
import com.newsfilter.mapper.NewsMapper;
import com.newsfilter.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final LLMService llmService;

    public List<NewsResponse> ingestNews(List<NewsRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            throw new BadRequestException("Request body must not be empty");
        }
        List<News> entities = requests.stream()
                .filter(Objects::nonNull)
                .map(this::buildEntityWithTopics)
                .toList();
        log.info("Ingesting {} news items", entities.size());
        List<News> saved = newsRepository.saveAll(entities);
        return newsMapper.toResponseList(saved);
    }

    public List<NewsResponse> getLatest(Integer limit, String topics, String source) {
        int boundedLimit = limit == null ? 20 : Math.min(Math.max(limit, 1), 100);
        List<String> parsedTopics = parseTopics(topics);
        log.debug("Fetching latest news with limit {} topics {} source {}", boundedLimit, parsedTopics, source);
        List<News> items = newsRepository.findLatest(source, parsedTopics, boundedLimit);
        return newsMapper.toResponseList(items);
    }

    public NewsResponse getNewsById(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            News news = newsRepository.findById(objectId)
                    .orElseThrow(() -> new NotFoundException("News item not found"));
            return newsMapper.toResponse(news);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid id format");
        }
    }

    private News buildEntityWithTopics(NewsRequest request) {
        News entity = newsMapper.toEntity(request);
        if (CollectionUtils.isEmpty(entity.getTopics())) {
            entity.setTopics(llmService.extractTopics(entity.getText()));
        }
        return entity;
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