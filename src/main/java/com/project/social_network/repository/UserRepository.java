package com.project.social_network.repository;

import com.project.social_network.model.User;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional; // Add this import
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @Query("SELECT DISTINCT u FROM User u WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%")
  List<User> searchUser(@Param("query") String query);
}
