package com.project.social_network.repository;

import com.project.social_network.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
  Group findByName(String name);
}
