package com.project.social_network.controller;

import com.project.social_network.config.Translator;
import com.project.social_network.dto.ReelDto;
import com.project.social_network.exceptions.ReelException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.User;
import com.project.social_network.response.ResponseData;
import com.project.social_network.service.interfaces.ReelService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("${api.prefix}/reel")
@Tag(name = "Reel Controller", description = "APIs for controlling reels")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class ReelController {

  private final Translator translator;

  private final ReelService reelService;

  private final UserService userService;

  @PostMapping("/create")
  @Operation(summary = "Create a new reel", description = "Create a new reel with content and an optional image file")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Reel created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input or creation failed")
  })
  public ResponseEntity<ResponseData<ReelDto>> createReel(
      @Parameter(description = "Image file to upload") @RequestParam(value = "file") MultipartFile file,
      @Parameter(description = "Content of the reel") @RequestParam("content") @NotBlank String content,
      @Parameter(description = "JWT token for authentication") @RequestHeader("Authorization") String jwt)
      throws UserException, ReelException, IOException {

    validateFile(file);
    User user = getUserFromJwt(jwt);
    ReelDto reelDto = reelService.createReel(file, content, user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(),
            Translator.toLocale("reel.create.success"), reelDto));
  }

  @GetMapping("/{reelId}")
  @Operation(summary = "Get a reel by ID", description = "Retrieve a reel by its ID")
  public ResponseEntity<ResponseData<ReelDto>> findReelById(
      @PathVariable Long reelId,
      @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    getUserFromJwt(jwt); // Xác thực
    ReelDto reelDto = reelService.findReelById(reelId);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        translator.toLocale("reel.find.success", reelId), reelDto));
  }

  @DeleteMapping("/{reelId}")
  @Operation(summary = "Delete a reel by ID", description = "Delete a reel by its ID")
  public ResponseEntity<Void> deleteReel(
      @PathVariable Long reelId,
      @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    User user = getUserFromJwt(jwt);
    reelService.deleteReelById(reelId, user.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get all reels", description = "Retrieve all reels")
  public ResponseEntity<ResponseData<List<ReelDto>>> getAllReels(
      @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    getUserFromJwt(jwt); // Kiểm tra JWT
    List<ReelDto> reelDtos = reelService.findAllReel();
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        Translator.toLocale("reel.get.all.success"), reelDtos));
  }

  private static void validateFile(MultipartFile file) {
    if (file != null) {
      if (file.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException("File size exceeds limit (5MB)");
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("Only image files are allowed");
      }
    }
  }

  private User getUserFromJwt(String jwt) throws UserException {
    return userService.findUserProfileByJwt(jwt);
  }
}
