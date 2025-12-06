package com.newsfilter.demofilter.domain.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_preferences", uniqueConstraints = @UniqueConstraint(name = "uk_user_topic", columnNames = {
        "user_id", "topic_id" }), indexes = {
                @Index(name = "idx_preference_user", columnList = "user_id"),
                @Index(name = "idx_preference_topic", columnList = "topic_id"),
                @Index(name = "idx_preference_priority", columnList = "priority")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
