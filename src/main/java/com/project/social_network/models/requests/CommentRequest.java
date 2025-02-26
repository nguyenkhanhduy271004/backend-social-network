package com.project.social_network.models.requests;

import lombok.Data;

@Data
public class CommentRequest {
  private Long postId;
  private Long commentId;
  private String content;
}
