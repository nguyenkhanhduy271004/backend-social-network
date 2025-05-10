package com.project.social_network.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.project.social_network.model.Message;
import com.project.social_network.service.interfaces.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@Tag(name = "Chat Controller")
@RequiredArgsConstructor
public class ChatController {

  private final MessageService messageService;

  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chat/{senderId}/{receiverId}")
  public void handlePrivateMessage(
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
