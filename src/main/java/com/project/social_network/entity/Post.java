package com.project.social_network.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
  @JsonBackReference
  private User user;

  private String content;
  private String image;
  private String video;
  private boolean isReply;
  private boolean isPost;

  @ManyToOne
  private Post replyFor;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Like> likes = new ArrayList<>();

  @OneToMany
  private List<Post> replyPost = new ArrayList<>();

  @OneToMany
  private List<User> rePostUsers = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = true)
  private Group group;

  private LocalDateTime createdAt;

}
