package com.project.social_network.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Story extends BaseEntity{

  @ManyToOne
  private User user;

  private String content;
  private String image;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Like> likes = new ArrayList<>();

}
