package com.project.social_network.controller;

import static com.project.social_network.dto.AccountDto.convertToDto;

import com.project.social_network.converter.UserConverter;
import com.project.social_network.dto.AccountDto;
import com.project.social_network.dto.UserDto;
import com.project.social_network.model.Account;
import com.project.social_network.model.User;
import com.project.social_network.request.PaginationRequest;
import com.project.social_network.request.UpdateUserRequest;
import com.project.social_network.response.PagingResult;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.impl.AccountServiceImpl;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/user")
@Tag(name = "User Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserController {

  UserUtil userUtil;

  UserService userService;

  UserConverter userConverter;

  AccountServiceImpl accountServiceImpl;

  @Operation(summary = "Get all users", description = "Retrieves a list of all users")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get user successfully"),
      @ApiResponse(responseCode = "400", description = "Get user failed")})
  @GetMapping("/")
  ResponseEntity<ResponseData<PagingResult<UserDto>>> getUser(
      @RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "ASC") String direction,
      @RequestParam(defaultValue = "id") String sortBy) {

    userService.findUserProfileByJwt(jwt);

    PaginationRequest request = new PaginationRequest(page, size, sortBy,
        Sort.Direction.fromString(direction));

    PagingResult<UserDto> result = userService.findAllUsers(request);

    return ResponseEntity.ok(
        new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", result));
  }

  @Operation(summary = "Get random users", description = "Retrieves a list of random users who are not followed by the current user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get random user successfully"),
      @ApiResponse(responseCode = "400", description = "Get random user failed")})
  @GetMapping("/random")
  ResponseEntity<?> getUserRandom(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    List<UserDto> userDtos = userService.getRandomUsers(user);

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get random user successfully", userDtos));
  }

  @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user profile successfully"),
      @ApiResponse(responseCode = "400", description = "Get user profile failed")})
  @GetMapping("/profile")
  ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    UserDto userDto = userConverter.toUserDto(user);
    userDto.setReq_user(true);

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get user profile successfully", userDto));
  }

  @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user by ID successfully"),
      @ApiResponse(responseCode = "400", description = "Get user by ID failed")})
  @GetMapping("/{userId}")
    // @Cacheable(value = "users", key = "#userId")
  ResponseEntity<?> getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) {

    User reqUser = userService.findUserProfileByJwt(jwt);
    User user = userService.findUserById(userId);

    UserDto userDto = userConverter.toUserDto(user);
    userDto.setReq_user(userUtil.isReqUser(reqUser, user));
    userDto.setFollowed(userUtil.isFollowedByReqUser(reqUser, user));

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", userDto));
  }

  @Operation(summary = "Search users", description = "Search users by query")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user by query successfully"),
      @ApiResponse(responseCode = "400", description = "Get user by query failed")})
  @GetMapping("/search")
  ResponseEntity<?> getUserByQuery(@RequestParam String query, @RequestHeader("Authorization") String jwt) {
    User reqUser = userService.findUserProfileByJwt(jwt);
    List<UserDto> userDtos = userService.searchUser(query, reqUser.getId());

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Search user successfully", userDtos));
  }

  @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update user successfully"),
      @ApiResponse(responseCode = "400", description = "Update user failed")})
  @PutMapping(consumes = "multipart/form-data")
  ResponseEntity<?> updateUser(
      @RequestPart UpdateUserRequest req,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @RequestHeader("Authorization") String jwt) throws IOException {
    User reqUser = userService.findUserProfileByJwt(jwt);
    UserDto userDto = userService.updateUser(reqUser.getId(), image, req);

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Update user successfully", userDto));
  }

  @Operation(summary = "Follow a user", description = "Allows a user to follow another user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Follow user successfully"),
      @ApiResponse(responseCode = "400", description = "Follow user failed")})
  @PutMapping("/{userId}/follow")
  ResponseEntity<?> followUser(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) {
    User reqUser = userService.findUserProfileByJwt(jwt);
    UserDto userDto = userService.followUser(userId, reqUser);

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Follow user successfully", userDto));
  }

  @GetMapping("/info")
  ResponseEntity<?> getUserInfo(Principal principal) {
    Account account = accountServiceImpl.getAccount(Long.valueOf(principal.getName()));
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Get user info successfully", account));
  }
}
