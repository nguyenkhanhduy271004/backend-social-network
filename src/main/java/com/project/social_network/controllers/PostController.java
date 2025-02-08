package com.project.social_network.controllers;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.PostDto;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.PostReplyRequest;
import com.project.social_network.models.responses.ApiResponse;
import com.project.social_network.services.interfaces.PostService;
import com.project.social_network.services.interfaces.UserService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private PostConverter postConverter;

  @PostMapping("/create")
  public ResponseEntity<PostDto> createPost(@RequestBody Post req, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    Post post = postService.createPost(req, user);

    PostDto postDto = postConverter.toPostDto(post, user);

    return new ResponseEntity<>(postDto, HttpStatus.CREATED);
  }

  @PostMapping("/reply")
  public ResponseEntity<PostDto> replyPost(@RequestBody PostReplyRequest req, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    Post post = postService.createdReply(req, user);

    PostDto postDto = postConverter.toPostDto(post, user);

    return new ResponseEntity<>(postDto, HttpStatus.CREATED);
  }

  @PutMapping("/{postId}/repost")
  public ResponseEntity<PostDto> repost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    Post post = postService.rePost(postId, user);

    PostDto postDto = postConverter.toPostDto(post, user);

    return new ResponseEntity<>(postDto, HttpStatus.OK);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> findPostById(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    Post post = postService.findById(postId);

    PostDto postDto = postConverter.toPostDto(post, user);

    return new ResponseEntity<>(postDto, HttpStatus.OK);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    postService.deletePostById(postId, user.getId());

    ApiResponse res = new ApiResponse("Post deleted successfully", true);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/")
  public ResponseEntity<List<PostDto>> getAllPosts(@RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    List<Post> post = postService.findAllPost();

   List<PostDto> postDtos = postConverter.toPostDtos(post, user);

    return new ResponseEntity<>(postDtos, HttpStatus.OK);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PostDto>> getUsersAllPosts(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    List<Post> post = postService.getUserPost(user);

    List<PostDto> postDtos = postConverter.toPostDtos(post, user);

    return new ResponseEntity<>(postDtos, HttpStatus.OK);
  }

  @GetMapping("/user/{userId}/likes")
  public ResponseEntity<List<PostDto>> findPostByLikesContainesUser(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    List<Post> post = postService.findByLikesContainsUser(user);

    List<PostDto> postDtos = postConverter.toPostDtos(post, user);

    return new ResponseEntity<>(postDtos, HttpStatus.OK);
  }
}
