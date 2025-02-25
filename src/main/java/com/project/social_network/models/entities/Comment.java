package com.project.social_network.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Comment extends BaseEntity{

  @ManyToOne
  private User user;
  @ManyToOne
  private Post post;
  private String content;
}
