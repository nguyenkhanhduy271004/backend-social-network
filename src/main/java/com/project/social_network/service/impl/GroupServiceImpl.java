package com.project.social_network.service.impl;

import com.project.social_network.entity.Group;
import com.project.social_network.entity.User;
import com.project.social_network.exception.GroupException;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.service.interfaces.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;

  public GroupServiceImpl(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
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
}
