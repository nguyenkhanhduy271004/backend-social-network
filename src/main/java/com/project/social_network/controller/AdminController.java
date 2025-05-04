package com.project.social_network.controller;

import com.project.social_network.converter.GroupConverter;
import com.project.social_network.dto.AdminDashboardDto;
import com.project.social_network.dto.GroupDto;
import com.project.social_network.dto.PostDto;
import com.project.social_network.dto.ReelDto;
import com.project.social_network.dto.StoryDto;
import com.project.social_network.dto.UserDto;
import com.project.social_network.exception.GroupException;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.Group;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.interfaces.GroupService;
import com.project.social_network.service.interfaces.PostService;
import com.project.social_network.service.interfaces.ReelService;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller")
public class AdminController {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private StoryService storyService;

  @Autowired
  private ReelService reelService;

  @Autowired
  private GroupService groupService;

  @Autowired
  private GroupConverter groupConverter;

  private void validateAdminAccess(String jwt) throws UserException {
    User requestingUser = userService.findUserProfileByJwt(jwt);
    if (!requestingUser.isAdmin()) {
      throw new UserException("Unauthorized: Admin access required");
    }
  }

  @Operation(summary = "Get dashboard metrics", description = "Retrieves key metrics for the admin dashboard")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get dashboard metrics successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/dashboard")
  public Object getDashboardMetrics(@RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);
    List<UserDto> allUsers = userService.findAllUsers();
    List<Post> allPosts = postService.findAllPosts();

    Map<String, Integer> postsByMonth = new HashMap<>();
    for (Post post : allPosts) {
      LocalDateTime createdAt = post.getCreatedAt();
      if (createdAt != null) {
        String monthKey = createdAt.getMonth().toString() + " " + createdAt.getYear();
        postsByMonth.put(monthKey, postsByMonth.getOrDefault(monthKey, 0) + 1);
      }
    }

    List<StoryDto> stories = storyService.findAllStory();
    List<ReelDto> reels = reelService.findAllReel();
    List<Group> groups = groupService.getAllGroups();
    int storyCount = stories.size();
    int reelCount = reels.size();
    int groupCount = groups.size();
    int commentCount = 0;
    int likeCount = 0;
    int messageCount = 0;

    AdminDashboardDto dashboardDto = AdminDashboardDto.builder()
        .totalUsers(allUsers.size())
        .totalPosts(allPosts.size())
        .totalStories(storyCount)
        .totalReels(reelCount)
        .totalGroups(groupCount)
        .totalComments(commentCount)
        .totalLikes(likeCount)
        .totalMessages(messageCount)
        .postsByMonth(postsByMonth)
        .build();

    return new ResponseData<>(HttpStatus.OK.value(), "Dashboard metrics retrieved successfully",
        dashboardDto);
  }

  // USER MANAGEMENT

  @Operation(summary = "Get all users for admin", description = "Retrieves a list of all users with admin capabilities")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get users successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/users")
  public Object getAllUsers(@RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    List<UserDto> users = userService.findAllUsers();
    return new ResponseData<>(HttpStatus.OK.value(), "Users retrieved successfully", users);
  }

  @Operation(summary = "Get user by ID", description = "Retrieves a specific user by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/users/{userId}")
  public Object getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    User user = userService.findUserById(userId);
    return new ResponseData<>(HttpStatus.OK.value(), "User retrieved successfully", user);
  }

  @Operation(summary = "Update user admin status", description = "Grant or revoke admin privileges for a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User admin status updated successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @PutMapping("/users/{userId}/admin-status")
  public Object updateUserAdminStatus(
      @PathVariable Long userId,
      @RequestParam boolean isAdmin,
      @RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    UserDto updatedUser = userService.updateUserAdminStatus(userId, isAdmin);
    return new ResponseData<>(HttpStatus.OK.value(), "User admin status updated successfully",
        updatedUser);
  }

  @Operation(summary = "Delete user", description = "Permanently delete a user account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @DeleteMapping("/users/{userId}")
  public Object deleteUser(@PathVariable Long userId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    userService.deleteUser(userId);
    return new ResponseData<>(HttpStatus.OK.value(), "User deleted successfully", null);
  }

  // POST MANAGEMENT

  @Operation(summary = "Get all posts for moderation", description = "Retrieves all posts for admin moderation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/posts")
  public Object getAllPosts(@RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    List<PostDto> posts = postService.findAllPost();
    return new ResponseData<>(HttpStatus.OK.value(), "Posts retrieved successfully", posts);
  }

  @Operation(summary = "Get post by ID", description = "Retrieves a specific post by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/posts/{postId}")
  public Object getPostById(@PathVariable Long postId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    PostDto postDto = postService.findById(postId);
    return new ResponseData<>(HttpStatus.OK.value(), "Post retrieved successfully", postDto);
  }

  @Operation(summary = "Delete post", description = "Remove a post that violates community guidelines")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @DeleteMapping("/posts/{postId}")
  public Object deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    postService.deletePost(postId, userService.findUserProfileByJwt(jwt));
    return new ResponseData<>(HttpStatus.OK.value(), "Post deleted successfully", null);
  }

  // STORY MANAGEMENT

  @Operation(summary = "Get all stories", description = "Retrieves all stories for admin moderation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/stories")
  public Object getAllStories(@RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    List<StoryDto> stories = storyService.findAllStory();
    return new ResponseData<>(HttpStatus.OK.value(), "Stories retrieved successfully", stories);
  }

  @Operation(summary = "Get story by ID", description = "Retrieves a specific story by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Story retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/stories/{storyId}")
  public Object getStoryById(@PathVariable Long storyId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    StoryDto story = storyService.findStoryById(storyId);
    return new ResponseData<>(HttpStatus.OK.value(), "Story retrieved successfully", story);
  }

  @Operation(summary = "Delete story", description = "Remove a story that violates community guidelines")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Story deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @DeleteMapping("/stories/{storyId}")
  public Object deleteStory(@PathVariable Long storyId, @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {
    validateAdminAccess(jwt);
    User admin = userService.findUserProfileByJwt(jwt);

    storyService.deleteStoryById(storyId, admin.getId());
    return new ResponseData<>(HttpStatus.OK.value(), "Story deleted successfully", null);
  }

  // REEL MANAGEMENT

  @Operation(summary = "Get all reels", description = "Retrieves all reels for admin moderation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reels retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/reels")
  public Object getAllReels(@RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    List<ReelDto> reels = reelService.findAllReel();
    return new ResponseData<>(HttpStatus.OK.value(), "Reels retrieved successfully", reels);
  }

  @Operation(summary = "Get reel by ID", description = "Retrieves a specific reel by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reel retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/reels/{reelId}")
  public Object getReelById(@PathVariable Long reelId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    ReelDto reel = reelService.findReelById(reelId);
    return new ResponseData<>(HttpStatus.OK.value(), "Reel retrieved successfully", reel);
  }

  @Operation(summary = "Delete reel", description = "Remove a reel that violates community guidelines")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reel deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @DeleteMapping("/reels/{reelId}")
  public Object deleteReel(@PathVariable Long reelId, @RequestHeader("Authorization") String jwt)
      throws UserException, ReelException {
    validateAdminAccess(jwt);
    User admin = userService.findUserProfileByJwt(jwt);

    reelService.deleteReelById(reelId, admin.getId());
    return new ResponseData<>(HttpStatus.OK.value(), "Reel deleted successfully", null);
  }

  // GROUP MANAGEMENT

  @Operation(summary = "Get all groups", description = "Retrieves all groups for admin moderation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Groups retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/groups")
  public Object getAllGroups(@RequestHeader("Authorization") String jwt) throws UserException {
    validateAdminAccess(jwt);

    List<Group> groups = groupService.getAllGroups();
    List<GroupDto> groupDtos = groupConverter.toGroupDtos(groups);
    return new ResponseData<>(HttpStatus.OK.value(), "Groups retrieved successfully", groupDtos);
  }

  @Operation(summary = "Get group by ID", description = "Retrieves a specific group by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Group retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @GetMapping("/groups/{groupId}")
  public Object getGroupById(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt)
      throws UserException {
    validateAdminAccess(jwt);

    Group group = groupService.getGroupById(groupId);
    GroupDto groupDto = groupConverter.toGroupDto(group);
    return new ResponseData<>(HttpStatus.OK.value(), "Group retrieved successfully", groupDto);
  }

  @Operation(summary = "Delete group", description = "Remove a group that violates community guidelines")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Group deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Admin access required")
  })
  @DeleteMapping("/groups/{groupId}")
  public Object deleteGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String jwt)
      throws UserException, GroupException {
    validateAdminAccess(jwt);
    User admin = userService.findUserProfileByJwt(jwt);

    groupService.deleteGroup(groupId, admin);
    return new ResponseData<>(HttpStatus.OK.value(), "Group deleted successfully", null);
  }
}