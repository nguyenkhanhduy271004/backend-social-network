package com.project.social_network.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
  private Date createdDate;


  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {
    private Long id;
    private String name;
  }

}
