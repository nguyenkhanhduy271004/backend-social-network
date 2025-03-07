package com.project.social_network.controllers;

import com.project.social_network.converter.ReelConverter;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.ReelDto;
import com.project.social_network.models.dtos.StoryDto;
import com.project.social_network.models.entities.Reel;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.responses.ApiResponse;
import com.project.social_network.models.responses.ResponseData;
import com.project.social_network.models.responses.ResponseError;
import com.project.social_network.services.interfaces.ReelService;
import com.project.social_network.services.interfaces.UploadImageFile;
import com.project.social_network.services.interfaces.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

@RestController
@RequestMapping("/api/reel")
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
  public Object createReel(
      @RequestParam(value = "file") MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, ReelException, IOException {

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

      return new ResponseData<>(HttpStatus.CREATED.value(), "Create reel successfully", reelDto);
    } catch (StoryException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create reel failed");

    }

  }

  @GetMapping("/{reelId}")
  public Object findReelById(@PathVariable Long reelId, @RequestHeader("Authorization") String jwt) throws UserException, ReelException {
    User user = userService.findUserProfileByJwt(jwt);


    try {
      Reel reel = reelService.findReelById(reelId);

      ReelDto reelDto = reelConverter.toReelDto(reel, user);

      return new ResponseData<>(HttpStatus.OK.value(), "Get reel by id" + reelId + " successfully", reelDto);
    } catch (StoryException e) {
      return new ResponseError(HttpStatus.NOT_FOUND.value(), "Get reel by id" + reelId + " failed");

    }

  }

  @DeleteMapping("/{reelId}")
  public Object deleteReel(@PathVariable Long reelId, @RequestHeader("Authorization") String jwt) throws UserException, ReelException {
    User user = userService.findUserProfileByJwt(jwt);


    try {
      reelService.deleteReelById(reelId, user.getId());

      return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete reel by id" + reelId + " successfully");
    } catch (StoryException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete reel by id" + reelId + " failed");

    }
  }

  @GetMapping("/")
  public Object getAllReels(@RequestHeader("Authorization") String jwt) throws UserException, ReelException {
    User user = userService.findUserProfileByJwt(jwt);


    try {
      List<Reel> reels = reelService.findAllReel();

      List<ReelDto> reelDtos = new ArrayList<>();

      for (Reel reel : reels) {
        ReelDto reelDto = reelConverter.toReelDto(reel, user);
        reelDtos.add(reelDto);
      }

      return new ResponseData<>(HttpStatus.OK.value(), "Get all reels successfully", reelDtos);
    } catch (StoryException e) {
      return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all reels failed");

    }

  }
}
