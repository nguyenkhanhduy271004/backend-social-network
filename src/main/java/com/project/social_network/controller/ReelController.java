package com.project.social_network.controller;

import com.project.social_network.converter.ReelConverter;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.dto.ReelDto;
import com.project.social_network.model.entity.Reel;
import com.project.social_network.model.entity.User;
import com.project.social_network.model.response.ResponseData;
import com.project.social_network.model.response.ResponseError;
import com.project.social_network.service.interfaces.ReelService;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reel")
@Api(tags = "Reel Controller", description = "APIs for controller reels")
public class ReelController {

  @Autowired
  private ReelService reelService;

  @Autowired
  private UserService userService;

  @Autowired
  private ReelConverter reelConverter;

  @Autowired
  private UploadImageFile uploadImageFile;

  @PostMapping("/create")
  @ApiOperation(value = "Create a new reel", notes = "Create a new reel with content and an optional image file.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Reel created successfully"),
      @ApiResponse(code = 400, message = "Invalid input or creation failed")
  })
  public ResponseEntity<?> createReel(
      @ApiParam(value = "Image file to upload", required = false) @RequestParam(value = "file", required = false) MultipartFile file,
      @ApiParam(value = "Content of the reel", required = true) @RequestParam("content") String content,
      @ApiParam(value = "JWT token for authentication", required = true) @RequestHeader("Authorization") String jwt) throws UserException, ReelException, IOException {

    User user = userService.findUserProfileByJwt(jwt);

    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Reel req = new Reel();
    req.setContent(content);
    req.setImage(imageFileUrl);

    try {
      Reel reel = reelService.createReel(req, user);
      ReelDto reelDto = reelConverter.toReelDto(reel, user);

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.CREATED.value(), "Create reel successfully", reelDto), HttpStatus.CREATED);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create reel failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{reelId}")
  @ApiOperation(value = "Get a reel by ID", notes = "Retrieve a reel by its ID.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Reel retrieved successfully"),
      @ApiResponse(code = 404, message = "Reel not found")
  })
  public ResponseEntity<?> findReelById(
      @ApiParam(value = "ID of the reel to retrieve", required = true) @PathVariable Long reelId,
      @ApiParam(value = "JWT token for authentication", required = true) @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      Reel reel = reelService.findReelById(reelId);
      ReelDto reelDto = reelConverter.toReelDto(reel, user);

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get reel by id " + reelId + " successfully", reelDto), HttpStatus.OK);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "Get reel by id " + reelId + " failed"), HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{reelId}")
  @ApiOperation(value = "Delete a reel by ID", notes = "Delete a reel by its ID.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Reel deleted successfully"),
      @ApiResponse(code = 400, message = "Deletion failed")
  })
  public ResponseEntity<?> deleteReel(
      @ApiParam(value = "ID of the reel to delete", required = true) @PathVariable Long reelId,
      @ApiParam(value = "JWT token for authentication", required = true) @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      reelService.deleteReelById(reelId, user.getId());
      return new ResponseEntity<>(new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete reel by id " + reelId + " successfully"), HttpStatus.NO_CONTENT);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete reel by id " + reelId + " failed"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/")
  @ApiOperation(value = "Get all reels", notes = "Retrieve all reels.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Reels retrieved successfully"),
      @ApiResponse(code = 400, message = "Retrieval failed")
  })
  public ResponseEntity<?> getAllReels(
      @ApiParam(value = "JWT token for authentication", required = true) @RequestHeader("Authorization") String jwt) throws UserException, ReelException {

    User user = userService.findUserProfileByJwt(jwt);

    try {
      List<Reel> reels = reelService.findAllReel();
      List<ReelDto> reelDtos = new ArrayList<>();

      for (Reel reel : reels) {
        ReelDto reelDto = reelConverter.toReelDto(reel, user);
        reelDtos.add(reelDto);
      }

      return new ResponseEntity<>(new ResponseData<>(HttpStatus.OK.value(), "Get all reels successfully", reelDtos), HttpStatus.OK);
    } catch (StoryException e) {
      return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all reels failed"), HttpStatus.BAD_REQUEST);
    }
  }
}