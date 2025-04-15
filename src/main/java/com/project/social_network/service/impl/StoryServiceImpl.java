package com.project.social_network.service.impl;

import com.project.social_network.converter.StoryConverter;
import com.project.social_network.model.dto.StoryDto;
import com.project.social_network.model.entity.Story;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.repository.StoryRepository;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UploadImageFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoryServiceImpl implements StoryService {

  @Autowired
  private StoryRepository storyRepository;
  @Autowired
  private StoryConverter storyConverter;
  @Autowired
  private UploadImageFile uploadImageFile;

  @Override
  public StoryDto createStory(MultipartFile file, String content, User user) throws UserException, IOException {
    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Story req = new Story();
    req.setContent(content);
    req.setImage(imageFileUrl);
    req.setUser(user);

    Story story = storyRepository.save(req);

    return storyConverter.toStoryDto(story, story.getUser());
  }

  @Override
  public List<StoryDto> findAllStory() {

    return storyRepository.findAllByIsDeletedFalse()
        .stream()
        .map((story) -> storyConverter.toStoryDto(story, story.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public StoryDto findStoryById(Long storyId) {
    Story story = storyRepository.findById(storyId).orElseThrow(() -> new PostException("Story not found with id: " + storyId));
    return storyConverter.toStoryDto(story, story.getUser());
  }

  public Story findStoryById2(Long storyId) {
    return storyRepository.findById(storyId).orElseThrow(() -> new PostException("Story not found with id: " + storyId));
  }

  @Override
  public void deleteStoryById(Long storyId, Long userId) throws UserException, StoryException {
    Story story = findStoryById2(storyId);

    if(!userId.equals(story.getUser().getId())) {
      throw new UserException("You can't delete another user's story");
    }

    storyRepository.deleteById(story.getId());
  }

  @Override
  public List<StoryDto> getUserStory(User user) {

    return storyRepository.findByUser(user)
        .stream()
        .map((story) -> storyConverter.toStoryDto(story, story.getUser()))
        .collect(Collectors.toList());
  }
}
