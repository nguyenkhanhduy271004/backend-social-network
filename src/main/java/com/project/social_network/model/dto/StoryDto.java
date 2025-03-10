package com.project.social_network.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryDto {
  private Long id;

  private String content;

  private String image;

  private UserDto user;

  private LocalDateTime createdAt;

  private int totalLikes;

  private boolean isLiked;

}
