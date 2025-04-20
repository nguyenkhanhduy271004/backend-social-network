package com.project.social_network.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateGroupRequest {

  @NotBlank(message = "Group name must not be blank")
  private String name;

}
