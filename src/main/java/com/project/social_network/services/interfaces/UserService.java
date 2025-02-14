package com.project.social_network.services.interfaces;

import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.User;
import java.util.List;

public interface UserService {

  User findUserById(Long userId) throws UserException;

  User findUserProfileByJwt(String jwt) throws UserException;

  User updateUser(Long userId, User user) throws UserException;

  User followUser(Long userId, User user) throws UserException;

  List<User> searchUser(String query);

  List<User> findAllUsers();
}
