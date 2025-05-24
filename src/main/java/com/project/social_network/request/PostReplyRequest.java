package com.project.social_network.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReplyRequest {

  @Min(value = 1, message = "Post id must be greater than 0")
  private Long postId;
  @NotBlank(message = "Reply content cannot be empty")
  @Size(max = 1000, message = "Reply must be less than 1000 characters")
  private String content;
  private String image;
  private LocalDateTime createdAt;
}
