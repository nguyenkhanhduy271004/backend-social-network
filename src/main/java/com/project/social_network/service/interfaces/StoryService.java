package com.project.social_network.service.interfaces;

import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Story;
import com.project.social_network.model.entity.User;
import java.util.List;

public interface StoryService {
  Story createStory(Story req, User user) throws UserException;
  List<Story> findAllStory();
  Story findStoryById(Long storyId);
  void deleteStoryById(Long storyId, Long userId) throws UserException, StoryException;
  List<Story> getUserStory(User user);
}
