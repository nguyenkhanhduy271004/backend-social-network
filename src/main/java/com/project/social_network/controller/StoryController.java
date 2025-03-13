package com.project.social_network.controller;

import com.project.social_network.converter.StoryConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.dto.response.StoryDto;
import com.project.social_network.entity.Story;
import com.project.social_network.entity.User;
import com.project.social_network.dto.response.ResponseData;
import com.project.social_network.dto.response.ResponseError;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/story")
@Tag(name = "Story Controller")
@SecurityRequirement(name = "bearerAuth")
public class StoryController {

  @Autowired
  private StoryService storyService;

  @Autowired
  private UserService userService;

  @Autowired
  private StoryConverter storyConverter;

  @Autowired
  private UploadImageFile uploadImageFile;

  @PostMapping("/create")
  @Operation(summary = "Create a new story", description = "Create a new story with content and an optional image file.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Story created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed")
  })
  public ResponseEntity<?> createStory(
      @Parameter(description = "Image file to upload", required = false)
      @RequestParam(value = "file", required = false) MultipartFile file,
      @Parameter(description = "Content of the story", required = true)
      @RequestParam("content") String content,
      @Parameter(description = "JWT token for authentication", required = true)
      @RequestHeader("Authorization") String jwt) throws UserException, StoryException, IOException {

    User user = userService.findUserProfileByJwt(jwt);

    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Story req = new Story();
    req.setContent(content);
    req.setImage(imageFileUrl);

    try {
      Story story = storyService.createStory(req, user);
      StoryDto storyDto = storyConverter.toStoryDto(story, user);

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.CREATED.value(), "Create story successfully", storyDto), HttpStatus.CREATED);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create story failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{storyId}")
  @Operation(summary = "Get a story by ID", description = "Retrieve a story by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Story retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Story not found")
  })
  public ResponseEntity<?> findStoryById(
      @Parameter(description = "ID of the story to retrieve", required = true)
      @PathVariable Long storyId,
      @Parameter(description = "JWT token for authentication", required = true)
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      Story story = storyService.findStoryById(storyId);
      StoryDto storyDto = storyConverter.toStoryDto(story, user);

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get story by id " + storyId + " successfully", storyDto), HttpStatus.OK);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "Get story by id " + storyId + " failed"), HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{storyId}")
  @Operation(summary = "Delete a story by ID", description = "Delete a story by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Story deleted successfully"),
      @ApiResponse(responseCode = "400", description = "Deletion failed")
  })
  public ResponseEntity<?> deleteStory(
      @Parameter(description = "ID of the story to delete", required = true)
      @PathVariable Long storyId,
      @Parameter(description = "JWT token for authentication", required = true)
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      storyService.deleteStoryById(storyId, user.getId());
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete story by id " + storyId + " successfully"), HttpStatus.NO_CONTENT);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete story by id " + storyId + " failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/")
  @Operation(summary = "Get all stories", description = "Retrieve all stories.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Retrieval failed")
  })
  public ResponseEntity<?> getAllStories(
      @Parameter(description = "JWT token for authentication", required = true)
      @RequestHeader("Authorization") String jwt) throws UserException, PostException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      List<Story> stories = storyService.findAllStory();
      List<StoryDto> storyDtos = new ArrayList<>();

      for (Story story : stories) {
        StoryDto storyDto = storyConverter.toStoryDto(story, user);
        storyDtos.add(storyDto);
      }

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get all stories successfully", storyDtos), HttpStatus.OK);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all stories failed"), HttpStatus.BAD_REQUEST);
    }
  }
}
