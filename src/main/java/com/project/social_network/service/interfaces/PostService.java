package com.project.social_network.service.interfaces;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Comment;
import com.project.social_network.model.entity.Post;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.request.CommentRequest;
import com.project.social_network.model.request.PostReplyRequest;
import java.util.List;

public interface PostService {
  Post createPost(Post req, User user) throws UserException;
  List<Post> findAllPost();
  Post rePost(Long postId, User user) throws UserException, PostException;
  Post findById(Long postId) throws PostException;
  void deletePostById(Long postId, Long userId) throws UserException, PostException;
  Post removeFromRePost(Long postId, User user) throws UserException, PostException;
  Post createdReply(PostReplyRequest req, User user) throws UserException, PostException;
  List<Post> getUserPost(User user);
  List<Post> findByLikesContainsUser(User user);
  Post updatePost(Post post);
  Post createComment(CommentRequest commentRequest, User user) throws UserException, PostException;
  List<Comment> getAllCommentsByPostId(Long postId) throws  UserException, PostException;
  List<Post> getRepostedPostsByUserId(Long userId);
}
