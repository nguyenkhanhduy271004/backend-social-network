package com.project.social_network.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

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

  private boolean isReply;
  private boolean isPost;

  private LocalDateTime createdAt;

}
