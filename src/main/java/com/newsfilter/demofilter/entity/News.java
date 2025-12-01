package com.newsfilter.demofilter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.index.TextScore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "news")
public class News {

    @Id
    private ObjectId id;

    @Indexed(name = "idx_news_source")
    private String source;

    @TextIndexed(weight = 2)
    private String text;

    private String mediaUrl;

    @Indexed(name = "idx_news_posted_at")
    private Instant postedAt;

    @Indexed(name = "idx_news_link", unique = true)
    private String link;

    @Indexed(name = "idx_news_topics")
    private List<String> topics;

    @CreatedDate
    @Indexed(name = "idx_news_created_at")
    private Instant createdAt;

    @TextScore
    private Float score;
}