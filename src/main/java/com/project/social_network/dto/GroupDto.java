package com.project.social_network.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

  private Long id;
  private String name;
  private User admin;
  private List<PostDto> posts = new ArrayList<>();
  private List<User> members = new ArrayList<>();
  private Date createdDate;


  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User {
    private Long id;
    private String fullName;
  }

}
