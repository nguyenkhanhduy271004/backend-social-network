package com.project.social_network.converter;

import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import com.project.social_network.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupConverter {

  private final ModelMapper modelMapper;
  private final PostConverter postConverter;

  public GroupConverter(ModelMapper modelMapper, PostConverter postConverter) {
    this.modelMapper = modelMapper;
    this.postConverter = postConverter;
  }

  public GroupDto toGroupDto(Group group) {
    if (group == null) {
      return null;
    }

    GroupDto groupDto = new GroupDto();

    groupDto.setId(group.getId());
    groupDto.setName(group.getName());
    groupDto.setAdmin(toGroupUserDto(group.getAdmin()));
    groupDto.setMembers(toGroupUserDtos(group.getUsers()));
    groupDto.setPosts(toPostDtos(group.getPosts()));

    return groupDto;
  }

  public List<GroupDto> toGroupDtos(List<Group> groups) {
    if (groups == null || groups.isEmpty()) {
      return Collections.emptyList();
    }

    return groups.stream()
        .map(this::toGroupDto)
        .collect(Collectors.toList());
  }

  private GroupDto.User toGroupUserDto(User user) {
    if (user == null) {
      return null;
    }

    GroupDto.User userDto = new GroupDto.User();
    userDto.setId(user.getId());
    userDto.setFullName(user.getFullName());
    return userDto;
  }

  private List<GroupDto.User> toGroupUserDtos(List<User> users) {
    if (users == null || users.isEmpty()) {
      return Collections.emptyList();
    }

    return users.stream()
        .map(this::toGroupUserDto)
        .collect(Collectors.toList());
  }

  private List<PostDto> toPostDtos(List<Post> posts) {
    if (posts == null || posts.isEmpty()) {
      return Collections.emptyList();
    }

    return posts.stream()
        .map(post -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }
}