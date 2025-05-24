package com.project.social_network.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageRequest {

  @Min(value = 1, message = "Receiver id must be greater than 0")
  private Long receiverId;

  @NotNull(message = "Content must be not null")
  private String content;
}
