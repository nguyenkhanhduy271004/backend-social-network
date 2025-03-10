package com.project.social_network.service.impl;

import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.request.CommentRequest;
import com.project.social_network.repository.CommentRepository;
import com.project.social_network.service.interfaces.CommentService;
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
