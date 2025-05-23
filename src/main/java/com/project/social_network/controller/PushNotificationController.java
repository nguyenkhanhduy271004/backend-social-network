package com.project.social_network.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.social_network.model.Notification;
import com.project.social_network.service.impl.PushNotificationServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/push-notifications")
@Slf4j
@Tag(name = "Push Notification Controller")
public class PushNotificationController {

  private final PushNotificationServiceImpl service;

  public PushNotificationController(PushNotificationServiceImpl service) {
    this.service = service;
  }

  @CrossOrigin(origins = "*")
  @GetMapping(value = "/{userID}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<List<Notification>>> streamLastMessage(@PathVariable Long userID) {
    return service.getNotificationsByUserToID(userID);
  }

}