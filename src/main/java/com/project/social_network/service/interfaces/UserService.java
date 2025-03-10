package com.project.social_network.service.interfaces;

import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.User;
import java.util.List;

public interface UserService {

  User findUserById(Long userId) throws UserException;

  User findUserProfileByJwt(String jwt) throws UserException;

  User updateUser(Long userId, User user) throws UserException;

  User followUser(Long userId, User user) throws UserException;

  List<User> searchUser(String query, Long userId);

  List<User> findAllUsers();
}
