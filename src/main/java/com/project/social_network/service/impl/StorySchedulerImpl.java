package com.project.social_network.service.impl;

import com.project.social_network.model.Story;
import com.project.social_network.repository.StoryRepository;
import com.project.social_network.service.interfaces.StorySchedulerService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StorySchedulerImpl implements StorySchedulerService {

  private final StoryRepository storyRepository;

  public StorySchedulerImpl(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @Async
  @Scheduled(fixedRate = 60000)
  @Override
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