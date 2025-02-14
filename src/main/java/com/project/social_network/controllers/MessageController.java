package com.project.social_network.controllers;

import com.project.social_network.exception.UserException;
import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.Message;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.requests.MessageRequest;
import com.project.social_network.services.interfaces.MessageService;
import com.project.social_network.services.interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
  @Autowired
  private MessageService messageService;
  @Autowired
  private UserService userService;

  @PostMapping("/send")
  public Message sendMessage(@RequestBody MessageRequest messageRequest, @RequestHeader("Authorization") String jwt) throws UserException {
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.sendMessage(user.getId(), messageRequest.getReceiverId(), messageRequest.getContent());
  }

  @GetMapping("/history")
  public List<Message> getChatHistory(@RequestParam Long receiverId, @RequestHeader("Authorization") String jwt)  throws UserException{
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.getChatHistory(user.getId(), receiverId);
  }

  @GetMapping("/user")
  public List<UserDto> getUserForMessage(@RequestHeader("Authorization") String jwt)  throws UserException{
    User user = userService.findUserProfileByJwt(jwt);
    return messageService.findDistinctReceiverIdsBySenderId(user.getId());
  }
}
