package com.project.social_network.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank(message = "Email không được để trống!")
  private String email;
  @NotBlank(message = "Password không được để trống!")
  private String password;
  @NotBlank(message = "Tên không được để trống")
  private String fullName;
  @NotBlank(message = "Ngày sinh không được để trống")
  private String birthDate;

}
