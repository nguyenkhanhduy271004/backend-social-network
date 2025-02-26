package com.project.social_network.services.impl;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.CommentDto;
import com.project.social_network.models.entities.Comment;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.CommentRequest;
import com.project.social_network.repositories.CommentRepository;
import com.project.social_network.services.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public void deleteCommentById(Long commentId, User user) throws UserException, CommentException {
    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException("Không tìm thấy comment id: " + commentId));
    if(!user.getId().equals(comment.getUser().getId())) {
      throw new UserException("Không có quyền xóa comment");
    }
    commentRepository.deleteById(commentId);
  }

  @Override
  public Comment editComment(CommentRequest commentRequest, User user) {
    Comment comment = commentRepository.findById(commentRequest.getCommentId()).orElseThrow(() -> new CommentException("Không tìm thấy comment id: " + commentRequest.getCommentId()));
    if(!user.getId().equals(comment.getUser().getId())) {
      throw new UserException("Không có quyền xóa comment");
    }

    comment.setContent(commentRequest.getContent());
    commentRepository.save(comment);
    return comment;
  }
}
