package com.project.social_network.controller;

import com.project.social_network.converter.GroupConverter;
import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import com.project.social_network.request.CreateGroupRequest;
import com.project.social_network.request.UpdateGroupRequest;
import com.project.social_network.response.ResponseData;
import com.project.social_network.response.ResponseError;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/groups")
@Tag(name = "Group Controller", description = "APIs for managing groups")
@SecurityRequirement(name = "bearerAuth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class GroupController {

  UserService userService;

  GroupService groupService;

  GroupConverter groupConverter;

  @Operation(summary = "Create a new group", description = "Create a new group")
  @PostMapping
  ResponseEntity<?> createGroup(
      @Valid @RequestBody CreateGroupRequest createGroupRequest,
      @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    Group group = groupService.createGroup(createGroupRequest, user);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.CREATED.value(), "Create group successfully",
            groupConverter.toGroupDto(group)),
        HttpStatus.CREATED);
  }

  @Operation(summary = "Update group", description = "Update the group's name")
  @PutMapping("/{groupId}")
  ResponseEntity<?> updateGroup(
      @Min(1) @PathVariable Long groupId,
      @Valid @RequestBody UpdateGroupRequest updateGroupRequest,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    Group updatedGroup = groupService.updateGroup(groupId, updateGroupRequest.getName(), admin);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Update group successfully",
            groupConverter.toGroupDto(updatedGroup)),
        HttpStatus.OK);
  }

  @Operation(summary = "Delete a group", description = "Delete a group by ID")
  @DeleteMapping("/{groupId}")
  ResponseEntity<?> deleteGroup(
      @Min(1) @PathVariable Long groupId,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.deleteGroup(groupId, admin);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Group deleted successfully", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Join a group", description = "User joins a group or sends a join request if the group is private")
  @PostMapping("/{groupId}/join")
  ResponseEntity<?> joinGroup(
      @Min(1) @PathVariable Long groupId,
      @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.joinGroup(groupId, user);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Join request processed successfully", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Leave a group", description = "User leaves a group")
  @PostMapping("/{groupId}/leave")
  ResponseEntity<?> leaveGroup(
      @Min(1) @PathVariable Long groupId,
      @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.leaveGroup(groupId, user);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Left group successfully", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Accept join request", description = "Admin accepts a user's request to join the group")
  @PostMapping("/{groupId}/accept-request/{userId}")
  ResponseEntity<?> acceptJoinRequest(
      @Min(1) @PathVariable Long groupId,
      @Min(1) @PathVariable Long userId,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.acceptJoinRequest(groupId, userId, admin);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Join request accepted", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Reject join request", description = "Admin rejects a user's request to join the group")
  @PostMapping("/{groupId}/reject-request/{userId}")
  ResponseEntity<?> rejectJoinRequest(
      @Min(1) @PathVariable Long groupId,
      @Min(1) @PathVariable Long userId,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.rejectJoinRequest(groupId, userId, admin);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Join request rejected", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Get pending join requests", description = "Admin gets list of pending join requests")
  @GetMapping("/{groupId}/pending-requests")
  ResponseEntity<?> getPendingRequests(
      @Min(1) @PathVariable Long groupId,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    List<GroupDto.User> pendingRequests = groupService.getPendingRequestsWithUserInfo(groupId,
        admin);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Pending requests retrieved successfully",
            pendingRequests),
        HttpStatus.OK);
  }

  @Operation(summary = "Get all groups", description = "Retrieve a list of all groups")
  @GetMapping
  ResponseEntity<?> getAllGroups() {
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(groupService.getAllGroups());
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Get groups successfully", groupDtos),
        HttpStatus.OK);
  }

  @GetMapping("/{groupId}")
  ResponseEntity<?> getGroupById(@Min(1) @PathVariable Long groupId) {
    Group group = groupService.getGroupById(groupId);
    GroupDto groupDto = groupConverter.toGroupDto(group);
    return ResponseEntity.ok(
        new ResponseData<>(HttpStatus.OK.value(), "Group retrieved successfully", groupDto));
  }

  @Operation(summary = "Get posts from a group", description = "Retrieve all posts of a specific group")
  @GetMapping("/{groupId}/posts")
  ResponseEntity<?> getPostsByGroupId(@Min(1) @PathVariable Long groupId) {
    List<PostDto> posts = groupService.getPostsByGroupId(groupId);
    if (posts.isEmpty()) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "No posts found"),
          HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Get posts for group successfully", posts),
        HttpStatus.OK);
  }

  @Operation(summary = "Get joined groups of the user", description = "Retrieve all groups the authenticated user is part of")
  @GetMapping("/my-groups")
  ResponseEntity<?> getGroupsByUser(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    List<Group> joinedGroups = groupService.getGroupsByUser(user);
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(joinedGroups);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Get my groups successfully", groupDtos),
        HttpStatus.OK);
  }

  @Operation(summary = "Get posts from all groups", description = "Retrieve all posts from every group")
  @GetMapping("/posts")
  ResponseEntity<?> getPostsFromAllGroups() {
    List<PostDto> posts = groupService.getPostsFromAllGroups();
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.OK.value(), "Get posts from all groups successfully", posts),
        HttpStatus.OK);
  }

  @DeleteMapping("/{groupId}/user/{userId}")
  ResponseEntity<?> removeUserFromGroup(
      @Min(1) @PathVariable Long groupId,
      @PathVariable Long userId,
      @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.removeMember(groupId, userId, user);
    return new ResponseEntity<>(
        new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete user from group successfully"),
        HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{groupId}/request-join")
  ResponseEntity<?> requestToJoinGroup(
      @Min(1) @PathVariable Long groupId,
      @RequestHeader("Authorization") String jwt) {
    String result = groupService.requestToJoinGroup(groupId, jwt);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/{groupId}/approve-request/{requestId}")
  ResponseEntity<?> approveJoinRequest(
      @Min(1) @PathVariable Long groupId,
      @Min(1) @PathVariable Long requestId,
      @RequestParam boolean approve,
      @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    String result = groupService.approveJoinRequest(groupId, requestId, approve, admin);
    return ResponseEntity.ok(result);
  }

}
