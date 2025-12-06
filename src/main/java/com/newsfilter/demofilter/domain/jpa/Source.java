package com.newsfilter.demofilter.domain.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sources", indexes = {
        @Index(name = "idx_source_name", columnList = "name"),
        @Index(name = "idx_source_type", columnList = "type"),
        @Index(name = "idx_source_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SourceType type;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserSubscription> subscriptions = new ArrayList<>();

    public enum SourceType {
        RSS,
        API,
        SCRAPER
    }
}
