package com.project.social_network.service.interfaces;

import com.project.social_network.model.Comment;
import com.project.social_network.model.User;
import com.project.social_network.request.CommentRequest;

public interface CommentService {
  void deleteCommentById(Long commentId, User user);

  Comment editComment(CommentRequest commentRequest, User user);
}
