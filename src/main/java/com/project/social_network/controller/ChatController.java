package com.project.social_network.controller;

import com.project.social_network.model.Message;
import com.project.social_network.service.interfaces.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Tag(name = "Chat Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ChatController {

  MessageService messageService;

  SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chat/{senderId}/{receiverId}")
  void handlePrivateMessage(
      @Payload Message message,
      @DestinationVariable Long senderId,
      @DestinationVariable Long receiverId) {
    Message savedMessage = messageService.sendMessage(
        message.getSenderId(),
        message.getReceiverId(),
        message.getContent());

    messagingTemplate.convertAndSendToUser(
        receiverId.toString(),
        "/private",
        savedMessage);

    messagingTemplate.convertAndSendToUser(
        senderId.toString(),
        "/private",
        savedMessage);
  }
}
