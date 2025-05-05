package com.project.social_network.service.interfaces;

import com.project.social_network.exceptions.PostException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Like;
import com.project.social_network.model.User;
import java.util.List;

public interface LikeService {

  Like likePost(Long postId, User user) throws UserException, PostException;
  List<Like> getAllLikes(Long postId) throws PostException;
}
