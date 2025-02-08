package com.project.social_network.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
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
