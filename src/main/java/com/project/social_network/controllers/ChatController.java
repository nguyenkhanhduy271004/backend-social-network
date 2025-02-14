package com.project.social_network.controllers;

import com.project.social_network.models.entities.Message;
import com.project.social_network.services.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  @Autowired
  private MessageService messageService;

  @MessageMapping("/sendMessage")
  @SendTo("/topic/messages")
  public Message sendMessage(Message message) {
    return messageService.sendMessage(message.getSenderId(), message.getReceiverId(), message.getContent());
  }

}
