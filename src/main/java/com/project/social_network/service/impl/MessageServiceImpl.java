package com.project.social_network.service.impl;

import com.project.social_network.converter.UserConverter;
import com.project.social_network.dto.UserDto;
import com.project.social_network.model.Message;
import com.project.social_network.model.User;
import com.project.social_network.repository.MessageRepository;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.service.interfaces.MessageService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserConverter userConverter;

  public Message sendMessage(Long senderId, Long receiverId, String content) {
    Message message = new Message();
    message.setSenderId(senderId);
    message.setReceiverId(receiverId);
    message.setContent(content);
    message.setTimestamp(LocalDateTime.now());

    return messageRepository.save(message);
  }

  public List<Message> getChatHistory(Long senderId, Long receiverId) {
    return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId, senderId, receiverId);
  }

  @Override
  public List<UserDto> findDistinctReceiverIdsBySenderId(Long senderId) {
    List<Long> userIds = messageRepository.findDistinctChatPartners(senderId);

    List<UserDto> userDtos = new ArrayList<>();

    for (Long userId:userIds) {
      User user = userRepository.findById(userId).get();
      UserDto userDto = userConverter.toUserDto(user);
      userDtos.add(userDto);
    }

    return userDtos;
  }
}
