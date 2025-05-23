package com.project.social_network.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.project.social_network.request.MessageRequest;
import com.project.social_network.response.Message;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Realtime Chat Controller")
public class RealTimeChat {

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/chat/{receiverId}")
  public void sendToUser(@Payload MessageRequest messageRequest, @DestinationVariable String receiverId) {
    simpMessagingTemplate.convertAndSendToUser(receiverId, "/private", messageRequest);
  }

  @MessageMapping("/private")
  public void sendToSpecificUser(@Payload Message message) {
    simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
  }
}
