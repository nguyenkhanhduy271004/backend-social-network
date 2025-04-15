package com.project.social_network.service.interfaces;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.dto.request.CommentRequest;

public interface CommentService {
  void deleteCommentById(Long commentId, User user) throws UserException, CommentException;
  Comment editComment(CommentRequest commentRequest, User user);
}
