package com.project.social_network.services.impl;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Comment;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.CommentRequest;
import com.project.social_network.models.requests.PostReplyRequest;
import com.project.social_network.repositories.CommentRepository;
import com.project.social_network.repositories.PostRepository;
import com.project.social_network.services.interfaces.PostService;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostConverter postConverter;
  @Autowired
  private CommentRepository commentRepository;

  @Override
  public Post createPost(Post req, User user) throws UserException {
    Post post = postConverter.postConverter(req, user);
    return postRepository.save(post);
  }

  @Override
  public List<Post> findAllPost() {
    return postRepository.findAllByIsPostTrueOrderByCreatedAtDesc();
  }

  @Override
  public Post rePost(Long postId, User user) throws UserException, PostException {
    Post post = findById(postId);
    if (post.getRePostUsers().contains(user)) {
      post.getRePostUsers().remove(user);
    } else {
      post.getRePostUsers().add(user);
    }
    postRepository.save(post);
    return post;
  }

  @Override
  public Post findById(Long postId) throws PostException {
    Post post = postRepository.findById(postId).orElseThrow(() -> new PostException("Post not found with id: " + postId));
    return post;
  }

  @Override
  public void deletePostById(Long postId, Long userId) throws UserException, PostException {
    Post post = findById(postId);

    if(!userId.equals(post.getUser().getId())) {
      throw new UserException("You can't delete another user's post");
    }

    postRepository.deleteById(post.getId());
  }

  @Override
  public Post removeFromRePost(Long postId, User user) throws UserException, PostException {
    return null;
  }

  @Override
  public Post createdReply(PostReplyRequest req, User user) throws UserException, PostException {
    Post replyFor = findById(req.getPostId());

    Post post = postConverter.postReplyConverter(req, user);
    post.setReplyFor(replyFor);

    Post savedPost = postRepository.save(post);

    replyFor.getReplyPost().add(savedPost);
    postRepository.save(replyFor);

    return savedPost;
  }


  @Override
  public List<Post> getUserPost(User user) {
    return postRepository.findByRePostUsersContainsOrUser_IdAndIsPostTrueOrderByCreatedAtDesc(user, user.getId());
  }

  @Override
  public List<Post> findByLikesContainsUser(User user) {
    return postRepository.findByLikesUser_id(user.getId());
  }

  @Override
  public Post updatePost(Post post) {
    return postRepository.save(post);
  }

  @Override
  public Post createComment(CommentRequest commentRequest, User user) throws UserException, PostException {
    Post post = findById(commentRequest.getPostId());

    Comment comment = new Comment();
    comment.setUser(user);
    comment.setPost(post);
    comment.setContent(commentRequest.getContent());

    post.getComments().add(comment);

    return postRepository.save(post);
  }


  @Override
  public List<Comment> getAllCommentsByPostId(Long postId) throws UserException, PostException {
    List<Comment> comments = commentRepository.findByPost_Id(postId);
    return commentRepository.findByPost_Id(postId);
  }

  @Override
  public List<Post> getRepostedPostsByUserId(Long userId) {
    return postRepository.findRepostedPostsByUserId(userId);
  }

}
