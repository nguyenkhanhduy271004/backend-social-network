package com.project.social_network.service.interfaces;

import com.project.social_network.entity.Group;
import com.project.social_network.entity.User;
import java.util.List;

public interface GroupService {
  Group createGroup(String name, User owner);
  Group updateGroup(Long groupId, String name, User admin);
  void deleteGroup(Long groupId, User admin);
  void joinGroup(Long groupId, User user);
  void leaveGroup(Long groupId, User user);
  void removeMember(Long groupId, Long userId, User admin);
  void promoteToAdmin(Long groupId, Long userId, User admin);
  List<Group> getAllGroups();
  Group getGroupById(Long groupId);
  List<Group> getGroupsByUser(User user);

}
