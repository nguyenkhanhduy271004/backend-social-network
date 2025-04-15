package com.project.social_network.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {

  public Long id;
  private UserDto user;
  private PostDto post;
}
