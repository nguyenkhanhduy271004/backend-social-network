package com.project.social_network.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity{

  @Column(nullable = false)
  private Long senderId;

  @Column(nullable = false)
  private Long receiverId;

  @Column(nullable = false, length = 500)
  private String content;

  private LocalDateTime timestamp;
}

