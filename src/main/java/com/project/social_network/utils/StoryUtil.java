package com.project.social_network.utils;

import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class StoryUtil {
  public boolean isLikedByReqUser(User reqUser, Story story) {

    for(Like like:story.getLikes()) {
      if(like.getUser().getId().equals(reqUser.getId())) {
        return true;
      }
    }
    return false;
  }
}
