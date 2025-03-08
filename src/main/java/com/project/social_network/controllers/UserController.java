package com.project.social_network.controllers;

import com.project.social_network.converter.UserConverter;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.responses.ResponseData;
import com.project.social_network.models.responses.ResponseError;
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
  public Object getUser(@RequestHeader("Authorization") String jwt) throws UserException {

    try {
      User user = userService.findUserProfileByJwt(jwt);
      List<User> users = userService.findAllUsers();
      List<UserDto> userDtos = new ArrayList<>();

      for(User us:users) {
        UserDto userDto = userConverter.toUserDto(us);
        userDtos.add(userDto);
      }
      return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", userDtos);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user failed");
    }
  }

  @GetMapping("/random")
  public Object getUserRandom(@RequestHeader("Authorization") String jwt) throws UserException {

   try {
     User user = userService.findUserProfileByJwt(jwt);
     List<User> users = userService.findAllUsers();
     List<UserDto> userDtos = new ArrayList<>();

     for(User us:users) {
       UserDto userDto = userConverter.toUserDto(us);
       if(!user.getFollowings().contains(us) && !us.getId().equals(user.getId())) {
         userDtos.add(userDto);
       }
     }

     return new ResponseData<>(HttpStatus.OK.value(), "Get random user successfully", userDtos);
   } catch (UserException e) {
     return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get random user failed");
   }
  }

  @GetMapping("/profile")
  public Object getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
   try {
     User user = userService.findUserProfileByJwt(jwt);
     UserDto userDto = userConverter.toUserDto(user);
     userDto.setReq_user(true);

     return new ResponseData<>(HttpStatus.OK.value(), "Get user profile successfully", userDto);
   } catch (UserException e) {
     return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user profile failed");
   }
  }

  @GetMapping("/{userId}")
  public Object getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User reqUser = userService.findUserProfileByJwt(jwt);

      User user = userService.findUserById(userId);
      UserDto userDto = userConverter.toUserDto(user);
      userDto.setReq_user(userUtil.isReqUser(reqUser, user));
      userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));

      return new ResponseData<>(HttpStatus.OK.value(), "Get user by id successfully", userDto);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user by id failed");
    }
  }

  @GetMapping("/search")
  public Object getUserByQuery(@RequestParam String query, @RequestHeader("Authorization") String jwt) throws UserException {
   try {
     User reqUser = userService.findUserProfileByJwt(jwt);

     List<User> users = userService.searchUser(query, reqUser.getId());
     List<UserDto> userDtos = userConverter.toUserDtos(users);

     return new ResponseData<>(HttpStatus.OK.value(), "Get user by query successfully", userDtos);
   } catch (UserException e) {
     return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user by query failed");
   }
  }

  @PutMapping("/update")
  public Object getUserById(@RequestBody User req, @RequestHeader("Authorization") String jwt) throws UserException {
   try {
     User reqUser = userService.findUserProfileByJwt(jwt);

     User user = userService.updateUser(reqUser.getId(), req);
     UserDto userDto = userConverter.toUserDto(user);

     return new ResponseData<>(HttpStatus.OK.value(), "Update user successfully", userDto);

   } catch (UserException e) {
     return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed");
   }
  }

  @PutMapping("/{userId}/follow")
  public Object followUser(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User reqUser = userService.findUserProfileByJwt(jwt);

      User user = userService.followUser(userId, reqUser);
      UserDto userDto = userConverter.toUserDto(user);
      userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));

      return new ResponseData<>(HttpStatus.OK.value(), "Follow user successfully", userDto);

    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Follow user failed");

    }
  }
}
