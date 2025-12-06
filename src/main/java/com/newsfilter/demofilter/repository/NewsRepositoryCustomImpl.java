package com.newsfilter.demofilter.repository;

import com.newsfilter.demofilter.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<News> findLatest(String source, List<String> topics, int limit) {
        List<Criteria> criteria = new ArrayList<>();
        if (StringUtils.hasText(source)) {
            criteria.add(Criteria.where("source").is(source));
        }
        if (!CollectionUtils.isEmpty(topics)) {
            criteria.add(Criteria.where("topics").in(topics));
        }

        Query query = criteria.isEmpty() ? new Query() : new Query(new Criteria().andOperator(criteria));
        query.with(Sort.by(Sort.Direction.DESC, "postedAt"));
        query.limit(limit);
        return mongoTemplate.find(query, News.class);
    }

    @Override
    public List<News> searchByText(String text, int limit) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(text);
        Query query = TextQuery.queryText(criteria).sortByScore().limit(limit);
        return mongoTemplate.find(query, News.class);
    }
}