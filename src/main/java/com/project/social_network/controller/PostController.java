package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.exception.CommentException;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.dto.CommentDto;
import com.project.social_network.model.dto.PostDto;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.Post;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.request.CommentRequest;
import com.project.social_network.model.request.PostReplyRequest;
import com.project.social_network.model.response.ApiResponse;
import com.project.social_network.model.response.ResponseData;
import com.project.social_network.model.response.ResponseError;
import com.project.social_network.service.interfaces.CommentService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.ApiResponseUtils;
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
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private PostConverter postConverter;

  @Autowired
  private UploadImageFile uploadImageFile;


  @PostMapping("/create")
  public Object createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {

    User user = userService.findUserProfileByJwt(jwt);

    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Post req = new Post();
    req.setContent(content);
    req.setImage(imageFileUrl);

    try {
      Post post = postService.createPost(req, user);
      PostDto postDto = postConverter.toPostDto(post, user);
      return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.create.success"), postDto);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
  }

  @PutMapping("/{postId}/edit")
  public Object editPost(
      @PathVariable @Min(1) Long postId,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, PostException, IOException {

    User user = userService.findUserProfileByJwt(jwt);

    Post post = postService.findById(postId);

    if (!post.getUser().getId().equals(user.getId())) {
      throw new PostException("You do not have permission to edit this post.");
    }

    String imageFileUrl = post.getImage();
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    post.setContent(content);
    post.setImage(imageFileUrl);


    try {
      Post updatedPost = postService.updatePost(post);
      PostDto postDto = postConverter.toPostDto(updatedPost, user);

      return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Edit post successfully", postDto);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Edit post failed");
    }
  }



  @PostMapping("/reply")
  public Object replyPost(@RequestBody PostReplyRequest req, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);


    try {
      Post post = postService.createdReply(req, user);

      PostDto postDto = postConverter.toPostDto(post, user);

      return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Reply post successfully", postDto);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Reply post failed");
    }

  }

  @PutMapping("/{postId}/repost")
  public Object repost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

   try {
     Post post = postService.rePost(postId, user);

     PostDto postDto = postConverter.toPostDto(post, user);

     return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Repost post successfully", postDto);
   } catch (PostException e) {

     return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Repost post failed");
   }

  }

  @GetMapping("/{postId}")
  public Object findPostById(@PathVariable @Min(1) Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      Post post = postService.findById(postId);

      PostDto postDto = postConverter.toPostDto(post, user);

      return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Find post by id: " + postId, postDto);

    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Find post by id: " + postId + " failed");

    }

  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<ApiResponse<Object>> deletePost(
      @PathVariable @Min(1) Long postId,
      @RequestHeader("Authorization") String jwt
  ) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    postService.deletePostById(postId, user.getId());

    ApiResponse<Object> response = ApiResponseUtils.success("Post deleted successfully", null);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  

  @GetMapping("/")
  public ResponseData<List<PostDto>> getAllPosts(@RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    List<Post> post = postService.findAllPost();

    List<PostDto> postDtos = postConverter.toPostDtos(post, user);

    return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.get.success"), postDtos);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PostDto>> getUsersAllPosts(@PathVariable @Min(1) Long userId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    User postUser = userService.findUserById(userId);

    List<Post> post = postService.getUserPost(postUser);

    List<PostDto> postDtos = postConverter.toPostDtos(post, user);

    return new ResponseEntity<>(postDtos, HttpStatus.OK);
  }

  @GetMapping("/user/{userId}/likes")
  public Object findPostByLikesContainesUser(@RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      List<Post> post = postService.findByLikesContainsUser(user);

      List<PostDto> postDtos = postConverter.toPostDtos(post, user);

      return new ResponseData<>(HttpStatus.OK.value(), "Get all post for user successfully", postDtos);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get post for user failed");
    }

  }

  @GetMapping("/{postId}/comment")
  public Object getAllCommentsByPostId(@PathVariable @Min(1) Long postId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<Comment> comments = postService.getAllCommentsByPostId(postId);
      List<CommentDto> commentDtos = new ArrayList<>();
      for(Comment comment:comments) {
        CommentDto commentDto = postConverter.toCommentDto(comment);
        commentDtos.add(commentDto);
      }

      return new ResponseData<>(HttpStatus.OK.value(), "Get all comments by post successfully", commentDtos);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all comments by post failed");
    }
  }

  @PostMapping("/{postId}/comment")
  public Object createComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);
    try {
      Post post = postService.createComment(commentRequest, user);
      PostDto postDto = postConverter.toPostDto(post, post.getUser());

      return new ResponseData<>(HttpStatus.OK.value(), "Create comment successfully", postDto);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create comment failed");
    }

  }

  @DeleteMapping("/{commentId}/comment")
  public Object deleteComment(@PathVariable @Min(1) Long commentId, @RequestHeader("Authorization") String jwt) throws UserException, CommentException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      commentService.deleteCommentById(commentId, user);

      return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete comment successfully");
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete comment failed");
    }
  }

  @PutMapping("/{commentId}/comment")
  public Object editComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String jwt) throws UserException, CommentException {
    User user = userService.findUserProfileByJwt(jwt);

    try {
      Comment comment = commentService.editComment(commentRequest, user);

      return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Edit comment successfully");
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Edit comment failed");
    }
  }

  @GetMapping("/repost")
  public Object getRepostedPosts(@RequestHeader("Authorization") String jwt) throws UserException{
    User user = userService.findUserProfileByJwt(jwt);
    try {
      List<Post> posts = postService.getRepostedPostsByUserId(user.getId());
      List<PostDto> postDtos = new ArrayList<>();
      for (Post post:posts) {
        PostDto postDto = postConverter.toPostDto(post, user);
        postDtos.add(postDto);
      }

      return new ResponseData<>(HttpStatus.OK.value(), "Get repost for user successfully", postDtos);
    } catch (PostException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get repost for user failed");
    }
  }

}