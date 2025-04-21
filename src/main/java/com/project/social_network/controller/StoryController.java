package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.converter.StoryConverter;
import com.project.social_network.response.ResponseData;
import com.project.social_network.dto.StoryDto;
import com.project.social_network.model.User;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.service.interfaces.StoryService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/story")
@Tag(name = "Story Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class StoryController {

  private final StoryService storyService;
  private final UserService userService;
  private final Translator translator;
  private final UploadImageFile uploadImageFile;

  @PostMapping("/create")
  @Operation(summary = "Create a new story")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Story created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed")
  })
  public ResponseEntity<ResponseData<StoryDto>> createStory(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") @NotBlank String content,
      @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException, IOException {

    if (file != null) uploadImageFile.validateImage(file);
    User user = getUserFromJwt(jwt);
    StoryDto storyDto = storyService.createStory(file, content, user);

    return buildResponse(HttpStatus.CREATED, "story.create.success", storyDto);
  }

  @GetMapping("/{storyId}")
  @Operation(summary = "Get a story by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Story retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Story not found")
  })
  public ResponseEntity<ResponseData<StoryDto>> findStoryById(
      @PathVariable @Min(1) Long storyId,
      @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {

    getUserFromJwt(jwt); // ensure valid user
    StoryDto storyDto = storyService.findStoryById(storyId);

    return buildResponse(HttpStatus.OK, "story.find.success", storyDto, storyId);
  }

  @DeleteMapping("/{storyId}")
  @Operation(summary = "Delete a story by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Story deleted successfully"),
      @ApiResponse(responseCode = "400", description = "Deletion failed")
  })
  public ResponseEntity<Void> deleteStory(
      @PathVariable @Min(1) Long storyId,
      @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {

    User user = getUserFromJwt(jwt);
    storyService.deleteStoryById(storyId, user.getId());

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all stories")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Retrieval failed")
  })
  public ResponseEntity<ResponseData<List<StoryDto>>> getAllStories(
      @RequestHeader("Authorization") String jwt)
      throws UserException, StoryException {

    getUserFromJwt(jwt);
    List<StoryDto> storyDtos = storyService.findAllStory();

    return buildResponse(HttpStatus.OK, "story.get.all.success", storyDtos);
  }


  private User getUserFromJwt(String jwt) throws UserException {
    return userService.findUserProfileByJwt(jwt);
  }

  private <T> ResponseEntity<ResponseData<T>> buildResponse(HttpStatus status, String messageKey, T data, Object... args) {
    return ResponseEntity.status(status)
        .body(new ResponseData<>(status.value(), translator.toLocale(messageKey, args), data));
  }
}
