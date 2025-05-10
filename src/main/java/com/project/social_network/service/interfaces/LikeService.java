package com.project.social_network.service.interfaces;

import java.util.List;

import com.project.social_network.model.Like;
import com.project.social_network.model.User;

public interface LikeService {

  Like likePost(Long postId, User user);

  List<Like> getAllLikes(Long postId);
}
