package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.dto.ReelDto;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.User;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.interfaces.ReelService;
import com.project.social_network.service.interfaces.UserService;
import com.project.social_network.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/reel")
@Tag(name = "Reel Controller", description = "APIs for controlling reels")
@SecurityRequirement(name = "bearerAuth")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ReelController {

  Translator translator;
  ReelService reelService;
  UserService userService;
  FileUtil fileUtil;

  @PostMapping("/create")
  @Operation(summary = "Create a new reel", description = "Create a new reel with content and a required video/image file")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Reel created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  ResponseEntity<ResponseData<ReelDto>> createReel(
      @Parameter(description = "Video or image file to upload") @RequestParam("file") MultipartFile file,
      @Parameter(description = "Content of the reel") @RequestParam @NotBlank(message = "Content cannot be empty") @Size(max = 1000, message = "Content must be less than 1000 characters") String content,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt)
      throws UserException, ReelException, IOException {

    fileUtil.validateFile(file);
    User user = userService.findUserProfileByJwt(jwt);
    ReelDto reelDto = reelService.createReel(file, content, user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("reel.create.success"), reelDto));
  }

  @GetMapping("/{reelId}")
  @Operation(summary = "Get a reel by ID", description = "Retrieve a reel by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reel retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "404", description = "Reel not found")
  })
  ResponseEntity<ResponseData<ReelDto>> findReelById(
      @PathVariable @Min(value = 1, message = "Reel ID must be positive") Long reelId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt)
      throws UserException, ReelException {

    userService.findUserProfileByJwt(jwt);
    ReelDto reelDto = reelService.findReelById(reelId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), translator.toLocale("reel.find.success", reelId), reelDto));
  }

  @DeleteMapping("/{reelId}")
  @Operation(summary = "Delete a reel by ID", description = "Delete a reel by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Reel deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "404", description = "Reel not found")
  })
  ResponseEntity<Void> deleteReel(
      @PathVariable @Min(value = 1, message = "Reel ID must be positive") Long reelId,
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt)
      throws UserException, ReelException {

    User user = userService.findUserProfileByJwt(jwt);
    reelService.deleteReelById(reelId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all reels", description = "Retrieve all reels")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reels retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  ResponseEntity<ResponseData<List<ReelDto>>> getAllReels(
      @RequestHeader("Authorization") @NotBlank(message = "Authorization header is required") String jwt)
      throws UserException, ReelException {

    userService.findUserProfileByJwt(jwt);
    List<ReelDto> reelDtos = reelService.findAllReel();
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("reel.get.all.success"), reelDtos));
  }
}