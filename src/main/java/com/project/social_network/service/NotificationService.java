package com.project.social_network.service;

import com.project.social_network.constant.JobQueue;
import com.project.social_network.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendNotification(Long userId, Long postId, String message) {
        try {
            NotificationMessage notification = new NotificationMessage();
            notification.setUserId(userId);
            notification.setPostId(postId);
            notification.setMessage(message);

            logger.info("Sending notification to user {}: {}", userId, message);

            rabbitTemplate.convertAndSend(JobQueue.QUEUE_DEV, notification);

            logger.info("Notification sent successfully to user {}", userId);
        } catch (Exception e) {
            logger.error("Error sending notification to user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }
}