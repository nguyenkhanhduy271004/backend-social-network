package com.project.social_network.repositories;

import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
  List<Story> findByUser(User user);
  List<Story> findAllByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime createdAt);
  List<Story> findAllByIsDeletedFalse();


}
