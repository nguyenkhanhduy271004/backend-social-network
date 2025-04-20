package com.project.social_network.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity{

  @ManyToOne
  @JsonBackReference
  private User user;

  @ManyToOne
  private Post post;
  private String content;
}
