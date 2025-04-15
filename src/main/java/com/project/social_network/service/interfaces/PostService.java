package com.project.social_network.service.interfaces;

import com.project.social_network.model.dto.request.CommentRequest;
import com.project.social_network.model.dto.request.PostReplyRequest;
import com.project.social_network.model.dto.PostDto;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.Post;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
  PostDto createPost(String content, MultipartFile file, String jwt) throws UserException;
  PostDto createPostForGroup(String content, MultipartFile file, String jwt, Long groupId) throws UserException;
  List<PostDto> findAllPost();
  PostDto rePost(Long postId, User user) throws UserException, PostException;
  PostDto findById(Long postId) throws PostException;
  Post findByPostId(Long postId) throws PostException;
  void deletePostById(Long postId, Long userId) throws UserException, PostException;
  PostDto removeFromRePost(Long postId, User user) throws UserException, PostException;
  PostDto createdReply(PostReplyRequest req, User user) throws UserException, PostException;
  List<PostDto> getUserPost(User user);
  List<PostDto> findByLikesContainsUser(User user);
  PostDto updatePost(Long postId, MultipartFile file, String content, String jwt);
  PostDto createComment(CommentRequest commentRequest, User user) throws UserException, PostException;
  List<Comment> getAllCommentsByPostId(Long postId) throws  UserException, PostException;
  List<PostDto> getRepostedPostsByUserId(Long userId);
}
