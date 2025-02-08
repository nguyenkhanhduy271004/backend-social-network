package com.project.social_network.utils;

import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.User;
import org.springframework.stereotype.Component;

@Component
public class PostUtil {

  public boolean isLikedByReqUser(User reqUser, Post post) {

    for(Like like:post.getLikes()) {
      if(like.getUser().getId().equals(reqUser.getId())) {
        return true;
      }
    }
    return false;
  }

  public boolean isRePostByReqUser(User reqUser, Post post) {
    for (User user:post.getRePostUsers()) {
      if(user.getId().equals(reqUser.getId())) {
        return true;
      }
    }
    return false;
  }
}
