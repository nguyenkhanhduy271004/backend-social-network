package com.project.social_network.repository;

import com.project.social_network.model.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long senderId, Long receiverId, Long receiverId2, Long senderId2);

  @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId")
  List<Long> findDistinctChatPartners(Long userId);

}