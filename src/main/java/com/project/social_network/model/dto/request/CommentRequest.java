package com.project.social_network.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
  private Long postId;
  private Long commentId;
  private String content;
}
