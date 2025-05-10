package com.project.social_network.service.impl;

import com.project.social_network.converter.StoryConverter;
import com.project.social_network.dto.StoryDto;
import com.project.social_network.exceptions.PostException;
import com.project.social_network.exceptions.StoryException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Story;
import com.project.social_network.model.User;
import com.project.social_network.repository.StoryRepository;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UploadImageFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

  private final StoryConverter storyConverter;

  private final UploadImageFile uploadImageFile;

  private final StoryRepository storyRepository;

  @Override
  public StoryDto createStory(MultipartFile file, String content, User user) throws IOException {
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
  @Cacheable(value = "stories", key = "#storyId")
  public StoryDto findStoryById(Long storyId) {
    Story story = storyRepository.findById(storyId)
        .orElseThrow(() -> new PostException("Story not found with id: " + storyId));
    return storyConverter.toStoryDto(story, story.getUser());
  }

  public Story findStoryById2(Long storyId) {
    return storyRepository.findById(storyId)
        .orElseThrow(() -> new PostException("Story not found with id: " + storyId));
  }

  @Override
  @CacheEvict(value = "stories", key = "#storyId")
  public void deleteStoryById(Long storyId, Long userId) {
    Story story = findStoryById2(storyId);

    if (!userId.equals(story.getUser().getId())) {
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
