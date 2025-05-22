package com.project.social_network.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIChatService {
  private final ChatClient chatClient;

  public AIChatService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String chat(String message) {
    return this.chatClient.prompt()
        .user(message)
        .call()
        .content();
  }
}
