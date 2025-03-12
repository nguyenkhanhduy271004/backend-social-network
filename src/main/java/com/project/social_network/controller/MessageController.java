package com.project.social_network.controller;

import com.project.social_network.exception.UserException;
import com.project.social_network.dto.UserDto;
import com.project.social_network.entity.Message;
import com.project.social_network.entity.User;
import com.project.social_network.dto.request.MessageRequest;
import com.project.social_network.service.interfaces.MessageService;
import com.project.social_network.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Message Controller")
@RestController
@RequestMapping("/api/messages")
public class MessageController {
  @Autowired
  private MessageService messageService;

  @Autowired
  private UserService userService;

  @PostMapping("/send")
  @Operation(summary = "Send a message", description = "Send a new message to a specific user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message sent successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to send message"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public Message sendMessage(@RequestBody MessageRequest messageRequest, @RequestHeader("Authorization") String jwt) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.sendMessage(user.getId(), messageRequest.getReceiverId(), messageRequest.getContent());
  }

  @GetMapping("/history")
  @Operation(summary = "Get chat history", description = "Retrieve chat history between the current user and another user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Chat history retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve chat history"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public List<Message> getChatHistory(@RequestParam Long receiverId, @RequestHeader("Authorization") String jwt)  throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.getChatHistory(user.getId(), receiverId);
  }

  @GetMapping("/user")
  @Operation(summary = "Get users for messaging", description = "Retrieve a list of users the current user has messaged.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve users"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public List<UserDto> getUserForMessage(@RequestHeader("Authorization") String jwt)  throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.findDistinctReceiverIdsBySenderId(user.getId());
  }
}
