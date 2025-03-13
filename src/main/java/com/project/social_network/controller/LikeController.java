package com.project.social_network.controller;

import com.project.social_network.converter.LikeConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.dto.response.LikeDto;
import com.project.social_network.entity.Like;
import com.project.social_network.entity.User;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.service.interfaces.LikeService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like Controller")
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
  @Operation(summary = "Like a post", description = "Allows a user to like a post using their JWT token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Post liked successfully"),
      @ApiResponse(responseCode = "400", description = "Like post failed"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
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
  @Operation(summary = "Get all likes for a post", description = "Retrieve all users who liked a specific post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Likes retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve likes"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
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
