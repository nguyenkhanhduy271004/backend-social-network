package com.project.social_network.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DeepseekController {

  private final OllamaChatModel chatModel;

  @Autowired
  public DeepseekController(OllamaChatModel chatModel) {
    this.chatModel = chatModel;
  }


  @GetMapping("/ai/generate")
  public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    Logger logger = LoggerFactory.getLogger(DeepseekController.class);
    try {
      String response = this.chatModel.call(message);
      return Map.of("generation", response);
    } catch (Exception e) {
      logger.error("Error generating response for message: {}", message, e);
      return Map.of("error", "Failed to generate response: " + e.getMessage());
    }
  }

  @GetMapping("/ai/generateStream")
  public Flux<ChatResponse> generateStream(@RequestParam(value = "message",
      defaultValue = "Tell me a joke") String message) {
    Prompt prompt = new Prompt(new UserMessage(message));
    return this.chatModel.stream(prompt);
  }

}