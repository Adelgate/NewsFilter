package com.newsfilter.demofilter.repository.jpa;

import com.newsfilter.demofilter.domain.jpa.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findBySlug(String slug);

    Optional<Topic> findByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}
