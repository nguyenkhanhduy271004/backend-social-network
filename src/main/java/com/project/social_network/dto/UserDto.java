package com.project.social_network.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

  private Long id;
  private String fullName;
  private String email;
  private String image;
  private String location;
  private String website;
  private String birthDate;
  private String mobile;
  private String backgroundImage;
  private String bio;
  private boolean req_user;
  private boolean loginWithGoogle;
  private boolean isAdmin;

  private List<UserDto> followers = new ArrayList<>();
  private List<UserDto> following = new ArrayList<>();

  private boolean followed;

  private boolean isVerified;
}
