package com.project.social_network.repository;

import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByIsPostTrueOrderByCreatedAtDesc();
  Page<Post> findAllByIsPostTrue(Pageable pageable);

  List<Post> findByRePostUsersContainsOrUser_IdAndIsPostTrueOrderByCreatedAtDesc(User user, Long userId);

  List<Post> findByLikesContainingOrderByContentDesc(User user);

  @Query("SELECT p FROM Post p JOIN p.likes l WHERE l.user.id=:userId")
  List<Post> findByLikesUser_id(Long userId);

  @Query("SELECT p FROM Post p JOIN p.rePostUsers u WHERE u.id = :userId")
  List<Post> findRepostedPostsByUserId(@Param("userId") Long userId);
}
