package com.project.social_network.controller;

import com.project.social_network.dto.StoryDto;
import com.project.social_network.model.User;
import com.project.social_network.response.ResponseData;
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
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/story")
@Tag(name = "Story Controller")
@SecurityRequirement(name = "bearerAuth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class StoryController {


  UserService userService;

  StoryService storyService;

  UploadImageFile uploadImageFile;

  @PostMapping("/create")
  @Operation(summary = "Create a new story")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Story created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed")
  })
  ResponseEntity<ResponseData<StoryDto>> createStory(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("content") @NotBlank String content,
      @RequestHeader("Authorization") String jwt) throws IOException {

    if (file != null) {
      uploadImageFile.validateImage(file);
    }
    User user = userService.findUserProfileByJwt(jwt);
    StoryDto storyDto = storyService.createStory(file, content, user);

    return ResponseEntity.ok(
        new ResponseData<>(HttpStatus.CREATED.value(), "story.create.success", storyDto));
  }

  @GetMapping("/{storyId}")
  @Operation(summary = "Get a story by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Story retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Story not found")
  })
  ResponseEntity<ResponseData<StoryDto>> findStoryById(
      @PathVariable @Min(1) Long storyId,
      @RequestHeader("Authorization") String jwt) {

    userService.findUserProfileByJwt(jwt);
    StoryDto storyDto = storyService.findStoryById(storyId);
    return ResponseEntity.ok(
        new ResponseData<>(HttpStatus.OK.value(), "story.find.success", storyDto));
  }

  @DeleteMapping("/{storyId}")
  @Operation(summary = "Delete a story by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Story deleted successfully"),
      @ApiResponse(responseCode = "400", description = "Deletion failed")
  })
  ResponseEntity<Void> deleteStory(
      @PathVariable @Min(1) Long storyId,
      @RequestHeader("Authorization") String jwt) {

    User user = userService.findUserProfileByJwt(jwt);
    storyService.deleteStoryById(storyId, user.getId());

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all stories")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Retrieval failed")
  })
  ResponseEntity<ResponseData<List<StoryDto>>> getAllStories(
      @RequestHeader("Authorization") String jwt) {

    userService.findUserProfileByJwt(jwt);
    List<StoryDto> storyDtos = storyService.findAllStory();
    return ResponseEntity.ok(
        new ResponseData<>(HttpStatus.OK.value(), "story.get.all.success", storyDtos));

  }

}
