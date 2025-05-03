package com.project.social_network.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_table")
public class Group extends BaseEntity {

  private String name;

  private boolean isPublic = true;

  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  @ManyToOne
  @JoinColumn(name = "admin_id", nullable = false)
  private User admin;

  @ManyToMany
  @JoinTable(name = "user_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  @JsonIgnore
  private List<User> users = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "group_join_requests", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  @JsonIgnore
  private List<User> pendingRequests = new ArrayList<>();

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<Post> posts = new ArrayList<>();
}
