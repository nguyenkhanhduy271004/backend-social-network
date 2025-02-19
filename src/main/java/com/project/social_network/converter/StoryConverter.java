package com.project.social_network.converter;

import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StoryConverter {

  public Story storyConverter(Story story, User user) {
    ModelMapper modelMapper = new ModelMapper();
    Story newStory = modelMapper.map(story, Story.class);
    return newStory;
  }
}
