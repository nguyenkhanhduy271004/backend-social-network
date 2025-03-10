package com.project.social_network.service.interfaces;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Like;
import com.project.social_network.model.entity.User;
import java.util.List;

public interface LikeService {

  Like likePost(Long postId, User user) throws UserException, PostException;
  List<Like> getAllLikes(Long postId) throws PostException;
}
