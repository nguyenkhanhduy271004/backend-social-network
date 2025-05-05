package com.project.social_network.service.interfaces;

import com.project.social_network.exceptions.CommentException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Comment;
import com.project.social_network.model.User;
import com.project.social_network.request.CommentRequest;

public interface CommentService {
  void deleteCommentById(Long commentId, User user) throws UserException, CommentException;
  Comment editComment(CommentRequest commentRequest, User user);
}
