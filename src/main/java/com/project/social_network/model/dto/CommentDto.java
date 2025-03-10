package com.project.social_network.model.dto;

import com.project.social_network.model.response.UserCommentResponse;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

  private Long postId;
  private UserCommentResponse user;
  private String content;
  private Date createdDate;
}
