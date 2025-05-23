package com.project.social_network.controller;

import com.project.social_network.constant.JobQueue;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.CommentDto;
import com.project.social_network.dto.NotificationMessage;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Comment;
import com.project.social_network.model.User;
import com.project.social_network.request.CommentRequest;
import com.project.social_network.request.PostReplyRequest;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.interfaces.CommentService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/posts")
@Tag(name = "Post Controller")
@SecurityRequirement(name = "bearerAuth")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {

  PostService postService;
  UserService userService;
  CommentService commentService;
  PostConverter postConverter;
  FileUtil fileUtil;
  RabbitTemplate rabbitTemplate;


  @PostMapping("")
  @Operation(summary = "Create new post", description = "Allows users to create a new post with optional image")
  ResponseEntity<ResponseData<PostDto>> createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam @NotBlank(message = "Content cannot be empty") @Size(max = 1000, message = "Content must be less than 1000 characters") String content,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    fileUtil.validateFile(file);
    PostDto postDto = postService.createPost(content, file, jwt);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.CREATED.value(), "post.create.success", postDto));
  }

  @PostMapping("/{groupId}")
  @Operation(summary = "Create post for group", description = "Allows users to post in a group")
  ResponseEntity<ResponseData<PostDto>> createPostForGroup(
      @PathVariable @Min(1) Long groupId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam @NotBlank(message = "Content cannot be empty") @Size(max = 1000, message = "Content must be less than 1000 characters") String content,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    fileUtil.validateFile(file);
    PostDto postDto = postService.createPostForGroup(content, file, jwt, groupId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.create.success", postDto));
  }

  @PutMapping("/{postId}/edit")
  @Operation(summary = "Edit post", description = "Edit a previously created post")
  ResponseEntity<ResponseData<PostDto>> editPost(
      @PathVariable @Min(1) Long postId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam @NotBlank(message = "Content cannot be empty") @Size(max = 1000, message = "Content must be less than 1000 characters") String content,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    fileUtil.validateFile(file);
    PostDto updatedPost = postService.updatePost(postId, file, content, jwt);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.edit.success", updatedPost));
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "Delete post", description = "Delete a post by its ID")
  ResponseEntity<Void> deletePost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    postService.deletePostById(postId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Find post by ID", description = "Retrieve post details by post ID")
  ResponseEntity<ResponseData<PostDto>> findPostById(
      @PathVariable @Min(1) Long postId) {

    PostDto postDto = postService.findById(postId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.find.success", postDto));
  }

  @GetMapping
  @Operation(summary = "Get all posts", description = "Retrieve all public posts")
  ResponseEntity<ResponseData<List<PostDto>>> getAllPosts() {

    List<PostDto> postDtos = postService.findAllPost();
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.find.success", postDtos));
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Get user's posts", description = "Retrieve all posts of a specific user")
  ResponseEntity<ResponseData<List<PostDto>>> getUsersAllPosts(@PathVariable @Min(1) Long userId) {

    User user = userService.findUserById(userId);
    List<PostDto> postDtos = postService.getUserPost(user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.find.success", postDtos));
  }

  @GetMapping("/user/{userId}/likes")
  @Operation(summary = "Get posts liked by user", description = "Retrieve posts liked by a user")
  ResponseEntity<ResponseData<List<PostDto>>> getLikedPosts(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    List<PostDto> postDtos = postService.findByLikesContainsUser(user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.get.liked.success", postDtos));
  }

  @PostMapping("/reply")
  @Operation(summary = "Reply to a post", description = "Reply to an existing post")
  ResponseEntity<ResponseData<PostDto>> replyPost(
      @Valid @RequestBody PostReplyRequest req,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.createdReply(req, user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.reply.success", postDto));
  }

  @PutMapping("/{postId}/repost")
  @Operation(summary = "Repost", description = "Repost an existing post")
  ResponseEntity<ResponseData<PostDto>> repost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.rePost(postId, user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.repost.success", postDto));
  }

  @GetMapping("/repost")
  @Operation(summary = "Get reposted posts", description = "Retrieve reposts by the current user")
  ResponseEntity<ResponseData<List<PostDto>>> getRepostedPosts(
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    List<PostDto> postDtos = postService.getRepostedPostsByUserId(user.getId());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.get.repost.success", postDtos));
  }

  @GetMapping("/{postId}/comment")
  @Operation(summary = "Get comments", description = "Retrieve all comments of a post")
  ResponseEntity<ResponseData<List<CommentDto>>> getAllCommentsByPostId(
      @PathVariable @Min(1) Long postId) {

    List<Comment> comments = postService.getAllCommentsByPostId(postId);
    List<CommentDto> commentDtos = comments.stream()
        .map(postConverter::toCommentDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "post.get.comment.success", commentDtos));
  }

  @PostMapping("/{postId}/comment")
  @Operation(summary = "Add comment", description = "Add a comment to a post")
  ResponseEntity<ResponseData<PostDto>> createComment(
      @PathVariable @Min(1) Long postId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.createComment(commentRequest, user);

    String message = "User " + user.getFullName() + " commented on your post: " + postId;

    if (!user.getId().equals(postDto.getUser().getId())) {
      NotificationMessage request = new NotificationMessage();
      request.setUserId(postDto.getUser().getId());
      request.setPostId(postId);
      request.setMessage(message);
      try {
        rabbitTemplate.convertAndSend(JobQueue.QUEUE_DEV, request);
      } catch (Exception e) {
        log.error("Failed to send notification: {}", e.getMessage());
      }
    }

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "comment.create.success", postDto));
  }

  @PutMapping("/{commentId}/comment")
  @Operation(summary = "Edit comment", description = "Edit an existing comment")
  ResponseEntity<ResponseData<CommentDto>> editComment(
      @PathVariable @Min(1) Long commentId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    Comment comment = commentService.editComment(commentRequest, user);
    CommentDto commentDto = postConverter.toCommentDto(comment);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "comment.edit.success", commentDto));
  }

  @DeleteMapping("/{commentId}/comment")
  @Operation(summary = "Delete comment", description = "Delete a comment by its ID")
  ResponseEntity<Void> deleteComment(
      @PathVariable @Min(1) Long commentId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    commentService.deleteCommentById(commentId, user);
    return ResponseEntity.noContent().build();
  }
}