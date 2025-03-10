package com.project.social_network.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity{

  @ManyToOne
  private User user;
  @ManyToOne
  private Post post;
  private String content;
}
