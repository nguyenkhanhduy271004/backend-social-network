package com.project.social_network.models.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostDto {

  private Long id;

  private String content;

  private String image;

  private String video;

  private UserDto user;

  private LocalDateTime createdAt;

  private int totalLikes;

  private int totalReplies;

  private int totalComments;

  private boolean isLiked;

  private boolean isRePost;

  private List<Long> rePostUserId;
  private List<PostDto> replyPosts;
}
