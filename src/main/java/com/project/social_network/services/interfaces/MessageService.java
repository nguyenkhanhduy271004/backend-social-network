package com.project.social_network.services.interfaces;

import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.Message;
import java.util.List;

public interface MessageService {
  Message sendMessage(Long senderId, Long receiverId, String content);
  List<Message> getChatHistory(Long senderId, Long receiverId);
  List<UserDto> findDistinctReceiverIdsBySenderId(Long senderId);
}
