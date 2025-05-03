package com.project.social_network.repository;

import com.project.social_network.model.Group;
import com.project.social_network.model.JoinRequest;
import com.project.social_network.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
  List<JoinRequest> findByGroupAndPending(Group group, boolean pending);
  Optional<JoinRequest> findByGroupAndUser(Group group, User user);
}

