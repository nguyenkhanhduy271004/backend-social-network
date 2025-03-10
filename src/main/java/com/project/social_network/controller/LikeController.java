package com.project.social_network.controller;

import com.project.social_network.converter.LikeConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.dto.LikeDto;
import com.project.social_network.model.entity.Like;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.response.ResponseData;
import com.project.social_network.model.response.ResponseError;
import com.project.social_network.service.interfaces.LikeService;
import com.project.social_network.service.interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LikeController {

  @Autowired
  private UserService userService;
  @Autowired
  private LikeService likeService;
  @Autowired
  private LikeConverter likeConverter;

  @PostMapping("/{postId}/likes")
  public Object likePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      Like like = likeService.likePost(postId, user);

      LikeDto likeDto = likeConverter.toLikeDto(like, user);

      return new ResponseData<>(HttpStatus.CREATED.value(), "Like post successfully", likeDto);

    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Like post failed");
    }


  }

  @PostMapping("/post/{postId}")
  public Object getAllPostsIsLiked(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      List<Like> likes = likeService.getAllLikes(postId);

      List<LikeDto> likeDtos = likeConverter.toLikeDtos(likes, user);

      return new ResponseData<>(HttpStatus.OK.value(), "Get all posts is liked successfully", likeDtos);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all posts is liked failed");

    }

  }
}
