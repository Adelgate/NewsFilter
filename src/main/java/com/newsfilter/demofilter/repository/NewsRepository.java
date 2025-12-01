package com.newsfilter.demofilter.repository;

import com.newsfilter.entity.News;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends MongoRepository<News, ObjectId>, NewsRepositoryCustom {

    @Aggregation(pipeline = {
            "{ '$unwind' : '$topics' }",
            "{ '$group' : { '_id' : '$topics' } }",
            "{ '$sort' : { '_id' : 1 } }"
    })
    List<TopicAggregation> aggregateTopics();
}
