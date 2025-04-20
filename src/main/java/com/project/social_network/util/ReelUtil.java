package com.project.social_network.util;

import com.project.social_network.model.Like;
import com.project.social_network.model.Reel;
import com.project.social_network.model.User;
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
