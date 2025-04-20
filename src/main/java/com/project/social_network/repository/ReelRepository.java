package com.project.social_network.repository;

import com.project.social_network.model.Reel;
import com.project.social_network.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReelRepository extends JpaRepository<Reel, Long> {
  List<Reel> findByUser(User user);
  List<Reel> findAllByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime createdAt);
  List<Reel> findAllByIsDeletedFalse();
}
