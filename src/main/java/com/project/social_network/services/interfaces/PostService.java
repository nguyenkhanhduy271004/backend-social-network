package com.project.social_network.services.interfaces;

import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.PostReplyRequest;
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
}
