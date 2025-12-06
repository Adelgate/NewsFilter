package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.dto.NewsResponse;
import com.newsfilter.demofilter.entity.News;
import com.newsfilter.demofilter.exception.BadRequestException;
import com.newsfilter.demofilter.mapper.NewsMapper;
import com.newsfilter.demofilter.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public List<NewsResponse> search(String query, Integer limit) {
        if (!StringUtils.hasText(query)) {
            throw new BadRequestException("Query parameter 'q' is required");
        }
        int boundedLimit = limit == null ? 20 : Math.min(Math.max(limit, 1), 100);
        List<News> result = newsRepository.searchByText(query, boundedLimit);
        log.debug("Search for '{}' returned {} results", query, result.size());
        return newsMapper.toResponseList(result);
    }
}
