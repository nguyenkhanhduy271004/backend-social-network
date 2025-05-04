package com.project.social_network.service.interfaces;

import com.project.social_network.model.Notification;
import java.util.List;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface PushNotificationService {
  Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserToID(Long userID);
}
