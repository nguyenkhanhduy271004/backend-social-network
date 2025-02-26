package com.project.social_network.services.interfaces;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.CommentDto;
import com.project.social_network.models.entities.Comment;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.CommentRequest;

public interface CommentService {
  void deleteCommentById(Long commentId, User user) throws UserException, CommentException;
  Comment editComment(CommentRequest commentRequest, User user);
}
