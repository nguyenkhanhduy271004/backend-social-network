package com.project.social_network.models.dtos;

import com.project.social_network.models.entities.User;
import com.project.social_network.models.responses.UserCommentResponse;
import java.util.Date;
import lombok.Data;

@Data
public class CommentDto {

  private Long postId;
  private UserCommentResponse user;
  private String content;
  private Date createdDate;
}
