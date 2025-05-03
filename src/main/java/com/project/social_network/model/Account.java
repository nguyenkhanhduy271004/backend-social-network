package com.project.social_network.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  private Long id;

  private String fullName;

  private String email;

  private String pictureUrl;

  private String roles;

  public Account(String fullName, String email, String pictureUrl) {
    this.fullName = fullName;
    this.email = email;
    this.pictureUrl = pictureUrl;
  }
}
