package com.project.social_network.utils;

import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.Reel;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ReelUtil {
  public boolean isLikedByReqUser(User reqUser, Reel reel) {

    for(Like like:reel.getLikes()) {
      if(like.getUser().getId().equals(reqUser.getId())) {
        return true;
      }
    }
    return false;
  }
}
