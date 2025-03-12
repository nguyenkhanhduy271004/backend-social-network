package com.project.social_network.util;

import com.project.social_network.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

  public boolean isReqUser(User reqUser, User user2) {
    return reqUser.getId().equals(user2.getId());
  }

  public boolean isFollowedByReqUser(User reqUser, User user2) {
    return reqUser.getFollowings().contains(user2);
  }

}
