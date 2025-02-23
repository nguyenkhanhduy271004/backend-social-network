package com.project.social_network.controllers;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.converter.StoryConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.StoryException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.PostDto;
import com.project.social_network.models.dtos.StoryDto;
import com.project.social_network.models.entities.Post;
import com.project.social_network.models.entities.Story;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.responses.ApiResponse;
import com.project.social_network.services.interfaces.PostService;
import com.project.social_network.services.interfaces.StoryService;
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
@RequestMapping("/api/story")
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
  public ResponseEntity<StoryDto> createStory(
      @RequestParam(value = "file") MultipartFile file,
      @RequestParam("content") String content,
      @RequestHeader("Authorization") String jwt) throws UserException, StoryException, IOException {

    User user = userService.findUserProfileByJwt(jwt);

    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }

    Story req = new Story();
    req.setContent(content);
    req.setImage(imageFileUrl);

    Story story = storyService.createStory(req, user);
    StoryDto storyDto = storyConverter.toStoryDto(story, user);

    return new ResponseEntity<>(storyDto, HttpStatus.CREATED);
  }

  @GetMapping("/{storyId}")
  public ResponseEntity<StoryDto> findPostById(@PathVariable Long storyId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    Story story = storyService.findStoryById(storyId);

    StoryDto storyDto = storyConverter.toStoryDto(story, user);

    return new ResponseEntity<>(storyDto, HttpStatus.OK);
  }

  @DeleteMapping("/{storyId}")
  public ResponseEntity<ApiResponse> deletePost(@PathVariable Long storyId, @RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    storyService.deleteStoryById(storyId, user.getId());

    ApiResponse res = new ApiResponse("Story deleted successfully", true);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/")
  public ResponseEntity<List<StoryDto>> getAllStories(@RequestHeader("Authorization") String jwt) throws UserException, PostException {
    User user = userService.findUserProfileByJwt(jwt);

    List<Story> stories = storyService.findAllStory();

    List<StoryDto> storyDtos = new ArrayList<>();

    for(Story story : stories) {
      StoryDto storyDto = storyConverter.toStoryDto(story, user);
      storyDtos.add(storyDto);
    }

    return new ResponseEntity<>(storyDtos, HttpStatus.OK);
  }
}
