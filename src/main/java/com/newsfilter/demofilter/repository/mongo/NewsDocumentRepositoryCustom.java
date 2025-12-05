package com.newsfilter.demofilter.repository.mongo;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;

import java.util.List;

public interface NewsDocumentRepositoryCustom {

    List<NewsDocument> findLatest(Long sourceId, List<String> topics, int limit);

    List<NewsDocument> searchByText(String text, int limit);
}
