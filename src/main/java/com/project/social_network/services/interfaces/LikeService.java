package com.project.social_network.services.interfaces;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.User;
import java.util.List;

public interface LikeService {

  Like likePost(Long postId, User user) throws UserException, PostException;
  List<Like> getAllLikes(Long postId) throws PostException;
}
