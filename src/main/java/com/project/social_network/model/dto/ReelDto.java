package com.project.social_network.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ReelDto {
  private Long id;

  private String content;

  private String image;

  private User user;

  private LocalDateTime createdAt;

  private int totalLikes;

  private boolean isLiked;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User {
    private Long id;
    private String fullName;
  }
}
