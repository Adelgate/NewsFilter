package com.newsfilter.demofilter.repository.jpa;

import com.newsfilter.demofilter.domain.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByInstagramId(String instagramId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
