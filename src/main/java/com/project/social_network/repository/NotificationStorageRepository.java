package com.project.social_network.repository;

import com.project.social_network.model.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStorageRepository extends JpaRepository<Notification, String> {

  Optional<Notification> findById(Long id);

  List<Notification> findByUserToId(Long id);


  List<Notification> findByUserToIdAndDeliveredFalse(Long id);


}