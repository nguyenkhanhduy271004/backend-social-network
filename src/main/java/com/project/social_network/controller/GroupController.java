package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.GroupConverter;
import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.response.GroupDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.entity.Group;
import com.project.social_network.entity.Post;
import com.project.social_network.entity.User;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.UserException;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  private UploadImageFile uploadImageFile;

  @Autowired
  private PostService postService;

  @Autowired
  private PostConverter postConverter;

  @Autowired
  private GroupConverter groupConverter;

  @Operation(summary = "Create a group", description = "API to create a new group")
  @PostMapping
  public ResponseEntity<GroupDto> createGroup(@RequestParam String name, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    return ResponseEntity.ok(groupConverter.toGroupDto(groupService.createGroup(name, user)));
  }

  @Operation(summary = "Update group", description = "API to update group information")
  @PutMapping("/{groupId}")
  public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestParam String name, @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    return ResponseEntity.ok(groupService.updateGroup(groupId, name, admin));
  }

  @Operation(summary = "Delete group", description = "API to delete a group")
  @DeleteMapping("/{groupId}")
  public ResponseEntity<String> deleteGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.deleteGroup(groupId, admin);
    return ResponseEntity.ok("Group deleted successfully");
  }

  @Operation(summary = "Join a group", description = "API to join an existing group")
  @PostMapping("/{groupId}/join")
  public ResponseEntity<String> joinGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.joinGroup(groupId, user);
    return ResponseEntity.ok("Joined group successfully");
  }

  @Operation(summary = "Leave a group", description = "API to leave a group")
  @PostMapping("/{groupId}/leave")
  public ResponseEntity<String> leaveGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    groupService.leaveGroup(groupId, user);
    return ResponseEntity.ok("Left group successfully");
  }

  @Operation(summary = "Remove a member from a group", description = "API to remove a member from a group (admin only)")
  @DeleteMapping("/{groupId}/members/{userId}")
  public ResponseEntity<String> removeMember(@PathVariable Long groupId, @PathVariable Long userId, @RequestHeader("Authorization") String jwt) {
    User admin = userService.findUserProfileByJwt(jwt);
    groupService.removeMember(groupId, userId, admin);
    return ResponseEntity.ok("Member removed successfully");
  }

  @Operation(summary = "Get all groups", description = "API to retrieve all groups")
  @GetMapping
  public ResponseEntity<List<GroupDto>> getAllGroups() {
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(groupService.getAllGroups());
    return ResponseEntity.ok(groupDtos);
  }

  @Operation(summary = "Get group by ID", description = "API to retrieve a group by its ID")
  @GetMapping("/{groupId}")
  public ResponseEntity<Group> getGroupById(@PathVariable Long groupId) {
    return ResponseEntity.ok(groupService.getGroupById(groupId));
  }

  public ResponseEntity<?> createPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt,
      @RequestParam("groupId") Long groupId) throws UserException, PostException, IOException {

    User user = userService.findUserProfileByJwt(jwt);
    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Post req = new Post();
    req.setContent(content);
    req.setImage(imageFileUrl);

    try {
      Post post = postService.createPostForGroup(req, user, groupId);
      PostDto postDto = postConverter.toPostDto(post, user);
      return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("post.create.success"), postDto));
    } catch (PostException e) {
      return ResponseEntity.badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
  }

}
