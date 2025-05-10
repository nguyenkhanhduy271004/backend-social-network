package com.project.social_network.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Comment;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.request.CommentRequest;
import com.project.social_network.request.PostReplyRequest;

public interface PostService {
  PostDto createPost(String content, MultipartFile file, String jwt);

  PostDto createPostForGroup(String content, MultipartFile file, String jwt, Long groupId);

  List<PostDto> findAllPost();

  Page<PostDto> findAllPost(Pageable pageable);

  PostDto rePost(Long postId, User user);

  PostDto findById(Long postId);

  Post findByPostId(Long postId);

  void deletePostById(Long postId, Long userId);

  PostDto removeFromRePost(Long postId, User user);

  PostDto createdReply(PostReplyRequest req, User user);

  List<PostDto> getUserPost(User user);

  List<PostDto> findByLikesContainsUser(User user);

  PostDto updatePost(Long postId, MultipartFile file, String content, String jwt);

  PostDto createComment(CommentRequest commentRequest, User user);

  List<Comment> getAllCommentsByPostId(Long postId);

  List<PostDto> getRepostedPostsByUserId(Long userId);

  List<Post> findAllPosts();

  void deletePost(Long postId, User admin);
}
