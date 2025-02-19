package com.project.social_network.services.interfaces;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.PostReplyRequest;
import java.util.List;

public interface StoryService {
  Story createStory(Story req, User user) throws UserException;
  List<Story> findAllStory();
  Story findStoryById(Long storyId);
  void deleteStoryById(Long storyId, Long userId) throws UserException, StoryException;
  List<Story> getUserStory(User user);
}
