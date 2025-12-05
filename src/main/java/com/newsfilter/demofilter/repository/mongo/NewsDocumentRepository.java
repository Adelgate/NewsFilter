package com.newsfilter.demofilter.repository.mongo;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsDocumentRepository extends MongoRepository<NewsDocument, ObjectId>, NewsDocumentRepositoryCustom {

    Optional<NewsDocument> findByUrl(String url);

    List<NewsDocument> findBySourceId(Long sourceId);

    List<NewsDocument> findBySourceIdAndPublishedAtAfter(Long sourceId, Instant after);

    List<NewsDocument> findByTopicsContaining(String topic);

    List<NewsDocument> findBySourceIdAndTopicsContaining(Long sourceId, String topic);

    boolean existsByUrl(String url);

    @Aggregation(pipeline = {
            "{ '$unwind' : '$topics' }",
            "{ '$group' : { '_id' : '$topics' } }",
            "{ '$sort' : { '_id' : 1 } }"
    })
    List<TopicAggregation> aggregateTopics();
}
