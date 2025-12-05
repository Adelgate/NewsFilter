package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import com.newsfilter.demofilter.repository.mongo.TopicAggregation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService {

    private final NewsDocumentRepository newsDocumentRepository;

    public List<String> getAllTopics() {
        List<TopicAggregation> aggregated = newsDocumentRepository.aggregateTopics();
        List<String> topics = aggregated.stream()
                .map(TopicAggregation::get_id)
                .sorted()
                .collect(Collectors.toList());
        log.debug("Found {} topics", topics.size());
        return topics;
    }
}