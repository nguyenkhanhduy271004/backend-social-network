package com.project.social_network.service.interfaces;

import java.util.List;

import com.project.social_network.dto.GroupUserDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import com.project.social_network.request.CreateGroupRequest;

public interface GroupService {
  Group createGroup(CreateGroupRequest createGroupRequest, User owner);

  Group updateGroup(Long groupId, String name, User admin);

  void deleteGroup(Long groupId, User admin);

  void joinGroup(Long groupId, User user);

  void leaveGroup(Long groupId, User user);

  void removeMember(Long groupId, Long userId, User admin);

  void promoteToAdmin(Long groupId, Long userId, User admin);

  List<Group> getAllGroups();

  Group getGroupById(Long groupId);

  List<Group> getGroupsByUser(User user);

  List<GroupUserDto> getUsersByGroupId(Long groupId);

  List<PostDto> getPostsFromAllGroups();

  List<PostDto> getPostsByGroupId(Long groupId);

  void acceptJoinRequest(Long groupId, Long userId, User admin);

  void rejectJoinRequest(Long groupId, Long userId, User admin);

  List<User> getPendingRequests(Long groupId, User admin);
}
