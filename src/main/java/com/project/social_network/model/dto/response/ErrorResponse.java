package com.project.social_network.model.dto.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
  private Date timestamp;
  private int status;
  private String path;
  private String error;
  private String message;
}
