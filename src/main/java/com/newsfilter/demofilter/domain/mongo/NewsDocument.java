package com.newsfilter.demofilter.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "news_documents")
@CompoundIndex(name = "idx_source_published", def = "{'sourceId': 1, 'publishedAt': -1}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDocument {

    @Id
    private ObjectId id;

    /**
     * Reference to JPA Source entity ID
     */
    @Indexed(name = "idx_news_source_id")
    private Long sourceId;

    @TextIndexed(weight = 3)
    private String title;

    @TextIndexed(weight = 2)
    private String content;

    private String summary;

    private String author;

    @Indexed(name = "idx_news_url", unique = true)
    private String url;

    private String imageUrl;

    @Indexed(name = "idx_news_published_at")
    private Instant publishedAt;

    private Instant fetchedAt;

    /**
     * Topics/categories for this news article
     */
    @Indexed(name = "idx_news_topics")
    private List<String> topics;

    /**
     * Original raw payload (JSON/XML) from the source
     */
    private String rawPayload;

    /**
     * Additional metadata (language, sentiment, etc.)
     */
    private Map<String, Object> metadata;
}
