package com.project.social_network.services.impl;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.User;
import com.project.social_network.repositories.LikeRepository;
import com.project.social_network.repositories.PostRepository;
import com.project.social_network.services.interfaces.LikeService;
import com.project.social_network.services.interfaces.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

  @Autowired
  private LikeRepository likeRepository;
  @Autowired
  private PostService postService;
  @Autowired
  private PostRepository postRepository;

  @Override
  public Like likePost(Long postId, User user) throws UserException, PostException {
    Like isLikeExist = likeRepository.isLikeExist(user.getId(), postId);

    if(isLikeExist != null) {
      likeRepository.deleteById(isLikeExist.getId());
      return isLikeExist;
    }

    Post post = postService.findById(postId);

    Like like = new Like();
    like.setPost(post);
    like.setUser(user);

    Like savedLike = likeRepository.save(like);
    post.getLikes().add(savedLike);
    postRepository.save(post);

    return savedLike;
  }

  @Override
  public List<Like> getAllLikes(Long postId) throws PostException {

    Post post = postService.findById(postId);

    List<Like> likes = likeRepository.findByPostId(postId);

    return likes;
  }
}
