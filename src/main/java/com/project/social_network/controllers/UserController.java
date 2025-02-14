package com.project.social_network.controllers;

import com.project.social_network.converter.UserConverter;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.User;
import com.project.social_network.services.interfaces.UserService;
import com.project.social_network.utils.UserUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserConverter userConverter;
  @Autowired
  private UserUtil userUtil;

  @GetMapping("/")
  public ResponseEntity<List<UserDto>> getUser(@RequestHeader("Authorization") String jwt) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);

    List<User> users = userService.findAllUsers();
    List<UserDto> userDtos = new ArrayList<>();

    for(User us:users) {
      UserDto userDto = userConverter.toUserDto(user);
      userDtos.add(userDto);
    }
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }

  @GetMapping("/profile")
  public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    UserDto userDto = userConverter.toUserDto(user);
    userDto.setReq_user(true);
    return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException {
    User reqUser = userService.findUserProfileByJwt(jwt);

    User user = userService.findUserById(userId);
    UserDto userDto = userConverter.toUserDto(user);
    userDto.setReq_user(userUtil.isReqUser(reqUser, user));
    userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));
    return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
  }

  @GetMapping("/search")
  public ResponseEntity<List<UserDto>> getUserById(@RequestParam String query, @RequestHeader("Authorization") String jwt) throws UserException {
    User reqUser = userService.findUserProfileByJwt(jwt);

    List<User> users = userService.searchUser(query);
    List<UserDto> userDtos = userConverter.toUserDtos(users);
    return new ResponseEntity<>(userDtos, HttpStatus.ACCEPTED);
  }

  @PutMapping("/update")
  public ResponseEntity<UserDto> getUserById(@RequestBody User req, @RequestHeader("Authorization") String jwt) throws UserException {
    User reqUser = userService.findUserProfileByJwt(jwt);

    User user = userService.updateUser(reqUser.getId(), req);
    UserDto userDto = userConverter.toUserDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
  }

  @PutMapping("/{userId}/follow")
  public ResponseEntity<UserDto> followUser(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException {
    User reqUser = userService.findUserProfileByJwt(jwt);

    User user = userService.followUser(userId, reqUser);
    UserDto userDto = userConverter.toUserDto(user);
    userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));
    return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
  }
}
