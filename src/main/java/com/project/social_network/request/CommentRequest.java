package com.project.social_network.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

  @Min(value = 1, message = "Post id must be greater than 0")
  private Long postId;

  @Min(value = 1, message = "Comment id must be greater than 0")
  private Long commentId;

  @NotBlank(message = "Comment content cannot be empty")
  @Size(max = 500, message = "Comment must be less than 500 characters")
  private String content;
}
