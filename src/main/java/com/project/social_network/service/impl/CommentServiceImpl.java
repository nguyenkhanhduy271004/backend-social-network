package com.project.social_network.service.impl;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.Comment;
import com.project.social_network.model.User;
import com.project.social_network.repository.CommentRepository;
import com.project.social_network.request.CommentRequest;
import com.project.social_network.service.interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  @Override
  public void deleteCommentById(Long commentId, User user) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentException("Comment id not found: " + commentId));
    if (!user.getId().equals(comment.getUser().getId())) {
      throw new UserException("You do not have the right to delete comments!");
    }
    commentRepository.deleteById(commentId);
  }

  @Override
  public Comment editComment(CommentRequest commentRequest, User user) {
    Comment comment = commentRepository.findById(commentRequest.getCommentId())
        .orElseThrow(() -> new CommentException(
            "Không tìm thấy comment id: " + commentRequest.getCommentId()));
    if (!user.getId().equals(comment.getUser().getId())) {
      throw new UserException("You do not have the right to delete comments!");
    }

    comment.setContent(commentRequest.getContent());
    commentRepository.save(comment);
    return comment;
  }
}
