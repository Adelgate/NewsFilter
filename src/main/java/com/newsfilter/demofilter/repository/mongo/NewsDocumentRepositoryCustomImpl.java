package com.newsfilter.demofilter.repository.mongo;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsDocumentRepositoryCustomImpl implements NewsDocumentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<NewsDocument> findLatest(Long sourceId, List<String> topics, int limit) {
        List<Criteria> criteria = new ArrayList<>();

        if (sourceId != null) {
            criteria.add(Criteria.where("sourceId").is(sourceId));
        }

        if (!CollectionUtils.isEmpty(topics)) {
            criteria.add(Criteria.where("topics").in(topics));
        }

        Query query = criteria.isEmpty() ? new Query() : new Query(new Criteria().andOperator(criteria));
        query.with(Sort.by(Sort.Direction.DESC, "publishedAt"));
        query.limit(limit);

        return mongoTemplate.find(query, NewsDocument.class);
    }

    @Override
    public List<NewsDocument> searchByText(String text, int limit) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(text);
        Query query = TextQuery.queryText(criteria).sortByScore().limit(limit);
        return mongoTemplate.find(query, NewsDocument.class);
    }
}
