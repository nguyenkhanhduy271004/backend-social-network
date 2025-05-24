package com.project.social_network.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.social_network.exception.PostException;
import com.project.social_network.model.Like;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.repository.LikeRepository;
import com.project.social_network.repository.PostRepository;
import com.project.social_network.service.interfaces.LikeService;
import com.project.social_network.service.interfaces.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final PostService postService;

  private final LikeRepository likeRepository;

  private final PostRepository postRepository;

  @Override
  public Like likePost(Long postId, User user) {
    Like isLikeExist = likeRepository.isLikeExist(user.getId(), postId);

    if (isLikeExist != null) {
      likeRepository.deleteById(isLikeExist.getId());
      throw new PostException("You already liked this post!");
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
  public List<Like> getAllLikes(Long postId) {

    Post post = postService.findByPostId(postId);

    if (post == null) {
      throw new PostException("Post not found!");
    }

    return likeRepository.findByPostId(postId);
  }
}
