package com.project.social_network.converter;

import com.project.social_network.models.dtos.PostDto;
import com.project.social_network.models.dtos.StoryDto;
import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import com.project.social_network.utils.StoryUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoryConverter {


  @Autowired
  private UserConverter userConverter;
  @Autowired
  private StoryUtil storyUtil;

  public Story storyConverter(Story story, User user) {
    ModelMapper modelMapper = new ModelMapper();
    Story newStory = modelMapper.map(story, Story.class);
    newStory.setUser(user);
    return newStory;
  }

  public StoryDto toStoryDto(Story story, User reqUser) {
    UserDto user = userConverter.toUserDto(story.getUser());

    boolean isLiked = storyUtil.isLikedByReqUser(reqUser, story);

    StoryDto storyDto = new StoryDto();
    storyDto.setId(story.getId());
    storyDto.setContent(story.getContent());
    storyDto.setCreatedAt(story.getCreatedDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime());
    storyDto.setImage(story.getImage());
    storyDto.setTotalLikes(story.getLikes().size());
    storyDto.setUser(user);
    storyDto.setLiked(isLiked);

    return storyDto;
  }


}
