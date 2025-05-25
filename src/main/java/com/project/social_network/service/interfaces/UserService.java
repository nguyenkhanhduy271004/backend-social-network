package com.project.social_network.service.interfaces;

import com.project.social_network.request.UpdateUserRequest;
import java.io.IOException;
import java.util.List;

import com.project.social_network.dto.UserDto;
import com.project.social_network.model.User;
import com.project.social_network.request.PaginationRequest;
import com.project.social_network.response.PagingResult;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User findUserById(Long userId);

  User findUserProfileByJwt(String jwt);

  UserDto updateUser(Long userId, MultipartFile image, UpdateUserRequest user) throws IOException;

  UserDto followUser(Long userId, User user);

  List<UserDto> searchUser(String query, Long userId);

  List<UserDto> findAllUsers();

  PagingResult<UserDto> findAllUsers(PaginationRequest request);

  List<UserDto> getRandomUsers(User user);

  UserDto updateUserAdminStatus(Long userId, boolean isAdmin);

  void deleteUser(Long userId);

  void validateAdminAccess(String jwt);
}
