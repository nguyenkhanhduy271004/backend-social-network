package com.project.social_network.converter;

import com.project.social_network.dto.response.StoryDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
import com.project.social_network.util.StoryUtil;
import java.time.ZoneId;
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
