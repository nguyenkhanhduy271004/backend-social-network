package com.project.social_network.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verification {

  private boolean status = false;
  private LocalDateTime startedAt;
  private LocalDateTime endsAt;
  private String planType;
}
