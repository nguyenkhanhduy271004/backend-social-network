package com.project.social_network.converter;

import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.User;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public UserDto toUserDto(User user) {
    ModelMapper modelMapper = new ModelMapper();
    UserDto userDto = modelMapper.map(user, UserDto.class);

    userDto.setFollowers(toUserDtos(user.getFollowers()));
    userDto.setFollowing(toUserDtos(user.getFollowings()));

    return userDto;
  }

  public List<UserDto> toUserDtos(List<User> followers) {
    List<UserDto> userDtos = new ArrayList<>();
    for (User user : followers) {
      UserDto userDto = new UserDto();
      userDto.setId(user.getId());
      userDto.setEmail(user.getEmail());
      userDto.setFullName(user.getFullName());
      userDto.setImage(user.getImage());

      userDtos.add(userDto);
    }

    return userDtos;
  }
}
