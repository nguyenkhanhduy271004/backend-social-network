package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.LikeConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.dto.LikeDto;
import com.project.social_network.response.ResponseData;
import com.project.social_network.model.Like;
import com.project.social_network.model.User;
import com.project.social_network.service.interfaces.LikeService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Like Controller", description = "APIs for liking and retrieving likes on posts")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Validated
public class LikeController {

  private final UserService userService;
  private final LikeService likeService;
  private final LikeConverter likeConverter;
  private final Translator translator;

  @PostMapping("/{postId}/likes")
  @Operation(summary = "Like a post", description = "Allows a user to like a post using their JWT token")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Post liked successfully"),
      @ApiResponse(responseCode = "400", description = "Like post failed"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<ResponseData<LikeDto>> likePost(
      @Parameter(description = "ID of the post to like") @PathVariable @Min(1) Long postId,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    Like like = likeService.likePost(postId, user);
    LikeDto likeDto = likeConverter.toLikeDto(like, user);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(),
            translator.toLocale("like.create.success"), likeDto));
  }

  @GetMapping("/{postId}/likes")
  @Operation(summary = "Get all likes for a post", description = "Retrieve all users who liked a specific post")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Likes retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve likes"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<ResponseData<List<LikeDto>>> getAllLikesForPost(
      @Parameter(description = "ID of the post to retrieve likes") @PathVariable @Min(1) Long postId,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    List<Like> likes = likeService.getAllLikes(postId);
    List<LikeDto> likeDtos = likeConverter.toLikeDtos(likes, user);

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        translator.toLocale("like.get.all.success"), likeDtos));
  }
}
