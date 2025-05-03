package com.project.social_network.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGroupRequest {
  @NotBlank(message = "Group name is required")
  private String name;
  private boolean isPublic;
}
