package com.project.social_network.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @NotBlank(message = "Email không được để trống!")
  private String email;
  @NotBlank(message = "Password không được để trống!")
  private String password;
}
