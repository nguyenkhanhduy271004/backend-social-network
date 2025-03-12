package com.project.social_network.service;

import com.project.social_network.entity.Story;
import com.project.social_network.repository.StoryRepository;
import java.time.LocalDateTime;
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