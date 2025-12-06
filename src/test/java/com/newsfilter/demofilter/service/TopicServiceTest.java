package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import com.newsfilter.demofilter.repository.mongo.TopicAggregation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.newsfilter.demofilter.entity.News;
import com.newsfilter.demofilter.repository.NewsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private NewsDocumentRepository newsDocumentRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    void getAllTopics_shouldReturnSortedUniqueTopics() {
        TopicAggregation first = () -> "ai";
        TopicAggregation second = () -> "finance";
        when(newsDocumentRepository.aggregateTopics()).thenReturn(List.of(second, first));

        List<String> topics = topicService.getAllTopics();

        assertEquals(List.of("ai", "finance"), topics);
    }
}