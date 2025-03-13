package com.project.social_network.service.interfaces;

import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Message;
import java.util.List;

public interface MessageService {
  Message sendMessage(Long senderId, Long receiverId, String content);
  List<Message> getChatHistory(Long senderId, Long receiverId);
  List<UserDto> findDistinctReceiverIdsBySenderId(Long senderId);
}
