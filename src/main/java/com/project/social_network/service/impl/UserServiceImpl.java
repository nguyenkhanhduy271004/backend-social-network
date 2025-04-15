package com.project.social_network.service.impl;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.converter.UserConverter;
import com.project.social_network.model.dto.UserDto;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.User;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.service.interfaces.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private UserConverter userConverter;

  @Override
  public User findUserById(Long userId) throws UserException {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserException("User not found with id: " + userId));
  }


  @Override
  public User findUserProfileByJwt(String jwt) throws UserException {
    String email = jwtProvider.getEmailFromToken(jwt);
    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new UserException("User not found with email: " + email);
    }
    return user;
  }

  @Override
  public UserDto updateUser(Long userId, User user) throws UserException {
    User existUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found with id: " + userId));

    if (user.getFullName() != null) {
      existUser.setFullName(user.getFullName());
    }

    if(user.getImage() != null) {
      existUser.setImage(user.getImage());
    }

    if(user.getBackgroundImage() != null) {
      existUser.setBackgroundImage(user.getBackgroundImage());
    }

    if(user.getBirthDate() != null) {
      existUser.setBirthDate(user.getBirthDate());
    }

    if(user.getLocation() != null) {
      existUser.setLocation(user.getLocation());
    }

    if(user.getBio() != null) {
      existUser.setBio(user.getBio());
    }

    if(user.getWebsite() != null) {
      existUser.setWebsite(user.getWebsite());
    }

    return userConverter.toUserDto(userRepository.save(existUser));
  }

  @Override
  public UserDto followUser(Long userId, User user) throws UserException {
    User followToUser = findUserById(userId);

    if(user.getFollowings().contains(followToUser) && followToUser.getFollowers().contains(user)) {
      user.getFollowings().remove(followToUser);
      followToUser.getFollowers().remove(user);
    } else {
      user.getFollowings().add(followToUser);
      followToUser.getFollowers().add(user);
    }

    userRepository.save(followToUser);
    userRepository.save(user);
    return userConverter.toUserDto(followToUser);
  }

  @Override
  public List<UserDto> searchUser(String query, Long userId) {
    return userRepository.searchUser(query)
        .stream()
        .filter(user -> !user.getId().equals(userId))
        .map((user) -> userConverter.toUserDto(user))
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> findAllUsers() {
    return userRepository.findAll()
        .stream()
        .map((user) -> userConverter.toUserDto(user))
        .collect(Collectors.toList());
  }
}
