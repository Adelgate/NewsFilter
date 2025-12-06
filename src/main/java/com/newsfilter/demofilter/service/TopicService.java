package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.repository.NewsRepository;
import com.newsfilter.demofilter.repository.TopicAggregation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService {

    private final NewsRepository newsRepository;

    public List<String> getAllTopics() {
        List<TopicAggregation> aggregated = newsRepository.aggregateTopics();
        List<String> topics = aggregated.stream()
                .map(TopicAggregation::getId)
                .sorted()
                .collect(Collectors.toList());
        log.debug("Found {} topics", topics.size());
        return topics;
    }
}