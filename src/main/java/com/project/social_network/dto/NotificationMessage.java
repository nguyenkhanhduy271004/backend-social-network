package com.project.social_network.dto;

import java.io.Serializable;

public class NotificationMessage implements Serializable {
  private Long userId;
  private Long postId;
  private String message;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
