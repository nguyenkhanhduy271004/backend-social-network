package com.project.social_network.converter;

import com.project.social_network.model.dto.UserDto;
import com.project.social_network.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

  private final ModelMapper modelMapper;

  @Autowired
  public UserConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
  }

  public UserDto toUserDto(User user) {
    if (user == null) {
      return null;
    }

    UserDto userDto = modelMapper.map(user, UserDto.class);
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
          userDto.setEmail(user.getEmail());
          userDto.setFullName(user.getFullName());
          userDto.setImage(user.getImage());
          return userDto;
        })
        .collect(Collectors.toList());
  }
}