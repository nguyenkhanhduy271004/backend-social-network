package com.project.social_network.services;

import com.project.social_network.models.entities.Story;
import com.project.social_network.repositories.StoryRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StoryScheduler {

  private final StoryRepository storyRepository;

  public StoryScheduler(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @Async
  @Scheduled(fixedRate = 60000)
  public void deleteExpiredStories() {

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime twentyFourHoursAgo = now.minusHours(24);

    List<Story> expiredStories = storyRepository.findAllByCreatedAtBeforeAndIsDeletedFalse(twentyFourHoursAgo);

    for (Story story : expiredStories) {
      story.setDeleted(true);
      storyRepository.save(story);
    }
  }
}