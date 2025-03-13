package com.project.social_network.converter;

import com.project.social_network.dto.response.LikeDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Like;
import com.project.social_network.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {

  @Autowired
  private UserConverter userConverter;
  @Autowired
  private PostConverter postConverter;

  public LikeDto toLikeDto(Like like, User reqUser) {
    UserDto user = userConverter.toUserDto(like.getUser());
    UserDto reqUserDto = userConverter.toUserDto(reqUser);
    PostDto post = postConverter.toPostDto(like.getPost(), reqUser);

    LikeDto likeDto = new LikeDto();
    likeDto.setId(like.getId());
    likeDto.setPost(post);
    likeDto.setUser(user);

    return likeDto;

  }

  public List<LikeDto> toLikeDtos(List<Like> likes, User reqUser) {
    List<LikeDto> likeDtos = new ArrayList<>();

    for(Like like : likes) {
      UserDto user = userConverter.toUserDto(like.getUser());
      PostDto post = postConverter.toPostDto(like.getPost(), reqUser);

      LikeDto likeDto = new LikeDto();
      likeDto.setId(like.getId());
      likeDto.setPost(post);
      likeDto.setUser(user);
      likeDtos.add(likeDto);
    }

    return likeDtos;
  }
}
