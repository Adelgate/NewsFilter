package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.dto.NewsResponse;
import com.newsfilter.demofilter.exception.BadRequestException;
import com.newsfilter.demofilter.mapper.NewsMapper;
import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

        @Mock
        private NewsDocumentRepository newsDocumentRepository;
        @Mock
        private NewsMapper newsMapper;
        @Mock
        private LLMService llmService;

        @InjectMocks
        private NewsService newsService;

        @Test
        void ingestNews_shouldThrowOnEmptyPayload() {
                assertThrows(BadRequestException.class, () -> newsService.ingestNews(List.of()));
        }

        @Test
        void ingestNews_shouldPersistAndMap() {
                NewsRequest request = new NewsRequest("telegram", "Sample text", null, Instant.now(),
                                "https://example.com",
                                List.of("ai"));
                NewsDocument entity = NewsDocument.builder()
                                .content("Sample text")
                                .publishedAt(request.postedAt())
                                .url(request.link())
                                .topics(request.topics())
                                .build();
                NewsDocument savedEntity = NewsDocument.builder()
                                .id(null)
                                .content("Sample text")
                                .publishedAt(request.postedAt())
                                .url(request.link())
                                .topics(request.topics())
                                .build();
                NewsResponse response = new NewsResponse("507f1f77bcf86cd799439011", "telegram", "Sample text", null,
                                request.postedAt(), request.link(), request.topics(), Instant.now());

                when(newsMapper.toDocument(any())).thenReturn(entity);
                when(newsDocumentRepository.saveAll(any())).thenReturn(List.of(savedEntity));
                when(newsMapper.toResponseList(any())).thenReturn(List.of(response));

                List<NewsResponse> responses = newsService.ingestNews(List.of(request));

                assertEquals(1, responses.size());
                assertEquals("telegram", responses.getFirst().source());
        }
}