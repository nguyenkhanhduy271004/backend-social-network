package com.project.social_network.models.dtos;

import com.project.social_network.models.entities.User;
import lombok.Data;

@Data
public class CommentDto {

  private Long postId;
  private User user;
  private String content;

}
