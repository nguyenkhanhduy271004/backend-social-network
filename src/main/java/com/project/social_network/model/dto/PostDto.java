package com.project.social_network.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

  private Long id;

  private Long groupId;

  private String nameGroup;

  private String content;

  private String image;

  private String video;

  private User user;

  private LocalDateTime createdAt;

  private int totalLikes;

  private int totalReplies;

  private int totalComments;

  private boolean isLiked;

  private boolean isRePost;

  private List<Long> rePostUserId;
  private List<PostDto> replyPosts;


  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User {
    private Long id;
    private String fullName;
  }
}
