package com.project.social_network.service.interfaces;

import com.project.social_network.model.Notification;
import java.util.List;

public interface NotificationStorageService {
  Notification createNotificationStorage(Notification notificationStorage);

  Notification getNotificationsByID(Long id);

  List<Notification> getNotificationsByUserIDNotRead(Long userID);

  List<Notification> getNotificationsByUserID(Long userID);

  Notification changeNotifStatusToRead(Long notifID);

  void clear();
}
