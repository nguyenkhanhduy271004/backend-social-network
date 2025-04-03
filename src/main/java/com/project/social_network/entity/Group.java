package com.project.social_network.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_table")
public class Group extends BaseEntity {

  private String name;

  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ManyToOne
  @JoinColumn(name = "admin_id", nullable = false)
  private User admin;


  @ManyToMany
  @JoinTable(
      name = "user_groups",
      joinColumns = @JoinColumn(name = "group_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @JsonIgnore
  private List<User> users = new ArrayList<>();


  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<Post> posts = new ArrayList<>();
}
