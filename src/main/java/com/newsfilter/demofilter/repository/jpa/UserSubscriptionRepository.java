package com.newsfilter.demofilter.repository.jpa;

import com.newsfilter.demofilter.domain.jpa.Source;
import com.newsfilter.demofilter.domain.jpa.User;
import com.newsfilter.demofilter.domain.jpa.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findByUser(User user);

    List<UserSubscription> findByUserAndActiveTrue(User user);

    List<UserSubscription> findBySource(Source source);

    Optional<UserSubscription> findByUserAndSource(User user, Source source);

    boolean existsByUserAndSource(User user, Source source);

    long countByUserAndActiveTrue(User user);
}
