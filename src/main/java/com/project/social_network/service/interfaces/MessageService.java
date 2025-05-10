package com.project.social_network.service.interfaces;

import java.util.List;

import com.project.social_network.dto.UserDto;
import com.project.social_network.model.Message;

public interface MessageService {
  Message sendMessage(Long senderId, Long receiverId, String content);

  List<Message> getChatHistory(Long senderId, Long receiverId);

  List<UserDto> findDistinctReceiverIdsBySenderId(Long senderId);
}
