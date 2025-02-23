package com.project.social_network.services;

import com.project.social_network.models.entities.Story;
import com.project.social_network.repositories.StoryRepository;
import java.util.Date;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StoryScheduler {

  private final StoryRepository storyRepository;

  public StoryScheduler(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
  }

  @Scheduled(fixedRate = 60000)
  public void deleteExpiredStories() {
    Date now = new Date();
    long twentyFourHoursAgo = now.getTime() - (24 * 60 * 60 * 1000);

    List<Story> expiredStories = storyRepository.findAllByCreatedAtBeforeAndIsDeletedFalse(new Date(twentyFourHoursAgo));

    for (Story story : expiredStories) {
      story.setDeleted(true);
      storyRepository.save(story);
    }
  }
}