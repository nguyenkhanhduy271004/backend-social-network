package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.StoryConverter;
import com.project.social_network.model.dto.response.ResponseData;
import com.project.social_network.model.dto.StoryDto;
import com.project.social_network.model.entity.User;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/story")
@Tag(name = "Story Controller")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class StoryController {

  private final StoryService storyService;
  private final UserService userService;
  private final StoryConverter storyConverter;
  private final UploadImageFile uploadImageFile;
  private final Translator translator;

  public StoryController(StoryService storyService, UserService userService,
      StoryConverter storyConverter, UploadImageFile uploadImageFile,
      Translator translator) {
    this.storyService = storyService;
    this.userService = userService;
    this.storyConverter = storyConverter;
    this.uploadImageFile = uploadImageFile;
    this.translator = translator;
  }

  @PostMapping("/create")
  @Operation(summary = "Create a new story", description = "Create a new story with content and an optional image file")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Story created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed")
  })
  public ResponseEntity<ResponseData<StoryDto>> createStory(
      @Parameter(description = "Image file to upload") @RequestParam(value = "file", required = false) MultipartFile file,
      @Parameter(description = "Content of the story") @RequestParam("content") @NotBlank String content,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException, IOException {
    validateFile(file);
    User user = userService.findUserProfileByJwt(jwt);
    StoryDto storyDto = storyService.createStory(file, content, user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(), translator.toLocale("story.create.success"), storyDto));
  }

  @GetMapping("/{storyId}")
  @Operation(summary = "Get a story by ID", description = "Retrieve a story by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Story retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Story not found")
  })
  public ResponseEntity<ResponseData<StoryDto>> findStoryById(
      @Parameter(description = "ID of the story to retrieve") @PathVariable @Min(1) Long storyId,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {
    User user = userService.findUserProfileByJwt(jwt);
    StoryDto storyDto = storyService.findStoryById(storyId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        translator.toLocale("story.find.success", storyId), storyDto));
  }

  @DeleteMapping("/{storyId}")
  @Operation(summary = "Delete a story by ID", description = "Delete a story by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Story deleted successfully"),
      @ApiResponse(responseCode = "400", description = "Deletion failed")
  })
  public ResponseEntity<Void> deleteStory(
      @Parameter(description = "ID of the story to delete") @PathVariable @Min(1) Long storyId,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {
    User user = userService.findUserProfileByJwt(jwt);
    storyService.deleteStoryById(storyId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all stories", description = "Retrieve all stories")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Retrieval failed")
  })
  public ResponseEntity<ResponseData<List<StoryDto>>> getAllStories(
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {
    User user = userService.findUserProfileByJwt(jwt);
    List<StoryDto> storyDtos = storyService.findAllStory();
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        translator.toLocale("story.get.all.success"), storyDtos));
  }

  private void validateFile(MultipartFile file) {
    if (file != null) {
      if (file.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException("File size exceeds limit");
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("Only image files are allowed");
      }
    }
  }
}