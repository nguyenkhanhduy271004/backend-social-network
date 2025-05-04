package com.project.social_network.service.impl;


import com.project.social_network.model.Notification;
import com.project.social_network.repository.NotificationStorageRepository;
import com.project.social_network.service.interfaces.PushNotificationService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {


  private final NotificationStorageRepository notificationStorageRepository;


  public PushNotificationServiceImpl(NotificationStorageRepository notificationStorageRepository) {
    this.notificationStorageRepository = notificationStorageRepository;
  }

  private List<Notification> getNotifs(Long userID) {
    var notifs = notificationStorageRepository.findByUserToIdAndDeliveredFalse(userID);
    notifs.forEach(x -> x.setDelivered(true));
    notificationStorageRepository.saveAll(notifs);
    return notifs;
  }

  @Override
  public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserToID(Long userID) {
    if (userID != null) {
      return Flux.interval(Duration.ofSeconds(5))
          .publishOn(Schedulers.boundedElastic())
          .map(sequence -> {
            List<Notification> notifications;
            try {
              notifications = getNotifs(userID);
              if (notifications == null) {
                notifications = new ArrayList<>();
              }
            } catch (Exception e) {
              e.printStackTrace();
              notifications = new ArrayList<>();
            }

            return ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence))
                .event("user-list-event")
                .data(notifications)
                .build();
          })
          .doOnError(e -> {
            if (e instanceof IOException) {
              System.out.println("Client closed the SSE connection.");
            } else {
              e.printStackTrace();
            }
          });
    }

    return Flux.interval(Duration.ofSeconds(5))
        .map(sequence ->
            ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence))
                .event("user-list-event")
                .data(new ArrayList<>())
                .build()
        );
  }

}