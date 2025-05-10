package com.project.social_network.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.social_network.dto.LikeDto;
import com.project.social_network.model.Like;
import com.project.social_network.model.User;

@Component
public class LikeConverter {

  private final UserConverter userConverter;
  private final PostConverter postConverter;

  public LikeConverter(UserConverter userConverter, PostConverter postConverter) {
    this.userConverter = userConverter;
    this.postConverter = postConverter;
  }

  public LikeDto toLikeDto(Like like, User reqUser) {
    if (like == null || reqUser == null) {
      return null;
    }

    LikeDto likeDto = new LikeDto();
    likeDto.setId(like.getId());
    likeDto.setUser(userConverter.toUserDto(like.getUser()));
    likeDto.setPost(postConverter.toPostDto(like.getPost(), reqUser));

    return likeDto;
  }

  public List<LikeDto> toLikeDtos(List<Like> likes, User reqUser) {
    if (likes == null || likes.isEmpty() || reqUser == null) {
      return Collections.emptyList();
    }

    return likes.stream()
        .map(like -> toLikeDto(like, reqUser))
        .filter(likeDto -> likeDto != null)
        .collect(Collectors.toList());
  }
}