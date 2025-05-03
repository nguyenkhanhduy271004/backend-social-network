package com.project.social_network.util;

import com.project.social_network.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

  public boolean isReqUser(User reqUser, User user2) {
    return reqUser.getId().equals(user2.getId());
  }

  public boolean isFollowedByReqUser(User reqUser, User user2) {
    return reqUser.getFollowings().contains(user2);
  }

  public String generateAuthUrl(String loginType) {
    String url = "";
    switch (loginType.toLowerCase()) {
      case "google":
        url = "https://accounts.google.com/o/oauth2/auth"
            + "?client_id=615612093999-3ommfatdj5qm627gs6um9dneft4civdv.apps.googleusercontent.com"
            + "&redirect_uri=http://localhost:3000/auth/google/callback"
            + "&response_type=code"
            + "&scope=email%20profile";
        break;

      case "facebook":
        url = "https://www.facebook.com/v12.0/dialog/oauth"
            + "?client_id=YOUR_FACEBOOK_CLIENT_ID"
            + "&redirect_uri=http://localhost:3000/auth/facebook/callback"
            + "&response_type=code"
            + "&scope=email,public_profile";
        break;

      default:
        throw new IllegalArgumentException("Unsupported login type: " + loginType);
    }

    return url;
  }


}
