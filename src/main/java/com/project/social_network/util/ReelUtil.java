package com.project.social_network.util;

import com.project.social_network.model.entity.Like;
import com.project.social_network.model.entity.Reel;
import com.project.social_network.model.entity.User;
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
