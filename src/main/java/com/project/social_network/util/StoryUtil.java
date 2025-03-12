package com.project.social_network.util;

import com.project.social_network.entity.Like;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
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
