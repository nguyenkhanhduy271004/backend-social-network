package com.project.social_network.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.social_network.dto.StoryDto;
import com.project.social_network.model.User;

public interface StoryService {
  StoryDto createStory(MultipartFile file, String content, User user) throws IOException;

  List<StoryDto> findAllStory();

  StoryDto findStoryById(Long storyId);

  void deleteStoryById(Long storyId, Long userId);

  List<StoryDto> getUserStory(User user);
}
