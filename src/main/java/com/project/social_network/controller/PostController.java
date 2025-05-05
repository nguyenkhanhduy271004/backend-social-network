package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.CommentDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.exceptions.CommentException;
import com.project.social_network.exceptions.PostException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Comment;
import com.project.social_network.model.Group;
import com.project.social_network.model.JoinRequest;
import com.project.social_network.model.User;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.repository.JoinRequestRepository;
import com.project.social_network.request.CommentRequest;
import com.project.social_network.request.PostReplyRequest;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.interfaces.CommentService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/posts")
@Tag(name = "Post Controller")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class PostController {

  private final PostService postService;
  private final UserService userService;
  private final CommentService commentService;
  private final PostConverter postConverter;
  private final GroupRepository groupRepository;
  private final JoinRequestRepository joinRequestRepository;

  //POST CRUD

  @PostMapping("/create")
  @Operation(summary = "Create new post", description = "Allows users to create a new post with optional image")
  public ResponseEntity<ResponseData<PostDto>> createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {

    validateFile(file);
    PostDto postDto = postService.createPost(content, file, jwt);
    return okResponse("post.create.success", postDto);
  }

  @PostMapping("/{groupId}")
  @Operation(summary = "Create post for group", description = "Allows users to post in a group")
  public ResponseEntity<ResponseData<PostDto>> createPostForGroup(
      @PathVariable @Min(1) Long groupId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    validateFile(file);
    PostDto postDto = postService.createPostForGroup(content, file, jwt, groupId);
    return okResponse("post.create.success", postDto);
  }

  @PutMapping("/{postId}/edit")
  @Operation(summary = "Edit post", description = "Edit a previously created post")
  public ResponseEntity<ResponseData<PostDto>> editPost(
      @PathVariable @Min(1) Long postId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {

    validateFile(file);
    PostDto updatedPost = postService.updatePost(postId, file, content, jwt);
    return okResponse("post.edit.success", updatedPost);
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "Delete post", description = "Delete a post by its ID")
  public ResponseEntity<Void> deletePost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    postService.deletePostById(postId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Find post by ID", description = "Retrieve post details by post ID")
  public ResponseEntity<ResponseData<PostDto>> findPostById(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    PostDto postDto = postService.findById(postId);
    return okResponse("post.find.success", postDto);
  }

  @GetMapping
  @Operation(summary = "Get all posts", description = "Retrieve all public posts")
  public ResponseEntity<ResponseData<List<PostDto>>> getAllPosts() throws UserException, PostException {

    List<PostDto> postDtos = postService.findAllPost();
    return okResponse("post.get.all.success", postDtos);
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Get user's posts", description = "Retrieve all posts of a specific user")
  public ResponseEntity<ResponseData<List<PostDto>>> getUsersAllPosts(
      @PathVariable @Min(1) Long userId) throws UserException, PostException {

    User user = userService.findUserById(userId);
    List<PostDto> postDtos = postService.getUserPost(user);
    return okResponse("post.get.user.success", postDtos);
  }

  @GetMapping("/user/{userId}/likes")
  @Operation(summary = "Get posts liked by user", description = "Retrieve posts liked by a user")
  public ResponseEntity<ResponseData<List<PostDto>>> getLikedPosts(
      @PathVariable @Min(1) Long userId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    List<PostDto> postDtos = postService.findByLikesContainsUser(user);
    return okResponse("post.get.liked.success", postDtos);
  }

  //REPLY / REPOST

  @PostMapping("/reply")
  @Operation(summary = "Reply to a post", description = "Reply to an existing post")
  public ResponseEntity<ResponseData<PostDto>> replyPost(
      @Valid @RequestBody PostReplyRequest req,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    PostDto postDto = postService.createdReply(req, user);
    return okResponse("post.reply.success", postDto);
  }

  @PutMapping("/{postId}/repost")
  @Operation(summary = "Repost", description = "Repost an existing post")
  public ResponseEntity<ResponseData<PostDto>> repost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    PostDto postDto = postService.rePost(postId, user);
    return okResponse("post.repost.success", postDto);
  }

  @GetMapping("/repost")
  @Operation(summary = "Get reposted posts", description = "Retrieve reposts by the current user")
  public ResponseEntity<ResponseData<List<PostDto>>> getRepostedPosts(
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    List<PostDto> postDtos = postService.getRepostedPostsByUserId(user.getId());
    return okResponse("post.get.reposted.success", postDtos);
  }

  //COMMENT

  @GetMapping("/{postId}/comment")
  @Operation(summary = "Get comments", description = "Retrieve all comments of a post")
  public ResponseEntity<ResponseData<List<CommentDto>>> getAllCommentsByPostId(
      @PathVariable @Min(1) Long postId) throws UserException, PostException {

    List<Comment> comments = postService.getAllCommentsByPostId(postId);
    List<CommentDto> commentDtos = comments.stream()
        .map(postConverter::toCommentDto)
        .collect(Collectors.toList());
    return okResponse("comment.get.all.success", commentDtos);
  }

  @PostMapping("/{postId}/comment")
  @Operation(summary = "Add comment", description = "Add a comment to a post")
  public ResponseEntity<ResponseData<PostDto>> createComment(
      @PathVariable @Min(1) Long postId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = getUserFromJwt(jwt);
    PostDto postDto = postService.createComment(commentRequest, user);
    return okResponse("comment.create.success", postDto);
  }

  @PutMapping("/{commentId}/comment")
  @Operation(summary = "Edit comment", description = "Edit an existing comment")
  public ResponseEntity<ResponseData<CommentDto>> editComment(
      @PathVariable @Min(1) Long commentId,
      @Valid @RequestBody CommentRequest commentRequest,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {

    User user = getUserFromJwt(jwt);
    Comment comment = commentService.editComment(commentRequest, user);
    CommentDto commentDto = postConverter.toCommentDto(comment);
    return okResponse("comment.edit.success", commentDto);
  }

  @DeleteMapping("/{commentId}/comment")
  @Operation(summary = "Delete comment", description = "Delete a comment by its ID")
  public ResponseEntity<Void> deleteComment(
      @PathVariable @Min(1) Long commentId,
      @RequestHeader("Authorization") String jwt) throws UserException, CommentException {

    User user = getUserFromJwt(jwt);
    commentService.deleteCommentById(commentId, user);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{groupId}/request-join")
  public ResponseEntity<?> requestToJoinGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User user = getUserFromJwt(jwt);
    Group group = groupRepository.findById(groupId).orElseThrow();
    if (group.isPublic()) {
      group.getUsers().add(user);
      groupRepository.save(group);
      return ResponseEntity.ok("Joined group directly.");
    }

    if (joinRequestRepository.findByGroupAndUser(group, user).isPresent()) {
      return ResponseEntity.badRequest().body("You already sent a join request.");
    }

    JoinRequest joinRequest = new JoinRequest();
    joinRequest.setGroup(group);
    joinRequest.setUser(user);
    joinRequest.setPending(true);
    joinRequestRepository.save(joinRequest);

    return ResponseEntity.ok("Join request sent. Wait for approval.");
  }

  @PostMapping("/{groupId}/approve-request/{requestId}")
  public ResponseEntity<?> approveJoinRequest(
      @PathVariable Long groupId,
      @PathVariable Long requestId,
      @RequestParam boolean approve,
      @RequestHeader("Authorization") String jwt
  ) {
    User admin = getUserFromJwt(jwt);
    Group group = groupRepository.findById(groupId).orElseThrow();

    if (!group.getAdmin().equals(admin)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized.");
    }

    JoinRequest request = joinRequestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found"));

    if (!request.getGroup().getId().equals(groupId)) {
      return ResponseEntity.badRequest().body("Request not for this group.");
    }

    if (!request.isPending()) {
      return ResponseEntity.badRequest().body("Request already handled.");
    }

    if (approve) {
      group.getUsers().add(request.getUser());
      groupRepository.save(group);
      request.setApproved(true);
    } else {
      request.setApproved(false);
    }

    request.setPending(false);
    joinRequestRepository.save(request);

    return ResponseEntity.ok("Request has been " + (approve ? "approved." : "rejected."));
  }



  private void validateFile(MultipartFile file) {
    if (file != null) {
      if (file.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException("File size exceeds limit (5MB)");
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("Only image files are allowed");
      }
    }
  }

  private User getUserFromJwt(String jwt) throws UserException {
    return userService.findUserProfileByJwt(jwt);
  }

  private <T> ResponseEntity<ResponseData<T>> okResponse(String messageKey, T data) {
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale(messageKey), data));
  }
}
