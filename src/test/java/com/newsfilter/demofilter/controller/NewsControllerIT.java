package com.newsfilter.demofilter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfilter.demofilter.domain.jpa.Source;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.repository.jpa.SourceRepository;
import com.newsfilter.demofilter.repository.mongo.NewsDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NewsControllerIT {

        @Container
        static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");

        @Container
        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

        @DynamicPropertySource
        static void setProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
                registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
                registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
                registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        }

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private SourceRepository sourceRepository;

        @Autowired
        private NewsDocumentRepository newsDocumentRepository;

        @BeforeEach
        void setup() {
                newsDocumentRepository.deleteAll();
                sourceRepository.deleteAll();
        }

        @Test
        void ingestAndFetchLatestFlow() throws Exception {
                // 1. Seed PostgreSQL with Source
                Source telegramSource = Source.builder()
                                .name("telegram")
                                .type(Source.SourceType.API)
                                .url("https://telegram.org")
                                .active(true)
                                .createdAt(Instant.now())
                                .subscriptions(Collections.emptyList())
                                .build();
                sourceRepository.save(telegramSource);

                List<Source> sources = sourceRepository.findAll();
                System.out.println("Seeded sources: " + sources.size());

                // 2. Ingest News (POST)
                NewsRequest request = new NewsRequest(
                                "telegram",
                                "Spring AI lands",
                                null,
                                Instant.now(),
                                "https://example.com/ai",
                                List.of("ai", "spring"));

                mockMvc.perform(post("/api/news/bulk")
                                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                                .content(objectMapper.writeValueAsString(List.of(request))))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$[0].id").exists());

                // 3. Verify Mongo (Fetch Latest)
                mockMvc.perform(get("/api/news/latest").param("limit", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].source").value("telegram"));
        }
}