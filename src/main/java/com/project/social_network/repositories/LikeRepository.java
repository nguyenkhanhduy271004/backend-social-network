package com.project.social_network.repositories;

import com.project.social_network.models.entities.Like;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

  @Query("SELECT l FROM Like l WHERE l.user.id=:userId AND l.post.id=:postId")
  Like isLikeExist(@Param("userId") Long userId, @Param("postId") Long postId);

  @Query("SELECT l FROM Like l WHERE l.post.id=:postId")
  List<Like> findByPostId(@Param("postId") Long postId);
}
