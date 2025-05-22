package com.project.social_network.controller;

import com.project.social_network.service.AIDatabaseService;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class DeepseekController {

  OllamaChatModel chatModel;
  AIDatabaseService aiDatabaseService;

  @Autowired
  DeepseekController(OllamaChatModel chatModel, AIDatabaseService aiDatabaseService) {
    this.chatModel = chatModel;
    this.aiDatabaseService = aiDatabaseService;
  }

  @GetMapping("/generate")
  Map<String, String> generate(
      @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    Logger logger = LoggerFactory.getLogger(DeepseekController.class);
    try {
      String response = this.chatModel.call(message);
      return Map.of("generation", response);
    } catch (Exception e) {
      logger.error("Error generating response for message: {}", message, e);
      return Map.of("error", "Failed to generate response: " + e.getMessage());
    }
  }

  @GetMapping("/generateStream")
  Flux<ChatResponse> generateStream(
      @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    Prompt prompt = new Prompt(new UserMessage(message));
    return this.chatModel.stream(prompt);
  }

  @GetMapping("/query")
  ResponseEntity<?> processQuery(@RequestParam String query) {
    try {
      List<Map<String, Object>> results = aiDatabaseService.processNaturalLanguageQuery(query);
      return ResponseEntity.ok(results);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (SecurityException e) {
      return ResponseEntity.status(403).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error processing query: " + e.getMessage());
    }
  }

}