package com.project.social_network.controllers;

import com.project.social_network.converter.LikeConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.LikeDto;
import com.project.social_network.models.entities.Like;
import com.project.social_network.models.entities.User;
import com.project.social_network.services.interfaces.LikeService;
import com.project.social_network.services.interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<LikeDto> likePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    Like like = likeService.likePost(postId, user);

    LikeDto likeDto = likeConverter.toLikeDto(like, user);

    return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
  }

  @PostMapping("/post/{postId}")
  public ResponseEntity<List<LikeDto>> getAllPosts(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    List<Like> likes = likeService.getAllLikes(postId);

    List<LikeDto> likeDtos = likeConverter.toLikeDtos(likes, user);

    return new ResponseEntity<>(likeDtos, HttpStatus.OK);
  }
}
