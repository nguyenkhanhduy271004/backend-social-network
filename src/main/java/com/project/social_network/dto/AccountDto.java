package com.project.social_network.dto;

import com.project.social_network.model.Account;
import com.project.social_network.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

  private String fullName;
  private String email;

  public static final AccountDto convertToDto(Account account) {
    return AccountDto.builder()
        .fullName(account.getFullName())
        .email(account.getEmail())
        .build();
  }
}
