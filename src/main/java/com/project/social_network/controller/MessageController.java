package com.project.social_network.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.social_network.dto.UserDto;
import com.project.social_network.model.Message;
import com.project.social_network.model.User;
import com.project.social_network.request.MessageRequest;
import com.project.social_network.service.interfaces.MessageService;
import com.project.social_network.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/v1/messages")
@Tag(name = "Message Controller", description = "APIs for sending and retrieving messages between users.")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

  private final UserService userService;

  private final MessageService messageService;

  @PostMapping("/send")
  @Operation(summary = "Send a message", description = "Send a new message to a specific user.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message sent successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to send message"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public Message sendMessage(
      @RequestBody MessageRequest messageRequest,
      @RequestHeader("Authorization") String jwt) {
    User sender = userService.findUserProfileByJwt(jwt);
    return messageService.sendMessage(sender.getId(), messageRequest.getReceiverId(), messageRequest.getContent());
  }

  @GetMapping("/history")
  @Operation(summary = "Get chat history", description = "Retrieve chat history between the current user and another user.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Chat history retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve chat history"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public List<Message> getChatHistory(
      @RequestParam Long receiverId,
      @RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.getChatHistory(user.getId(), receiverId);
  }

  @GetMapping("/user")
  @Operation(summary = "Get users for messaging", description = "Retrieve a list of users the current user has messaged.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Failed to retrieve users"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public List<UserDto> getUserForMessage(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.findDistinctReceiverIdsBySenderId(user.getId());
  }
}
