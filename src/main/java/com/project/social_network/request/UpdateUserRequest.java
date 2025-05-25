package com.project.social_network.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {

  private Long id;
  private String fullName;
  private String location;
  private String website;
  private String birthDate;
  private String mobile;
  private String bio;
  private String image;
  private String backgroundImage;
}
