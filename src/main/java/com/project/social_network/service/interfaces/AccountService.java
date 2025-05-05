package com.project.social_network.service.interfaces;

import com.project.social_network.dto.IdTokenRequestDto;
import com.project.social_network.model.Account;

public interface AccountService {

  Account getAccount(Long id);

  String loginOAuthGoogle(IdTokenRequestDto requestBody);

  Account createOrUpdateUser(Account account);
}
