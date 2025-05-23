package com.project.social_network.consumer;

import com.project.social_network.constant.JobQueue;
import com.project.social_network.dto.NotificationMessage;
import com.project.social_network.model.Notification;
import com.project.social_network.model.User;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.service.interfaces.NotificationStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

@Component
public class Consumer {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private NotificationStorageService notificationStorageService;

  @Autowired
  private UserRepository userRepository;

  @RabbitListener(queues = JobQueue.QUEUE_DEV)
  public void receiveMessage(NotificationMessage message) {
    try {
      if (message == null) {
        logger.warn("Received null message");
        throw new AmqpRejectAndDontRequeueException("Message is null");
      }

      logger.info("Received notification message for user {}: {}", message.getUserId(), message);

      if (message.getPostId() == null || message.getUserId() == null || message.getMessage() == null) {
        logger.warn("Invalid message format: {}", message);
        throw new AmqpRejectAndDontRequeueException("Invalid message format");
      }

      User userTo = userRepository.findById(message.getUserId())
          .orElseThrow(() -> new RuntimeException("User not found"));

      Notification notification = Notification.builder()
          .content(message.getMessage())
          .userTo(userTo)
          .delivered(false)
          .read(false)
          .build();

      notificationStorageService.createNotificationStorage(notification);

      messagingTemplate.convertAndSendToUser(
          message.getUserId().toString(),
          "/queue/notifications",
          message);

      logger.info("Notification sent to user {}: {}", message.getUserId(), message.getMessage());

    } catch (Exception e) {
      logger.error("Error processing notification message: {}", e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Error processing message", e);
    }
  }
}
