package com.project.social_network.converter;

import com.project.social_network.dto.response.CommentDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Comment;
import com.project.social_network.entity.Post;
import com.project.social_network.entity.User;
import com.project.social_network.dto.request.PostReplyRequest;
import com.project.social_network.dto.response.UserCommentResponse;
import com.project.social_network.util.PostUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

  @Autowired
  private UserConverter userConverter;
  @Autowired
  private PostUtil postUtil;

  public Post postConverter(Post post, User user) {
    ModelMapper modelMapper = new ModelMapper();
    Post newPost = modelMapper.map(post, Post.class);
    newPost.setCreatedAt(LocalDateTime.now());
    newPost.setUser(user);
    newPost.setReply(false);
    newPost.setPost(true);
    newPost.setVideo(post.getVideo());
    return newPost;
  }

  public Post postReplyConverter(PostReplyRequest post, User user) {
    Post newPost = new Post();
    newPost.setContent(post.getContent());
    newPost.setCreatedAt(LocalDateTime.now());

    if (post.getImage() != null) {
      newPost.setImage(post.getImage());
    }

    newPost.setUser(user);
    newPost.setReply(true);
    newPost.setPost(false);

    return newPost;
  }


  public PostDto toPostDto(Post post, User reqUser) {
    PostDto.User user = new PostDto.User();

    user.setId(post.getUser().getId());
    user.setFullName(post.getUser().getFullName());

    boolean isLiked = postUtil.isLikedByReqUser(reqUser, post);
    boolean isReposted = postUtil.isRePostByReqUser(reqUser, post);


    List<Long> rePostUserId = new ArrayList<>();

    for(User user1:post.getRePostUsers()) {
      rePostUserId.add(user1.getId());
    }

    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    postDto.setCreatedAt(post.getCreatedAt());
    postDto.setImage(post.getImage());
    postDto.setTotalLikes(post.getLikes().size());
    postDto.setTotalReplies(post.getRePostUsers().size());
    postDto.setTotalComments(post.getComments().size());
    postDto.setUser(user);
    postDto.setLiked(isLiked);
    postDto.setRePost(isReposted);
    postDto.setRePostUserId(rePostUserId);
    postDto.setReplyPosts(toPostDtos(post.getReplyPost(), reqUser));
    postDto.setVideo(post.getVideo());

    return postDto;
  }

  public PostDto toPostDtoForGroup(Post post, User reqUser, String nameGroup, Long groupId) {
    PostDto.User user = new PostDto.User();

    user.setId(post.getUser().getId());
    user.setFullName(post.getUser().getFullName());

    boolean isLiked = postUtil.isLikedByReqUser(reqUser, post);
    boolean isReposted = postUtil.isRePostByReqUser(reqUser, post);


    List<Long> rePostUserId = new ArrayList<>();

    for(User user1:post.getRePostUsers()) {
      rePostUserId.add(user1.getId());
    }

    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    postDto.setCreatedAt(post.getCreatedAt());
    postDto.setImage(post.getImage());
    postDto.setTotalLikes(post.getLikes().size());
    postDto.setTotalReplies(post.getRePostUsers().size());
    postDto.setTotalComments(post.getComments().size());
    postDto.setUser(user);
    postDto.setLiked(isLiked);
    postDto.setRePost(isReposted);
    postDto.setRePostUserId(rePostUserId);
    postDto.setReplyPosts(toPostDtos(post.getReplyPost(), reqUser));
    postDto.setVideo(post.getVideo());
    postDto.setNameGroup(nameGroup);
    postDto.setGroupId(groupId);

    return postDto;
  }

  public List<PostDto> toPostDtos(List<Post> posts, User reqUser) {
    List<PostDto> postDtos = new ArrayList<>();

    for(Post post:posts) {
      PostDto postDto = toReplyPostDto(post, reqUser);
      postDtos.add(postDto);
    }

    return postDtos;
  }

  private PostDto toReplyPostDto(Post post, User reqUser) {
    PostDto.User user = new PostDto.User();

    user.setId(post.getUser().getId());
    user.setFullName(post.getUser().getFullName());

    boolean isLiked = postUtil.isLikedByReqUser(reqUser, post);
    boolean isReposted = postUtil.isRePostByReqUser(reqUser, post);


    List<Long> rePostUserId = new ArrayList<>();

    for(User user1:post.getRePostUsers()) {
      rePostUserId.add(user1.getId());
    }

    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    postDto.setCreatedAt(post.getCreatedAt());
    postDto.setImage(post.getImage());
    postDto.setTotalLikes(post.getLikes().size());
    postDto.setTotalReplies(post.getRePostUsers().size());
    postDto.setTotalComments(post.getComments().size());
    postDto.setUser(user);
    postDto.setLiked(isLiked);
    postDto.setRePost(isReposted);
    postDto.setRePostUserId(rePostUserId);
    postDto.setVideo(post.getVideo());

    return postDto;

  }

  public CommentDto toCommentDto(Comment comment) {
    CommentDto commentDto = new CommentDto();
    commentDto.setPostId(comment.getPost().getId());

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
