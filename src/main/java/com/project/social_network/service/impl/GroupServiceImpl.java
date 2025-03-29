package com.project.social_network.service.impl;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.converter.UserConverter;
import com.project.social_network.dto.response.GroupDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Group;
import com.project.social_network.entity.User;
import com.project.social_network.exception.GroupException;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.service.interfaces.GroupService;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;
  private final UserConverter userConverter;
  private final PostConverter postConverter;

  public GroupServiceImpl(GroupRepository groupRepository, UserConverter userConverter,
      PostConverter postConverter) {
    this.groupRepository = groupRepository;
    this.userConverter = userConverter;
    this.postConverter = postConverter;
  }

  @Override
  public Group createGroup(String name, User owner) {
    Group group = new Group();
    group.setName(name);
    group.getUsers().add(owner);
    group.setAdmin(owner);
    return groupRepository.save(group);
  }

  @Override
  public Group updateGroup(Long groupId, String name, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    group.setName(name);
    return groupRepository.save(group);
  }

  @Override
  public void deleteGroup(Long groupId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    groupRepository.delete(group);
  }

  @Override
  public void joinGroup(Long groupId, User user) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    if (!group.getUsers().contains(user)) {
      group.getUsers().add(user);
      groupRepository.save(group);
    }
  }

  @Override
  public void leaveGroup(Long groupId, User user) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    group.getUsers().remove(user);

    if (group.getAdmin().equals(user)) {
      if (!group.getUsers().isEmpty()) {
        group.setAdmin(group.getUsers().get(0));
      } else {
        groupRepository.delete(group);
        return;
      }
    }

    groupRepository.save(group);
  }

  @Override
  @Transactional
  public void removeMember(Long groupId, Long userId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    group.getUsers().removeIf(user -> user.getId().equals(userId));

    groupRepository.save(group);
  }

  @Override
  public void promoteToAdmin(Long groupId, Long userId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    User newAdmin = group.getUsers().stream()
        .filter(user -> user.getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new GroupException("User not found in group"));

    group.setAdmin(newAdmin);
    groupRepository.save(group);
  }

  @Override
  public List<Group> getAllGroups() {
    return groupRepository.findAll();
  }

  @Override
  public Group getGroupById(Long groupId) {
    return groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));
  }


  private void checkIfAdmin(Group group, User user) {
    if (!group.getAdmin().equals(user)) {
      throw new GroupException("User is not the admin of this group");
    }
  }

  @Override
  public List<Group> getGroupsByUser(User user) {
    return groupRepository.findByUsersContaining(user);
  }

  @Override
  public List<GroupDto.User> getUsersByGroupId(Long groupId) {
    return groupRepository.findById(groupId)
        .map(group -> group.getUsers().stream()
            .map((user -> {
               return new GroupDto.User(user.getId(), user.getFullName());
            }))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  @Transactional(readOnly = true)
  public List<PostDto> getPostsFromAllGroups() {
    List<Group> groups = groupRepository.findAll();
    return groups.stream()
        .flatMap(group -> group.getPosts().stream())
        .map(post -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public List<PostDto> getPostsByGroupId(Long groupId) {
    return groupRepository.findById(groupId)
        .stream()
        .flatMap(group -> group.getPosts().stream())
        .map(post -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

}
