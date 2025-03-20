package com.project.social_network.repository;

import com.project.social_network.entity.Group;
import com.project.social_network.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
  Group findByName(String name);
  List<Group> findByUsersContaining(User user);
}
