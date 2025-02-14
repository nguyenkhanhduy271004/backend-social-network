package com.project.social_network.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long senderId;

  @Column(nullable = false)
  private Long receiverId;

  @Column(nullable = false, length = 500)
  private String content;

  private LocalDateTime timestamp;
}

