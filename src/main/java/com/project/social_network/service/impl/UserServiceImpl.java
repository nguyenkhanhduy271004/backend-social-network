package com.project.social_network.service.impl;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.converter.UserConverter;
import com.project.social_network.dto.UserDto;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.User;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.request.PaginationRequest;
import com.project.social_network.response.PagingResult;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.PaginationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final JwtProvider jwtProvider;

  private final UserConverter userConverter;

  private final UserRepository userRepository;

  @Override
//  @Cacheable(value = "users", key = "#userId")
  public User findUserById(Long userId) throws UserException {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserException("User not found with id: " + userId));
  }

  @Override
  public User findUserProfileByJwt(String jwt) throws UserException {
    String email = jwtProvider.getEmailFromToken(jwt);
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException("User not found with email: " + email));
  }

  @Override
  @CachePut(value = "users", key = "#userId")
  public UserDto updateUser(Long userId, User user) throws UserException {
    User existUser = findUserById(userId);

    updateUserDetails(existUser, user);

    return userConverter.toUserDto(userRepository.save(existUser));
  }

  @Override
  public UserDto followUser(Long userId, User user) throws UserException {
    User followToUser = findUserById(userId);

    if (user.getFollowings().contains(followToUser) && followToUser.getFollowers().contains(user)) {
      user.getFollowings().remove(followToUser);
      followToUser.getFollowers().remove(user);
    } else {
      user.getFollowings().add(followToUser);
      followToUser.getFollowers().add(user);
    }

    userRepository.saveAll(List.of(user, followToUser));
    return userConverter.toUserDto(followToUser);
  }

  @Override
  public List<UserDto> searchUser(String query, Long userId) {
    return userRepository.searchUser(query)
        .stream()
        .filter(user -> !user.getId().equals(userId))
        .map(userConverter::toUserDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> findAllUsers() {

    List<UserDto> users = userRepository.findAll().stream().map(userConverter::toUserDto).collect(Collectors.toList());
    return users;
  }

  @Override
  public PagingResult<UserDto> findAllUsers(PaginationRequest request) {
    final Pageable pageable = PaginationUtils.getPageable(request);
    final Page<User> entities = userRepository.findAll(pageable);

    final List<UserDto> entitiesDto = entities.stream().map(userConverter::toUserDto).toList();
    return new PagingResult<>(
        entitiesDto,
        entities.getTotalPages(),
        entities.getTotalElements(),
        entities.getSize(),
        entities.getNumber(),
        entities.isEmpty()
    );
  }

  @Override
  public UserDto updateUserAdminStatus(Long userId, boolean isAdmin) throws UserException {
    User user = findUserById(userId);
    user.setAdmin(isAdmin);
    return userConverter.toUserDto(userRepository.save(user));
  }

  @Override
  @CacheEvict(value = "users", key = "#userId")
  public void deleteUser(Long userId) throws UserException {
    User user = findUserById(userId);
    userRepository.delete(user);
  }

  @Override
  public List<UserDto> getRandomUsers(User user) {

    List<User> users = userRepository.findAll();

    List<User> followings = user.getFollowings();

    List<UserDto> randomUsers = new ArrayList<>();

    for (User user1 : users) {
      if (!followings.contains(user1) && !user1.getId().equals(user.getId())) {
        UserDto userDto = userConverter.toUserDto(user1);
        randomUsers.add(userDto);
      }
    }

    Collections.shuffle(randomUsers);

    if (randomUsers.size() > 10) {
      randomUsers = randomUsers.subList(0, 5);
    }

    return randomUsers;
  }

  private void updateUserDetails(User existUser, User user) {
    Optional.ofNullable(user.getFullName()).ifPresent(existUser::setFullName);
    Optional.ofNullable(user.getImage()).ifPresent(existUser::setImage);
    Optional.ofNullable(user.getBackgroundImage()).ifPresent(existUser::setBackgroundImage);
    Optional.ofNullable(user.getBirthDate()).ifPresent(existUser::setBirthDate);
    Optional.ofNullable(user.getLocation()).ifPresent(existUser::setLocation);
    Optional.ofNullable(user.getBio()).ifPresent(existUser::setBio);
    Optional.ofNullable(user.getWebsite()).ifPresent(existUser::setWebsite);
  }
}
