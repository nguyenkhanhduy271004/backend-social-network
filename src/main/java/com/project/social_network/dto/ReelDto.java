package com.project.social_network.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReelDto {
  private Long id;

  private String content;

  private String image;

  private UserDto user;

  private LocalDateTime createdAt;

  private int totalLikes;

  private boolean isLiked;
}
