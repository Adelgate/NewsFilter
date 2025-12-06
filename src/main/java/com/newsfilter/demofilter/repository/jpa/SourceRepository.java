package com.newsfilter.demofilter.repository.jpa;

import com.newsfilter.demofilter.domain.jpa.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    Optional<Source> findByName(String name);

    List<Source> findByActiveTrue();

    List<Source> findByType(Source.SourceType type);

    List<Source> findByActiveTrueAndType(Source.SourceType type);

    boolean existsByName(String name);
}
