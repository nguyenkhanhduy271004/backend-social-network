package com.project.social_network.service.impl;

import com.project.social_network.model.Notification;
import com.project.social_network.repository.NotificationStorageRepository;
import com.project.social_network.service.interfaces.NotificationStorageService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationStorageServiceImpl implements NotificationStorageService {

  private final NotificationStorageRepository notifRepository;

  public NotificationStorageServiceImpl(NotificationStorageRepository notifRepository) {
    this.notifRepository = notifRepository;
  }

  @Override
  public Notification createNotificationStorage(Notification notificationStorage) {
    return notifRepository.save(notificationStorage);
  }

  @Override
  public Notification getNotificationsByID(Long id) {
    return notifRepository.findById(id).orElseThrow(() -> new RuntimeException("notification not found!"));
  }

  @Override
  public List<Notification> getNotificationsByUserIDNotRead(Long userID) {
    return notifRepository.findByUserToIdAndDeliveredFalse(userID);
  }

  @Override
  public List<Notification> getNotificationsByUserID(Long userID) {
    return notifRepository.findByUserToId(userID);
  }

  @Override
  public Notification changeNotifStatusToRead(Long notifID) {
    var notif = notifRepository.findById(notifID)
        .orElseThrow(() -> new RuntimeException("not found!"));
    notif.setRead(true);
    return notifRepository.save(notif);
  }

  @Override
  public void clear() {
    notifRepository.deleteAll();
  }
}