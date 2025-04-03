package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.request.CommentRequest;
import com.project.social_network.dto.request.PostReplyRequest;
import com.project.social_network.dto.response.CommentDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.entity.Comment;
import com.project.social_network.entity.User;
import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.service.interfaces.CommentService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Controller")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private PostConverter postConverter;


  @PostMapping("/create")
  @Operation(summary = "Create post", description = "API create new post")
  public ResponseEntity<?> createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {


    try {
      PostDto postDto = postService.createPost(content, file, jwt);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.create.success"), postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
  }

  @PostMapping("/{groupId}")
  @Operation(summary = "Create post for group", description = "API to create a new post in a specific group")
  public ResponseEntity<?> createPostForGroup(
      @PathVariable Long groupId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      PostDto postDto = postService.createPostForGroup(content, file, jwt, groupId);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
          Translator.toLocale("post.create.success"), postDto));
    } catch (UserException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    } catch (PostException e) {
      return ResponseEntity.badRequest()
          .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
  }


  @PutMapping("/{postId}/edit")
  @Operation(summary = "Edit post", description = "API edit existing post")
  public ResponseEntity<?> editPost(
      @PathVariable @Min(1) Long postId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {

    try {
      PostDto updatedPost = postService.updatePost(postId, file, content, jwt);
      return ResponseEntity.accepted().body(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Edit post successfully", updatedPost));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Edit post failed"));
    }
  }

  @PostMapping("/reply")
  @Operation(summary = "Reply to post", description = "API create reply to post")
  public ResponseEntity<?> replyPost(
      @RequestBody PostReplyRequest req,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      PostDto postDto = postService.createdReply(req, user);
      return ResponseEntity.accepted().body(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Reply post successfully", postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Reply post failed"));
    }
  }

  @PutMapping("/{postId}/repost")
  @Operation(summary = "Repost", description = "API repost a post")
  public ResponseEntity<?> repost(
      @PathVariable Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      PostDto postDto = postService.rePost(postId, user);
      return ResponseEntity.accepted().body(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Repost post successfully", postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Repost post failed"));
    }
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Find post by ID", description = "API find post by ID")
  public ResponseEntity<?> findPostById(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      PostDto postDto = postService.findById(postId);
      return ResponseEntity.accepted().body(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Find post by id: " + postId, postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Find post by id: " + postId + " failed"));
    }
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "Delete post", description = "API delete post by ID")
  public ResponseEntity<?> deletePost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      postService.deletePostById(postId, user.getId());
      return ResponseEntity.noContent().build();
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Post deleted failed"));
    }
  }

  @GetMapping("/")
  @Operation(summary = "Get all posts", description = "API get all posts")
  public ResponseEntity<?> getAllPosts(
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<PostDto> postDtos = postService.findAllPost();
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get all posts successfully", postDtos));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all posts failed"));
    }
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Get user's posts", description = "API get all posts by user ID")
  public ResponseEntity<?> getUsersAllPosts(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      User postUser = userService.findUserById(userId);
      List<PostDto> postDtos = postService.getUserPost(postUser);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get all user posts successfully", postDtos));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all user posts failed"));
    }
  }

  @GetMapping("/user/{userId}/likes")
  @Operation(summary = "Find posts liked by user", description = "API find posts liked by user")
  public ResponseEntity<?> findPostByLikesContainsUser(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<PostDto> postDtos = postService.findByLikesContainsUser(user);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get all liked posts successfully", postDtos));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get liked posts failed"));
    }
  }

  @GetMapping("/{postId}/comment")
  @Operation(summary = "Get comments by post ID", description = "API get all comments by post ID")
  public ResponseEntity<?> getAllCommentsByPostId(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<Comment> comments = postService.getAllCommentsByPostId(postId);
      List<CommentDto> commentDtos = new ArrayList<>();
      for (Comment comment : comments) {
        CommentDto commentDto = postConverter.toCommentDto(comment);
        commentDtos.add(commentDto);
      }
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get all comments by post successfully", commentDtos));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all comments by post failed"));
    }
  }

  @PostMapping("/{postId}/comment")
  @Operation(summary = "Create comment", description = "API create comment on post")
  public ResponseEntity<?> createComment(
      @PathVariable @Min(1) Long postId,
      @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      PostDto postDto = postService.createComment(commentRequest, user);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Create comment successfully", postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create comment failed"));
    }
  }

  @DeleteMapping("/{commentId}/comment")
  @Operation(summary = "Delete comment", description = "API delete comment by ID")
  public ResponseEntity<?> deleteComment(
      @PathVariable @Min(1) Long commentId,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      commentService.deleteCommentById(commentId, user);
      return ResponseEntity.noContent().build();
    } catch (CommentException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete comment failed"));
    }
  }

  @PutMapping("/{commentId}/comment")
  @Operation(summary = "Edit comment", description = "API edit comment by ID")
  public ResponseEntity<?> editComment(
      @PathVariable @Min(1) Long commentId,
      @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      Comment comment = commentService.editComment(commentRequest, user);
      return ResponseEntity.noContent().build();
    } catch (CommentException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Edit comment failed"));
    }
  }

  @GetMapping("/repost")
  @Operation(summary = "Get reposted posts", description = "API get reposted posts by user")
  public ResponseEntity<?> getRepostedPosts(
      @RequestHeader("Authorization") String jwt) throws UserException {

    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<PostDto> postDtos = postService.getRepostedPostsByUserId(user.getId());
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get reposted posts successfully", postDtos));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get reposted posts failed"));
    }
  }
}