package com.project.social_network.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.social_network.enums.AuthProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity {

  private String fullName;
  private String location;
  private String website;
  private String birthDate;
  private String email;
  private String password;
  private String mobile;
  private String image;
  private String backgroundImage;
  private String bio;
  private boolean req_user;
  private boolean loginWithGoogle;
  private boolean isAdmin;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Story> stories = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Reel> reels = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Like> likes = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Comment> comments = new ArrayList<>();

  @Embedded
  private Verification verification;

  @JsonIgnore
  @ManyToMany
  private List<User> followers = new ArrayList<>();

  @JsonIgnore
  @ManyToMany
  private List<User> followings = new ArrayList<>();

  @ManyToMany(mappedBy = "users")
  private List<Group> groups = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

}
