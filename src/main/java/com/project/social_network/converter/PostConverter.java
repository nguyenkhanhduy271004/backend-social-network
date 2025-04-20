package com.project.social_network.converter;

import com.project.social_network.request.PostReplyRequest;
import com.project.social_network.dto.CommentDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.response.UserCommentResponse;
import com.project.social_network.model.Comment;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.util.PostUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {

  private final UserConverter userConverter;
  private final PostUtil postUtil;
  private final ModelMapper modelMapper;

  public PostConverter(UserConverter userConverter, PostUtil postUtil, ModelMapper modelMapper) {
    this.userConverter = userConverter;
    this.postUtil = postUtil;
    this.modelMapper = modelMapper;
  }

  public Post postConverter(Post post, User user) {
    if (post == null || user == null) {
      return null;
    }

    Post newPost = modelMapper.map(post, Post.class);
    newPost.setCreatedAt(LocalDateTime.now());
    newPost.setUser(user);
    newPost.setReply(false);
    newPost.setPost(true);
    newPost.setVideo(post.getVideo());
    return newPost;
  }

  public Post postReplyConverter(PostReplyRequest post, User user) {
    if (post == null || user == null) {
      return null;
    }

    Post newPost = new Post();
    newPost.setContent(post.getContent());
    newPost.setCreatedAt(LocalDateTime.now());
    newPost.setImage(post.getImage());
    newPost.setUser(user);
    newPost.setReply(true);
    newPost.setPost(false);
    return newPost;
  }

  public PostDto toPostDto(Post post, User reqUser) {
    return toPostDtoInternal(post, reqUser, null, null);
  }

  public PostDto toPostDtoForGroup(Post post, User reqUser, String nameGroup, Long groupId) {
    return toPostDtoInternal(post, reqUser, nameGroup, groupId);
  }

  private PostDto toPostDtoInternal(Post post, User reqUser, String nameGroup, Long groupId) {
    if (post == null || reqUser == null) {
      return null;
    }

    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    postDto.setCreatedAt(post.getCreatedAt());
    postDto.setImage(post.getImage());
    postDto.setVideo(post.getVideo());

    PostDto.User userDto = new PostDto.User();
    userDto.setId(post.getUser().getId());
    userDto.setFullName(post.getUser().getFullName());
    postDto.setUser(userDto);

    postDto.setTotalLikes(post.getLikes() != null ? post.getLikes().size() : 0);
    postDto.setTotalReplies(post.getRePostUsers() != null ? post.getRePostUsers().size() : 0);
    postDto.setTotalComments(post.getComments() != null ? post.getComments().size() : 0);

    postDto.setLiked(postUtil.isLikedByReqUser(reqUser, post));
    postDto.setRePost(postUtil.isRePostByReqUser(reqUser, post));

    List<Long> rePostUserIds = post.getRePostUsers() != null
        ? post.getRePostUsers().stream().map(User::getId).collect(Collectors.toList())
        : Collections.emptyList();
    postDto.setRePostUserId(rePostUserIds);

    postDto.setReplyPosts(toPostDtos(post.getReplyPost(), reqUser));

    if (nameGroup != null && groupId != null) {
      postDto.setNameGroup(nameGroup);
      postDto.setGroupId(groupId);
    }

    return postDto;
  }

  public List<PostDto> toPostDtos(List<Post> posts, User reqUser) {
    if (posts == null || posts.isEmpty() || reqUser == null) {
      return Collections.emptyList();
    }

    return posts.stream()
        .map(post -> toPostDtoInternal(post, reqUser, null, null))
        .filter(postDto -> postDto != null)
        .collect(Collectors.toList());
  }

  public CommentDto toCommentDto(Comment comment) {
    if (comment == null) {
      return null;
    }

    CommentDto commentDto = new CommentDto();
    commentDto.setPostId(comment.getPost() != null ? comment.getPost().getId() : null);

    UserCommentResponse user = new UserCommentResponse();
    user.setId(comment.getUser().getId());
    user.setFullName(comment.getUser().getFullName());
    user.setImage(comment.getUser().getImage());
    commentDto.setUser(user);

    commentDto.setContent(comment.getContent());
    commentDto.setCreatedDate(comment.getCreatedDate());
    return commentDto;
  }
}