package com.project.social_network.models.dtos;

import lombok.Data;

@Data
public class LikeDto {

  public Long id;
  private UserDto user;
  private PostDto post;
}
