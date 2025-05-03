package com.project.social_network.service.interfaces;

import java.util.List;

import com.project.social_network.dto.UserDto;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.User;

public interface UserService {

  User findUserById(Long userId) throws UserException;

  User findUserProfileByJwt(String jwt) throws UserException;

  UserDto updateUser(Long userId, User user) throws UserException;

  UserDto followUser(Long userId, User user) throws UserException;

  List<UserDto> searchUser(String query, Long userId);

  List<UserDto> findAllUsers();

  List<UserDto> getRandomUsers(User user);

  UserDto updateUserAdminStatus(Long userId, boolean isAdmin) throws UserException;

  void deleteUser(Long userId) throws UserException;
}
