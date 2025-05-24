package com.project.social_network.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.GroupUserDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.exception.GroupException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.Group;
import com.project.social_network.model.JoinRequest;
import com.project.social_network.model.User;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.repository.JoinRequestRepository;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.request.CreateGroupRequest;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

  private final PostConverter postConverter;

  private final UserRepository userRepository;

  private final GroupRepository groupRepository;

  private final JoinRequestRepository joinRequestRepository;

  private final UserService userService;

  @Override
  public Group createGroup(CreateGroupRequest createGroupRequest, User owner) {
    Group group = new Group();
    group.setName(createGroupRequest.getName());
    group.setPublic(createGroupRequest.isPublic());
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

    if (isMember) {
      throw new GroupException("User is already a member of this group");
    }

    boolean hasPendingRequest = group.getPendingRequests().stream()
        .anyMatch(u -> u.getId().equals(user.getId()));

    if (hasPendingRequest) {
      throw new GroupException("User already has a pending join request");
    }

    if (group.isPublic()) {
      group.getUsers().add(user);
    } else {
      group.getPendingRequests().add(user);
    }

    groupRepository.save(group);
  }

  @Override
  public void leaveGroup(Long groupId, User user) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    group.getUsers().remove(user);
    group.getPendingRequests().remove(user);

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
  public void acceptJoinRequest(Long groupId, Long userId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException("User not found"));

    if (!group.getPendingRequests().contains(user)) {
      throw new GroupException("No pending request found for this user");
    }

    group.getPendingRequests().remove(user);
    group.getUsers().add(user);
    groupRepository.save(group);
  }

  @Override
  public void rejectJoinRequest(Long groupId, Long userId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException("User not found"));

    if (!group.getPendingRequests().remove(user)) {
      throw new GroupException("No pending request found for this user");
    }

    groupRepository.save(group);
  }

  @Override
  public List<User> getPendingRequests(Long groupId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    return group.getPendingRequests();
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
        .map(post -> postConverter.toPostDtoForGroup(post, post.getUser(), post.getGroup().getName(),
            post.getGroup().getId()))
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

  @Override
  public void approveJoinRequest(Group group, JoinRequest request, boolean approve) {

    if (approve) {
      group.getUsers().add(request.getUser());
      groupRepository.save(group);
      request.setApproved(true);
    } else {
      request.setApproved(false);
    }

    request.setPending(false);
    joinRequestRepository.save(request);
  }

  @Override
  public String requestToJoinGroup(Long groupId, String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    if (group.isPublic()) {
      group.getUsers().add(user);
      groupRepository.save(group);
      return "Joined group directly.";
    }

    if (joinRequestRepository.findByGroupAndUser(group, user).isPresent()) {
      throw new GroupException("You already sent a join request.");
    }

    JoinRequest joinRequest = new JoinRequest();
    joinRequest.setGroup(group);
    joinRequest.setUser(user);
    joinRequest.setPending(true);
    joinRequestRepository.save(joinRequest);

    return "Join request sent. Wait for approval.";
  }

  @Override
  public String approveJoinRequest(Long groupId, Long requestId, boolean approve, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    if (!group.getAdmin().equals(admin)) {
      throw new GroupException("Not authorized.");
    }

    JoinRequest request = joinRequestRepository.findById(requestId)
        .orElseThrow(() -> new GroupException("Request not found"));

    if (!request.getGroup().getId().equals(groupId)) {
      throw new GroupException("Request not for this group.");
    }

    if (!request.isPending()) {
      throw new GroupException("Request already handled.");
    }

    approveJoinRequest(group, request, approve);

    return "Request has been " + (approve ? "approved." : "rejected.");
  }

  @Override
  public List<GroupDto.User> getPendingRequestsWithUserInfo(Long groupId, User admin) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupException("Group not found"));

    checkIfAdmin(group, admin);

    return group.getPendingRequests().stream()
        .map(user -> {
          GroupDto.User userDto = new GroupDto.User();
          userDto.setId(user.getId());
          userDto.setFullName(user.getFullName());
          return userDto;
        })
        .collect(Collectors.toList());
  }

  private void checkIfAdmin(Group group, User user) {
    if (!group.getAdmin().getId().equals(user.getId())) {
      throw new GroupException("Only the group admin can perform this action");
    }
  }
}
