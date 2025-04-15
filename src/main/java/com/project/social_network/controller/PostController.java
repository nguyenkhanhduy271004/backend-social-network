package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.model.dto.request.CommentRequest;
import com.project.social_network.model.dto.request.PostReplyRequest;
import com.project.social_network.model.dto.CommentDto;
import com.project.social_network.model.dto.PostDto;
import com.project.social_network.model.dto.response.ResponseData;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.service.interfaces.CommentService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Controller")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class PostController {

  private final PostService postService;
  private final UserService userService;
  private final CommentService commentService;
  private final PostConverter postConverter;

  public PostController(PostService postService, UserService userService,
      CommentService commentService, PostConverter postConverter) {
    this.postService = postService;
    this.userService = userService;
    this.commentService = commentService;
    this.postConverter = postConverter;
  }

  @PostMapping("/create")
  @Operation(summary = "Create post", description = "API create new post")
  public ResponseEntity<ResponseData<PostDto>> createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {
    validateFile(file);
    PostDto postDto = postService.createPost(content, file, jwt);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.create.success"), postDto));
  }

  @PostMapping("/{groupId}")
  @Operation(summary = "Create post for group", description = "API to create a new post in a specific group")
  public ResponseEntity<ResponseData<PostDto>> createPostForGroup(
      @PathVariable @Min(1) Long groupId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    PostDto postDto = postService.createPostForGroup(content, file, jwt, groupId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.create.success"), postDto));
  }

  @PutMapping("/{postId}/edit")
  @Operation(summary = "Edit post", description = "API edit existing post")
  public ResponseEntity<ResponseData<PostDto>> editPost(
      @PathVariable @Min(1) Long postId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {
    PostDto updatedPost = postService.updatePost(postId, file, content, jwt);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.edit.success"), updatedPost));
  }

  @PostMapping("/reply")
  @Operation(summary = "Reply to post", description = "API create reply to post")
  public ResponseEntity<ResponseData<PostDto>> replyPost(
      @Valid @RequestBody PostReplyRequest req,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.createdReply(req, user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.reply.success"), postDto));
  }

  @PutMapping("/{postId}/repost")
  @Operation(summary = "Repost", description = "API repost a post")
  public ResponseEntity<ResponseData<PostDto>> repost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.rePost(postId, user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.repost.success"), postDto));
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Find post by ID", description = "API find post by ID")
  public ResponseEntity<ResponseData<PostDto>> findPostById(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    PostDto postDto = postService.findById(postId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.find.success"), postDto));
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "Delete post", description = "API delete post by ID")
  public ResponseEntity<Void> deletePost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    postService.deletePostById(postId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all posts", description = "API get all posts")
  public ResponseEntity<ResponseData<List<PostDto>>> getAllPosts(
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    List<PostDto> postDtos = postService.findAllPost();
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.get.all.success"), postDtos));
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Get user's posts", description = "API get all posts by user ID")
  public ResponseEntity<ResponseData<List<PostDto>>> getUsersAllPosts(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User postUser = userService.findUserById(userId);
    List<PostDto> postDtos = postService.getUserPost(postUser);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.get.user.success"), postDtos));
  }

  @GetMapping("/user/{userId}/likes")
  @Operation(summary = "Find posts liked by user", description = "API find posts liked by user")
  public ResponseEntity<ResponseData<List<PostDto>>> findPostByLikesContainsUser(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    List<PostDto> postDtos = postService.findByLikesContainsUser(user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.get.liked.success"), postDtos));
  }

  @GetMapping("/{postId}/comment")
  @Operation(summary = "Get comments by post ID", description = "API get all comments by post ID")
  public ResponseEntity<ResponseData<List<CommentDto>>> getAllCommentsByPostId(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    List<Comment> comments = postService.getAllCommentsByPostId(postId);
    List<CommentDto> commentDtos = comments.stream()
        .map(postConverter::toCommentDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("comment.get.all.success"), commentDtos));
  }

  @PostMapping("/{postId}/comment")
  @Operation(summary = "Create comment", description = "API create comment on post")
  public ResponseEntity<ResponseData<PostDto>> createComment(
      @PathVariable @Min(1) Long postId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    PostDto postDto = postService.createComment(commentRequest, user);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("comment.create.success"), postDto));
  }

  @DeleteMapping("/{commentId}/comment")
  @Operation(summary = "Delete comment", description = "API delete comment by ID")
  public ResponseEntity<Void> deleteComment(
      @PathVariable @Min(1) Long commentId,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {
    User user = userService.findUserProfileByJwt(jwt);
    commentService.deleteCommentById(commentId, user);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{commentId}/comment")
  @Operation(summary = "Edit comment", description = "API edit comment by ID")
  public ResponseEntity<ResponseData<CommentDto>> editComment(
      @PathVariable @Min(1) Long commentId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {
    User user = userService.findUserProfileByJwt(jwt);
    Comment comment = commentService.editComment(commentRequest, user);
    CommentDto commentDto = postConverter.toCommentDto(comment);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("comment.edit.success"), commentDto));
  }

  @GetMapping("/repost")
  @Operation(summary = "Get reposted posts", description = "API get reposted posts by user")
  public ResponseEntity<ResponseData<List<PostDto>>> getRepostedPosts(
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    List<PostDto> postDtos = postService.getRepostedPostsByUserId(user.getId());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.get.reposted.success"), postDtos));
  }
  private void validateFile(MultipartFile file) {
    if (file != null) {
      if (file.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException("File size exceeds limit");
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("Only image files are allowed");
      }
    }
  }

}