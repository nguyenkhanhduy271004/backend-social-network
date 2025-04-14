package com.project.social_network.converter;

import com.project.social_network.dto.response.LikeDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Like;
import com.project.social_network.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeConverter {

  private final UserConverter userConverter;
  private final PostConverter postConverter;

  @Autowired
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