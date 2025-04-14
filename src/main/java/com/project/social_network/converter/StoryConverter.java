package com.project.social_network.converter;

import com.project.social_network.dto.response.StoryDto;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
import com.project.social_network.util.StoryUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StoryConverter {

  private final UserConverter userConverter;
  private final StoryUtil storyUtil;
  private final ModelMapper modelMapper;

  public StoryConverter(UserConverter userConverter, StoryUtil storyUtil, ModelMapper modelMapper) {
    this.userConverter = userConverter;
    this.storyUtil = storyUtil;
    this.modelMapper = modelMapper;
  }

  public Story storyConverter(Story story, User user) {
    if (story == null || user == null) {
      return null;
    }

    Story newStory = modelMapper.map(story, Story.class);
    newStory.setUser(user);
    return newStory;
  }

  public StoryDto toStoryDto(Story story, User reqUser) {
    if (story == null || reqUser == null) {
      return null;
    }

    StoryDto storyDto = modelMapper.map(story, StoryDto.class);
    storyDto.setUser(userConverter.toUserDto(story.getUser()));
    storyDto.setTotalLikes(story.getLikes() != null ? story.getLikes().size() : 0);
    storyDto.setLiked(storyUtil.isLikedByReqUser(reqUser, story));

    return storyDto;
  }
}