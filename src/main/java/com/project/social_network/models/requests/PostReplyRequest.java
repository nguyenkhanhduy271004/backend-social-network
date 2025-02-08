package com.project.social_network.models.requests;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PostReplyRequest {

  private String content;
  private Long postId;
  private LocalDateTime createdAt;
  private String image;
}
