package com.newsfilter.demofilter.repository.jpa;

import com.newsfilter.demofilter.domain.jpa.Topic;
import com.newsfilter.demofilter.domain.jpa.User;
import com.newsfilter.demofilter.domain.jpa.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    List<UserPreference> findByUser(User user);

    List<UserPreference> findByUserOrderByPriorityDesc(User user);

    List<UserPreference> findByTopic(Topic topic);

    Optional<UserPreference> findByUserAndTopic(User user, Topic topic);

    boolean existsByUserAndTopic(User user, Topic topic);

    long countByUser(User user);
}
