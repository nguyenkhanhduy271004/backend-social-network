package com.project.social_network.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Story extends BaseEntity{

  @ManyToOne
  @JsonBackReference
  private User user;

  private String content;
  private String image;
  private boolean isDeleted = false;

  @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
  private List<Like> likes = new ArrayList<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

}
