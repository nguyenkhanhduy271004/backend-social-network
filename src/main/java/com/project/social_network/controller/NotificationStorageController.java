package com.project.social_network.controller;

import com.project.social_network.model.Notification;
import com.project.social_network.service.impl.NotificationStorageServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notification")
@RestController
@Tag(name = "Notification Storage Controller")
public class NotificationStorageController {

  private final NotificationStorageServiceImpl notifService;


  public NotificationStorageController(NotificationStorageServiceImpl notifService) {
    this.notifService = notifService;
  }

  @GetMapping("/{userID}")
  public ResponseEntity<List<Notification>> getNotificationsByUserID(@Min(1) @PathVariable Long userID) {
    return ResponseEntity.ok(notifService.getNotificationsByUserID(userID));
  }

  @PatchMapping("/read/{notifID}")
  public ResponseEntity<?> changeNotifStatusToRead(@Min(1) @PathVariable Long notifID) {
    return ResponseEntity.ok(notifService.changeNotifStatusToRead(notifID));
  }


}