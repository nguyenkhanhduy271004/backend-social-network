package com.project.social_network.service.impl;

import com.project.social_network.model.entity.Like;
import com.project.social_network.model.entity.Post;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.repository.LikeRepository;
import com.project.social_network.repository.PostRepository;
import com.project.social_network.service.interfaces.LikeService;
import com.project.social_network.service.interfaces.PostService;
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

    Post post = postService.findByPostId(postId);

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

    Post post = postService.findByPostId(postId);

    List<Like> likes = likeRepository.findByPostId(postId);

    return likes;
  }
}
