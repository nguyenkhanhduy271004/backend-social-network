package com.project.social_network.service.interfaces;

import com.project.social_network.dto.GroupUserDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import com.project.social_network.exception.GroupException;
import java.util.List;

public interface GroupService {
  Group createGroup(String name, User owner);
  Group updateGroup(Long groupId, String name, User admin) throws GroupException;
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
}
