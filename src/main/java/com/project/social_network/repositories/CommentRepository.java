package com.project.social_network.repositories;

import com.project.social_network.models.entities.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost_Id(Long postId);
}
