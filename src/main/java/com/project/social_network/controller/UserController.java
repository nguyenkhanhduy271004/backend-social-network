package com.project.social_network.controller;

import com.project.social_network.converter.UserConverter;
import com.project.social_network.exception.UserException;
import com.project.social_network.dto.UserDto;
import com.project.social_network.entity.User;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserConverter userConverter;

  @Autowired
  private UserUtil userUtil;

  @Operation(summary = "Get all users", description = "Retrieves a list of all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user successfully"),
      @ApiResponse(responseCode = "400", description = "Get user failed")
  })
  @GetMapping("/")
  public Object getUser(@RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      List<User> users = userService.findAllUsers();
      List<UserDto> userDtos = new ArrayList<>();

      for (User us : users) {
        UserDto userDto = userConverter.toUserDto(us);
        userDtos.add(userDto);
      }
      return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", userDtos);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user failed");
    }
  }

  @Operation(summary = "Get random users", description = "Retrieves a list of random users who are not followed by the current user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get random user successfully"),
      @ApiResponse(responseCode = "400", description = "Get random user failed")
  })
  @GetMapping("/random")
  public Object getUserRandom(@RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      List<User> users = userService.findAllUsers();
      List<UserDto> userDtos = new ArrayList<>();

      for (User us : users) {
        UserDto userDto = userConverter.toUserDto(us);
        if (!user.getFollowings().contains(us) && !us.getId().equals(user.getId())) {
          userDtos.add(userDto);
        }
      }

      return new ResponseData<>(HttpStatus.OK.value(), "Get random user successfully", userDtos);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get random user failed");
    }
  }

  @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user profile successfully"),
      @ApiResponse(responseCode = "400", description = "Get user profile failed")
  })
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

  @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user by ID successfully"),
      @ApiResponse(responseCode = "400", description = "Get user by ID failed")
  })
  @GetMapping("/{userId}")
  public Object getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User reqUser = userService.findUserProfileByJwt(jwt);
      User user = userService.findUserById(userId);
      UserDto userDto = userConverter.toUserDto(user);
      userDto.setReq_user(userUtil.isReqUser(reqUser, user));
      userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));

      return new ResponseData<>(HttpStatus.OK.value(), "Get user by ID successfully", userDto);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user by ID failed");
    }
  }

  @Operation(summary = "Search users", description = "Search users by query")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user by query successfully"),
      @ApiResponse(responseCode = "400", description = "Get user by query failed")
  })
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

  @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update user successfully"),
      @ApiResponse(responseCode = "400", description = "Update user failed")
  })
  @PutMapping("/update")
  public Object updateUser(@RequestBody User req, @RequestHeader("Authorization") String jwt) throws UserException {
    try {
      User reqUser = userService.findUserProfileByJwt(jwt);
      User user = userService.updateUser(reqUser.getId(), req);
      UserDto userDto = userConverter.toUserDto(user);

      return new ResponseData<>(HttpStatus.OK.value(), "Update user successfully", userDto);
    } catch (UserException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed");
    }
  }

  @Operation(summary = "Follow a user", description = "Allows a user to follow another user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Follow user successfully"),
      @ApiResponse(responseCode = "400", description = "Follow user failed")
  })
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
