package com.project.social_network.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post extends BaseEntity{

  @ManyToOne
  private User user;

  private String content;
  private String image;
  private String video;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Like> likes = new ArrayList<>();

  @OneToMany
  private List<Post> replyPost = new ArrayList<>();

  @OneToMany
  private List<User> rePostUsers = new ArrayList<>();

  @ManyToOne
  private Post replyFor;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  private boolean isReply;
  private boolean isPost;

  private LocalDateTime createdAt;

}
