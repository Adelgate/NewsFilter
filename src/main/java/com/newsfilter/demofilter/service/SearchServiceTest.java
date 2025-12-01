package com.newsfilter.demofilter.service;

import com.newsfilter.dto.NewsResponse;
import com.newsfilter.entity.News;
import com.newsfilter.exception.BadRequestException;
import com.newsfilter.mapper.NewsMapper;
import com.newsfilter.repository.NewsRepository;
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
    private NewsRepository newsRepository;
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
        when(newsRepository.searchByText(anyString(), anyInt())).thenReturn(List.of());
        when(newsMapper.toResponseList(List.of())).thenReturn(List.of());

        searchService.search("ai", 5);

        verify(newsRepository).searchByText("ai", 5);
    }
}
