package com.project.social_network.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
  @NotBlank(message = "Email không được để trống!")
  private String email;
  @NotBlank(message = "Password không được để trống!")
  private String password;
}
