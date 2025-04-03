package com.project.social_network.controller;

import com.project.social_network.converter.GroupConverter;
import com.project.social_network.dto.response.GroupDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.entity.Group;
import com.project.social_network.entity.User;
import com.project.social_network.exception.GroupException;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping
  public ResponseEntity<?> createGroup(@RequestBody String name, @RequestHeader("Authorization") String jwt) {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      Group group = groupService.createGroup(name, user);
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.CREATED.value(), "Create group successfully", groupConverter.toGroupDto(group)), HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create group failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{groupId}")
  public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestParam String name, @RequestHeader("Authorization") String jwt) {
    try {
      User admin = userService.findUserProfileByJwt(jwt);
      Group updatedGroup = groupService.updateGroup(groupId, name, admin);
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Update group successfully", groupConverter.toGroupDto(updatedGroup)), HttpStatus.OK);
    } catch (GroupException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update group failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/{groupId}")
  public ResponseEntity<?> deleteGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    try {
      User admin = userService.findUserProfileByJwt(jwt);
      groupService.deleteGroup(groupId, admin);
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Group deleted successfully", null), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete group failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/{groupId}/join")
  public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      groupService.joinGroup(groupId, user);
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Joined group successfully", null), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Join group failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/{groupId}/leave")
  public ResponseEntity<?> leaveGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    try {
      User user = userService.findUserProfileByJwt(jwt);
      groupService.leaveGroup(groupId, user);
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Left group successfully", null), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Leave group failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllGroups() {
    try {
      List<GroupDto> groupDtos = groupConverter.toGroupDtos(groupService.getAllGroups());
      return ResponseEntity.ok(groupDtos);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get groups failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{groupId}")
  public ResponseEntity<?> getGroupById(@PathVariable Long groupId) {
    try {
      return ResponseEntity.ok(groupConverter.toGroupDto(groupService.getGroupById(groupId)));
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Group not found"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{groupId}/posts")
  public ResponseEntity<?> getPostsByGroupId(@PathVariable Long groupId) {
    try {
      List<PostDto> posts = groupService.getPostsByGroupId(groupId);
      if (posts.isEmpty()) {
        return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "No posts found"), HttpStatus.NOT_FOUND);
      }
      return ResponseEntity.ok(posts);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get posts failed"), HttpStatus.BAD_REQUEST);
    }
  }
}