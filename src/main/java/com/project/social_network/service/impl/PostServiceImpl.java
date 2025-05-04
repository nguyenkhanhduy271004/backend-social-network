package com.project.social_network.service.impl;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.request.CommentRequest;
import com.project.social_network.request.PostReplyRequest;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Comment;
import com.project.social_network.model.Group;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.repository.CommentRepository;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.repository.PostRepository;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PostServiceImpl implements PostService {

  @Autowired
  private PostConverter postConverter;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private GroupRepository groupRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private UploadImageFile uploadImageFile;

  @Override
  public PostDto createPost(String content, MultipartFile file, String jwt) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      try {
        imageFileUrl = uploadImageFile.uploadImage(file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    Post req = new Post();
    req.setContent(content);
    req.setImage(imageFileUrl);
    return postConverter.toPostDto(postRepository.save(postConverter.postConverter(req, user)), user);
  }

  @Override
  public PostDto createPostForGroup(String content, MultipartFile file, String jwt, Long groupId) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      try {
        imageFileUrl = uploadImageFile.uploadImage(file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    Post req = new Post();
    req.setContent(content);
    req.setImage(imageFileUrl);
    Post post = postConverter.postConverter(req, user);

    Group group = groupRepository.findById(groupId).orElseThrow(() -> new PostException("Not found group"));
    List<Post> oldPost = group.getPosts();
    oldPost.add(req);
    group.setPosts(oldPost);
    post.setGroup(group);

    return postConverter.toPostDtoForGroup(postRepository.save(post), user, group.getName(), post.getGroup().getId());
  }

  @Override
  public List<PostDto> findAllPost() {
    return postRepository.findAllByIsPostTrueOrderByCreatedAtDesc().stream()
        .map(post -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public Page<PostDto> findAllPost(Pageable pageable) throws PostException {
    try {
      Page<Post> posts = postRepository.findAllByIsPostTrue(pageable);
      return posts.map(post -> postConverter.toPostDto(post, post.getUser()));
    } catch (Exception e) {
      throw new PostException("Failed to retrieve posts: " + e.getMessage());
    }
  }

  @Override
  public PostDto rePost(Long postId, User user) throws UserException, PostException {
    Post post = findByPostId(postId);
    if (post.getRePostUsers().contains(user)) {
      post.getRePostUsers().remove(user);
    } else {
      post.getRePostUsers().add(user);
    }

    return postConverter.toPostDto(postRepository.save(post), post.getUser());
  }

  @Override
  @Cacheable(value = "posts", key = "#postId")
  public PostDto findById(Long postId) throws PostException {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostException("Post not found with id: " + postId));
    return postConverter.toPostDto(post, post.getUser());
  }

  @Override
  @CacheEvict(value = "posts", key = "#postId")
  public void deletePostById(Long postId, Long userId) throws UserException, PostException {
    Post post = findByPostId(postId);

    if (!userId.equals(post.getUser().getId())) {
      throw new UserException("You can't delete another user's post");
    }

    postRepository.deleteById(post.getId());
  }

  @Override
  public PostDto removeFromRePost(Long postId, User user) throws UserException, PostException {
    return null;
  }

  @Override
  public PostDto createdReply(PostReplyRequest req, User user) throws UserException, PostException {
    Post replyFor = findByPostId(req.getPostId());

    Post post = postConverter.postReplyConverter(req, user);
    post.setReplyFor(replyFor);

    Post savedPost = postRepository.save(post);

    replyFor.getReplyPost().add(savedPost);
    postRepository.save(replyFor);

    return postConverter.toPostDto(savedPost, user);
  }

  @Override
  public List<PostDto> getUserPost(User user) {
    return postRepository.findByRePostUsersContainsOrUser_IdAndIsPostTrueOrderByCreatedAtDesc(user, user.getId())
        .stream()
        .map((post) -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public List<PostDto> findByLikesContainsUser(User user) {
    return postRepository.findByLikesUser_id(user.getId())
        .stream()
        .map((post) -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  @CachePut(value = "posts", key = "#postId")
  public PostDto updatePost(Long postId, MultipartFile file, String content, String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    Post post = findByPostId(postId);

    if (!post.getUser().getId().equals(user.getId())) {
      throw new PostException("You do not have permission to edit this post.");
    }

    String imageFileUrl = post.getImage();
    if (file != null && !file.isEmpty()) {
      try {
        imageFileUrl = uploadImageFile.uploadImage(file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    post.setContent(content);
    post.setImage(imageFileUrl);
    return postConverter.toPostDto(postRepository.save(post), post.getUser());
  }

  @Override
  public PostDto createComment(CommentRequest commentRequest, User user) throws UserException, PostException {
    Post post = findByPostId(commentRequest.getPostId());

    Comment comment = new Comment();
    comment.setUser(user);
    comment.setPost(post);
    comment.setContent(commentRequest.getContent());

    post.getComments().add(comment);

    return postConverter.toPostDto(postRepository.save(post), user);
  }

  @Override
  public List<Comment> getAllCommentsByPostId(Long postId) throws UserException, PostException {
    List<Comment> comments = commentRepository.findByPost_Id(postId);
    return commentRepository.findByPost_Id(postId);
  }

  @Override
  public List<PostDto> getRepostedPostsByUserId(Long userId) {
    return postRepository.findRepostedPostsByUserId(userId)
        .stream()
        .map((post) -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public Post findByPostId(Long postId) throws PostException {
    return postRepository.findById(postId)
        .orElseThrow(() -> new PostException("Post not found with id: " + postId));
  }

  @Override
  public List<Post> findAllPosts() {
    return postRepository.findAll();
  }

  @Override
  public void deletePost(Long postId, User admin) throws PostException, UserException {
    if (!admin.isAdmin()) {
      throw new UserException("Unauthorized: Admin access required");
    }

    Post post = findByPostId(postId);
    postRepository.delete(post);
  }
}
