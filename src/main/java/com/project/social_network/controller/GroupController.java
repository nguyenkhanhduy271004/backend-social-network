package com.project.social_network.controller;

import com.project.social_network.converter.GroupConverter;
import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.request.CreateGroupRequest;
import com.project.social_network.request.UpdateGroupRequest;
import com.project.social_network.response.ResponseData;
import com.project.social_network.response.ResponseError;
import com.project.social_network.model.Group;
import com.project.social_network.model.User;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@Tag(name = "Group Controller", description = "APIs for managing groups")
@SecurityRequirement(name = "bearerAuth")
public class GroupController {

  @Autowired
  private GroupService groupService;

  @Autowired
  private UserService userService;

  @Autowired
  private GroupConverter groupConverter;

  @Operation(summary = "Create a new group", description = "Create a new group")
  @PostMapping
  public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest createGroupRequest, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    Group group = groupService.createGroup(createGroupRequest.getName(), user);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.CREATED.value(), "Create group successfully", groupConverter.toGroupDto(group)), HttpStatus.CREATED);
  }

  @Operation(summary = "Update group", description = "Update the group's name")
  @PutMapping("/{groupId}")
  public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @Valid @RequestBody
      UpdateGroupRequest updateGroupRequest, @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    Group updatedGroup = groupService.updateGroup(groupId, updateGroupRequest.getName(), admin);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Update group successfully", groupConverter.toGroupDto(updatedGroup)), HttpStatus.OK);
  }

  @Operation(summary = "Delete a group", description = "Delete a group by ID")
  @DeleteMapping("/{groupId}")
  public ResponseEntity<?> deleteGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.deleteGroup(groupId, admin);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Group deleted successfully", null), HttpStatus.OK);
  }

  @Operation(summary = "Join a group", description = "User joins a group")
  @PostMapping("/{groupId}/join")
  public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.joinGroup(groupId, user);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Joined group successfully", null), HttpStatus.OK);
  }

  @Operation(summary = "Leave a group", description = "User leaves a group")
  @PostMapping("/{groupId}/leave")
  public ResponseEntity<?> leaveGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.leaveGroup(groupId, user);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Left group successfully", null), HttpStatus.OK);
  }

  @Operation(summary = "Get all groups", description = "Retrieve a list of all groups")
  @GetMapping
  public ResponseEntity<?> getAllGroups() {
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(groupService.getAllGroups());
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get groups successfully", groupDtos), HttpStatus.OK);
  }

  @Operation(summary = "Get group by ID", description = "Retrieve a specific group by ID")
  @GetMapping("/{groupId}")
  public ResponseEntity<?> getGroupById(@PathVariable Long groupId) {
    return ResponseEntity.ok(groupConverter.toGroupDto(groupService.getGroupById(groupId)));
  }

  @Operation(summary = "Get posts from a group", description = "Retrieve all posts of a specific group")
  @GetMapping("/{groupId}/posts")
  public ResponseEntity<?> getPostsByGroupId(@PathVariable Long groupId) {
    List<PostDto> posts = groupService.getPostsByGroupId(groupId);
    if (posts.isEmpty()) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "No posts found"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get posts for group successfully", posts), HttpStatus.OK);
  }

  @Operation(summary = "Get joined groups of the user", description = "Retrieve all groups the authenticated user is part of")
  @GetMapping("/my-groups")
  public ResponseEntity<?> getGroupsByUser(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    List<Group> joinedGroups = groupService.getGroupsByUser(user);
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(joinedGroups);
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get my groups successfully", groupDtos), HttpStatus.OK);
  }

  @Operation(summary = "Get posts from all groups", description = "Retrieve all posts from every group")
  @GetMapping("/posts")
  public ResponseEntity<?> getPostsFromAllGroups() {
    return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get posts from all groups successfully", groupService.getPostsFromAllGroups()), HttpStatus.OK);
  }
}
