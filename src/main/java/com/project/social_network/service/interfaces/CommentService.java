package com.project.social_network.service.interfaces;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.entity.Comment;
import com.project.social_network.entity.User;
import com.project.social_network.dto.request.CommentRequest;

public interface CommentService {
  void deleteCommentById(Long commentId, User user) throws UserException, CommentException;
  Comment editComment(CommentRequest commentRequest, User user);
}
