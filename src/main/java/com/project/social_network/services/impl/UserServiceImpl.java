package com.project.social_network.services.impl;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.User;
import com.project.social_network.repositories.UserRepository;
import com.project.social_network.services.interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtProvider jwtProvider;

  @Override
  public User findUserById(Long userId) throws UserException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException("User not found with id: " + userId));
    return user;
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
  public User updateUser(Long userId, User user) throws UserException {
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

    return userRepository.save(existUser);
  }

  @Override
  public User followUser(Long userId, User user) throws UserException {
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
    return followToUser;
  }

  @Override
  public List<User> searchUser(String query) {
    return userRepository.searchUser(query);
  }
}
