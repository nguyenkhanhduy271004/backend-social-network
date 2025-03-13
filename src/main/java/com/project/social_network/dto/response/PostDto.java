package com.project.social_network.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
