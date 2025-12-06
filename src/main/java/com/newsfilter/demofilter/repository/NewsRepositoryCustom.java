package com.newsfilter.demofilter.repository;

import com.newsfilter.demofilter.entity.News;

import java.util.List;

public interface NewsRepositoryCustom {

    List<News> findLatest(String source, List<String> topics, int limit);

    List<News> searchByText(String text, int limit);
}
