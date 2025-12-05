package com.newsfilter.demofilter.service;

import com.newsfilter.demofilter.exception.BadRequestException;
import com.newsfilter.demofilter.mapper.NewsMapper;
import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private NewsDocumentRepository newsDocumentRepository;
    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private SearchService searchService;

    @Test
    void search_shouldThrowOnBlankQuery() {
        assertThrows(BadRequestException.class, () -> searchService.search(" ", 10));
    }

    @Test
    void search_shouldDelegateToRepository() {
        when(newsDocumentRepository.searchByText(anyString(), anyInt())).thenReturn(List.of());
        when(newsMapper.toResponseList(List.of())).thenReturn(List.of());

        searchService.search("ai", 5);

        verify(newsDocumentRepository).searchByText("ai", 5);
    }
}
