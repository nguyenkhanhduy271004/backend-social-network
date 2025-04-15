package com.project.social_network.service.impl;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.model.dto.GroupUserDto;
import com.project.social_network.model.dto.PostDto;
import com.project.social_network.model.entity.Group;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.GroupException;
import com.project.social_network.exception.UserException;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.service.interfaces.GroupService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupServiceImpl implements GroupService {

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostConverter postConverter;

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

    boolean isMember = group.getUsers().stream()
        .anyMatch(u -> u.getId().equals(user.getId()));

    if (!isMember) {
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

    User userToRemove = group.getUsers().stream()
        .filter(user -> user.getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new UserException("User not found in the group"));

    group.getUsers().remove(userToRemove);
    userToRemove.getGroups().remove(group);

    userRepository.save(userToRemove);
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

  @Override
  public List<Group> getGroupsByUser(User user) {
    return groupRepository.findByUsersContaining(user);
  }

  @Override
  public List<GroupUserDto> getUsersByGroupId(Long groupId) {
    return groupRepository.findUsersByGroupId(groupId);
  }

  @Transactional(readOnly = true)
  public List<PostDto> getPostsFromAllGroups() {
    List<Group> groups = groupRepository.findAll();
    return groups.stream()
        .flatMap(group -> group.getPosts().stream())
        .map(post -> postConverter.toPostDtoForGroup(post, post.getUser(), post.getGroup().getName(), post.getGroup().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public List<PostDto> getPostsByGroupId(Long groupId) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    return group.getPosts().stream()
        .map(post -> postConverter.toPostDto(post, post.getUser()))
        .collect(Collectors.toList());
  }

  private void checkIfAdmin(Group group, User user) {
    if (user == null || group.getAdmin() == null || !group.getAdmin().getId().equals(user.getId())) {
      throw new GroupException("User is not the admin of this group");
    }
  }
}
