package com.project.social_network.service.impl;

import com.project.social_network.converter.StoryConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
import com.project.social_network.repository.StoryRepository;
import com.project.social_network.service.interfaces.StoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {

  @Autowired
  private StoryRepository storyRepository;
  @Autowired
  private StoryConverter storyConverter;

  @Override
  public Story createStory(Story req, User user) throws UserException {
    Story story = storyConverter.storyConverter(req, user);
    return storyRepository.save(story);
  }

  @Override
  public List<Story> findAllStory() {
    return storyRepository.findAllByIsDeletedFalse();
  }

  @Override
  public Story findStoryById(Long storyId) {
    Story story = storyRepository.findById(storyId).orElseThrow(() -> new PostException("Story not found with id: " + storyId));
    return story;
  }

  @Override
  public void deleteStoryById(Long storyId, Long userId) throws UserException, StoryException {
    Story story = findStoryById(storyId);

    if(!userId.equals(story.getUser().getId())) {
      throw new UserException("You can't delete another user's story");
    }

    storyRepository.deleteById(story.getId());
  }

  @Override
  public List<Story> getUserStory(User user) {
    return storyRepository.findByUser(user);
  }
}
