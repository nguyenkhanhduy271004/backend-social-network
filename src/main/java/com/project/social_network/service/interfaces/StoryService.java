package com.project.social_network.service.interfaces;

import com.project.social_network.dto.response.StoryDto;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StoryService {
  StoryDto createStory(MultipartFile file, String content) throws UserException, IOException;
  List<StoryDto> findAllStory();
  StoryDto findStoryById(Long storyId);
  void deleteStoryById(Long storyId, Long userId) throws UserException, StoryException;
  List<StoryDto> getUserStory(User user);
}
