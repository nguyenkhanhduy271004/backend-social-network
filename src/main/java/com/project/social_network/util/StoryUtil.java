package com.project.social_network.util;

import com.project.social_network.model.Like;
import com.project.social_network.model.Story;
import com.project.social_network.model.User;
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
