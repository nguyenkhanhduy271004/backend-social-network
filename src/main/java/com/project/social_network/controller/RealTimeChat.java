package com.project.social_network.controller;

import com.project.social_network.dto.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeChat {

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/chat/{receiverId}")
  public void sendToUser(@Payload MessageRequest messageRequest, @DestinationVariable String receiverId) {
    simpMessagingTemplate.convertAndSendToUser(receiverId, "/private", messageRequest);
  }
}

