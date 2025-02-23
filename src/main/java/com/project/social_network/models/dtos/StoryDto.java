package com.project.social_network.models.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class StoryDto {
  private Long id;

  private String content;

  private String image;

  private UserDto user;

    private LocalDateTime createdAt;

  private int totalLikes;

  private boolean isLiked;

}
