package com.project.social_network.repository;

import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.GroupUserDto;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {
  Group findByName(String name);
  List<Group> findByUsersContaining(User user);

  @Query("SELECT new com.project.social_network.dto.GroupUserDto(u.id, u.fullName) " +
      "FROM Group g JOIN g.users u WHERE g.id = :groupId")
  List<GroupUserDto> findUsersByGroupId(@Param("groupId") Long groupId);
}
