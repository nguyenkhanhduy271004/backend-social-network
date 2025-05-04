package com.project.social_network.model;

import com.project.social_network.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Notification extends BaseEntity {

  private String content;

  @ManyToOne
  private User userTo;

  @ManyToOne
  private User userFrom;

  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  private boolean delivered;
  private boolean read;
}
