package com.project.social_network.controller;

import com.project.social_network.service.interfaces.UploadImageFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Upload File Controller")
public class UploadFileController {

  private final UploadImageFile uploadImageFile;

  @PostMapping("/image")
  @Operation(summary = "Upload an image", description = "Uploads an image and returns its URL")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid image file"),
      @ApiResponse(responseCode = "500", description = "Server error while uploading")
  })
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      String imageUrl = uploadImageFile.uploadImage(file);
      return ResponseEntity.ok(imageUrl);
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Error uploading image: " + e.getMessage());
    }
  }
}
