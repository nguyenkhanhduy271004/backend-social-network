package com.project.social_network.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReplyRequest {

  private String content;
  private Long postId;
  private LocalDateTime createdAt;
  private String image;
}
