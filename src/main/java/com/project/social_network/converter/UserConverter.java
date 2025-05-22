package com.project.social_network.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.project.social_network.dto.UserDto;
import com.project.social_network.model.User;

@Component
public class UserConverter {

  private final ModelMapper modelMapper;

  public UserConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
  }

  public List<UserDto> getFollowers(User user) {

    List<UserDto> userDtos = new ArrayList<>();

    for(User user1 : user.getFollowers()) {
      UserDto userDto = new UserDto();
      userDto.setId(user1.getId());;
      userDtos.add(userDto);
    }

    return userDtos;
  }

  public List<UserDto> getFollowing(User user) {

    List<UserDto> userDtos = new ArrayList<>();

    for(User user1 : user.getFollowings()) {
      UserDto userDto = new UserDto();
      userDto.setId(user1.getId());;
      userDtos.add(userDto);
    }

    return userDtos;
  }

  public UserDto toUserDto(User user) {
    if (user == null) {
      return null;
    }

    UserDto userDto = modelMapper.map(user, UserDto.class);
    userDto.setAdmin(user.isAdmin());
    userDto.setFollowers(toShallowUserDtos(user.getFollowers()));
    userDto.setFollowing(toShallowUserDtos(user.getFollowings()));

    return userDto;
  }

  public List<UserDto> toShallowUserDtos(List<User> users) {
    if (users == null || users.isEmpty()) {
      return Collections.emptyList();
    }

    return users.stream()
        .map(user -> {
          UserDto userDto = new UserDto();
          userDto.setId(user.getId());
          userDto.setAdmin(user.isAdmin());
          userDto.setEmail(user.getEmail());
          userDto.setFullName(user.getFullName());
          userDto.setImage(user.getImage());
          userDto.setFollowers(getFollowers(user));
          userDto.setFollowing(getFollowing(user));
          return userDto;
        })
        .collect(Collectors.toList());
  }
}