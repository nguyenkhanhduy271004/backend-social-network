package com.project.social_network.service.interfaces;

import com.project.social_network.dto.UserDto;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.User;
import java.util.List;

public interface UserService {

  User findUserById(Long userId) throws UserException;

  User findUserProfileByJwt(String jwt) throws UserException;

  UserDto updateUser(Long userId, User user) throws UserException;

  UserDto followUser(Long userId, User user) throws UserException;

  List<UserDto> searchUser(String query, Long userId);

  List<UserDto> findAllUsers();
}
