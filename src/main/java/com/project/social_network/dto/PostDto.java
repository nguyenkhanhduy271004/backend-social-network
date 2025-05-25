package com.project.social_network.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

  @JsonFormat(pattern="yyyy-MM-dd")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
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
