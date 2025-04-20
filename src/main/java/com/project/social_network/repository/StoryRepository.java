package com.project.social_network.repository;

import com.project.social_network.model.Story;
import com.project.social_network.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
  List<Story> findByUser(User user);
  List<Story> findAllByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime createdAt);
  List<Story> findAllByIsDeletedFalse();


}
